
document.addEventListener("DOMContentLoaded",function(){
	let rank=document.getElementsByClassName('rank');
	let rankWidth=rank[0].children[1].offsetWidth;
	let anime_childW=250; //cssの画像の横幅と合わせる
	let ImgCount=Math.floor(rankWidth/anime_childW);
	
	let margins=rankWidth-(anime_childW*ImgCount);
	let margin=margins/(ImgCount*2);
	
	let pages=2;
	let initImgCount=ImgCount*pages+2;
	let imgRootUrl;
	let imgData;
	let calculateLayoutTimer;

	let root=location.protocol+"/anime-web/";
	let url=root+"api/db/rankedAnime/";
	let rankObj=[];
	
	//apiから画像のパスとファイル名を取得
	getData(root,url).then((result) =>{
		imgRootUrl= result[1];
		imgData=result[0];
		console.dir(imgData);
		

		//ランキングの数だけ実行
		for(let rankIndex=0;rankIndex<rank.length;rankIndex++){
			
			rankObj[rankIndex]={
				pos:0,
				index:0
			}
			
			
			setElements(imgData,initImgCount,imgRootUrl,rankIndex,margin);
			addScrollEvents(rank[rankIndex].children[1],anime_childW,rankObj[rankIndex],initImgCount,imgRootUrl);
			
			//左ボタン
			rank[rankIndex].children[0].addEventListener('click',function(){
				buttonEvent(1,rank[rankIndex].children[1],rankWidth,rankObj[rankIndex],anime_childW,imgData,
					imgRootUrl
				);
			})
			
			//右ボタン
			rank[rankIndex].children[2].addEventListener('click',function(){
				
				buttonEvent(-1,rank[rankIndex].children[1],rankWidth,rankObj[rankIndex],anime_childW,imgData,
					imgRootUrl
				)
			});
		}
	})
	
	function calculateLayout(){
		rank=document.getElementsByClassName('rank');
		rankWidth=rank[0].children[1].offsetWidth;
		anime_childW=250; //cssの画像の横幅と合わせてください
		ImgCount=Math.floor(rankWidth/anime_childW);

		//余白
		margins=rankWidth-(anime_childW*ImgCount);
		margin=margins/(ImgCount*2);

		initImgCount=ImgCount*pages+2;
	}
	
	window.addEventListener('resize', function(){
		
	
		calculateLayout();
		
		for(let i=0;i<rank.length;i++){
			calculateMargin(rank[i].children[1],anime_childW);
		}
		
		if (calculateLayoutTimer) {
			//センダリング処理が終わる前に次のスクロールが終わると
			//バグるのでスクロールしたらセンダリングをキャンセル
			//
		    clearTimeout(calculateLayoutTimer); 
		}
		
		calculateLayoutTimer=setTimeout(function(){
			const rank_animeTemplate=document.getElementById('rank-animeTemplate');
			for(let i=0;i<rank.length;i++){
				
				centering(rank[i].children[1],anime_childW,rankObj,initImgCount,imgData,imgRootUrl,rank_animeTemplate)
				RangeToAddElem(rank[i].children[1],imgRootUrl,rank_animeTemplate,margin);
			}
		},100);
		
	});
	
	function RangeToAddElem(rank_scroll,imgRootUrl,rank_animeTemplate,margin){
		let ImgCount=Math.floor(rankWidth/anime_childW);
		let min=rankObj.index-ImgCount<0?0:rankObj.index-ImgCount;
		let max=ImgCount*pages+rankObj.index;
		
		for(let i=min;i<=max;i++){
			addElement(i,imgData,rank_scroll,imgRootUrl,rank_animeTemplate,margin);
		}
		let pos=rank_scroll.scrollLeft;
		let scrollPos=Math.round(pos/anime_childW);
		ImgCount=Math.floor(rankWidth/anime_childW);
		rankObj.pos=anime_childW*scrollPos*-1;
		rankObj.index=scrollPos;	
		initImgCount=ImgCount*pages+2;

	}
	function addScrollEvents(rank_scroll,anime_childW,rankObj,initImgCount,imgRootUrl){
		const rank_animeTemplate=document.getElementById('rank-animeTemplate');
		
		let scrollTimeout;
		rank_scroll.addEventListener('scrollend',function(){
			
			scrollTimeout=setTimeout(function(){
				
				centering(rank_scroll,anime_childW,rankObj,
				         initImgCount,imgData,imgRootUrl,rank_animeTemplate);
						

			},100);
		})
		
		rank_scroll.addEventListener('scroll',function(){
			if (scrollTimeout) {
				//スクロールが終わる前に次のスクロールが終わるとバグるので
				//スクロールしたらセンダリングをキャンセル
			    clearTimeout(scrollTimeout); 
			}
		})
		
	}



	function buttonEvent(direction,rank_scroll,rankWidth,rankObj,anime_childW,imgData,imgRootUrl){
		moveElements(direction,rank_scroll,rankWidth,rankObj);



	}
	function addElement(index,imgData,rank_scroll,imgRootUrl,rank_animeTemplate,imgMargin){
		let animeLen=rank_scroll.children[0].children.length;

		if(index<0){
			return;
		}
		//挿入
		if(animeLen>index&&index>=0){

			let originalName=imgData[index]['originalName'];
			let url=imgRootUrl['url']+'content/anime-web/upload/img/thumbnail/'+originalName+".webp"

			setAnimeElem(rank_scroll,index,url,originalName);
			
		}else if(imgData.length>index){//追加
			let element=createAnimeElements(imgData,imgRootUrl,rank_animeTemplate,imgMargin,index);
			rank_scroll.children[0].appendChild(element);
			
		}
		
		
	}

	function moveElements(direction,rank_scroll,rankWidth,rankObj){
		let elements=rank_scroll.children[0].children;
		
		let count=0;

		for (let i=0;i<elements.length;i++){

			if(direction==1&&rankObj.index!=0){//右に移動
				
				rank_scroll.scrollTo({
					left:(rankObj.pos*-1)-rankWidth,
					behavior:'smooth'
				});
				
				count++;
				
			}else if(direction==-1){//左に移動
				rank_scroll.scrollTo({
					left:(rankObj.pos*-1)+rankWidth,
					behavior:'smooth'
				});
				count++;
			}

		}
	}


	function setElements(imgData,initImgCount,imgRootUrl,rankIndex,margin){
		const rank_animeTemplate=document.getElementById('rank-animeTemplate');
		let rank_div=document.getElementsByClassName('rank-div')[rankIndex];

		let maxImg=imgData.length<initImgCount-1?imgData.length:initImgCount;
		for(let i=0;i<maxImg;i++){
			
			
			let createElements=createAnimeElements(imgData,imgRootUrl,rank_animeTemplate,margin,i)
			rank_div.appendChild(createElements);
		}
	}

	function createAnimeElements(imgData,imgRootUrl,rank_animeTemplate,margin,index){
		let rank_anime=rank_animeTemplate.cloneNode(true);
		rank_anime.style.display="block";
		rank_anime.style.marginLeft =margin+"px";
		rank_anime.style.marginRight=margin+"px";

		rank_anime.setAttribute('class','rank-anime');
		rank_anime.setAttribute('id','');
		
		let originalName=imgData[index]['originalName'];

		let rank_animeTitle=rank_anime.children[0].children[1].children[0];
		rank_animeTitle.innerText=originalName;
		
		let rank_animeImg=rank_anime.children[0].children[0].children[0];
		rank_animeImg.setAttribute("src",imgRootUrl['url']+'content/anime-web/upload/img/thumbnail/'+originalName+".webp");
		
		
		
		return rank_anime;
	}

	function calculateMargin(rank_scroll,anime_childW){
		let rankWidth=rank_scroll.offsetWidth;
		let ImgCount=Math.floor(rankWidth/anime_childW);

		//余白
		let margins=rankWidth-(anime_childW*ImgCount);
		let margin=margins/(ImgCount*2);
		

		
		for(let i=0;i<rank_scroll.children[0].children.length;i++){
			rank_scroll.children[0].children[i].style.marginLeft =margin+"px";
			rank_scroll.children[0].children[i].style.marginRight=margin+"px";

		}
	}

	async function  api(url) {
		const response = await fetch(url);
		const json = await response.json(); 

		return json;
	}

	async function getData(root,url){
		
		
		imgData=await api(url);
		imgRootUrl= await api(root+"api/setting/");
		return [imgData,imgRootUrl];
		
	}

	function processRankElems(rankObj,scrollPos,imgData,rank_scroll,imgRootUrl,rank_animeTemplate,imgMargin,anime_childW){
		let abs=Math.abs(rankObj.index-scrollPos);
		
		let rankWidth=rank_scroll.offsetWidth;
		let ImgCount=Math.floor(rankWidth/anime_childW);
		
		let dir;
		let initImgCount=ImgCount*pages+2;
		let imgIndex=(initImgCount)+rankObj.index;

		if(rankObj.index-scrollPos<0){
			dir=0;
			for(let i=0;i<abs;i++){

				addElement(imgIndex+i,imgData,rank_scroll,imgRootUrl,rank_animeTemplate,imgMargin);
				delElements(anime_childW,rank_scroll,rankObj.index+i,dir);
			}	
		}else{
			dir=1;
			let ii=1;
			for(let i=imgIndex;i+abs> imgIndex;i--){

				addElement(i-initImgCount-ImgCount-1,imgData,rank_scroll,imgRootUrl,rank_animeTemplate,imgMargin,1);
				delElements(anime_childW,rank_scroll,rankObj.index+initImgCount-ii,dir);
				ii++;
			}
		}
	}function delElements(anime_childW,rank_scroll,imgIndex,dir){
		let rankWidth=rank_scroll.offsetWidth;
		let maxImgIndex=rank_scroll.children[0].children.length-1;
		let ImgCount=Math.floor(rankWidth/anime_childW);
		if(imgIndex>=ImgCount&&dir==0){

			delElem(imgIndex-ImgCount,rank_scroll);
		}else if(dir==1&&imgIndex<=maxImgIndex){

			delElem(imgIndex,rank_scroll);
		}
	}
	function delElem(imgIndex,rank_scroll){
		setAnimeElem(rank_scroll,imgIndex,"","");
	}

	function setAnimeElem(rank_scroll,imgIndex,url,title){
		let item=rank_scroll.children[0].children[imgIndex];

		let img=item.children[0].children[0].children[0];
		let animeTitle=item.children[0].children[1].children[0];
		img.setAttribute('src',url);
		animeTitle.innerText=title;
	}


	function centering(rank_scroll,anime_childW,rankObj,initImgCount,imgData,imgRootUrl,rank_animeTemplate){
		let pos=rank_scroll.scrollLeft;
		let imgMargin=parseFloat(rank_scroll.children[0].children[0].style.marginLeft);
		let imgWidth=imgMargin*2+anime_childW;
		let scrollPos=Math.round(pos/imgWidth);

		if(Math.round(pos)!=Math.round(imgWidth*scrollPos)){
			
			rank_scroll.scrollTo({
				left:imgWidth*scrollPos,
				behavior:'smooth'
			});

		}

		//要素の追加と削除
		processRankElems(rankObj,scrollPos,imgData,
			rank_scroll,imgRootUrl,rank_animeTemplate,imgMargin,anime_childW);
			
			
		rankObj.pos=imgWidth*scrollPos*-1;
		rankObj.index=scrollPos;	

		
	}
});


