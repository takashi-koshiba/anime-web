import sys
import os

from pathlib import Path
import mysql.connector
import requests
import json
import argparse

from PIL import Image
import imagehash

import cv2


similarThreshold=0.9

DB_CONFIG = {
    'host': "localhost",
    'user': "java",
    'password': "java",
    'database': "db1"
}

PORT=8082  #実行しているjavaのポート番号



def execute_query(query, params=None):
    try:
        conn = connect_db()
        cursor = conn.cursor()
        if params:

            cursor.execute(query, params)
        else:
            cursor.execute(query)
        result = cursor.fetchall()
    except mysql.connector.Error as err:
      #  print(f"Error querry: {query}")
        print(f"Error details: {err}")
        result = None
    finally:
        conn.commit()
        cursor.close()
        conn.close()
    return result


def videoFrameHash(video_path,count,resize_to=(64, 36)):
    cap = cv2.VideoCapture(video_path)

    #print("ハッシュをリストに書き込み中")
    #print(str(video_path))
    if not cap.isOpened():
        raise IOError("動画を開けません")


    try:
        frame_count=cap.get(cv2.CAP_PROP_FRAME_COUNT)
        interval = frame_count // count  # 等間隔で何個のハッシュを取得するか
        hashes = []

        
        for i in range(count):
            cap.set(cv2.CAP_PROP_POS_FRAMES, i * interval)
            ret, frame = cap.read()
            if ret and frame is not None:
                try:
                    # OpenCVのBGR画像をPILのRGB画像に変換
                    pil_img = Image.fromarray(cv2.cvtColor(frame, cv2.COLOR_BGR2RGB))


                    resized_img = pil_img.resize(resize_to, Image.Resampling.BILINEAR)

                    # pHash計算
                    phash = imagehash.phash(resized_img)
                    phash_bytes = phash.hash.tobytes()

                    hashes.append(phash_bytes)

                except Exception as e:
                    print(f"ハッシュの計算で失敗: {e}")
        cap.release()
    
    except Exception as e:
        print(f"エラー{e}")

    return hashes
        
def connect_db():
    return mysql.connector.connect(**DB_CONFIG)


def calcDistance(phash1, phash2):
    def to_int(p):
        if isinstance(p, bytes):
            return int.from_bytes(p, byteorder='big')
        elif isinstance(p, str):
            return int(p[2:], 16) if p.startswith('0x') else int(p, 16)
        elif isinstance(p, int):
            return p
        else:
            raise ValueError("Unsupported phash type")

    int1 = to_int(phash1)
    int2 = to_int(phash2)
    distance = bin(int1 ^ int2).count('1')
    similarity = (64 - distance) / 64
    return similarity

def getVideoPath(rootDir,videoId):
    conn=connect_db()
    cursor = conn.cursor()
    
    sql ="""
    select anime.foldername,video.fname,video.ext 
    from video 
    join anime on video.anime_id=anime.id
    where video.video_id=%s
    """
    cursor.execute(sql,[videoId])
    sqlResult=cursor.fetchall()
    animeFolder=sqlResult[0][0]
    fname=sqlResult[0][1]+sqlResult[0][2]
    videoPath=Path(rootDir,animeFolder,fname)
    
    cursor.close()
    conn.close()
    
    return videoPath

def get_root_dir():
    url = f"http://localhost:{PORT}/anime-web/api/setting/"
    response = requests.get(url)
    return json.loads(response.text)['documentRoot']

#フレームごとの最大一致率を取得
def similar(searchPHash,targetPhash):
    resultSim=[]
    for searchP in searchPHash:
        maxSim=0
        for targetP in targetPhash:
            currernt=max(calcDistance(searchP,targetP[0]),maxSim)
            if currernt>maxSim:
                maxSim=currernt

        if maxSim<similarThreshold:
            maxSim=0
        resultSim.append(maxSim)
    return resultSim

if __name__ == "__main__":

    
    


    parser = argparse.ArgumentParser()
    parser.add_argument('filename')  # 必須の位置引数
    parser.add_argument('-v', '--verbose', action='store_true')

    args = parser.parse_args()


    fpath=args.filename
    if(not os.path.isfile(fpath)):
        print("ファイルが見つかりませんでした", file=sys.stderr)
        exit()

    fname, ext = os.path.splitext(fpath)
    fname=os.path.basename(fname)
    if ext not in [".ts",".mkv",".mp4",".flv",".webm"]:
        print("動画ファイルのパスを引数にしてください。", file=sys.stderr)
        exit()
        
    sql="""
    SELECT fname, id FROM (
        SELECT 
        RANK() OVER (ORDER BY LENGTH(originalName) DESC) AS rnk,
        'A' AS grp,
        originalName AS fname,
        id
    FROM anime
    UNION ALL
    SELECT 
        RANK() OVER (ORDER BY LENGTH(fname) DESC) AS rnk,
        'B' AS grp,
        fname,
        anime_id
     FROM alias
     ) AS t
    ORDER BY grp, rnk
    """
    titleList=execute_query(sql)
    myTitle=""
    myAnimeId=""
    for row in titleList:
        title=row[0]
        animeId=row[1]
        if title in fname:
            myTitle=title
            myAnimeId=animeId
            break
    #print(f"番組名「{myTitle}」がヒットしました。")
    if myTitle =="":
        print("登録されている番組と一致しませんでした。", file=sys.stderr)
        sys.exit(100) 
        exit()
    
    

    
    #同じanime_idの番組を取得
    videoIdsql=r"""
    select video_id,
    concat(%s,anime.foldername,'\\',video_info.fname,video_info.ext)
    from video 
    join (select distinct video_id from phash_video) as phash_video  
    using(video_id) 
    join video_info using(video_id)
    join anime on video_info.anime_id=anime.id
    
    where video_info.anime_id = %s"""
    videoIdList=execute_query(videoIdsql,[get_root_dir()+'content\\anime-web\\anime\\video\\',myAnimeId])
    
    if not  videoIdList :
        print("同番組のpHashがありませんでした。animeId:",myAnimeId, file=sys.stderr)
        sys.exit(200) 
        exit()
        
    
    #引数で指定した動画のハッシュを取得
    
    phashes=videoFrameHash(fpath,30,(64,36))
    
    targetVideoPhashSql="select distinct phash from phash_video where video_id =%s"
    #print(videoIdList)
    
    currentSim=0
    for row in videoIdList:
        videoId,fname=row
        #print(fname)
        targetPhash=execute_query(targetVideoPhashSql,[videoId])
        
        
        if not targetPhash:
            continue
        

        
        sim=similar(phashes,targetPhash)


            
        avgSim=sum(sim)/len(sim)
        
        if args.verbose:
            print(str(avgSim)+":"+fname)
            
            
        if currentSim<avgSim:
            currentSim=avgSim
    print(currentSim)
   
    