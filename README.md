# anime-web
<img src="https://github.com/takashi-koshiba/anime-web/blob/main/2.PNG">
録画したテレビ番組を管理するために作成しました。<br>
アマゾンプライムやネットフリックスのようにテレビ番組を確認できます。<br>
今期放送中のアニメを表示したり番組の人気度をランキング形式で表示します。<br>
偏差値でどのくらい人気なのかを把握できます。<br>

<br>
ファイルのアップロードは機能もあります。
<h1>動作条件</h1>
<ul>
  <li>OS:Windows</li>
  <li>DB:mysql</li>
  
</ul>

<h1>準備</h1>
下記をインストールしてください
<ul>
  
  <li>kakasi</li>
  <li>ffmpeg(.avif形式の画像に対応しているバージョンであること)</li>
  <li>python</li>
  <li>gradle</li>
  <li>java</li>
    <li>mysql</li>
</ul>
<br>
kakasiとffmpegはパスを通してください。

リポジトリをクローン
```bash

git clone https://github.com/takashi-koshiba/anime-web/
cd anime-web
```

  <br>
</p>

<p>・mysqlアカウント作成<br>
<span>　　ユーザー名：java<br></span>
<span>　　パスワード： java<br></span>
</p>

<p>・DBをリストア<br>
<span>　　create database db1<br></span>
<span>　　mysql -u java -p db1 < exec\program\db1.sql<br></span>　
</p>
  <br>

コンパイル

```bash
gradle build
```

実行
```bash
java -jar build\libs\web-0.0.1-SNAPSHOT.jar --server.port=8082
```

<br>
ブラウザで表示ができることを確認してください<br>
<a href="http://localhost:8082/anime-web/">http://localhost:8082/anime-web/</a>

<br> <br>
ディレクトリの設定<br>
<a href="http://localhost:8082/anime-web/etc/settings/directory/">http://localhost:8082/anime-web/etc/settings/directory/</a> <br>
ドキュメントルートは変更しなくても問題ありませんが、 <br>
容量が大きいドライブを指定することをお勧めします。 <br>


動画のディレクトリはテレビ番組のプレイリストのパスに使用されます。 <br>
変更しない場合は他のユーザーからは見れないため <br>
共有パスを指定してください。 <br>

アップロードのみを使用する場合は設定不要です。 <br>
 <br>
設定が出来たら送信ボタンを押して、 <br>
サーバーを再起動してください。 <br>

<h2>テレビ番組の設定</h2>
ここからはテレビを録画している方のみ設定してください。 <br>
アップローダーのみを使用したい方はスキップしてください。 <br>
#アニメの追加 <br>

http://localhost:8082/anime-web/etc/settings/addanime/ <br>
<br>
画像をアニメのタイトルと同じファイル名にして保存し、<br>
それをアップロードしてください。<br>
アップロードをするとkakasiを使用して漢字をひらがなに変換します。<br>
読み方に間違いがあれば修正し、下部のアップロードボタンを押下してください。<br>

アップロードすると画像はavif形式に変換されます。<br>

http://localhost:8082/anime-web/anime<br>
でアニメが追加されたことを確認できます。

<h2>動画の登録</h2>
/exec/program/配下のファイルを環境に合わせて編集してください。<br>
<br>
<ul>
  <li>rename_anime.php<br></li>
  動画のファイル名を統一します。
  リネームするディレクトリを指定してください。<br>

<br>
   <li>animecheck.py DBとファイルの整合性を確認します。<br></li>
 DBには存在しているがファイルがない場合はDBを削除<br>
 ファイルはあってDBにない場合はエラーを出力します。<br>
 このファイルの編集は不要です。 <br>
       <br>
       <li>gomi.py<br></li>
       http://localhost:8082/anime-web/etc/settings/exclude/
       
