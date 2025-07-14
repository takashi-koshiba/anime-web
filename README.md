# anime-web
<img src="https://github.com/takashi-koshiba/anime-web/blob/main/2.png">
録画したテレビ番組を管理するために作成しました。<br>
アマゾンプライムやネットフリックスのようにテレビ番組を確認できます。<br>
今期放送中のアニメを表示したり番組の人気度をランキング形式で表示します。<br>
偏差値でどのくらい人気なのかを把握できます。<br>

<br>
ファイルのアップロード機能もあります。
<h1>動作条件</h1>
<ul>
  <li>OS:Windows</li>
  <li>DB:mysql</li>
  
</ul>

<h1>デモ</h1>
<a href="https://youtu.be/Hc1_0fOKe4M">youtubeをご覧ください。</a>

<h1>既知のバグ</h1>
<ul>
 <li>
 
   ~~同じディレクトリに大量のファイルがあるとIOが低下する~~ 
       ←バージョン2.13で修正しました。
 </li>

</ul>



<h1>実装予定の機能</h1>
<ul>
<li>アップロード画像の拡大縮小機能</li>
<li>アップロードしたファイルの詳細情報を表示</li>
<li>アップロードしたファイルを大量に削除できるようにする</li>
  
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


> [!IMPORTANT]
> kakasiとffmpegはパスを通してください。<br>
> アップローダーでNVENCを選択する場合は<a href="#アップロード後の処理">設定</a>が必要です
<br>

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
<a href="http://localhost:8082/anime-web/etc/settings/directory/">http://localhost:8082/anime-web/etc/settings/setting/</a> <br>
ドキュメントルートは変更しなくても問題ありませんが、 <br>
容量が大きいドライブを指定することをお勧めします。 <br>


動画のディレクトリはテレビ番組のプレイリストのパスに使用されます。 <br>
変更しない場合は他のユーザーからは見れないため <br>
共有パスを指定してください。
※アップロードのみを使用する場合は設定不要です。 <br>
 <br>
設定が出来たら送信ボタンを押してください。 <br>
実行しているjavaのプロセスをkillするか、サーバーの再起動を行わないと設定が反映されませんのでご注意ください。 <br>

<h1>テレビ番組の設定</h1>

> [!NOTE]
> ここから先はテレビを録画している方のみ設定を行ってください。<br>
> ファイルのアップロードのみを使用したい場合はスキップしてください。<br>
<br>
#アニメの追加 <br>

http://localhost:8082/anime-web/etc/settings/addanime/ <br>
<br>
アニメの画像をアニメのタイトルと同じファイル名にリネームし、<br>
それをアップロードしてください。<br>
アップロードをするとkakasiを使用して漢字をひらがなに変換します。<br>
読み方に間違いがあれば修正し、下部のアップロードボタンを押下してください。<br>

アップロードすると画像はavif形式に変換されます。<br>

> [!IMPORTANT]
> avif形式への変換はffmpegで実行します。
> ffmpegのバージョンが古いと対応していないため、失敗します。

http://localhost:8082/anime-web/anime<br>
アニメが追加されたことを確認できます。

<h2>動画の登録</h2>
/exec/program/配下のファイルを環境に合わせて編集してください。<br>
<br>
<ul>
  <li>rename_anime.php<br></li>
  動画のファイル名の話数を0で埋めます。<br>
  話数と日付の間にスペースなどがないとリネームに失敗します。<br>
  編集箇所：ファイル中のリネームしたいファイルがあるディレクトリを編集してください。<br>

<br>
   <li>animecheck.py </li>
   DBとファイルの整合性を確認します。<br>
  これを実行すると動画とDBで一致しているかを確認できます。<br>
 DBには存在しているがファイルがない場合はDBを削除。<br>
 動画ファイルは存在しているが、DBにない場合はエラーを出力します。<br>
 編集箇所：このファイルの編集は不要です。<br>
       <br>
       <li>gomi.py<br></li>
       http://localhost:8082/anime-web/etc/settings/exclude/
       
