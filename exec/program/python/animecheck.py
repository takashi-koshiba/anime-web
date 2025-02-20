import mysql.connector
import requests
import json
import shutil
import glob
import os
import subprocess
import glob
import re
import cv2
from tqdm import tqdm
import sys

PORT=8082  #実行しているjavaのポート番号



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





def get_root_dir():
    url = f"http://localhost:{PORT}/anime-web/api/setting/"
    response = requests.get(url)
    return json.loads(response.text)['documentRoot']

def getAnime():
    query = "SELECT id,originalName,foldername FROM anime;"

    return execute_query(query, "")
    

def getVideo():
    query="select video_id,concat(foldername,'\\\\',fname,ext) from video join anime on video.anime_id=anime.id ;"

    return execute_query(query, "")
    
def isExistDBToFolder(videoPath,animeResult,videoResult):
    print("--------DBにないフォルダが存在するか---------------")
    print("")
    videoTitle = [videoPath+row[1] for row in videoResult]
    #print(videoTitle)
    #DBに登録されているフォルダが存在するか
    for animeId,originalName,foldername in animeResult:
        if not os.path.isdir(videoPath+foldername):
            print("DBに登録されているアニメのフォルダが存在しません。:"+videoPath+foldername)
        else:
            #動画がDBに登録されているか
            
            files = glob.glob(videoPath+foldername+"\\*")

            for file in files:
                if not file in videoTitle:   
                   print("DBに登録されていません"+file)
                 
            
            
def isExistFolderToDB(videoPath,animeResult):
    print("--------DBに登録されていないフォルダがあるか---------------")
    print("")
    folderName=[row[2] for row in animeResult]
    files = glob.glob(videoPath+"*")
    for file in files:
        if not os.path.basename(os.path.dirname(file+"\\")) in folderName:
            print("DBにアニメが登録されてません: "+os.path.basename(os.path.dirname(file+"\\")))
    #print(folderName)

def isExistDBtoVideo(videoResult,videoPath):
    print("-----------DBに登録されているアニメがアニメのディレクトリ配下に存在するか-------------------------")
    print("")
    for videoId ,videoTitle in videoResult:
        if not os.path.isfile(videoPath+videoTitle):
            
            #動画がない場合はDBを削除する
            print("動画が存在しません。DBを削除します。"+videoPath+videoTitle)
            query = f'''
               delete from video where video_id = %s
            '''
     
            execute_query(query,(videoId,))
            
            query = f'''
               delete from video_info where video_id = %s
            '''
     
            execute_query(query,(videoId,))
            
            
def main():
    
    root=get_root_dir()
    videoPath=root+"content\\anime-web\\anime\\video\\"

    animeResult=getAnime()
    videoResult=getVideo()
    
    #DBに登録されているフォルダが存在するか
    isExistDBToFolder(videoPath,animeResult,videoResult)
    
    #フォルダがDBに登録されているか
    isExistFolderToDB(videoPath,animeResult)

    videoResult=getVideo()
    
    #DBのアニメがディレクトリ配下に存在するか
    isExistDBtoVideo(videoResult,videoPath)
    
    
if __name__ == "__main__":
    main()
    
