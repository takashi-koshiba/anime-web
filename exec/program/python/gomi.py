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

DB_CONFIG = {
    'host': "localhost",
    'user': "java",
    'password': "java",
    'database': "db1"
}



PORT=8082  #実行しているjavaのポート番号


def main():
    root= get_root_dir()
    path=root+"content\\anime-web\\anime\\img\\"
    
    sql="""
    select title from excludeAnime order by id;

    """
    
    ♯inputPath = "D:\\TV\\ts\\"          #########いらない動画の移動元#############環境に合わせてください
    ♯outputPath="D:\\TV\\ts\\gomi\\"     #########いらない動画の移動先#############環境に合わせてください
    if not os.path.isdir(inputPath):
        print("パスがありません"+inputPath )
        return 
    
    
    if  not os.path.isdir(outputPath):
        print("パスがありません"+outputPath)
        return 
    
    
    result=execute_query(sql)

    

    
    for  query in result:
        print(query[0])
        es=glob.escape(query[0])
        print(f'{inputPath}*{query[0]}*.ts')
        for file in glob.glob(f'{inputPath}*{es}*.ts'):
            destination_path = f'{outputPath}' + os.path.basename(file)
            try:
                os.rename(file, destination_path)

            except Exception as e:
                print("Error details: " + str(e))  


            print(f"'{file}' was successfully moved to '{destination_path}'")
        
        
        
       # print(inputPath+"*"+query+"*")
        #new_path = shutil.move(inputPath+"*"+query+"*", outputPath)
        
        
        
    print("終了")
   
    
    

def get_root_dir():
    url = f"http://localhost:{PORT}/anime-web/api/setting/"
    response = requests.get(url)
    return json.loads(response.text)['documentRoot']

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


def insert_query(query,value):
    try:

        conn = connect_db()  
        cursor = conn.cursor()
        cursor.execute(query,value)  
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

if __name__ == "__main__":
    main()