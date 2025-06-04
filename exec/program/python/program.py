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


def insert_query(query,values):
    try:
        conn = connect_db()  
        cursor = conn.cursor()
        cursor.execute(query,values)  
        code = 0
    except Exception as e:
        print("Error querry: " + query)
        print("Error details: " + str(e))  
        code = 1
    finally:
        conn.commit()  
        cursor.close() 
        conn.close()  
    
    return code


def main():
     
    keyword=[   #開始   ,終了のキーワード
            ['- 番組内容','- 監督・演出'],
            ['詳細情報','- キャスト'],
            ['- あらすじ◇','- 出演者'],
            ['- ◇番組内容','- ◇キャスト'],
            ['あらすじ◇','出演者'],
            ['詳細情報','- 出演者'],
            ['- ◇番組内容','- ◇声の出演'],
            ['- 番組内容','- 出演者'],
            ['- 番組内容','- 監督・演出'],
            ['- 番組内容','- 声の出演'],
            ['- 番組内容','- 出演者'],
            ['- 番組内容','- 監督・演出'],
            ['- 番組内容','- 監督・演出'],
            ['- 番組内容','ジャンル : '],
            ['あらすじ１','原作脚本'],
            
            ['詳細情報','- 出演者①'],
            ['- 番組概要','- 【声優①】'],
            ['詳細情報','- 出演者【声優】'],
            ['詳細情報','- 主な声の出演者'],
            ['詳細情報','原作脚本'],
            ['- あらすじ','- ＣＡＳＴ'],
            
            ['- 番組概要','- スタッフ'],
            ['- あらすじ１','- 原作脚本'],
            ['- あらすじ','- ＣＡＳＴ'],
            ['- あらすじ','- ＣＡＳＴ'],
            ['- あらすじ','- ＣＡＳＴ'],
            ['- あらすじ','- ＣＡＳＴ'],
            
            
        ]
    # 検索対象の文字列リスト
    #start_keywords = [
    #    '- ◇番組内容', '- 番組内容', '◇番組内容', '番組内容', '- あらすじ◇',
     #   '番組内容①', '番組内容②', '番組内容③', '番組内容④', 'あらすじ◇','あらすじ：',
      #  'ＴＯＫＹＯ　ＭＸ１', 'ＮＨＫ総合１・東京', 'フジテレビ', 'テレビ東京１','テレビ朝日'
       # 'Ｊ：ＣＯＭテレビ', '番組内容', '詳細情報','Ｊ：ＣＯＭテレビ'
    #]    
    
    #end_keywords = [
    #    '- 出演者', '- ◇声の出演', '出演者', '◇声の出演', '- ◇キャスト', '出演者','- 監督・演出',
     #   '- おしらせ', '詳細情報', 'ジャンル : ', 'アニメ／特撮 - 国内アニメ',
      #  '出演者1', '出演者１', '- 出演者①','- 声の出演','声の出演','声の出演①','ジャンル :','原作脚本','◇キャスト',
       # '- 声優１','- 【声優①】','- 声の出演①','- 主な声の出演者','- 主なスタッフ','- 声優①','- 原作脚本','- 声の出演１',
        #'出演者①','- 原作脚本','- スタッフ１','- キャスト','- スタジオ出演者','【声優】','出演:'
    #]
    
    except_keywords=[
        '[新]','[多]','[字]','[再]'
        ]
    
    #コメントアウトして番組内容が記載されている ts.program.txt ファイルがあるパスを書いてください。
    #なおファイル名は動画のファイル名と一致している必要があります。
    #rename_anime.phpを実行する際に下記パスを指定して同じファイル名になるようにしてください。
    
    root_dir = r'D:\\java-web\\data\\bangumi\\' #環境に合わせて修正してください。
    if  not os.path.isdir(root_dir):
        print("パスがありません"+root_dir)
        return 
    
    # クエリ結果を取得
    result = execute_query("SELECT anime_id,video_id,fname FROM video where video_id not in (select video_id from video_prog);")
    tq = tqdm(total=len(result))
 

    for text in result:
        tq.update(1)
        
        program_dir = os.path.join(root_dir, f"{text[2]}.ts.program.txt")
        if os.path.isfile(program_dir):
            result = execute(program_dir, keyword,except_keywords,text[1])
            
            if result is None or len(result) !=2:
                continue;
            
            start_flag, end_flag = result
            if not start_flag or not end_flag:
                print(f"Start flag: {start_flag}, End flag: {end_flag}")
                print(f"Info: {program_dir}")
                #break

        
    tq.close()

   
    insertRanked_anime()
    insertRanked_anime_season()


