from multiprocessing import Pool

from concurrent.futures import ProcessPoolExecutor

from concurrent.futures import ThreadPoolExecutor, as_completed
from multiprocessing import Process
from pathlib import Path
import mysql.connector
import requests
import json
import shutil
import glob
import os
import subprocess
from PIL import Image
import imagehash
import subprocess
import multiprocessing
from tqdm import tqdm
import numpy as np
import cv2
import math
import msvcrt
import sys
from logging import getLogger, FileHandler, StreamHandler, DEBUG, Formatter

logger = getLogger(__name__)
logger.setLevel(DEBUG)

# ファイルハンドラー
log_path = Path(__file__).resolve().parent / 'sim.log'
file_handler = FileHandler(log_path, encoding='utf-8')
file_handler.setLevel(DEBUG)

# コンソールハンドラー
console_handler = StreamHandler()
console_handler.setLevel(DEBUG)

# 共通フォーマット
formatter = Formatter('%(asctime)s - %(levelname)s - %(message)s')
file_handler.setFormatter(formatter)
console_handler.setFormatter(formatter)

# ハンドラーを追加
logger.addHandler(file_handler)
logger.addHandler(console_handler)
logger.propagate = False


DB_CONFIG = {
    'host': "localhost",
    'user': "java",
    'password': "java",
    'database': "db1"
}

similarThreshold=0.87
num_workers = min (multiprocessing.cpu_count(),12)

def connect_db():
    return mysql.connector.connect(**DB_CONFIG)

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





    



PORT=8082  #実行しているjavaのポート番号





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


def avgSimilar(searchId,targetId):
    max_count=90 #比較するフレーム数

    result_sim=[]
    conn=connect_db()
    cursor = conn.cursor()
    
    
    for i in range(max_count):  
        current=getMaxSimilarity(searchId,targetId,max_count,i,cursor)
        if current<similarThreshold:
            current=0
            
        result_sim.append(current)
    
    logger.debug(str(searchId)+","+str(targetId))
    logger.debug(result_sim)
    logger.debug(sum(result_sim)/len(result_sim))
        
    cursor.close()
    conn.close()
    return sum(result_sim)/len(result_sim)


def getMaxSimilarity(searchId,targetId,max_count,current_count,cursor):
    searchSql = """
        SELECT  No,s.phash AS search_phash, t.phash AS target_phash
        FROM (
            SELECT * FROM (
                SELECT (ROW_NUMBER() OVER (ORDER BY phash) - 1) AS No, phash
                FROM phash_video
                WHERE video_id = %s
            ) AS cut
            where No=(select ( TRUNCATE(count(*)/%s,0))*%s  from phash_video WHERE video_id = %s)
            ) AS s
        CROSS JOIN (
            SELECT distinct phash FROM phash_video WHERE video_id = %s
        ) AS t
        order by No;
    """
    
    param=[searchId,max_count,current_count,searchId,targetId]
    cursor.execute(searchSql,param)
    phashList=cursor.fetchall()
    similarMax=0
    
    for phash in phashList:
        search=phash[1]
        target=phash[2]
        
        similarMax=max(calcDistance(search,target),similarMax)

    return similarMax

 
def get_root_dir():
    url = f"http://localhost:{PORT}/anime-web/api/setting/"
    response = requests.get(url)
    return json.loads(response.text)['documentRoot']

# 中央80%をクロップする関数
def asymmetric_crop(pil_img, left_ratio=0.0, top_ratio=0.3, right_ratio=0.0, bottom_ratio=0.3):
    w, h = pil_img.size
    left = int(w * left_ratio)
    top = int(h * top_ratio)
    right = w - int(w * right_ratio)
    bottom = h - int(h * bottom_ratio)
    return pil_img.crop((left, top, right, bottom))


