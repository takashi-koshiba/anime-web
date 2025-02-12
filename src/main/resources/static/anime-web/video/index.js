
document.addEventListener("DOMContentLoaded",function(){
	let play_p = document.getElementById('play_p');

	play_p.setAttribute('href','/anime-web/api/db/videoApi/playList/'+getId()+"?sort="+ getParam("sort"));

	
	//exec();
	window.addEventListener('resize', function(){
		setVideosH(20);
	});
	
	
	
	
	function getId(){
		let index=location.pathname.lastIndexOf('/');
		let result=location.pathname.substring(index+1);
		return result;
	}
	function getParam(p){
		let url = new URL(window.location.href);
		let params = url.searchParams;
		return params.get(p);
	}
	
	
	function setVideosH(h){
		
		let videos=document.getElementsByClassName('videos');

		for(let i=0;i<videos.length;i++){
			let pH=videos[i].children[0].clientHeight+h;

			videos[i].style.height=pH+"px";
		}
	}
	
	

});


