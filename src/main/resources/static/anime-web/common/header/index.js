
document.addEventListener("DOMContentLoaded",function(){
	let topItem_ground=document.getElementById('topItem_ground')
	let topItem_groundHeight=topItem_ground.offsetTop;
	
	let topImgWidth=document.getElementById('topImg').style.height;
	let topItem=document.getElementById('topItem');

			
	window.addEventListener("scroll",function(){

		if(topItem_groundHeight<=this.pageYOffset){
			topItem_ground.style.position="fixed";	
			topItem_ground.style.top="0px";
			topImg.style.marginBottom=topItem_ground.clientHeight+"px";
		}else{
			topItem_ground.style.position="relative";	
			topImg.style.marginBottom=0+"px";
	
		}
		
	})

});


