import mysql.connector
import requests
import json
import shutil
import glob
import os
import subprocess

import re
import cv2
from tqdm import tqdm
import sys


sys.stdout.reconfigure(encoding='utf-8', errors='ignore')
TS_FILES_DIR = 'D:\\TV\\ts\\'  #移動元のTSファイルのディレクトリ
MKV_FILES_PATH = 'D:\\TV\\ts\\encoded\\'  # エンコードしたmkv動画があるディレクトリ
TEMP_DIR = 'D:\\TV\\ts\\encoding\\'  # DB一致したタイトルのTSファイルの移動先ディレクトリ
PORT=8080  #実行しているjavaのポート番号

DB_CONFIG = {
    'host': "localhost",
    'user': "java",
    'password': "java",
    'database': "db1"
}


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


def insert_query(query):
    try:

        conn = connect_db()  
        cursor = conn.cursor()
        cursor.execute(query)  
        code = 0
    except Exception as e:
      #  print("Error querry: " + query)
        print("Error details: " + str(e))  
        code = 1
    finally:
        conn.commit()  
        cursor.close() 
        conn.close()  
    
    return code


def get_root_dir():
    url = f"http://localhost:{PORT}/anime-web/api/setting/"
    response = requests.get(url)
    return json.loads(response.text)['documentRoot']


def get_video_id(video_path):
 
    basename = os.path.splitext(os.path.basename(video_path))[0]
    
 
    query = "SELECT video_id FROM video WHERE fname=%s;"
    params = (basename,)

    result = execute_query(query, params)
    
    # 結果が存在する場合、そのvideo_idを返す。存在しない場合はNoneを返す。
    return result[0][0] if result else None


def process_ffprobe(video):
    cmd = f"ffprobe -hide_banner -v error -print_format json -show_streams \"{video}\""
    process = subprocess.run(cmd, shell=True, capture_output=True, text=True, encoding='utf-8')
    return json.loads(process.stdout)

def process_outputImg(root_dir,  foldername,mkv_file):
    try:
        
        new_dir = os.path.join(root_dir, "content\\anime-web\\anime\\img\\", foldername,os.path.splitext(os.path.basename(mkv_file))[0])
        os.makedirs(new_dir,exist_ok=True)
        
        cap = cv2.VideoCapture(mkv_file)
        totalframecount = int(cap.get(cv2.CAP_PROP_FRAME_COUNT))
        video_fps = cap.get(cv2.CAP_PROP_FPS)                 # フレームレートを取得する
        video_len_sec = totalframecount / video_fps         
        
        loopCount=10
        loopLenSec=int(video_len_sec/loopCount)
                      
        tq = tqdm(total = loopCount)
        for   i in range(loopCount):
            #cmd = f"ffmpeg  -i \"{mkv_file}\"  -vf scale=-1:480 -q:v 3 \"{new_dir}\\output_%03d.jpg\""
            cmd = f"echo Y| ffmpeg -ss {loopLenSec*i} -i \"{mkv_file}\"  -vsync vfr  -vf scale=640:360 -q:v 3 -frames:v 1 \"{new_dir}\\output_{i}.jpg\" "
            subprocess.run(cmd, shell=True, capture_output=True, text=True, encoding='utf-8')
            
            tq.update(1)
        tq.close()
        
    except Exception as e:
        print("Error details: " + str(e))  

def insertVideoInfo(video, video_id):
    video_info = process_ffprobe(video)
    streams_info = video_info.get('streams', [])
    err=0
    come_byte=0
    video_time=""
    date=None
    for value in streams_info:
        tags = value.get('tags', {})
        title = tags.get('title', '')
        
        video_time = tags.get('DURATION-eng')
        date = StrToDate(os.path.splitext(os.path.basename(video))[0])
        if 'NicoJK' in title:
            come_byte = tags.get('NUMBER_OF_BYTES-eng')
            break
            
    query = f'''
        INSERT INTO jk_rownumber (video_id, come_byte, video_time,hiduke) 
        VALUES({video_id}, "{come_byte}", "{video_time}",
         {"NULL" if not date else f'"{date}"'}
        )
    '''
    err = insert_query(query)
    return err 
        
    
    

def move_files_to_folder(files, destination_folder,videoid=0):

    try:
        
        #for file_path in files:
        print(files)
        shutil.move(files, destination_folder)
            
    except Exception as e:
        print("Error details: " + str(e))  
        if videoid ==0:
            pass
        
        sql=f"delete from video where video_id ={videoid}"
        insert_query(sql)
        

