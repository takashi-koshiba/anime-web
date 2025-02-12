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
from datetime import datetime

#################################
#################################
#引数なしで実行した場合は指定したディレクトリ内の動画を処理します。
#引数に動画のパスがある場合はその動画を移動します。



############環境に応じて修正してください########################################

sys.stdout.reconfigure(encoding='utf-8', errors='ignore')
♯TS_FILES_DIR = 'D:\\TV\\ts\\'  #移動元のTSファイルのディレクトリ
♯ENCODED_VIDEO_PATH = 'D:\\TV\\ts\\encoded\\'  # エンコードした動画があるディレクトリ(mp4 と mkvファイルなど)
♯TEMP_DIR = 'D:\\TV\\ts\\encoding\\'  # DBとファイル名が部分一致したtsファイルの移動先
PORT=8082  #実行しているjavaのポート番号
♯DUPLICATE_DIR="D:\\TV\\ts\\duplicate\\"  #動画がすでにDB書き込まれている場合に移動する動画の移動先
DB_CONFIG = {
    'host': "localhost",
    'user': "java",
    'password': "java",
    'database': "db1"
}
############################################################################


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
        print("")
        print(f"Error querry: {query}")
        print(f"sql error details: {err}")
        print("SELECT SQL params:"+params)
        print("")
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
        print("")
        print("Error querry: " + query)
        print("sql error details: " + str(e))  
        print("INSERT param :"+str(value))
        print("")
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


def get_video_id(video_path,ext):
 
    basename = os.path.splitext(os.path.basename(video_path))[0]
    
 
    query = "SELECT video_id FROM video WHERE fname=%s and ext=%s;"
    params = (basename,ext)

    result = execute_query(query, params)
    
    # 結果が存在する場合、そのvideo_idを返す。存在しない場合はNoneを返す。
    return result[0][0] if result else None


def process_ffprobe(video):
    cmd = f"ffprobe -hide_banner -v error -print_format json -show_streams \"{video}\""
    process = subprocess.run(cmd, shell=True, capture_output=True, text=True, encoding='utf-8')
    return json.loads(process.stdout)

def process_outputImg(root_dir,  foldername,video_file):
    try:
        
        new_dir = os.path.join(root_dir, "content\\anime-web\\anime\\img\\", foldername,os.path.splitext(os.path.basename(video_file))[0])
        os.makedirs(new_dir,exist_ok=True)
        
        cap = cv2.VideoCapture(video_file)
        totalframecount = int(cap.get(cv2.CAP_PROP_FRAME_COUNT))
        video_fps = cap.get(cv2.CAP_PROP_FPS)                 # フレームレートを取得する
        if(video_fps<=0):
            video_fps=1
        video_len_sec = totalframecount / video_fps         
      

        loopCount=30
        loopLenSec=int(video_len_sec/loopCount)
                      
        #tq = tqdm(total = loopCount)
        for   i in range(loopCount):
            #cmd = f"ffmpeg  -i \"{video_file}\"  -vf scale=-1:480 -q:v 3 \"{new_dir}\\output_%03d.jpg\""
            cmd = f"echo Y| ffmpeg -ss {loopLenSec*i} -i \"{video_file}\"  -vsync vfr -vf scale=640:360 -q:v 55 -frames:v 1  \"{new_dir}\\output_{i}.webp\" "
            
            if not os.path.isfile(f"{new_dir}\\output_{i}.webp"):
                print (f"{new_dir}\\output_{i}.webp")
                subprocess.run(cmd, shell=True, capture_output=True, text=True, encoding='utf-8')
            
      #      tq.update(1)
       # tq.close()
        
    except Exception as e:
        print("Info output_img details: " + str(e))  

def insertJk_rownumber(video, video_id):
    video_info = process_ffprobe(video)
    streams_info = video_info.get('streams', [])
    err=0
    come_byte=0
    video_time=""
    date=None
    strDate=None
    for value in streams_info:
        tags = value.get('tags', {})
        title = tags.get('title', '')
        
        video_time = tags.get('DURATION-eng')
        date=os.path.splitext(os.path.basename(video))[0]
        
        strDate=StrToDate(date)
        if strDate!=None and  len(strDate)==6:
            strDate=str(20)+strDate
        
        if 'NicoJK' in title:
            come_byte = tags.get('NUMBER_OF_BYTES-eng')
            break

    formatted_date = None
    if strDate:
        print(strDate)
        print(os.path.splitext(os.path.basename(video))[0])
        try:
            formatted_date = datetime.strptime(strDate.replace('-', '').ljust(12,'0'), "%Y%m%d%H%M%S").strftime("%Y-%m-%d %H:%M:%S")
        except Exception as e:
            formatted_date=None  
            print(e)
            
    query = '''
        INSERT INTO jk_rownumber (video_id, come_byte, video_time, hiduke) 
        VALUES (%s, %s, %s, %s)
    '''
    value = (
        video_id,
        come_byte,
        video_time,
        formatted_date
    )    
    err = insert_query(query,value)
    return err 
        
    
    