def getFramePhash(video_path,videoId, resize_to=(64, 36)):
    cap = cv2.VideoCapture(video_path)

    #print("ハッシュをリストに書き込み中")
    #print(str(video_path))
    if not cap.isOpened():
        
        raise IOError("動画を開けません"+str(video_path))

    result= []
    try:
        count=cap.get(cv2.CAP_PROP_FRAME_COUNT)
        progress = tqdm(total=count , desc="動画をフレームに変換中", postfix="", ncols=80)
        while True:
            ret, frame = cap.read()
            
            if not ret or frame is None:
                break
                
            try:
                # OpenCVのBGR画像をPILのRGB画像に変換
                pil_img = Image.fromarray(cv2.cvtColor(frame, cv2.COLOR_BGR2RGB))

                
                # クロップ
                cropped_img = asymmetric_crop(pil_img)
                # リサイズ（高速化のため）

                resized_img = cropped_img.resize(resize_to, Image.Resampling.BILINEAR)

                

                # pHash計算
                phash = imagehash.phash(resized_img)
                phash_bytes = phash.hash.tobytes()

                result.append((videoId, phash_bytes))
                

  

            except Exception as e:
                print(f"エラー発生: {e}")

            

            progress.update(1)
        progress.set_description("完了済") 
        progress.update(0) 
        progress.close()    
        

    except Exception as e:
        print("エラー",e)

    finally:
        cap.release()
    return   result

    
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
    
def commandFFmpeg(inputPath,ouputPath):
    
    framePath=Path(ouputPath,"frame_%09d.webp")
    result = subprocess.run([
    'ffmpeg',
    '-i', str(inputPath),
    '-vf', 'mpdecimate,scale=16:9',
    '-an', '-vsync', '0',
    '-q:v', '31',
    '-c:v', 'libwebp',
    str(framePath)
   ],
        capture_output=True,
        text=True,
        encoding='utf-8',  
        errors='ignore'   
    )
    print(result.stdout)
    print(result.stderr)

def mkFrameDir(root,animeFolder,fName):
    frameDir=Path(root,"content/anime-web/videoFrames",animeFolder,fName)
    
    # フォルダが存在しなければ作成（親ディレクトリも作る）
    frameDir.mkdir(parents=True, exist_ok=True)
    return frameDir
    
def compute_phash_from_frame(args):
    frame,resize_to,videoId=args

    result=[]
    try:
        # OpenCVのBGR画像をPILのRGB画像に変換
        pil_img = Image.fromarray(cv2.cvtColor(frame, cv2.COLOR_BGR2RGB))

        # リサイズ（高速化のため）
        resized_img = pil_img.resize(resize_to, Image.Resampling.BILINEAR)

        # pHash計算
        phash = imagehash.phash(resized_img)
        phash_bytes = phash.hash.tobytes()

        result.append((videoId, phash_bytes))

    except Exception as e:
        print(f"エラー: {e}")
    return result

#pHashをリストに入れる
def write_phash(args):
    try:
        videoId, rootDir = args
        videoFolder = Path(rootDir, "content/anime-web/anime/video")
        searchVideoPath = getVideoPath(videoFolder, videoId)
        resize = (64, 36)
        
        results = getFramePhash(searchVideoPath, videoId, resize_to=resize)
       
      
    
        return results  # pHash 結果を返す
    except Exception as e:
        print(f"エラー2{e}")
    
def similarity2DB(args):
    conn=connect_db()
    cursor = conn.cursor()

    searchVideoId, targetVideoId = args
    avgSim = avgSimilar(searchVideoId, targetVideoId)

    avgSimSql = """
        INSERT INTO duplicatevideo (videoId_search, videoId_target, similar)
        VALUES (%s, %s, %s)
    """
    param = [searchVideoId, targetVideoId, avgSim]
    cursor.execute(avgSimSql, param)
    conn.commit() 
    
    cursor.close()
    conn.close()

    return 1  # 進捗カウント用

def write_phash_thread(args):
    conn=connect_db()
    cursor = conn.cursor()

    r=write_phash(args)
    #取得したphashをDBにいれる
    for frame in r:
        video_id = frame[0]
        phash = frame[1]
        param = [video_id, phash]
        sql = "INSERT IGNORE INTO phash_video (video_id, phash) VALUES (%s, %s)"
        cursor.execute(sql, param)
    conn.commit()
    cursor.close()
    conn.close()
