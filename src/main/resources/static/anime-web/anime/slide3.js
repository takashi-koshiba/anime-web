
document.addEventListener("DOMContentLoaded",function(){
	let rank=document.getElementsByClassName('rank');
	let rankWidth=rank[0].children[1].offsetWidth;
	let anime_childW=250; //cssの画像の横幅と合わせる
	let ImgCount=Math.floor(rankWidth/anime_childW);

	//余白
	let margins=rankWidth-(anime_childW*ImgCount);
	let margin=margins/(ImgCount*2);
	
	let initImgCount=ImgCount*2;//最初に表示する画像の数



	
	let imgRootUrl;
	let imgData;
	

	let root=location.protocol+"/anime-web/";
	let url=root+"api/db/rankedAnime/";
	
	//apiから画像のパスとファイル名を取得
	getData(root,url).then((result) =>{
		imgRootUrl= result[1];
		imgData=result[0];
		
		for(let rankIndex=0;rankIndex<rank.length;rankIndex++){
			setElements(imgData,initImgCount,imgRootUrl,rankIndex,margin);
		}
	})

});

function setElements(imgData,initImgCount,imgRootUrl,rankIndex,margin){
	const rank_animeTemplate=document.getElementById('rank-animeTemplate');
	let rank_div=document.getElementsByClassName('rank-div')[rankIndex];
	
	console.dir(imgData);
	let originalName=imgData['originalName'];
	
	//要検証
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