def move_files_to_folder(files, destination_folder,videoid=0):

    try:
        
        #for file_path in files:
    #    print(files)
        shutil.move(files, destination_folder)
            
    except Exception as e:
        print("anime:"+files)
        print("move file error details: " + str(e))  
        
        if videoid ==0:
            return
        
        sql="delete from video where video_id =%s"
        insert_query(sql,videoid)
        
def moveTs(anime_data,extension):
    #DBに一致したらTSファイルを移動する
    print("tsファイル移動中")
    ts_files = glob.glob(os.path.join(TS_FILES_DIR, extension))
    for anime_name, _, _ in anime_data:
        for ts_file in ts_files:
            if anime_name in ts_file:
              #  print(TEMP_DIR)
                move_files_to_folder(ts_file, TEMP_DIR)
               # print(ts_file)
    print("tsファイルの移動が終了しました。")
def main():
    root_dir = get_root_dir()
 #   print(len(sys.argv))
 
    if  not os.path.isdir(TS_FILES_DIR):
        print("パスがありません"+TS_FILES_DIR)
        return
    
    if  not os.path.isdir(ENCODED_VIDEO_PATH):
        print("パスがありません"+ENCODED_VIDEO_PATH)
        return
    if  not os.path.isdir(TEMP_DIR):
        print("パスがありません"+TEMP_DIR)
        return
    if  not os.path.isdir(DUPLICATE_DIR):
        print("パスがありません"+DUPLICATE_DIR)
        return
    
    if len(sys.argv) >1:#パラメータで動画のパスがある場合はその動画を手動で移動する
        
        #引数で動画のパスがあれば移動する
        
        try:
            anime_id =(int) (input('animeIdを入力してください: '))
            query = "SELECT foldername FROM anime where id=%s limit 1;"
            params = (anime_id,)
            result = execute_query(query, params)
            foldername=result[0][0]
        except:
            print("error:処理に失敗しました。")
            exit()
        
        

        
        print("フォルダ名 :"+foldername)
        
        t=""
        while True:
            t = input ('フォルダ名が問題ないか  (y or n)')
            
            if t =='y' or t=='n':

                break  
        if t=='n':
            return
            
        video_dir = os.path.join(root_dir, "content\\anime-web\\anime\\video\\", foldername) 
        for i in range(1,len(sys.argv)):
            
            video_file=sys.argv[i]
            

            print(video_file)

        
          
            if os.path.isdir(video_dir):
                
                insertValue(video_file,anime_id,root_dir,foldername,video_dir)
            else:
                print("指定した動画が見つかりません")
        insertRanking()    
        return 
    
    if not os.path.isdir(root_dir):
        print(f"{root_dir} does not exist")
        return 

    print(f"Root directory: {root_dir}")
    
    anime_data = execute_query("""
        select fname, foldername, anime.id from anime join alias on anime.id = alias.anime_id
	union all(SELECT originalName, foldername, id FROM anime)
        ORDER BY CHAR_LENGTH(fname) DESC
        """)

    moveTs(anime_data,'*.ts')


    video_files = glob.glob(os.path.join(ENCODED_VIDEO_PATH, '*.mkv'))
    mp4_files = glob.glob(os.path.join(ENCODED_VIDEO_PATH, '*.mp4'))
    ts_files = glob.glob(os.path.join(ENCODED_VIDEO_PATH, '*.ts'))
    video_files=video_files+mp4_files+ts_files

    video_files.sort(key=os.path.getmtime)
    mkvCount=0
    
    tq = tqdm(total=len(video_files))
    for anime_name, foldername, anime_id in anime_data:
        mkvCount= mkvCount+1
        for video_file in video_files:
            
            
            if anime_name in video_file:
                video_dir = os.path.join(root_dir, "content\\anime-web\\anime\\video\\", foldername)
                
                if os.path.isdir(video_dir):
                  #  print(str(mkvCount)+"/"+str(len(anime_data)))
                    insertValue(video_file,anime_id,root_dir,foldername,video_dir)
                    tq.update(1)
                        
                else:
                    print(f"Directory does not exist: {video_dir}")
            
            
    tq.close()
    print("DB書き込み中")
   
    insertVideoInfo()
    insertRanking()
    
