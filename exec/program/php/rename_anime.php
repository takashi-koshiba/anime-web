<?php
//mb_regex_encoding('UTF-8');
mb_internal_encoding("UTF-8");
//[新]1話,１話,第1話,第１話, ＃１,#1,第一話、第壱話,
//上記のようなファイル名を変換します。

//実行後　：_０１  
//下記をコメントを外してしてリネームしたい動画や番組表があるフォルダを指定してください。
//rename_anime("D:\\TV\\ts\\");
//rename_anime("D:\\java-web\\data\\bangumi\\");
//rename_anime("D:\\java-web\\data\gomi\\");
//rename_anime("D:\\TV\\ts\\encoding\\");
//rename_anime("D:\\TV\\ts\\encoded\\");


function rename_anime($name){
$count=0;





  do{
    $count++;
    $name=htmlspecialchars($name);
    $cmd = "chcp 65001 & dir /B $name";
    //$cmd = "powershell -Command \"Get-ChildItem -Name -LiteralPath '$name'\"";
    exec($cmd, $opt);
    
    
    $rename_count=0;
    $count_opt=count($opt);
 
    



  for($i=0;$i<$count_opt;$i++){
    //$opt[$i]=mb_convert_encoding($opt[$i], "SJIS-win", "UTF-8");
   // echo $opt[$i];
    echo $i."/".$count_opt."\r\n";
    $match_num="";
    $re="";//上書き保存防止
    
    //exec($cmd, $opt);



    if(preg_match('/ts$/',$opt[$i]) || preg_match('/mp4$/',$opt[$i])|| preg_match('/mkv$/',$opt[$i])|| preg_match('/ts.program.txt$/',$opt[$i])|| preg_match('/ts.err$/',$opt[$i])|| preg_match('/ass$/',$opt[$i])){




        
        if(preg_match('/\［新\］/u',$opt[$i])){
			     $re = (str_replace('［新］', '', $opt[$i]));
			     $rename_count++;
		    }
        elseif(preg_match('/\[新\]/u',$opt[$i])){
          //$kkka1[$i] = (mb_ereg_replace('[[新]]', "", $opt[$i]));
          $re=str_replace('[新]','',$opt[$i]);
          //print $kkka1[$i];
          $rename_count++;

        }elseif(preg_match("/\[終\]/u", $opt[$i])){
    				$re=str_replace('[終]','',$opt[$i]);
    				//print $kkka1[$i];
    				$rename_count++;
    		}
        elseif(preg_match("/\［終\］/u", $opt[$i])){
    				$re=str_replace('\［終\］','',$opt[$i]);
    				//print $kkka1[$i];
    				$rename_count++;

    		}
        elseif(preg_match("/「新」/u", $opt[$i])){
          $re=str_replace('「新」','',$opt[$i]);
          //print $kkka1[$i];
          $rename_count++;

      }
        elseif(preg_match('/♯/u',$opt[$i])){
          $re = (str_replace('♯', '_', $opt[$i]));

          $rename_count++;

        }
        elseif(preg_match('/#/u',$opt[$i])){
           $re = (str_replace('#', '_', $opt[$i]));
          $rename_count++;

        } elseif(preg_match('/＃/u',$opt[$i])){
          $re = (str_replace('＃', '_', $opt[$i]));

         $rename_count++;


       }
        //第　全角数字　話
        elseif(!preg_match('/_[０-９]{2,4}/u',$opt[$i])&&
          preg_match("/[第_]\d{1,4}[話]/u", $opt[$i], $matches, PREG_OFFSET_CAPTURE) ){
          $match_str=$matches[0][0];//マッチした文字
           $num_len=mb_strlen($match_str)-2;//数字の文字数
          $match_num=mb_substr($match_str,1,$num_len);//数字だけにする
          $match_num_kana=mb_convert_kana($match_num,'N');//数字を全角にする
          if($match_num_kana==""){
            $match_num_kana=$match_num;
          }
          if(mb_strlen($num_len==1)){

            $re =str_replace($match_str, "_０{$match_num_kana}", $opt[$i]);
          }else{
            $re = str_replace($match_str, "_{$match_num_kana}", $opt[$i]);
          }

          $rename_count++;
        }
        //１文字専用
        elseif(preg_match('/_[０-９]{1}[話]/u',$opt[$i])&&!preg_match('/_[０-９]{2,}/u',$opt[$i])&&
          preg_match("/[第_]\d{1}[話]/u", $opt[$i], $matches, PREG_OFFSET_CAPTURE) ){

          $match_str=$matches[0];//マッチした文字
           $num_len=mb_strlen($match_str)-1;//数字の文字数
          $match_num=mb_substr($match_str,1,$num_len);//数字だけにする
          $match_num_kana=mb_convert_kana($match_num,'N');//数字を全角にする
          if($match_num_kana==""){
            $match_num_kana=$match_num;
          }
          if(mb_strlen($num_len==1)){

            $re = str_replace($match_str, "_０{$match_num_kana}", $opt[$i]);
          }else{
            $re = str_replace($match_str, "_{$match_num_kana}", $opt[$i]);
          }

          $rename_count++;
        }
        elseif(preg_match('/_[０-９]{1}/u',$opt[$i])&&!preg_match('/_[０-９]{2,}/u',$opt[$i])&&
          preg_match("/[第_]\d{1}/u", $opt[$i], $matches, PREG_OFFSET_CAPTURE) ){
          
          $match_str=$matches[0][0];//マッチした文字

          $num_len=mb_strlen($match_str)-1;//数字の文字数
          $match_num=mb_substr($match_str,1,$num_len);//数字だけにする
          $match_num_kana=mb_convert_kana($match_num,'N');//数字を全角にする
          if($match_num_kana==""){
            $match_num_kana=$match_num;
          }
          if(mb_strlen($num_len==1)){
            
            $re =  str_replace($match_str, "_０{$match_num_kana}", $opt[$i]);
          }else{
            $re = str_replace($match_str, "_{$match_num_kana}", $opt[$i]);
          }

          $rename_count++;
        }        //全角数字　話

        elseif(!preg_match('/_[０-９]{2,4}/u',$opt[$i])&&
          preg_match("/[第_]\d{1,4}/u", $opt[$i], $matches) ){
          $match_str=$matches[0];//マッチした文字
          $num_len=mb_strlen($match_str)-1;//数字の文字数
          $match_num=mb_substr($match_str,1,$num_len);//数字だけにする
          $match_num_kana=mb_convert_kana($match_num,'N');//数字を全角にする
          if($match_num_kana==""){
            $match_num_kana=$match_num;
          }
          if(mb_strlen($num_len==1)){
            $re = str_replace($match_str,"_０{$match_num_kana}",$opt[$i]);
           
          }else{
            $re = str_replace($match_str, "_{$match_num_kana}", $opt[$i]);
          }

          $rename_count++;
        }

        elseif(!preg_match('/_[０-９]{2,4}/u',$opt[$i])&&
        preg_match("/[第_].{0,4}[話夜羽回幕闇]/u", $opt[$i], $matches, PREG_OFFSET_CAPTURE)) 
         {
          $subStr = substr($opt[$i],0,$matches[0][1]);
          $kanji2NumberArr=kanji2Number($matches[0][0]);

          $resultStr=str_replace("羽","話",$kanji2NumberArr[0]);
          $resultStr=str_replace("回","話",$resultStr);
          $resultStr=str_replace("幕","話",$resultStr);
          $resultStr=str_replace("闇","話",$resultStr);


          if($kanji2NumberArr[1]){
            $re=$subStr.$resultStr.mb_substr($opt[$i],mb_strlen($subStr)+mb_strlen($kanji2NumberArr[0]));
            $rename_count++;
          }
          //echo $subStr.$resultStr.substr($opt[$i],strlen($subStr.$kanji2NumberArr[0]))."\r\n";
          //echo $opt[$i];
            //$rename_count++;echo "aaaaddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd"
        }


        //リネーム
        if($rename_count>0&& $re!=""){


          $original=mb_convert_encoding($name.$opt[$i], "SJIS", "UTF-8");
          $new = mb_convert_encoding($name.$re, "SJIS", "UTF-8");



          if (!file_exists($name.$opt[$i])) {
            echo $original."は存在しません。";
            continue;
          }

          rename($original, $new);
          //exec("ren $name.$opt[$i] $name.$re" );
          
        }

        

      }


  }//for
  $opt="";


}while($rename_count!=0&&$count<4);//修正箇所が複数ある場合があるので何回も実行します。

}

function kanji2Number($kanji) {

  $kanjiArray = array(
    "零" => 0,
    "一" => 1,
    "二" => 2,
    "三" => 3,
    "四" => 4,
    "五" => 5,
    "六" => 6,
    "七" => 7,
    "八" => 8,
    "九" => 9,
    "十" => 10,
    "壱" => 1,
    "弐" => 2,
    "参" => 3,
    "肆" => 4,
    "伍" => 5,
    "陸" => 6,
    "漆" => 7,
    "捌" => 8,
    "玖" => 9,
    "拾" => 10
  );





  $len = mb_strlen($kanji);
  $result = "";
  $num=0;
  $flag=false;
  for ($i = 0; $i < $len; $i++) {
    $char = mb_substr($kanji, $i, 1);
    if (isset($kanjiArray[$char])) {
      $temp=$kanjiArray[$char];
      if($temp==10&&$num!=0){
        $num*=$temp;
      }else{
        $num+=$temp;
      }
      $flag=true;
    } else {
      if($num!=0){
        $result.="$num";
      }
      $result .= $char;
      $num=0;

      
    }
  }

  return  Array($result,$flag);
}

?>

