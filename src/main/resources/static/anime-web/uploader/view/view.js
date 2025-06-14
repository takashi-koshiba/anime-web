document.addEventListener("DOMContentLoaded",function(){
	let pageObj={
		current:0,//現在いるページのインデックス
		pagemax:1,//ページの上限
		
		itemLimit:100,//表示するアイテムの数
		pageQty:5//表示するページの数
	}
	let items=[];
	(async function ()  {
	  items =await exec(); // execの処理が完了するまで待つ
	})();
	
	
	let itemViewBack =document.getElementById("itemViewBack");
	let hls;
	//表示をキャンセル
	itemViewBack.addEventListener("click",function(event){

		closeView(event,this);

	});
	
	let qtyValueTemp=getQtyValueOfbutton();
	
	let canSeekFlag=true;
	let prevSeekIndex=null;
	
	window.addEventListener("resize",function(){
		resizeScreen();

		
	})
	
	function resizeScreen(){
		changeViewDivSize();


		let canvas =  document.getElementById('canvasSeek');
		margin=0;
		if (canvas !=null){
			margin = parseFloat(window.getComputedStyle(canvas).marginLeft);
		}
		
		let width=document.body.clientWidth;
		
		if (document.fullscreenElement) {
			return;
		}
		if(canvas!=null){
			canvas.setAttribute('width',(document.body.clientWidth*0.8)-margin*2 );
		}

		let pauseDiv = document.getElementById("pauseDiv");
		if(pauseDiv!=null){
			pauseDiv.style.width = document.body.clientWidth * 0.8 + "px";
		}

		let progress = document.getElementById("progress");
		if(progress!=null){
			progress.style.width=(width*0.8)-margin*2+"px";
		}
		let videoContorols = document.getElementById("videoContorols");
		if(videoContorols!=null){
			videoContorols.style.width=(width*0.8)-margin*2+"px";
		}
	}
	
	function itemAdd(type,originalType,alias){
		//let type = obj.getAttribute("itemtype");
		//let alias=obj.getAttribute("title");
		
		//let originalType=obj.getAttribute("originalType");
		//console.dir(originalType);
		let path="/anime-web/get-file/upload/data/view/";
		
		
		let itemView = document.getElementById("itemView");

		while (itemView.children.length > 0) {
		  itemView.children[0].remove();
		}

		let elem;
		if(type=="IMAGE"){
			elem=document.createElement("img");
			elem.setAttribute("src",path+alias);

		
			setTypeElem(elem,originalType,type,alias);
		}else if(type=="VIDEO" &&getQtyValueOfbutton()!=-2){
			elem=document.createElement("video");
			elem.setAttribute('id','playable');
			//elem.setAttribute("controls", "");
			//elem.controls = true; // 常に表示
	
			if (Hls.isSupported()) {
				  hls = new Hls({
					    capLevelToPlayerSize: true, 
						maxBufferLength: 60, // バッファ
						maxBufferSize: 300 * 1000 * 1000, // バッファサイズの上限 
						maxMaxBufferLength: 60, // 最大バッファ長
						nudgeMaxRetry: 15, 
						nudgeOffset: 0.2,   
						backBufferLength: 10, 
					});

				hls.loadSource(path + alias);
				 
			    hls.attachMedia(elem);

				hls.autoLevelEnabled=false;
				
				hls.on(Hls.Events.MANIFEST_PARSED, function () {
			      console.log('HLS manifest parsed successfully.');
				  let highestQuality = hls.levels.length - 1; // 最高画質のインデックス
				  hls.currentLevel = getQtyValueOfbutton()==1?highestQuality:getQtyValueOfbutton();
				  addSwitchQltEventButton();
			    });
			    hls.on(Hls.Events.ERROR, function (event, data) {
			      console.error('HLS error occurred: ', data);
				  hls.recoverMediaError();
			    });
				hls.on(Hls.Events.LEVEL_SWITCHED, function (event, data) {
				       let newQuality = data.level; // 新しい画質のレベル
				       let bitrate = hls.levels[newQuality].bitrate; // ビットレート情報
				       console.log(`Quality changed to level ${newQuality} (Bitrate: ${bitrate} bps)`);
				   });
			} else if (elem.canPlayType('application/x-mpegURL')) {
			  console.log('Browser supports HLS natively.');
			  elem.src = videoSrc;
			} else {
			  console.error('HLS is not supported in this browser.');
			}

			
			elem.play();
			
			
			elem.addEventListener('click', function(e) {
				//親要素のイベントが伝搬されないようにする
				e.stopPropagation();
				})
				
			
			setTypeElem(elem,originalType,type,alias);
			
			let canvas = document.createElement("canvas");
			canvas.setAttribute('id','canvasSeek');
					
		
			itemView.appendChild(canvas); 
			//addSeekEvent(canvas,elem,alias);

			//'動画の一時停止用に見えないdivを追加'
			itemView.appendChild(addPauseDiv()); 
			
			//プログレスバーを表示
			let progress = document.createElement("progress");
			progress.value=0;
			progress.setAttribute('id','progress');
			addProgressEvents(progress,elem,canvas,alias);
			itemView.appendChild(progress); 
			
			
			let videoContorols = document.createElement("div");
			videoContorols.setAttribute('id','videoContorols');
			itemView.appendChild(videoContorols); 
			
			let playIcon = document.createElement("div");
			playIcon.setAttribute('id','playIcon');

			playIcon.style.backgroundSize = "contain"; 
			playIcon.addEventListener('click',function(){
				if (elem.paused) {
				    elem.play(); // 再生

				} else {
				    elem.pause(); // 一時停止
				}
			})
			
			
			videoContorols.appendChild(playIcon); 
			
			
			
			let videoTime_p = document.createElement("p");
			let videoTime_div = document.createElement("div");
			videoTime_p.setAttribute('id','videoTime_p');
			videoTime_div.setAttribute('id','videoTime_div');
			videoContorols.appendChild(videoTime_div); 
			videoTime_div.appendChild(videoTime_p); 

			
			let valumeIcon = document.createElement("div");
			valumeIcon.setAttribute('id','valumeIcon');
			valumeIcon.style.backgroundImage = 'url("/anime-web/uploader/view/icons/volume1.avif")';
			valumeIcon.addEventListener('click', function(e) {
				if(elem.volume>0){
					elem.volume = 0;
					valumeLevel_prg.value=0;
				}else{
					
					elem.volume=volumeLevel/100;
					valumeLevel_prg.value=volumeLevel;
				}
			});
			
			
			videoContorols.appendChild(valumeIcon); 
			
			let valumeLevel_div = document.createElement("div");	
			valumeLevel_div.setAttribute('id','valumeLevel_div');
			videoContorols.appendChild(valumeLevel_div); 
			
			//音量
			let volumeLevel=100;
			let valumeLevel_prg = document.createElement("input");
			valumeLevel_prg.setAttribute('type','range');	
			valumeLevel_prg.setAttribute('id','valumeLevel_prg');
			valumeLevel_prg.max=100;
			valumeLevel_prg.value=volumeLevel;
	
			
			
			valumeLevel_prg.addEventListener('input', function(e) {
				
				moveBar(e);
				

			});
			
			valumeLevel_prg.addEventListener('change', function(e) {
				moveBar(e);
			});
			
			
			
			
			valumeLevel_div.appendChild(valumeLevel_prg); 
			resizeScreen();
			
			function moveBar(e){

				
				elem.volume = e.target.value/100;


			}
			
			//動画の再生時間を反映
			elem.addEventListener('timeupdate', function() {
				let videoDuration=elem.duration;
				if(!isNaN(videoDuration))  {
					progress.value= ((elem.currentTime/videoDuration));
					videoTime_p.innerText=formatTime(elem.currentTime)+"/"+formatTime(videoDuration);
				}
				
				
			});
			elem.addEventListener('volumechange',function(){
				if(elem.volume>0){
					valumeIcon.style.backgroundImage = 'url("/anime-web/uploader/view/icons/volume1.avif")';
				}
				else if(elem.volume<=0){
					valumeIcon.style.backgroundImage = 'url("/anime-web/uploader/view/icons/volume2.avif")';
				}
			});
			
			elem.addEventListener('play', () => {
			  playIcon.style.backgroundImage = 'url("/anime-web/uploader/view/icons/stop.avif")';
			});

			// 一時停止時
			elem.addEventListener('pause', () => {
			  playIcon.style.backgroundImage = 'url("/anime-web/uploader/view/icons/play.avif")';
			});
			
			
			//シークと音量変更
			document.addEventListener('keydown', event => {
				let dist;
			    if (event.code === 'ArrowRight' ||event.code === 'KeyD') {	
					dist=5;
					seekKey(dist);
					
			    }else if(event.code === 'ArrowLeft'||event.code === 'KeyA'){
					dist=-5;
					seekKey(dist);
				}
				
				else if(event.code === 'ArrowUp'){
					let vol=10;
					volumeKey(vol);
				}
				else if(event.code === 'ArrowDown'){
					let vol=-10;
					volumeKey(vol);
				}
				console.dir(event.code );
				
				
			});
			document.addEventListener('keyup', event => {
				
				if(event.code === 'Space'){
					if (elem.paused) {
						elem.play(); // 再生

					} else {
						elem.pause(); // 一時停止
					}
				}
					
			})
			
			
			
			function volumeKey(vol) {
			    console.dir(valumeLevel_prg.value);
			    
			    let currentValue = Number(valumeLevel_prg.value);
				let result= Math.min(Math.max(currentValue + vol,0),100);
			    valumeLevel_prg.value = result;

			    console.dir(valumeLevel_prg.value);
				elem.volume=(result)/100;
				
				togglePlayBarTimer();
			}


			function seekKey(dist){
				elem.currentTime=elem.currentTime+dist;
				togglePlayBarTimer();
			}
			
		}else if(type=="AUDIO" ||getQtyValueOfbutton()==-2){

			elem=document.createElement("audio");
			elem.setAttribute('id','playable');
			//originalType=elem.;
			elem.setAttribute("src",path+alias+"?onlyAudio=true");
			elem.setAttribute("controls", "");
			elem.play();
			setTypeElem(elem,originalType,"AUDIO",alias);
			addSwitchQltEventButton();
			
			'動画の一時停止用に見えないdivを追加'
			//itemView.appendChild(addPauseDiv()); 
			
		}else{
			return;
			
		}
		
		//setTypeElem(elem,idName,type,alias);

		//elem.setAttribute("src","/anime-web/get-file/upload/image/view/data/"+alias);
		 
		
		
		
	//	console.dir(itemView.children.length);
		
		itemView.appendChild(elem); 
		
		
		itemViewBack.style.display="block";
		//console.dir(imgPath+this.getAttribute("title"));
		
		//動画と音声の一時停止のイベントを追加
		addEventPauseDiv();
	}
	  function  addProgressEvents(progress,video,canvas,alias){
		
		canvas.addEventListener('mousemove',async function(e){
			const canvas = document.getElementById("canvasSeek");
			const cvs = canvas.getContext("2d");
			cvs.clearRect(0, 0, canvas.width, canvas.height);

		});
	
		let prevFrame=-1;
		let canLoadSeekImg=true;
		progress.addEventListener('mousemove',async function(e){
			let percent=prgPos(e,this)

			const canvas = document.getElementById("canvasSeek");
			const cvs = canvas.getContext("2d");
			//itemView.appendChild(canvas);
			const getCanvasWidth = () => {
			    const itemV = canvas;
			    const style = window.getComputedStyle(canvas);
			    const marginLeft = parseFloat(style.marginLeft) || 0;
			    const marginRight = parseFloat(style.marginRight) || 0;
				
				const margin=marginLeft + marginRight;
				const w=(itemV.offsetWidth) - margin;
				
			    return [w,margin];
			};
			const canvasW = getCanvasWidth();
			
			let currentSeekFrame = Math.ceil(percent*Math.ceil(video.duration)) ;
			const frameStep = Math.ceil(video.duration/100);
			currentSeekFrame=(currentSeekFrame-(currentSeekFrame%frameStep));
		
			//currentSeekFrame=Math.min(currentSeekFrame-(currentSeekFrame%frameStep),0);
			
            try {
				if(prevFrame!=currentSeekFrame &&canLoadSeekImg){
					canLoadSeekImg=false;

					let imgData;
					if (Number.isNaN(currentSeekFrame)){
						imgData = await getSeekImage(alias, 0);
						
					}else {
						imgData = await getSeekImage(alias, currentSeekFrame);
					}
					
					renderSeekThumbnail(cvs, imgData, percent, 200, canvasW,currentSeekFrame);
					prevFrame=currentSeekFrame;
					canLoadSeekImg=true;
				}

            } catch (error) {
                console.error("Failed to get seek image:", error);
				canLoadSeekImg=true;
            }
		});
		
		//クリックした位置で再生
		progress.addEventListener('click',function(e){
			let percent=prgPos(e,this);
			video.currentTime = Math.floor(percent*Math.floor(video.duration)) ;
			
		})
	
		function prgPos(e, progress) {
		    const rect = progress.getBoundingClientRect();
		    const x = e.clientX - rect.left;
		    let percent = (x / rect.width);
		    // 0〜1の範囲に制限
		    percent = Math.max(0, Math.min(1, percent));
		    return percent;
		}

		
		function renderSeekThumbnail(cvs, result, percent, seekMaxX, canvasW,currentSeekFrame) {
		    //高さを90pxでリサイズ
			const h = 90;
		    const imgRatio = result.height / h;
		    const w = result.width / imgRatio;
			const imgMargin=5;//サムネの背景の余白
			const margin = parseFloat(window.getComputedStyle(canvas).marginLeft);
		    const canvasPosY = 150 - h - margin * 2 - 30;
			const elemWidth=canvasW[0];
			
		    //let seekImgPosX = (currentSeekPosX / seekMaxX) * elemWidth - (w + imgMargin * 2) / 2;
			let seekImgPosX = (elemWidth+margin)*percent-(w/2)-imgMargin;
			


			//画像のサムネイルが範囲外にならないように調整
			if(seekImgPosX<0)seekImgPosX=0;
			else if(seekImgPosX+w+imgMargin*2>canvas.width)seekImgPosX=canvas.width-(w+imgMargin*2);
			
		    const image = new Image();
		    image.src = result.imgPath;
		    image.onload = () => {
		        cvs.clearRect(0, 0, canvas.width, canvas.height);
		        cvs.save();
		
		        drawRoundedRect(cvs, seekImgPosX, canvasPosY, w + imgMargin * 2, h + imgMargin * 2, 5);
		        cvs.fillStyle = "rgb(255, 255, 255)";
		        cvs.fill();
				
				//再生時間を描画
				cvs.textAlign = 'center';
				cvs.font = '24px Arial';  
				cvs.fillStyle = '#FFB6C1'; 
				cvs.fillText(formatTime(currentSeekFrame) , seekImgPosX + (w / 2)+margin/2, 130);
				
		        drawRoundedRect(cvs, seekImgPosX + imgMargin / 2, canvasPosY + imgMargin / 2, w + imgMargin, h + imgMargin, 5);
		        cvs.clip();
		        cvs.drawImage(image, seekImgPosX + imgMargin / 2, canvasPosY + imgMargin / 2, w + imgMargin, h + imgMargin);
		        cvs.restore();
		
		        URL.revokeObjectURL(image.src);

		    };
					}
					


		
	}

	
	
	function formatTime(seconds) {
	  seconds = Math.floor(seconds); 
	  const hrs = Math.floor(seconds / 3600);
	  const mins = Math.floor((seconds % 3600) / 60);
	  const secs = seconds % 60;

	  // ゼロ埋め関数
	  const pad = n => n.toString().padStart(2, '0');

	  if (hrs > 0) {
	    return `${hrs}:${pad(mins)}:${pad(secs)}`;
	  } else {
	    return `${pad(mins)}:${pad(secs)}`;
	  }
	}
	
	function addEventPauseDiv(){
		let pauseDiv = document.getElementById("pauseDiv");
		let media= document.getElementById("playable");

		
		if(pauseDiv==null) return;
		pauseDiv.addEventListener('click',function(){
			if (media.paused) {
			    media.play(); // 再生

			} else {
			    media.pause(); // 一時停止
			}
		});
		//ダブルクリックでフルスクリーンにする
		pauseDiv.addEventListener('dblclick', function(){
			let overlay=document.getElementById("itemView");
			
			if (!document.fullscreenElement) {
			    try {
			      const promise = overlay.requestFullscreen();
				  fullScreen(true);
				  
				  
			      if (promise) {
			        promise.catch(err => {
			          console.error("Fullscreen request failed:", err);
			        });
			      }
			    } catch (err) {
			      console.error("Fullscreen request failed:", err);
			    }
			  } else {
				fullScreen(false);
			    document.exitFullscreen().catch(err => {
			      console.error("Exit fullscreen failed:", err);
			    });
			  }
			});
			

			pauseDiv.addEventListener('mousemove', function() {
				togglePlayBarTimer();
			});
			
			
			//表示をキャンセル
			let itemViewBack =document.getElementById("itemViewBack");
			itemViewBack.addEventListener("mousemove",function(e){
				if (e.target !== this) return;
				isVisible=false;
				togglePlayBar(isVisible);

			});

	}
	function fullScreen(isFullScreen){
		let canvas =  document.getElementById('canvasSeek');
		let pauseDiv = document.getElementById('pauseDiv');
		let progress=document.getElementById('progress');
		let videoContorols=document.getElementById('videoContorols');
		
		let width=document.body.clientWidth;
		const scrollbarWidth = window.innerWidth - document.body.clientWidth;
		if(isFullScreen){
			
			
			let margin = parseFloat(window.getComputedStyle(canvas).marginLeft);

			canvas.width=width-margin;

			pauseDiv.style.width = width+margin*2+ "px";
			
			progress.style.width=width-margin+ "px";

		}else{
			resizeScreen()
		}
		
	}
	
	
	function addPauseDiv(){
		let pauseDiv = document.createElement("div");
		pauseDiv.setAttribute("id", "pauseDiv");
		pauseDiv.style.width = document.body.clientWidth * 0.8 + "px";
		pauseDiv.style.height = "100%";
		pauseDiv.style.zIndex = "20"; 
		pauseDiv.style.objectFit = "contain";  
		pauseDiv.style.position="absolute";
		
		pauseDiv.style.opacity=0.3
		return pauseDiv;
	}

	function setTypeElem(elem,originalType,type,alias){
		elem.setAttribute("originaltype",originalType);

		elem.setAttribute("itemtype",type);
		elem.setAttribute("title",alias);
	}
	
	function changeViewDivSize(){
		let view_div_child = document.getElementById('view_div_child');
		let view_div_child2 =document.getElementById('view_div_child2');
		
		let view_div_childW=view_div_child.clientWidth;
		let fileElem = document.getElementsByClassName('fileElem');
		
		
		
		if(fileElem.length>1){
			let fileElemW=fileElem[1].clientWidth;	
			let fileElemQty=Math.floor(view_div_childW/fileElemW);				
			view_div_child2.style.width=fileElemW*fileElemQty+"px";
		}
			
	}
	function closeView(event,obj){
		if(event.target.id==obj.id){
			let itemViewBack = document.getElementById("itemViewBack");	
			itemViewBack.style.display="none";
		
		
			//メディアファイル
			let media= document.getElementById("playable");
			if(media){
				let type=media.getAttribute("itemtype");
				if(type=="AUDIO"){
					media.pause();
				}
				disposeHls(media);
				media.remove();
			}
			
			
			let itemView = document.getElementById("itemView");

			while (itemView.children.length > 0) {
			  itemView.children[0].remove();
			}
		}


	}	
	function disposeHls(elem){
		
		if(elem.getAttribute("itemtype")!="VIDEO") return;
		if(hls==='undefined' && hls==null) return; 
		
		hls.destroy();
		hls=null;
		
		if(elem!=null){
			elem.pause();
			
			
			
		}
	}
	
	async function fileApi() {
	  const paramArr=getParam();
	  const res = await fetch(`/anime-web/getFile/view/api/file?inputStr=${paramArr[0]}&column=${paramArr[1]}&ftype=${paramArr[3]}&sort=${paramArr[2]}`);
	  	  
	  //const res = await fetch(`http://localhost:8080/anime-web/getFile/view/api/file?inputStr=&column=1&ftype=-1&sort=0);
	  const items = await res.json();
	 // console.log(items)
	  
	  return items;
	  
	  
	  
	}
	function getQtyValueOfbutton(){
		let qltSelect = document.getElementsByClassName("qltSelect");
		let qltIndex=-1;
		let result=-1;
		for(let i =0;i<qltSelect.length;i++){
			let r=qltSelect[i].classList.contains("selectedButton");
			if(r){
				qltIndex=i;
			}
		}
		switch(qltIndex){
			case 0://音声のみ
				result=-2;
				break;
			case 1://最低画質
				result=0;
				break;
			case 2://最高画質
				result=1;
				break;
			case 3://自動
				result=-1;
				break
		}
		return result;
		
	
	}
	
	function getParam(){
		let paramArr=[];
		let url = new URL(window.location.href);
		let params = url.searchParams;
		paramArr[0]=params.get('fname')==null?"":params.get('fname'); 
		paramArr[1]=params.get('column')==null?"1":params.get('column');
		paramArr[2]=params.get('sort')==null?"0":params.get('sort'); 
		paramArr[3]=params.get('ftype')==null?"-1":params.get('ftype');  
		return paramArr;
		
	}
	
	async function insertElem(items, f, l) {
		let view_div_child2 = document.getElementById('view_div_child2');
		let fileElem = document.getElementsByClassName('fileElem')[0];
		let max = f + l > items.length ? items.length : f + l;
		let min = f > max ? max : f;

		async function loadOne(i) {
			if (i >= max) return;

			let clonefileElem = fileElem.cloneNode(true);
			clonefileElem.style.display = "block";

			let img = clonefileElem.children[0].children[0].children[0];
			img.setAttribute("title", items[i]["alias"]);

			let imgUrl=items[i]["url"];

			img.setAttribute("src", imgUrl);

			img.setAttribute("itemtype", items[i]["type"]);
			img.setAttribute("originaltype", items[i]["type"]);
			img.setAttribute("loading", "lazy");
			img.addEventListener("click", function () {
				itemAdd(this.getAttribute("itemtype"), this.getAttribute("originalType"), this.getAttribute("title"));
			});
			

			let title = clonefileElem.children[0].children[1].children[0].children[0];
			title.innerText = items[i]["fname"] + items[i]["lname"];

			let href = clonefileElem.children[0].children[1].children[0];
			href.setAttribute("href", "/anime-web/get-file/upload/data-original/view/" + items[i]["alias"]);
			href.setAttribute("download", items[i]["fname"] + items[i]["lname"]);

			let delCheckBox = clonefileElem.children[0].children[2].children[0].children[0];
			delCheckBox.setAttribute("alias", items[i]["alias"]);

			view_div_child2.appendChild(clonefileElem);
			
			img.onload = () => {
			    
				// 次の画像を読み込む
				loadOne(i + 1);
			};

			
			
		}

		loadOne(min);
	}

	async function exec(){
		const items = await fileApi(); 
		console.dir(items);
		pageObj.pagemax=Math.ceil(items.length/pageObj.itemLimit);
		let offset =pageObj.current*pageObj.itemLimit;
		await insertElem(items,offset,pageObj.itemLimit); 
		changeViewDivSize();
		addPageButton();
		return items;
		
	}
	function addPageClickEvent(pageElem,page){
		
	    pageElem.addEventListener("click",function(){
		    changePage(page);

		})
		
	}
	function changePage(page){
		pageObj.current=page;
		let offset =pageObj.current*pageObj.itemLimit;
		console.dir(offset);
		delItemAll();
		console.dir(items);
		if (items.length > 0) {
		  insertElem(items, offset, pageObj.itemLimit);
		  addPageButton();
		} else {
		  console.error("Items not loaded yet.");
		}
	}
	function delItemAll(){
		let view_div_child2 = document.getElementById('view_div_child2');
		console.dir(view_div_child2.children.length)
		let len=view_div_child2.children.length;
		for(let i=1;i<len;i++){
			
			
			//最初の要素は残しておく
			view_div_child2.removeChild(view_div_child2.lastChild);
		}
		
		window.scroll({
		  top: 0,
		  behavior: "instant",
		});
		
	}
	function delPageButton(){
		let pegeButtons = document.getElementById('pegeButtons');
		console.dir(pegeButtons.children.length)
		let len=pegeButtons.children.length;
		for(let i=1;i<len;i++){
			
			
			//最初の要素は残しておく
			pegeButtons.removeChild(pegeButtons.lastChild);
		}
	}
	
	function addSwitchQltEventButton(){
		
		
		let qltSelect = document.getElementsByClassName("qltSelect");
		
		if(hls!=null) highestQuality = hls.levels.length - 1; // 最高画質のインデックス
		
		for(let i =0;i<qltSelect.length;i++){
			qltSelect[i].addEventListener('click',function(){
				
				let media= document.getElementById("playable");
				const originalType = media?.getAttribute("originaltype");
				if(media?.getAttribute("originaltype")=== "undefined" || !media
			       ||media==null || originalType!="VIDEO") {
					qtyValueTemp=getQtyValueOfbutton();
					return;
				   }
				reloadFile();

				qtyValueTemp=getQtyValueOfbutton();
				if(hls==null) return;
				
				//disposeHls()
				hls.currentLevel = getQtyValueOfbutton()==1?highestQuality:getQtyValueOfbutton();
				
			})
		}
		
	}
	function reloadFile(){

		
		
		if(isSwitchAudio()){
			let media =document.getElementById("playable");
			let itemType=media.getAttribute("itemType");
			
			if(itemType!="AUDIO" &&itemType!="VIDEO") return;
			//削除
			disposeHls(media);



			//タイプの切り替え
			media.setAttribute("itemType",getQtyValueOfbutton()==-2?"AUDIO":"VIDEO");
		//	elem.setAttribute("originalType",getQtyValueOfbutton()==-2?"AUDIO":"VIDEO");
			let type = media.getAttribute("itemtype");
			let originalType=media.getAttribute("originalType");
			let alias=media.getAttribute("title");

		  	itemAdd(type,originalType,alias);

			qtyValueTemp=getQtyValueOfbutton();
			//return;
			
			//let itemViewBack = document.getElementById("itemViewBack");	
			//itemViewBack.style.display="none";
		}
	}
	
	function isSwitchAudio(){
		
		
		
		if((qtyValueTemp==-2&&qtyValueTemp!=getQtyValueOfbutton())
		    ||(qtyValueTemp!=-2&&-2==getQtyValueOfbutton())){
			return true
		}
		return false;
	}
	class Pos {
	    constructor(marginX, marginY, videoWidth, videoHeight) {
	        this.marginX = marginX;
	        this.minPosX = this.marginX;
	        this.maxPosX = videoWidth - this.marginX;
	        this.minPosY = videoHeight - marginY;
	        this.maxPosY = videoHeight;
	    }
		
		
	    getMarginX() {
	        return this.marginX;
	    }

	    getMinPosX() {
	        return this.minPosX;
	    }

	    getMaxPosX() {
	        return this.maxPosX;
	    }
		getMinPosY() {
	        return this.minPosY;
	    }
		getMaxPosY() {
		    return this.maxPosY;
		}
	}


	
	function addSeekEvent(elem, video, alias) {
	    const scrollbarWidth = window.innerWidth - document.body.clientWidth;
	    const canvas = document.getElementById("canvasSeek");
	    const cvs = canvas.getContext("2d");
	    //itemView.appendChild(canvas);

		//canvasをクリックでシークする
	    canvas.addEventListener("click", (event) => {
			const getCanvasWidth = () => {
			    const itemV = canvas;
			    const style = window.getComputedStyle(canvas);
			    const marginLeft = parseFloat(style.marginLeft) || 0;
			    const marginRight = parseFloat(style.marginRight) || 0;
				
				const margin=marginLeft + marginRight;
				const w=(itemV.offsetWidth) - margin;
				
			    return [w,margin];
			};

			const canvasW = getCanvasWidth();
			    



			const videoMarginX = (window.innerWidth - canvasW[0] ) / 2;

			const currentSeekPosX = event.x - videoMarginX+canvasW[1];
			const seekMaxX = canvasW[0]+canvasW[1];

			//console.dir(currentSeekPosX+"/"+seekMaxX+"l"+(currentSeekPosX/seekMaxX) );
			
	        video.currentTime = (Math.round((currentSeekPosX / seekMaxX) * 100000) / 100000) * video.duration;
			
	    });

		
		
		
	}

	

	function drawRoundedRect(context, x, y, width, height, radius) {
	    // 角丸のパスを作成
	    context.beginPath();
	    context.moveTo(x + radius, y);
	    context.lineTo(x + width - radius, y);
	    context.arcTo(x + width, y, x + width, y + radius, radius);
	    context.lineTo(x + width, y + height - radius);
	    context.arcTo(x + width, y + height, x + width - radius, y + height, radius);
	    context.lineTo(x + radius, y + height);
	    context.arcTo(x, y + height, x, y + height - radius, radius);
	    context.lineTo(x, y + radius);
	    context.arcTo(x, y, x + radius, y, radius);
	    context.closePath();
	
		return context;
	 
	    
	}

	async function  api(url) {
		const response = await fetch(url);
		const json = await response.json(); 
		return json;

	}
	async function getData(url){
		let data=await api(url);
		return data;
		
	}

	
	async function getSeekImage(alias,frame) {
		let url="/anime-web/get-file/anime/image/seek/"+alias+"/"+frame;
		let data = await getData(url);

		return data;
	}
	function addPageButton(){
		
		let pegeButtons = document.getElementById('pegeButtons');
		let pageButton = document.getElementsByClassName('pageButton')[0];
		delPageButton();
	//	console.dir(pageObj);
		//ページが個数の半分以下の時
		if(pageObj.current<Math.ceil(pageObj.pageQty/2) ||pageObj.pageQty>pageObj.pagemax){
			let max =pageObj.pageQty>pageObj.max?pageObj.max:pageObj.pageQty;
			for(let i=0;i<max;i++){
				let clonefileElem=pageButton.cloneNode(true);
				addPageClickEvent(clonefileElem,i);
				
				clonefileElem.style.display="inline-block";
				clonefileElem.children[0].innerText=i+1;
				
				if(pageObj.current==i) clonefileElem.style.color="red";
				pegeButtons.appendChild(clonefileElem);
			}
		}else if(pageObj.current+Math.floor(pageObj.pageQty/2)<pageObj.pagemax){
			
			let firstPos=pageObj.current-Math.floor(pageObj.pageQty/2);
			for(let i=firstPos;i<pageObj.pageQty+firstPos;i++){
				let clonefileElem=pageButton.cloneNode(true);
				addPageClickEvent(clonefileElem,i);
				
				clonefileElem.style.display="inline-block";
				clonefileElem.children[0].innerText=i+1;
				
				if(pageObj.current==i) clonefileElem.style.color="red";
				pegeButtons.appendChild(clonefileElem);
			}
		}else{
			//ページが最大値に近づいたとき
			for(let i=pageObj.pagemax-pageObj.pageQty;i<pageObj.pagemax;i++){
				let clonefileElem=pageButton.cloneNode(true);
				addPageClickEvent(clonefileElem,i);
				
				clonefileElem.style.display="inline-block";
				clonefileElem.children[0].innerText=i+1;
				
				if(pageObj.current==i) clonefileElem.style.color="red";
				pegeButtons.appendChild(clonefileElem);
			}
		}
	}
	
	
	//動画下部のアイテムの表示非表示を切り替える
	let moveTimeout;
	let isVisible=true;
	function togglePlayBarTimer(){
		

		
		
		// すでにタイマーがあればキャンセル
		  clearTimeout(moveTimeout);
			
		  //消えてたら表示
			isVisible=true;
			togglePlayBar(isVisible);
		  
		  // 4000ms後に非表示
		  moveTimeout = setTimeout(function() {
			isVisible=false;
		    togglePlayBar(isVisible);
			
		  }, 3000);
		  
		  
		  
		  
	}
	
	function togglePlayBar(isVisible) {
	    const canvas = document.getElementById('canvasSeek');
	    const progress = document.getElementById('progress');
	    const videoContorols=document.getElementById('videoContorols');
	    
	    [canvas, progress,videoContorols].forEach(el => {
	      if (!el) return; // 要素が存在しないときにスキップ

	      if (isVisible) {
	        el.classList.remove('hidden');
	      } else {
	        el.classList.add('hidden');
	      }
	    });
	  }

	
})