上記で除外設定したタイトルをgomiフォルダに移動します。<br>


 編集箇所：移動元と移動先のディレクトリを指定してください。<br>
    <br><br>
    <li>move.py <br></li>
    DBに一致したtsファイルを移動します。<br>
 エンコードしたファイルを移動してDBに書き込みます。<br>
 実行しても動画が移動されない場合は<br>
 個々のアニメの「別名追加」からファイル名を追加してください。<br>
 ※但しlocalhost以外からの接続だと表示されません。<br><br>
 編集箇所：ファイル内のディレクトリを編集してください。<br>

 <br>
 <li>program.py</li>
  .ts.program.txtから番組の説明を抽出します。<br>
 計算済みのテーブルを作成します。<br><br>
  編集箇所：ファイル中のts.program.txtがあるディレクトリを編集してください。<br>
</ul>

<br>
       

<br>
編集ができたら下記の順番で実行してください。<br><br>
DBを更新と動画ファイルを移動します。<br>
実行する順番<br>
rename_anime.php<br>
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
python exec\program\python\move.py
python exec\program\python\program.py
python exec\program\python\gomi.py
python exec\program\python\animecheck.py
```

> [!IMPORTANT]
> pythonのスクリプトを実行しないとDBの更新が行えません。<br>
> tsファイルのエンコードが終わったら上記スクリプトを定期的に実行してください。<br>



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
<a href="http://localhost:8082/anime-web/etc/settings/addanime/">アニメ追加</a>から追加してください。
<br>

<br>
4.<a href="#動画の登録">動画の登録</a>内のツールのrename_anime.phpから
<br>最後のanimeCheck.pyまで順番に実行すれば修正されます。<br>
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
下記をすべて満たしていないとランキングの取得ができません。<br>
・Amatsukazeを使用してmkvファイル内にassファイルが格納されていること<br>
・ファイル名に下記のいずれかのフォーマットで日付が記載されていること<br>
    YYYYMMDD-hhmm<br>
    YYYY-MM-DD-hhmm<br>
    YYYYMMDDhhmm<br>
    YYMMDD<br>
<br>

動画のファイル名にチャンネル名の記載があり、下記のチャンネル名に合致すること。<br>
BS/CSなどのコメントが取得できない局の追加は不要です。<br>
修正する必要がある場合はmove.pyの下記SQLを修正してください。<br>
修正箇所は3つほどあります。<br>
使用する数字は0以外なら問題ないです。<br>

```bash


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
                            
else 0 end
```
<br>
再放送の判定方法<br>
再放送をランキングの集計に含めると数値が下がるため除外してます。<br>
下記の場合は再放送と判定します。<br>
・ファイル名に[再]と記載がある<br>
・すでに他局で同番組がDBに登録されている場合<br>
但しすべての動画が上記に合致した場合は再放送の判定はせずに<br>
ランキングの集計対象にします。<br>
<br>

<h2>使用方法</h2>
見たい番組を選んでください。<br>
番組が決まっている場合は頭文字を選択したり、タイトルを入力して選択してください。<br>
<img src="https://github.com/takashi-koshiba/anime-web/blob/main/3.PNG">
<br>
<br>
<img src="https://github.com/takashi-koshiba/anime-web/blob/main/2.PNG"><br>
番組を選択すると画面が変わり、動画が表示されます。<br>
動画のタイトルを選択するかプレイリストDLボタンを押下すると.m3u8形式のプレイリストがダウンロードされます。<br>
プレイリストはvlcやmpc-hcなどの再生ソフトで再生できます。<br>
再生ができない場合は動画のパスが間違っている可能性があります。
その場合は<a href="http://localhost:8082/anime-web/etc/settings/setting/">設定</a>から「動画のディレクトリ」を動画がある場所に修正してください。

<h2>番組の重複確認</h2>
<p>動作確認中</p>
<p>動画の全フレームのphashをDBに格納します。</p>
<p>動画から90フレームほど抽出し何ビット一致するか確認し、一致率を計算します。<br>
OP/ED/CMは共通のため、最低でも10～30%ほどは一致しますが、
再放送などで重複あれば90～100%程の一致率です。<br>
フレームの上下でクロップしておりますので局ロゴや時間が乗っている場合でも高い一致率になります。</p>


<p>私の環境では２万個の番組がありますが、phashの書き込みに1,2ヶ月、一致率の計算で5,6日かかりました。<br>
実行するスクリプトは他のスクリプトと同時に実行しても問題ないです。排他制御はしておりますので
タスクスケジューラなどで登録して実行してください。<br>
maybeDuplicate.py はsimilarVideo.pyで取得したphashを使用するため、先にsimilarVideo.pyを実行して全ての動画のphashを登録してください。</p>
※必要なテーブルは動作確認ができ次第、pushします。

```bash
python similarVideo.py    --未処理の動画からphashを取得します。途中で処理を中止しても次回起動時は途中から実行できるので問題ないです。
python maybeDuplicate.py   --類似するtsファイルがあるかを確認し、ファイルを移動します。エンコードする前に重複した再放送を振り分けられます。
python simTest.py [-v] tsファイルのパス        -- maybeDuplicateで使用し、最大の一致率を返します。単体で使用する場合は -v のオプションを付けるとどの番組で重複しているかを確認できます。

