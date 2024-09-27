
document.addEventListener("DOMContentLoaded",function(){
	
	
	
	let poster_img = document.getElementById('poster_img');

	

	exec();
	
	async function exec(){
		let settingUrl='/anime-web/api/setting/';
		let id=getId();
		//anime-web/api/db/select-one/{id}
		let urlRank='/anime-web/api/db/select-one/'+id;
		let animeData=await getData(urlRank);
		
		let rootUrl=await getData(settingUrl);
		
		await setImg(rootUrl,animeData);

		
	}
	
	
	async function setImg(rootUrl,animeData){
		console.dir(animeData)
		let url=rootUrl['url']+'content/anime-web/upload/img/thumbnail/'+animeData['originalName']+".webp";
		poster_img.setAttribute('src',url);
	}
	
	function getId(){
		let index=location.pathname.lastIndexOf('/');
		let result=location.pathname.substring(index+1);
		return result;
	}
	
	async function  api(url) {

			const response = await fetch(url);
			const json = await response.json(); 
			return json;

	}
	async function getData(url){
		
		
		let setting=await api(url);
		return setting;
		
	}
	

});


