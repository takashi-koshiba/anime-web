import os
import subprocess
import shutil
import sys
from pathlib import Path
from logging import getLogger, FileHandler, StreamHandler, DEBUG, Formatter

logger = getLogger(__name__)
logger.setLevel(DEBUG)

# ファイルハンドラー
log_path = Path(__file__).resolve().parent / 'mybeDupliLog.log'


simPath=Path(__file__).resolve().parent / 'simTest.py'


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




def simTest(fname,output_path):
    proc = subprocess.run(
        ['python', simPath, fname],
        capture_output=True, text=True
    )

    output = proc.stdout.strip()
    print(output)
    try:

        if proc.returncode != 0:
            logger.debug("info: %s", proc.stderr.strip()+":"+fname)
          
            return None
        else:     
            value = float(output)
    except ZeroDivisionError:
        logger.debug("info: %s", proc.stderr.strip()+":"+fname)


        return None
    except ValueError:    

        logger.debug("エラー: %s", proc.stderr.strip()+":"+fname)
        sys.exit(1) 
        return None

    return value


if __name__ == "__main__":
    logger.debug('start')
    ############環境に応じて変更してください########################
    input_dir = r"D:\TV\ts\encoding"  #重複を確認したいtsファイルが有るパス
    maybe_duplicates_dir = r"D:\TV\ts\duplicate\maybeDupli"   #重複があった際の移動先ディレクトリ
    confirmed_unique_dir=r"D:\TV\ts\encoding_ok" #重複がなかった場合の移動先
    delDir=r"D:\TV\ts\del"
    ####################ここまで#####################################
    if not os.path.isdir(input_dir) or not os.path.isdir(maybe_duplicates_dir)or not os.path.isdir(confirmed_unique_dir)or not os.path.isdir(delDir):
        print("パスが存在しません")
    

    files = sorted(
        [f for f in os.listdir(input_dir) if f.endswith('.ts')],
        key=lambda f: os.path.getmtime(os.path.join(input_dir, f))
    )


    for f in files:
        full_path = os.path.abspath(os.path.join(input_dir, f))
        sim=simTest(full_path,confirmed_unique_dir)
        logger.debug(type(sim))
        logger.debug(str(sim)+" : "+full_path)
        
        


            
        if sim and  sim>0.8:
            logger.debug("重複確定！")
            
            shutil.move(full_path, delDir)
        
        elif sim and sim>0.7:
            logger.debug("たぶん重複！")

            shutil.move(full_path, maybe_duplicates_dir)
        else :
            logger.debug("重複なし")
            shutil.move(full_path, confirmed_unique_dir)


         