```
<br><br>
すでに処理済みの番組の重複確認や重複削除をする場合はSQLを実行して確認してください。

<a href="https://github.com/takashi-koshiba/anime-web/tree/main/exec/sql">sql</a>


<h1>アップローダー</h1>
ファイルをアップロードできます。<br>
アップロード後に後処理をサーバーで実施します。<br>
負荷が高いため、キューに入れて順次処理で実行します。<br>
<br>

セキュリティの観点からファイルはブラウザから直接読み取れない領域に保存してます。<br>
apiを経由してファイルにアクセスしますが、30分後にセッションが切れるため、<br>
ファイルが表示されなくなったらブラウザをリロードしてください。<br>


<h2 id="up_after">アップロード後の処理</h2>

動画の場合はhlsに変換します。<br>

音声の場合は音量を均一化します。<br>

画像の場合は縮小しサムネイルを生成します。<br>

> [!IMPORTANT]
> <a href="http://localhost:8082/anime-web/etc/settings/setting/">設定</a>のエンコーダーでNVENCを選択した場合は
> rigaya氏制作の<a href="https://github.com/rigaya/NVEnc">NVEnc</a>が使用可能です。<br>
> 使用する場合はダウンロードし、「NVEncC64.exe」のパスを通してください。
> ただしNvidia製GPUを搭載しているサーバーのみ利用可能です。

<h2>使用方法</h2>
アップロードする場合はログイン後にメニューバーから「アップロード」を選択してアップロードしてください。<br>

<img style="width:200px" src="https://github.com/takashi-koshiba/anime-web/blob/main/%E3%82%AD%E3%83%A3%E3%83%97%E3%83%81%E3%83%A3.PNG">
デフォルト設定では動画再生する際に画質は帯域によって変化します。<br>
画質を強制させたい場合はメニューバーから画質を指定してください。<br>

ファイル検索のアルゴリズムは
<a href="https://github.com/takashi-koshiba/similar-words">similar-words</a>
を使用しています

<h2>動画再生時のショートカットキー</h2>
<p>再生/停止：スペースキー</p>
<p>前方にシーク：Aキー/左矢印キー</p>
<p>後方にシーク：Dキー/右矢印キー</p>


<br><br>
動画や音楽はラウドネスノーマライズにしており、シークバーにマウスを合わせるとサムネイルが表示されます。
<img src="https://raw.githubusercontent.com/takashi-koshiba/anime-web/refs/heads/main/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%20(1).webp">


<h2>サムネイルが表示されないとき</h2>
動画や画像のファイルのサムネイルが表示されないときは<br>
uploadfileテーブルで該当のファイルの{extension}の列を確認してください<br>
extensionテーブルにないmimeの場合は追加を行ってください。<br>
<br>
例：mkvファイルでmimeが{video/x-matroska}の場合

```bash
insert into extension (ex,type) values("x-matroska",1);
```
typeは0なら画像<br>
1なら動画<br>
2なら音声ファイルです。
<br><br>

<h2>アップロード後の処理のトラブルシューティング</h2>
動画のエンコードが失敗しているときなどは<br>
/anime-web/logs/内のログでコマンド実行が失敗していないかを確認してください。