def insertValue(video_file,anime_id,root_dir,foldername,video_dir):
     

    filebasename=os.path.splitext(os.path.basename(video_file))[0]
    ext=os.path.splitext(os.path.basename(video_file))[1]
    
    if not os.path.isfile(video_file):
        #すでに移動済みの場合は何もしない
        return
  
    AI_id=getNextId()[0][0]
    if AI_id==None:
        AI_id=1
        
    err = insertVideoName(anime_id,filebasename,ext,AI_id)#ファイル名を書き込み
    if err==1 :
        
        print("anime"+filebasename)
        print("error:Failed to write video table:"+video_file)
        
        
        try:
            shutil.move(video_file, DUPLICATE_DIR)
        
        except:
            print("ファイルの移動に失敗しました。"+DUPLICATE_DIR+os.path.basename(video_file))
            print("すでにファイルが存在しているか、ロックされているかを確認してください")
            
        return
    
    err = insertJk_rownumber(video_file, AI_id)#コメント数などを書き込み
    
    if err ==1:
        #エラーの場合はランキング集計の対象にはなりませんが、動作には問題ありません。
        print("anime"+filebasename)
        print("info:Failed to write video information.")
        
    process_outputImg(root_dir,foldername,video_file)#画像の切り出し
    move_files_to_folder(video_file, video_dir,AI_id)#移動
    
    
