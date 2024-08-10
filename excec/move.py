import mysql.connector
import requests
import json
import shutil
import glob
import os
import subprocess
import sys
import re
TS_FILES_DIR = 'D:\\TV\\ts\\'  #移動元のTSファイルのディレクトリ
MKV_FILES_PATH = 'D:\\TV\\ts\\encoded\\'  # エンコードしたmkv動画があるディレクトリ
COMP_DIR = 'D:\\TV\\ts\\temp\\'  # DBでTSファイルの名前が一致したら移動するためのディレクトリ


DB_CONFIG = {
    'host': "localhost",
    'user': "java",
    'password': "java",
    'database': "db1"
}


def connect_db():
    return mysql.connector.connect(**DB_CONFIG)


def execute_query(query, fetch_all=True):
    conn = connect_db()
    cursor = conn.cursor()
    cursor.execute(query)
    result = cursor.fetchall() if fetch_all else cursor.fetchone()
    cursor.close()
    conn.close()
    return result


def insert_query(query):
    try:
        conn = connect_db()
        cursor = conn.cursor()
        cursor.execute(query)
    except Exception:
        print("error:"+query)
        conn.rollback()
        conn.commit()
        cursor.close()
        conn.close()
        sys.exit()
        
    finally:
        conn.commit()
        cursor.close()
        conn.close()
        

    
        



def get_root_dir():
    url = "http://localhost:8080/anime-web/api/setting/"
    response = requests.get(url)
    return json.loads(response.text)['documentRoot']


def get_video_id(video_path):
    basename = os.path.splitext(os.path.basename(video_path))[0]
    query = f"SELECT video_id FROM video WHERE fname='{basename}';"
    result = execute_query(query)
    return result[0][0] if result else None


def process_ffprobe(video):
    cmd = f"ffprobe -hide_banner -v error -print_format json -show_streams \"{video}\""
    process = subprocess.run(cmd, shell=True, capture_output=True, text=True, encoding='utf-8')
    return json.loads(process.stdout)

def process_outputImg(root_dir,  foldername,mkv_file):
    new_dir = root_dir+'content\\anime-web\\anime\\img\\'+foldername
    os.makedirs(new_dir,exist_ok=True)
    
    cmd = f"ffmpeg  -i \"{mkv_file}\" -r 1/60 -vf scale=-1:480 -q:v 3 \"{new_dir}\\output_%03d.jpg\""
    subprocess.run(cmd, shell=True, capture_output=True, text=True, encoding='utf-8')


def insertVideoInfo(video, video_id):
    video_info = process_ffprobe(video)
    streams_info = video_info.get('streams', [])

    for value in streams_info:
        tags = value.get('tags', {})
        title = tags.get('title', '')
        if 'NicoJK' in title:
            video_time = tags.get('DURATION-eng')
            come_byte = tags.get('NUMBER_OF_BYTES-eng')
            date = StrToDate(os.path.splitext(os.path.basename(video))[0])
            print(date,video_time,come_byte,video_id)

            query = f'''
                INSERT INTO jk_rownumber (video_id, come_byte, video_time,hiduke) 
                VALUES({video_id}, "{come_byte}", "{video_time}","{date}")
            '''
            insert_query(query)

            break


def move_files_to_folder(files, destination_folder):
    for file_path in files:
        shutil.move(file_path, destination_folder)


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
                move_files_to_folder([ts_file], COMP_DIR)

    #DBと一致したらmkvファイルを移動する
    mkv_files = glob.glob(os.path.join(MKV_FILES_PATH, '*.mkv'))
    mkv_files.sort(key=os.path.getmtime)

    for anime_name, foldername, anime_id in anime_data:
        for mkv_file in mkv_files:
            if anime_name in mkv_file:
                video_dir = os.path.join(root_dir, "content\\anime-web\\anime\\video\\", foldername)
                if os.path.isdir(video_dir):
                    filebasename=os.path.splitext(os.path.basename(mkv_file))[0]
                    insertVideoName(anime_id,filebasename)#ファイル名を書き込み
                    video_id = get_video_id(filebasename)
                    insertVideoInfo(mkv_file, video_id)#コメント数などを書き込み
                    process_outputImg(root_dir,foldername,mkv_file)#画像の切り出し
                    move_files_to_folder([mkv_file], video_dir)#移動
                else:
                    print(f"Directory does not exist: {video_dir}")


def StrToDate(str):
    pattern = [r'\d{8}', r'\d{4}-\d{2}-\d{2}', r'2\d{5}']
    print(f"Input string: {str}")
    
    for p in pattern:
        match = re.search(p, str)  # Use re.search to find the pattern anywhere in the string
        if match:  
            print(f"Pattern matched: {p}")
            return match.group()  # Return the matched string
    
    print("No pattern matched")
    return None  # Return None if no pattern matches

# Example usage:

def insertVideoName(anime_id,fname):
    sql=f"INSERT INTO video (anime_id,fname) VALUES({anime_id},\"{fname}\")"
    insert_query(sql)
    
if __name__ == "__main__":
    main()