def insertRanked_anime_season():
    insert_query("delete from ranked_anime_season","")
    sql = """
    
               insert into ranked_anime_season ( 
                   select distinct anime_id,t.year,t.season,rank() over (order by score desc ) as all_ranking,score,originalName,folderName ,"" as txt from (
 	select anime_id ,YEAR(hiduke) as year,
				    case
                        	    when MONTH(hiduke) <=3 then 1
                        	    when MONTH(hiduke) <=6 then 2
                        	    when MONTH(hiduke) <=9 then 3
                        	    when MONTH(hiduke) <=12 then 4
                        	    else 0
                    		    end 'season' ,min(hiduke) as hiduke from video_info 
                                    join anime on anime.id=anime_id
                                    
				    group by anime_id,year,season
                                    having count(*) >2

) as t
join score using(anime_id)
join anime on t.anime_id =anime.id
order by all_ranking desc 
)
    """
    insert_query(sql,"")
def insertRanked_anime():
    insert_query("delete from ranked_anime","")
    sql = """
    
                insert into ranked_anime (
                            
                    select id,originalName,foldername,score,txt , rank() over (order by score desc ) from anime
                    left join
                    (
                        -- あらすじ
                        select video_id,txt,anime_id from video_prog
                        join video using(video_id)
                         where video_id in(
                        
                            select min(video_id) as video_id  from video_info 
                            join (select video_id,txt from video_prog  where length(txt) >10) as t
                            using (video_id)
                            group by anime_id
                            having min(hiduke)
                        )

                        
                    )as summary on anime.id=summary.anime_id
                    join (
                        select anime_id,score,rank() over (order by score desc ) from
                        (
                            select distinct anime_id,score from score
                        )as score  
                    )  as score on anime.id=score.anime_id

                    order by score desc 

            );
    """
    insert_query(sql,"")
    
    
    
def execute(program_dir, keywords,except_keywords,video_id):
    try:
        with open(program_dir, 'r') as f:
            start_flag = False
            end_flag = False
            lines = f.readlines()  

            
            #番組の説明の開始と終了の文字が含まれているリストの組み合わせを探す
            included_words = []
            
            for line in lines:
                for word in keywords:
                    if word[0] in line or word[1] in line:
                        included_words.append(word)
            
            
            if len(included_words)==0:
                print("does not exist program details :videoid:"+str(video_id))
                print("番組の説明が見つかりませんでした。"+program_dir)
                return 
            

            prog_texts=[]
           
            for included_word in included_words:
                text=""
                for line in lines:
                    
                    line = line.strip()
                    
                    if line!="" and line in included_word[0]:
                        start_flag = True

                    elif line!="" and start_flag and line in included_word[1]:
                        end_flag = True
                        break
                
                    elif start_flag and line !="":
                        text=text+line+"\n"
                if text!="":
                    prog_texts.append(text)
                
                

            #複数合致したときに最小の文章を入れる
            prog_text=minLengthTxt(prog_texts)
            

            if len(prog_text)>0:
                
            
                sql = "insert into video_prog (video_id,txt) values(%s,%s)"
                values=(video_id,prog_text)
                insert_query(sql,values)
            else :
                print("does not exist program details :videoid:"+str(video_id))
                print("番組の説明が見つかりませんでした。"+program_dir)
            return start_flag, end_flag

    except Exception as e:
        print(f"Error querry {program_dir}: {e}")
        return False, False

def minLengthTxt(arrs):
    minLen = -1
    txt = ""

    for arr in arrs:
        if minLen == -1 or len(arr) < minLen:
            txt = arr
            minLen = len(arr)
    return txt


def instr(arr,txt):
    for i in arr:
        if i in txt:
            return True
    return False
    

if __name__ == "__main__":
    main()
    