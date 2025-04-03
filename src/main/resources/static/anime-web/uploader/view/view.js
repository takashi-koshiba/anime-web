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
		changeViewDivSize();
		
		let canvas =  document.getElementById('canvasSeek');
		
		canvas.setAttribute('width',(document.body.clientWidth*0.8)-32 );
	})
	function itemAdd(obj){
		let type = obj.getAttribute("itemtype");
		let alias=obj.getAttribute("title");
		let elem;
		let originalType=obj.getAttribute("originalType");
		//console.dir(originalType);
		let path="/anime-web/get-file/upload/data/view/";
		
		
		let itemView=document.getElementById("itemView");
		
		
		
		
		if(type=="IMAGE"){
			elem=document.createElement("img");
			elem.setAttribute("src",path+alias);

		
			setTypeElem(elem,originalType,type,alias);
		}else if(type=="VIDEO" &&getQtyValueOfbutton()!=-2){
			elem=document.createElement("video");
			elem.setAttribute("controls", "");
			elem.controls = true; // 常に表示

		
			
			if (Hls.isSupported()) {
				  hls = new Hls({
					    capLevelToPlayerSize: true, 
						maxBufferLength: 30, // バッファ
						maxBufferSize: 30 * 1000 * 1000, // バッファサイズの上限 
						maxMaxBufferLength: 60, // 最大バッファ長
						nudgeMaxRetry: 15, 
						nudgeOffset: 0.2,    
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
				
			//スマホ用に画面タッチで再生と一時停止できるようにする
			elem.addEventListener('touchstart', function(e) {
						    // コントローラー内のボタンがクリックされた場合、クリックを無視する
						    if (e.target.tagName === 'BUTTON') return;

						    // コントローラーが非表示ではない場合
						    if (elem.paused) {
						        elem.play(); // 再生
						    } else {
						        elem.pause(); // 一時停止
						    }
						});

			setTypeElem(elem,originalType,type,alias);
			
			let canvas = document.createElement("canvas");
			canvas.setAttribute('id','canvasSeek');
					
			console.dir(elem.clientWidth);
			canvas.setAttribute('width',(document.body.clientWidth*0.8)-32 );
			itemView.appendChild(canvas); 
			addSeekEvent(canvas,elem,alias);

			
		}else if(type=="AUDIO" ||getQtyValueOfbutton()==-2){

			elem=document.createElement("audio");
			//originalType=elem.;
			elem.setAttribute("src",path+alias+"?onlyAudio=true");
			elem.setAttribute("controls", "");
			elem.play();
			setTypeElem(elem,originalType,"AUDIO",alias);
			addSwitchQltEventButton();
		}else{
			return;
			
		}
		
		//setTypeElem(elem,idName,type,alias);

		//elem.setAttribute("src","/anime-web/get-file/upload/image/view/data/"+alias);
		 
		
		
		
	//	console.dir(itemView.children.length);
		
		itemView.appendChild(elem); 
		
		
		itemViewBack.style.display="block";
		//console.dir(imgPath+this.getAttribute("title"));

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
			let itemView = document.getElementById("itemViewBack");	
			itemView.style.display="none";
		
		
			//メディアファイル
			elem=document.getElementById("itemView").children[1];
			if(elem){
				let type=elem.getAttribute("itemtype");
				if(type=="AUDIO"){
					elem.pause();
				}
				disposeHls(elem);
				elem.remove();
			}
			
			elem=document.getElementById("itemView").children[0];
			elem.remove();
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
	
	async function insertElem(items,f,l){
	    let view_div_child2 = document.getElementById('view_div_child2');
	    let fileElem = document.getElementsByClassName('fileElem')[0];
	  
	    const thumbnail="/anime-web/get-file/anime/image/small/";
		let max=f+l>items.length?items.length:f+l;
		let min=f>max?max:f;
	    for(let i=min;i<max;i++){
	        let clonefileElem=fileElem.cloneNode(true);
		    clonefileElem.style.display="block";
		  
		    let img=clonefileElem.children[0].children[0].children[0];
		    img.setAttribute("title", items[i]["alias"]);
		    img.setAttribute("src", thumbnail+items[i]["alias"]);
		    img.setAttribute("itemtype", items[i]["type"]);
			img.setAttribute("originaltype", items[i]["type"]);
		    img.addEventListener("click",function(){
		  	    itemAdd(this);
		  		
		     });
		  
		     let title=clonefileElem.children[0].children[1].children[0].children[0];
		     title.innerText=items[i]["fname"]+items[i]["lname"];
		  
			 let href=clonefileElem.children[0].children[1].children[0];
			 href.setAttribute("href", "/anime-web/get-file/upload/data-original/view/"+items[i]["alias"]);
		     
			 href.setAttribute("download",items[i]["fname"]+items[i]["lname"]);
			 
			 view_div_child2.appendChild(clonefileElem);
			 
			 
			 let delCheckBox=clonefileElem.children[0].children[2].children[0].children[0];
			 delCheckBox.setAttribute("alias",items[i]["alias"]);
	     }
	  }
	
	async function exec(){
		const items = await fileApi(); 
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
				
				
				let itemView= document.getElementById("itemView");
				elem=itemView.children[0];

				
				const originalType = elem?.getAttribute("originaltype");
				if(elem?.getAttribute("originaltype")=== "undefined" || !elem
			       ||elem==null || originalType!="VIDEO") {
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
			

					
			

			let type=elem.getAttribute("itemType");
			
			if(type!="AUDIO" &&type!="VIDEO") return;
			//削除
			disposeHls(elem);
			elem.remove();
			//新しく追加
			
			elem.setAttribute("itemType",getQtyValueOfbutton()==-2?"AUDIO":"VIDEO");
		//	elem.setAttribute("originalType",getQtyValueOfbutton()==-2?"AUDIO":"VIDEO");
			itemAdd(elem);
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
	    itemView.appendChild(canvas);

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
		
		canvas.addEventListener("mouseleave", (event) => {
			cvs.clearRect(0, 0, canvas.width, canvas.height);
		})
		
		//サムネを表示
	    canvas.addEventListener("mousemove", async (event) => {
			
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
			    


			
			const canvasH = document.getElementById("itemView").children[1].offsetHeight;
			
	        const videoMarginX = (window.innerWidth - canvasW[0] ) / 2;

	        const currentSeekPosX = event.x - videoMarginX+canvasW[1];
	        const seekMaxX = canvasW[0]+canvasW[1];
	        const mousePosY = event.y;
	        const seekBarPos = new Pos(0, 25, seekMaxX, canvasH);
						
	        if (
	            currentSeekPosX >= seekBarPos.getMinPosX() &&
	            currentSeekPosX <= seekBarPos.getMaxPosX() &&
	            mousePosY > seekBarPos.getMinPosY()
	        ) {
	            const currentSeekFrame = Math.floor((currentSeekPosX / seekBarPos.getMaxPosX()) * video.duration)-1 ;
	            if (currentSeekFrame === prevSeekIndex) return;
	            prevSeekIndex = currentSeekFrame;

	            try {
	                const result = await getSeekImage(alias, currentSeekFrame);
	                renderSeekThumbnail(cvs, result, currentSeekPosX, seekMaxX, canvasW[0]);
	            } catch (error) {
	                console.error("Failed to get seek image:", error);
	            }
	        }
			
		
			
			// video へイベントを転送
			const newEvent = new MouseEvent("mousemove", {

			    bubbles: true,
			    cancelable: true,
			    view: window,
			    clientX: event.clientX,
			    clientY: event.clientY
			});

			// シークバーを強制的に表示
			video.focus();
			video.controls = true;  

			video.dispatchEvent(newEvent);


			
	    });
		
		
		function renderSeekThumbnail(cvs, result, currentSeekPosX, seekMaxX, elemWidth) {
			    const h = 90;
			    const imgRatio = result.height / h;
			    const w = result.width / imgRatio;
			    const imgMargin = 5;
			    const canvasPosY = 150 - h - imgMargin * 2 - 30;
			    let seekImgPosX = (currentSeekPosX / seekMaxX) * elemWidth - (w + imgMargin * 2) / 2;
			
				
				//画像をサムネイルが範囲外にならないように調整
				if(seekImgPosX+(w + imgMargin * 2) / 2<(w/2))seekImgPosX=0;
				else if(seekMaxX-(w + imgMargin * 2)<seekImgPosX)seekImgPosX=seekMaxX-(w + imgMargin * 2);
								
			    const image = new Image();
			    image.src = result.imgPath;
			    image.onload = () => {
			        cvs.clearRect(0, 0, canvas.width, canvas.height);
			        cvs.save();

			        drawRoundedRect(cvs, seekImgPosX, canvasPosY, w + imgMargin * 2, h + imgMargin * 2, 5);
			        cvs.fillStyle = "rgb(255, 255, 255)";
			        cvs.fill();

			        drawRoundedRect(cvs, seekImgPosX + imgMargin / 2, canvasPosY + imgMargin / 2, w + imgMargin, h + imgMargin, 5);
			        cvs.clip();
			        cvs.drawImage(image, seekImgPosX + imgMargin / 2, canvasPosY + imgMargin / 2, w + imgMargin, h + imgMargin);
			        cvs.restore();

			        URL.revokeObjectURL(image.src);
			    };
			}
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
		let url=location.protocol+"/anime-web/get-file/anime/image/seek/"+alias+"/"+frame;
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
	
	
		
	
	
})


