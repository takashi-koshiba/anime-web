#####################
##録画時に保存される番組表の内容をデータをDBに書き込み
#####################

import mysql.connector

import os

from tqdm import tqdm







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


def main():
     
    # 検索対象の文字列リスト
    start_keywords = [
        '- ◇番組内容', '- 番組内容', '◇番組内容', '番組内容', '- あらすじ◇',
        '番組内容①', '番組内容②', '番組内容③', '番組内容④', 'あらすじ◇',
        'ＴＯＫＹＯ　ＭＸ１', 'ＮＨＫ総合１・東京', 'フジテレビ', 'テレビ東京１','テレビ朝日'
        'Ｊ：ＣＯＭテレビ', '番組内容', '詳細情報','Ｊ：ＣＯＭテレビ'
    ]    
    
    end_keywords = [
        '- 出演者', '- ◇声の出演', '出演者', '◇声の出演', '- ◇キャスト', '出演者',
        '- おしらせ', '詳細情報', 'ジャンル : ', 'アニメ／特撮 - 国内アニメ',
        '出演者1', '出演者１', '- 出演者①','- 声の出演','声の出演','声の出演①','ジャンル :'
    ]
    except_keywords=[
        '[新]','[多]','[字]','[再]'
        ]
    
    #コメントアウトして番組内容が記載されている ts.program.txt ファイルがあるパスを書いてください。
    #なおファイル名は動画のファイル名と一致している必要があります。
    #rename_anime.phpを実行する際に下記パスを指定して同じファイル名になるようにしてください。
    
    #root_dir = r'D:\\java-web\\data\\bangumi\\'
    
    
    # クエリ結果を取得
    result = execute_query("SELECT anime_id,video_id,fname FROM video where video_id not in (select video_id from video_prog);")
    tq = tqdm(total=len(result))
 
   
    for text in result:
        tq.update(1)
        
        program_dir = os.path.join(root_dir, f"{text[2]}.ts.program.txt")
        if os.path.isfile(program_dir):
            start_flag, end_flag = execute(program_dir, start_keywords, end_keywords,except_keywords,text[1])
            
            if not start_flag or not end_flag:
                print(f"Start flag: {start_flag}, End flag: {end_flag}")
                print(f"Error: {program_dir}")
                break

        
    tq.close()
   
    
def execute(program_dir, start_keywords, end_keywords,except_keywords,video_id):
    try:
        with open(program_dir, 'r') as f:
            start_flag = False
            end_flag = False
            
            prog_text=""
            for line in f:
                line = line.strip()
                
                if line in start_keywords:
                    start_flag = True
                
                elif start_flag and line in end_keywords:
                    end_flag = True
                    break
                
                elif start_flag and line !=""  and  not instr(except_keywords,line):
                    prog_text=prog_text+line+"\n"
                    
                    
            if not start_flag:
                print("does not exist program details :videoid:"+str(video_id))
                
            sql = f"insert into video_prog (video_id,txt) values({video_id},\"{prog_text}\")"
            insert_query(sql);
                
            return start_flag, end_flag

    except Exception as e:
        print(f"Error querry {program_dir}: {e}")
        return False, False

def instr(arr,txt):
    for i in arr:
        if i in txt:
            return True
    return False
    

if __name__ == "__main__":
    main()
    