def insertRanking():
    sql="""
        delete from ranking;
        """
    insert_query(sql,"")
    
    sql ="""
    

        insert into ranking ( all_ranking, anime_id, year, season, mediun_come_byte,T_count) (
        select * from (
            select RANK() OVER(ORDER BY come_byte DESC) as all_ranking, anime_id,year,season,come_byte as mediun_come_byte,tt.T_count from (
                select *,ROW_NUMBER() OVER(PARTITION BY anime_id,year,season ORDER BY come_byte) as rownumber from
                (
                select anime_id,come_byte/NULLIF(time_to_sec(video_time), 0)as come_byte,YEAR(if(isnull(gt10th_video),hiduke,hiduke-interval 2 week)) as year,
                    case -- 十話以上の場合は2週間前にする
                        when MONTH(if(isnull(gt10th_video),hiduke,hiduke-interval 2 week)) <=3 then 1
                        when MONTH(if(isnull(gt10th_video),hiduke,hiduke-interval 2 week)) <=6 then 2
                        when MONTH(if(isnull(gt10th_video),hiduke,hiduke-interval 2 week)) <=9 then 3
                        when MONTH(if(isnull(gt10th_video),hiduke,hiduke-interval 2 week)) <=12 then 4
                        else 0
                    end 'season' ,offset,T_year,T_season,offsetT.T_count,
            
                    -- 局
                    case 

                       when video.fname like "%ＴＯＫＹＯ　ＭＸ１%" then 1
                       when video.fname like  "%ＮＨＫ総合１・東京%" then 2
                       when video.fname like  "%フジテレビ%" then 3
                       when video.fname like  "%テレビ東京１%" then 4
                       when video.fname like  "%テレビ朝日%" then 5
                       when video.fname like  "%Ｊ：ＣＯＭテレビ%" then 6
                       when video.fname like  "%ＴＢＳ１%" then 7
                       when video.fname like  "%ｔｖｋ１%" then 8
                       when video.fname like  "%日テレ１%" then 9
                       when video.fname like  "%ＮＨＫＥテレ１東京%" then 10
                       when video.fname like  "%(MX)%" then 1 
                       when video.fname like  "%(tvk)%" then 8
                       when video.fname like  "%(TX)%" then 4
                       when video.fname like  "%(CX)%" then 3
                       when video.fname like  "%ＴＯＫＹＯ　ＭＸ２%" then 1

                    else 0 end channel , min_data_channel


                    from video
                    left join(
                       -- 十話未満は除外
                        select video_id, "1" as gt10th_video from video where video_id in(
                            select video_id from video where fname like '%\\_%' 
                        ) and fname not  like '%\\_0%' 
                    )as gt10th_videos using(video_id)
                    
                    join jk_rownumber using(video_id)
            
                    -- 行数を２で割る
                    cross join (
                        select anime_id,CEILING(count(*)/2) as offset,YEAR(if(isnull(gt10th_video),hiduke,hiduke-interval 2 week)) as T_year,
                        case -- 十話以上の場合は１か月前にする
                            when MONTH(if(isnull(gt10th_video),hiduke,hiduke-interval 2 week)) <=3 then 1
                            when MONTH(if(isnull(gt10th_video),hiduke,hiduke-interval 2 week)) <=6 then 2
                            when MONTH(if(isnull(gt10th_video),hiduke,hiduke-interval 2 week)) <=9 then 3
                            when MONTH(if(isnull(gt10th_video),hiduke,hiduke-interval 2 week)) <=12 then 4
                            else 0
                        end 'T_season',count(*) as T_count
            
                        from video 
                        
                        left join(
                           -- 十話未満は除外
                            select video_id, "1" as gt10th_video from video where video_id in(
                                select video_id from video where fname like '%\\_%' 
                            ) and fname not  like '%\\_0%' 
                        )as gt10th_videos using(video_id)
                        
                        left join jk_rownumber using(video_id) 
                        where  anime_id in (
                            -- すべて除外されていないか、すべて除外されている場合は通す
                            select anime_id from (select anime_id,count(*) as original_C from video group by anime_id) as t
                            
                            join (
                                select anime_id,count(*) as execpt_C from video
                                left join jk_rownumber using(video_id) 
                                where  (
                                    fname  like  "%マルチ1" or fname  like "%[再]%" or fname  like "%-1" or fname  like "%(2)" or fname  like "%-cm"  or fname  like "%放送記念%" or fname  like "%放送開始記念%" or fname  like "%アンコール放送" or come_byte>0  or hiduke is  null
                                ) 
                                group by anime_id  
                            ) as tt using(anime_id)
                            where original_C=execpt_C
                        )
                        or fname not like  "%マルチ1" and fname not like "%[再]%" and fname not like "%-1" and fname not like "%(2)" and fname not like "%-cm"  and fname not like "%放送記念%" and fname not like "%放送開始記念%" and fname not like "%アンコール放送" and come_byte>0  and hiduke is not null
                        group by anime_id,T_year,T_season
                    )as offsetT using(anime_id)
            
                    -- --------------------------------------------------
                    left join (
                    -- アニメごとの最初のアニメの動画のチャンネルを取得
                    select video.anime_id,
                    case 
                    
                        when video.fname like "%ＴＯＫＹＯ　ＭＸ１%" then 1
                        when video.fname like  "%ＮＨＫ総合１・東京%" then 2
                        when video.fname like  "%フジテレビ%" then 3
                        when video.fname like  "%テレビ東京１%" then 4
                        when video.fname like  "%テレビ朝日%" then 5
                        when video.fname like  "%Ｊ：ＣＯＭテレビ%" then 6
                        when video.fname like  "%ＴＢＳ１%" then 7
                        when video.fname like  "%ｔｖｋ１%" then 8
                        when video.fname like  "%日テレ１%" then 9
                        when video.fname like  "%ＮＨＫＥテレ１東京%" then 10
                        when video.fname like  "%(MX)%" then 1 
                        when video.fname like  "%(tvk)%" then 8
                        when video.fname like  "%(TX)%" then 4
                        when video.fname like  "%(CX)%" then 3
                        when video.fname like  "%ＴＯＫＹＯ　ＭＸ２%" then 1
                
                    else 0 end  min_data_channel



                    from video join video_info using(video_id)                  
                    where (video_id) in(


                    -- チャンネルがある動画の内一番日付が若い動画を選択
                        -- ---------------
                        select min(video_id) from jk_rownumber join video using(video_id) 
                        where (anime_id,hiduke)  in(
                            select anime_id,min(hiduke) from (
                                select channel.video_id,hiduke ,channel.anime_id from (
                                    select case 
                            
                                        when video.fname like "%ＴＯＫＹＯ　ＭＸ１%" then 1
                                        when video.fname like  "%ＮＨＫ総合１・東京%" then 2
                                        when video.fname like  "%フジテレビ%" then 3
                                        when video.fname like  "%テレビ東京１%" then 4
                                        when video.fname like  "%テレビ朝日%" then 5
                                        when video.fname like  "%Ｊ：ＣＯＭテレビ%" then 6
                                        when video.fname like  "%ＴＢＳ１%" then 7
                                        when video.fname like  "%ｔｖｋ１%" then 8
                                        when video.fname like  "%日テレ１%" then 9
                                        when video.fname like  "%ＮＨＫＥテレ１東京%" then 10
                                        when video.fname like  "%(MX)%" then 1 
                                        when video.fname like  "%(tvk)%" then 8
                                        when video.fname like  "%(TX)%" then 4
                                        when video.fname like  "%(CX)%" then 3
                                        when video.fname like  "%ＴＯＫＹＯ　ＭＸ２%" then 1
                            
                                    else 0 end channel,video.fname,video_id,video.anime_id
                                from video
                            
                            
                            
                                )as channel
                                join video_info using(video_id)
                                where channel !=0
                            ) as minHiduke
                            group by anime_id
                        )
                        group by anime_id
                        -- -----------
                    )
                    ) as channel using(anime_id)
                    -- --------------------------------------------------
            
                    where  anime_id in (
                        -- すべて除外されていないか、すべて除外されている場合は通す
                        select anime_id from (select anime_id,count(*) as original_C from video group by anime_id) as t
                        
                        join (
                            select anime_id,count(*) as execpt_C from video
                            left join jk_rownumber using(video_id) 
                            where  (
                                fname  like  "%マルチ1" or fname  like "%[再]%" or fname  like "%-1" or fname  like "%(2)" or fname  like "%-cm"  or fname  like "%放送記念%" or fname  like "%放送開始記念%" or fname  like "%アンコール放送" or come_byte>0  or hiduke is  null
                            ) 
                            group by anime_id  
                        ) as tt using(anime_id)
                        where original_C=execpt_C
                    )
                    or fname not like  "%マルチ1" and fname not like "%[再]%" and fname not like "%-1" and fname not like "%(2)" and fname not like "%-cm"  and fname not like "%放送記念%" and fname not like "%放送開始記念%" and fname not like "%アンコール放送" and come_byte>0  and hiduke is not null
                    
            
            
                )as t
                where year=T_year and season=T_season  
                -- アニメの最初の動画のチャンネルと同じチャンネルのみを計算する
                and (min_data_channel=0 or min_data_channel=channel)
            
            )as tt
            where offset = rownumber
            )as ttt
        
            order by anime_id
        )
    """

    err = insert_query(sql,"")
    
    if err == 1:
        print("Failed to write ranking table.")
        
        
    sql="""
        delete from score;
        """
    insert_query(sql,"")
    
    
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
    
    insert_query(sql,"")
    
    if err == 1:
        print("Failed to write score table.")
        
    return err