上記で除外設定したタイトルをgomiフォルダに移動します。<br>


 移動元と移動先のディレクトリを指定してください。<br>
    <br><br>
    <li>move.py <br></li>
    DBに一致したtsファイルを移動します。<br>
 エンコードしたファイルを移動してDBに書き込みます。<br>
 実行しても動画が移動されない場合は<br>
 個々のアニメの「別名追加」からファイル名を追加してください。<br>
 ファイル内のディレクトリを編集してください。<br>

 <br>
 <li>program.py</li>
  ts.program.txtから番組の説明を抽出します。<br>
 計算済みのテーブルを作成します。<br>
  ts.program.txtがあるディレクトリを指定してください。<br>
</ul>

<br>
       

<br>
編集ができたら下記の順番で実行してください。<br><br>
DBを更新します。<br>
実行する順番<br>
rename.php<br>
↓<br>
animeCheck.py<br>
↓<br>
move.py<br>
↓<br>
program.py<br>
↓<br>
gomi.py<br>
↓<br>
animeCheck.py<br>
<br>

```bash
php.exe  exec\program\php\rename_anime.php
python exec\program\python\animecheck.py
python exec\program\python\move.p
python exec\program\python\program.py
python exec\program\python\gomi.py
python exec\program\python\animecheck.py
```


<br>
<h2>修正方法</h2>
番組の登録ミスや、続編などを別々に分ける場合は下記を実行してください。<br>
<br>
1.「anime」テーブルから該当のレコードを削除してください。<br>
子表も同時に削除されます。<br>
<br>
2.該当の動画をencodedフォルダに移動<br>
<br>
3.アニメを再度追加<br>
<br>
4.rename.phpから順番に実行すれば修正されます。<br>
<br>
animeCheck.pyでエラーが表示された場合はフォルダを削除するなどしてDBと一致させてください。<br>

<br>

<h2>ランキングの取得方法</h2>
tsファイルをAmatsukazeでエンコードし、エンコードしたmkvファイル内の<br>
assファイルのサイズを抽出しています。<br>
サイズ÷動画の秒数をDBに書き込みます。<br>
番組のタイトルごとに集計しており、中央値でランキングにしています。<br>
<br>
条件<br>
下記をすべて満たしていないとランキングの取得ができません。
Amatsukazeを使用してmkvファイル内にassファイルが格納されていること<br>
ファイル名にチャンネル名が記載されていること(例：NHK総合1など)<br>
ファイル名に下記のいずれかのフォーマットで日付が記載されていること<br>
    YYYYMMDD-hhmm<br>
    YYYY-MM-DD-hhmm<br>
    YYYYMMDDhhmm<br>
    YYMMDD<br>
<br>
再放送の判定方法<br>
再放送をランキングの集計に含めると数値が下がるため除外してます。<br>
下記の場合は再放送と判定します。<br>
・ファイル名に[再]と記載がある<br>
・ファイル名にｔｖｋ１の記載がある<br>
・すでに他局の番組が登録されている場合<br>
但しすべての動画が上記に合致した場合は再放送の判定はせずに<br>
ランキング集計対象にします。<br>
<br>



<h1>アップローダー</h1>
ファイルをアップロードできます。<br>
アップロード後に後処理をサーバーで実施します。<br>
負荷が高いため、キューに入れて順次処理で実行します。<br>
<br>

セキュリティの観点からファイルはブラウザから直接読み取れない領域に保存してます。<br>
apiを経由してファイルにアクセスしますが、30分後にセッションが切れるため、<br>
ファイルが表示されなくなったらブラウザをリロードしてください。<br>


<h2>アップロード後の処理</h2>

動画の場合はhlsに変換します。<br>

<img style="width:200px" src="https://github.com/takashi-koshiba/anime-web/blob/main/%E3%82%AD%E3%83%A3%E3%83%97%E3%83%81%E3%83%A3.PNG">
デフォルト設定では再生する際に画質は帯域によって変化します。<br>
画質を強制させたい場合はメニューバーから指定してください。<br>
音声の場合は音量を均一化します。<br>

画像の場合は縮小しサムネイルを生成します。<br>


ファイル検索のアルゴリズムは
<a href="https://github.com/takashi-koshiba/similar-words">similar-words</a>
を使用しています

  
