
document.addEventListener("DOMContentLoaded",function(){
	let animeAll = document.getElementById("animeAll");
	
	
	let InitrowCount=3;//表示する行の数
	let anime_childW=300; //cssの画像の横幅と合わせる
	let anime_childH=240;//cssと合わせる
	let obj={
		index:0
	}		
	
	//let initImgCount=Math.floor(getColumnCount(animeAll,anime_childW))*InitrowCount;

	let initImgCount=3*InitrowCount;




	getAnimeData(getPrefixIndex()).then(result => {
		let animeData=result[0];
		let root=result[1];
		console.dir(animeData)
		
		initAddElem(obj,initImgCount,anime_childH,animeData,root);
	//	setMargin(animeAll);
		
		window.addEventListener('resize', function(){
			//setMargin(animeAll);
			//setanimeAllHeight(obj.index,anime_childH,animeData);
		} );

		window.addEventListener('scroll', function(){
			
			if(animeData.length<=obj.index+1){
				return;
			}
			
			animeLoad(obj,initImgCount,anime_childH,animeData,root);
			
		});

		let load_div = document.getElementById("load_div");
		load_div.addEventListener('click',function(){
			if(animeData.length<=obj.index+1){
				return;
			}
			AddAnimeElements(obj,initImgCount,anime_childH,animeData,root);
		})
	})	
})
function animeLoad(obj,initImgCount,anime_childH,animeData,root){
	let load_div = document.getElementById("load_div");
	let scrollY=window.scrollY;
	let loadDivY=load_div.getBoundingClientRect().y;
	
	//表示ができるなら表示
	if((scrollY+window.innerHeight>loadDivY )&&(animeData.length>obj.index+1)){

		AddAnimeElements(obj,initImgCount,anime_childH,animeData,root);
		//setMargin(animeAll);
		return true;
	}

	return false;
	
}

function initAddElem(obj,initImgCount,anime_childH,animeData,root){
	AddAnimeElements(obj,initImgCount,anime_childH,animeData,root);
	let result=animeLoad(obj,initImgCount,anime_childH,animeData,root);
	while(result){
		result=animeLoad(obj,initImgCount,anime_childH,animeData,root);
	}
}

function AddAnimeElements(obj,initImgCount,anime_childH,animeData,root){
	obj.index=AddAnime(obj.index,animeAll,initImgCount,animeData,root);
	//setanimeAllHeight(obj.index,anime_childH);

}

function AddAnime(index,animeAll,initImgCount,animeData,root){
	
	let animeAllTemplate = document.getElementById("animeAllTemplate");
	
	
	
	if(animeData.length<=index){
		return index;
	}
	
	let i;
	for(i=index;i<initImgCount+index;i++){

		let animeAllT = cloneElements(animeAllTemplate,i,animeData,root);
		animeAll.appendChild(animeAllT);
		
		if(animeData.length<=i+1){
			return i;
		}
	}
	return i;
}

function cloneElements(animeAllTemplate,index,animeData,root){
	let animeAllT = animeAllTemplate.cloneNode(true);
	animeAllT.style.display="block";
	let img=animeAllT.children[0].children[0].children[0].children[0];
	let originalName=animeData[index].originalName;	
	img.setAttribute('src','/anime-web/get-file/anime/image/thumbnail/'+animeData[index]['id'])

	
	let img_a=animeAllT.children[0].children[0].children[0];
	img_a.setAttribute("href","./anime/video/"+animeData[index]['id']);
			
	
	animeAllT.children[0].children[1].children[0].innerText=animeData[index]['originalName'];

	let valueItem=animeAllT.children[0].children[1].children[1];
	valueItem.children[1].innerText=animeData[index]['score'];

	
	return animeAllT;
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
async function getAnimeData(paramIndex) {
	let url=location.protocol+"api/db/select-allSort/"+paramIndex;
	let root= await api(location.protocol+"api/setting/");
	let data = await getData(url);
	return [data,root] 
}
function getPrefixIndex(){
	let url = window.location.href;
	let newUrl = new URL(url);
	let data=newUrl.searchParams.get("prefix");
	let result=data?data:"";
	return result;
}