def insertVideoInfo():
    insert_query("delete from  video_info","")
    sql="""
                insert into video_info(
                select video_id, anime_id, fname, 
                       (COALESCE(come_byte, 0) / GREATEST(COALESCE(TIME_TO_SEC(video_time), 0), 1) / GREATEST(maxScore, 1)) as score,
                       COALESCE(noCmFrame, 0) as nocmframe, 
                       hiduke, video_time, ext 
                from video 
                left join jk_rownumber using(video_id)
                left join (
                    -- max kome ----------
                    select anime_id, COALESCE(max(score), 1) as maxScore 
                    from anime
                    join video on anime.id = video.anime_id
                    join (
                        select video_id, come_byte / GREATEST(COALESCE(TIME_TO_SEC(video_time), 0), 1) as score  
                        from jk_rownumber 
                    ) as kome using(video_id)
                    group by anime_id
                ) as max_kome using(anime_id)
                left join (
                    select video_id, min(fname) as noCmFrame 
                    from (
                        select video_id, fname, min(score) 
                        from (
                            SELECT * FROM label l
                            WHERE NOT EXISTS (
                                SELECT 1 FROM label l2
                                WHERE l.video_id = l2.video_id
                                AND l.fname = l2.fname
                                AND l2.label NOT IN (0, 1, 2, 3)
                            )
                        ) as label  
                        join (
                            -- 最小のlabelを取得
                            select video_id, min(label) as minL from label group by video_id
                        ) as minL using(video_id)
                        where label = minL
                        group by video_id, fname
                    ) as t
                    group by video_id
                ) as no_cm using(video_id)
                );

    """
    insert_query(sql,"")

def StrToDate(str):
    pattern = [r'2[0-9]{11}',r'2[0-9]{7}-[0-9]{4}',r'2[0-9]{7}', r'2[0-9]{3}-[0-9]{2}-[0-9]{2}', r'2[0-9]{5}']

    
    for p in pattern:
        match = re.search(p, str)
        if match:  
            print(f"Pattern matched: {p}")
            return match.group() 
    
    #print("No pattern matched")
    return


def getNextId():
    sql="SELECT nullif(max(video_id),0)+1 from video ;"
    data = execute_query(sql)

    return data
    
def insertVideoName(anime_id,fname,ext,AI_id):
    sql="INSERT INTO video (anime_id,fname,ext,video_id) VALUES(%s,%s,%s,%s)"
    value=(anime_id,fname,ext,AI_id)
    err = insert_query(sql,value)
    return err
if __name__ == "__main__":
    
    main()