def main():
    root_dir = get_root_dir()
    if not os.path.isdir(root_dir):
        print(f"{root_dir} does not exist")
        sys.exit(1)

    print(f"Root directory: {root_dir}")

    anime_data = execute_query("SELECT originalName, foldername, id FROM anime ORDER BY length(originalName) DESC")

    #DBに一致したらTSファイルを移動する
    ts_files = glob.glob(os.path.join(TS_FILES_DIR, '*.ts'))
    for anime_name, _, _ in anime_data:
        for ts_file in ts_files:
            if anime_name in ts_file:
                print(TEMP_DIR)
                move_files_to_folder(ts_file, TEMP_DIR)
                print(ts_file)

    #DBと一致したらエンコードしたmkvファイルを移動する
    mkv_files = glob.glob(os.path.join(MKV_FILES_PATH, '*.mkv'))
    mkv_files.sort(key=os.path.getmtime)
    mkvCount=0
    for anime_name, foldername, anime_id in anime_data:
        mkvCount= mkvCount+1
        for mkv_file in mkv_files:
            
            
            if anime_name in mkv_file:
                video_dir = os.path.join(root_dir, "content\\anime-web\\anime\\video\\", foldername)
                if os.path.isdir(video_dir):
                    print(str(mkvCount)+"/"+str(len(anime_data)))
                    filebasename=os.path.splitext(os.path.basename(mkv_file))[0]
                    err = insertVideoName(anime_id,filebasename)#ファイル名を書き込み
                    if err==1 :
                        print("error:Failed to write video table:"+mkv_file)
                        continue

                        
                        
                        
                    video_id = get_video_id(filebasename)
                    err = insertVideoInfo(mkv_file, video_id)#コメント数などを書き込み
                    
                    if err ==1:
                        print("info:Failed to write video information.")
                        
                    process_outputImg(root_dir,foldername,mkv_file)#画像の切り出し
                    move_files_to_folder(mkv_file, video_dir,video_id)#移動
                    
                        
                else:
                    print(f"Directory does not exist: {video_dir}")
    insertRanking()

def insertRanking():
    sql="""
        delete from ranking;
        """
    insert_query(sql)
    
    sql ="""
    

        insert into ranking ( all_ranking, anime_id, year, season, mediun_come_byte,T_count) (
        select * from (
            select RANK() OVER(ORDER BY come_byte DESC) as all_ranking, anime_id,year,season,come_byte as mediun_come_byte,tt.T_count from (
                select *,ROW_NUMBER() OVER(PARTITION BY anime_id,year,season ORDER BY come_byte) as rownumber from
                (
                select anime_id,come_byte/time_to_sec(video_time) as come_byte,YEAR(hiduke) as year,
                    case 
                        when MONTH(hiduke) <=3 then 1
                        when MONTH(hiduke) <=6 then 2
                        when MONTH(hiduke) <=9 then 3
                        when MONTH(hiduke) <=12 then 4
                        else 0
                    end 'season' ,offset,T_year,T_season,offsetT.T_count
            
                    from video join jk_rownumber using(video_id)
            
                    -- 行数を２で割る
                    cross join (
                        select anime_id,CEILING(count(*)/2) as offset,YEAR(hiduke) as T_year,
                        case 
                            when MONTH(hiduke) <=3 then 1
                            when MONTH(hiduke) <=6 then 2
                            when MONTH(hiduke) <=9 then 3
                            when MONTH(hiduke) <=12 then 4
                            else 0
                        end 'T_season',count(*) as T_count
            
                        from video left join jk_rownumber using(video_id) 
                        where  fname not like  "%マルチ1" and fname not like "%[再]%" and fname not like "%-1" and come_byte>0  and hiduke is not null
                        group by anime_id,T_year,T_season
                    )as offsetT using(anime_id)
            
            
            
                    where  fname not like  "%マルチ1" and fname not like "%[再]%" and fname not like "%-1" and come_byte>0  and hiduke is not null
            
            
            
                )as t
                where year=T_year and season=T_season
            
            )as tt
            where offset = rownumber
            )as ttt
        
            order by anime_id
        )
    """
    err = insert_query(sql)

    if err == 1:
        print("Failed to write ranking table.")
        
        
    sql="""
        delete from score;
        """
    insert_query(sql)
    
    
    sql="""
                    
            insert into score (
            select anime_id,kk.score,year,season from ranking join(
                select anime_id,
                (
                    sum(mediun_come_byte*T_count)/sum(T_count)-
                
                    -- 平均
                    (
                         select avg(come_byte) from (
                             select anime_id,sum(mediun_come_byte*T_count)/sum(T_count) as come_byte from ranking group by anime_id
                         )as avg_come_byte
                     ) 
            
                 )/(select STDDEV(come_byte) from (
                           select anime_id,sum(mediun_come_byte*T_count)/sum(T_count) as come_byte from ranking group by anime_id
                       )as STDDEV_come_byte
                    )*10+50 as score
                          
            
            from ranking group by anime_id
            
            ) as kk
            using (anime_id)
            )
        """
    insert_query(sql)
    
    if err == 1:
        print("Failed to write score table.")
        
    return err

def StrToDate(str):
    pattern = [r'[0-9]{8}', r'[0-9]{4}-[0-9]{2}-[0-9]{2}', r'[0-9]{6}']

    
    for p in pattern:
        match = re.search(p, str)
        if match:  
            print(f"Pattern matched: {p}")
            return match.group() 
    
    print("No pattern matched")
    return



def insertVideoName(anime_id,fname):
    sql=f"INSERT INTO video (anime_id,fname) VALUES({anime_id},\"{fname}\")"
    err = insert_query(sql)
    return err
if __name__ == "__main__":
    main()