if __name__ == "__main__":
    

    script_dir = os.path.dirname(os.path.abspath(__file__))

    lockfile_path = os.path.join(script_dir, "simlarVideo.lock")

    #排他制御
    try:
        print(f"ロックファイルの絶対パス: {lockfile_path}")
        lockfile = open(lockfile_path, "w")
        try:
            msvcrt.locking(lockfile.fileno(), msvcrt.LK_NBLCK, 1)  
        except OSError as e:
            print(f"ロック取得失敗（すでに起動中の可能性）: {e}")
            import time; time.sleep(10)
            lockfile.close()
            sys.exit(1)
    except IOError as e:
        print(f"ロックファイル作成時にエラー: {e}")
        import time; time.sleep(10)
        lockfile.close()
        sys.exit(1)
    
    try:
        rootDir=get_root_dir()
    
       
    
        videoPhashSql="""
        select video_id from video 
        where video_id not in (select distinct video_id from phash_video)
        
        -- and  anime_id = 9496
        
        order by video_id desc 
         -- limit 0
        """
    
    
        searchVideoSql="""
        select video_id,video.anime_id,anime.foldername,concat(video.fname,video.ext) as fname from video 
        join video_info using(video_id)
        join anime on anime.id=video.anime_id
    
        -- 処理済みは除外
        where video_id not in (select videoId_search from duplicateVideo) 
        
        -- 同じanimeIdの動画が2個以上
        and video.anime_id in (select anime_id from video group by anime_id having count(*)>=2)
        
        -- phashを計算済み
        and video.video_id in (select video_id from phash_video)
        order by hiduke 
        """
        conn=connect_db()
        cursor = conn.cursor()
        
        cursor.execute(videoPhashSql)
        videoPhashList=cursor.fetchall()
        
        cursor.close()
        conn.close()
        
        #動画の数だけphashを計算する
        args_list = [(row[0], str(rootDir)) for row in videoPhashList]
        
        with ThreadPoolExecutor(max_workers=num_workers) as tpe, tqdm(total=len(args_list), desc="全体進捗", ncols=80) as progress:
            futures = [tpe.submit(write_phash_thread, args) for args in args_list]
        
            for future in as_completed(futures):
                try:
                    future.result()
                except Exception as e:
                    print(f"[ERROR] スレッドで例外: {e}")
                finally:
                    progress.update(1)
         
        logger.debug("書き込みが終了しました。")
        logger.debug("類似度を計算中")

        #類似計算
        
        conn=connect_db()
        cursor = conn.cursor()
        
        cursor.execute(searchVideoSql)
        searchList=cursor.fetchall()
        
        cursor.close()
        conn.close()
        progress_similar = tqdm(total=len(searchList) , desc="類似度計算の進捗", postfix="", ncols=80)
        
        for searchVideoRow in searchList:
            searchVideoId=searchVideoRow[0]
            searchAnimeId=searchVideoRow[1]
            folderName=searchVideoRow[2]
            fname=searchVideoRow[3]

            #commandFFmpeg(searchVideoPath,frameDir)
            #重複フレームを削除
            #delDuplicateFiles(frameDir)
            
            
            #指定した動画idよりも前の動画を列挙
            targetVideoSql="""
            select video_id from video 
            
            -- 先にDBに登録されたものより後に登録されたものを「再放送」と扱います。
            -- 指定した番組と同じアニメであり、指定した番組よりもvideo_idが前の動画を列挙。
            where anime_id=%s and video_id <%s
            
            -- 類似度が高い動画は後に削除する必要があるため、計算する必要がありません。よって、検索対象から除外する。
            and video_id not in(select videoId_search from duplicateVideo where similar>%s )
            
            -- phashを計算済み
            and video.video_id in (select video_id from phash_video)
            
            """
            conn=connect_db()
            cursor = conn.cursor()
            
            param=[searchAnimeId,searchVideoId,similarThreshold]
            cursor.execute(targetVideoSql,param)
            targetList=cursor.fetchall()
            
            cursor.close()
            conn.close()
            
            #類似度をDBに書き込み
            args_list = [(searchVideoId, row[0]) for row in targetList]
            

            
            with Pool(processes=num_workers) as pool, tqdm(total=len(args_list), desc="類似度書き込み", ncols=80) as progress:
                for _ in pool.imap_unordered(similarity2DB, args_list):
                    progress.update(1)

            progress_similar.update(1)

        
        
    except Exception as e:
        print("エラー",e)
    finally:
        conn.close()
        cursor.close()
        

        # ロック解除
        lockfile.close()
        os.remove(lockfile_path)
    