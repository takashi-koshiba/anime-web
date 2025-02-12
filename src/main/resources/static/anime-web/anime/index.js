



		
	//センダリングのため、要素の大きさを変更する
	function changeViewDivSize(animeAllWidth,animeElemWidth){
		let animeAll =document.getElementById('animeAll');
		let fileElemQty=Math.floor(animeAllWidth/animeElemWidth);				
		animeAll.style.width=animeElemWidth*fileElemQty+"px";
		
			
	}
	
	function getAnimeAllWidth(){
		let animeAllP= document.getElementById('animeAllP');
		let animeAll =document.getElementById('animeAll');
			
		let animeAllPW=animeAllP.clientWidth;
		let fileElem = animeAll.children;
		

		let fileElemW=fileElem[0].clientWidth;
		return Array(animeAllPW,fileElemW);
		
	}