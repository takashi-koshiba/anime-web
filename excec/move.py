import mysql.connector
import requests
import json
import shutil
import glob
import os

#エンコードした動画ファイルを個々のディレクトリに移動します。

mkvFilesPath='D:\\TV\\ts\\encoded\\' #エンコードした動画があるフォルダ


if __name__ == "__main__":
    conn = mysql.connector.connect(
        host="localhost",
        user="java",
        password="java",
        database="db1"
    )
    

    cursor = conn.cursor()
    select_all_data_query = "SELECT originalName,foldername FROM anime ORDER BY length(originalName) DESC ;"
    cursor.execute(select_all_data_query)
    result = cursor.fetchall()
    
    cursor.close()
    conn.close()
    
    
    
    #移動先のルートディレクトリを取得
    url = "http://localhost:8080/anime-web/api/setting/"
    r = requests.get(url)
    rootPath=json.loads(r.text)['documentRoot']
    
    if not os.path.isdir(rootPath):
        print(rootPath+"は存在しません")
        exit()
        
    
    print("ルートディレクトリ:"+rootPath)


    
    ############mkvファイルを移動###############################
    

    mkvFiles = glob.glob(mkvFilesPath+'*.mkv')
    for row in result:
        
        for videoPath in mkvFiles:
            if row[0] in videoPath :
                print(row[0]+videoPath)
                sender=rootPath+"content\\anime-web\\anime\\video\\"+row[1]+"\\"
                if os.path.isdir(sender):
                    print(row[0]+"  ディレクトリに移動")
    
                    new_path = shutil.move(videoPath , sender)
                else:
                    print("パスが存在しません"+sender)
    
    
