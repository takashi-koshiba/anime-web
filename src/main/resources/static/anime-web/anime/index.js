
document.addEventListener("DOMContentLoaded",function(){
	let topItem_ground=document.getElementById('topItem_ground')
	let topItem_groundHeight=topItem_ground.offsetTop;
	
	let topImgWidth=document.getElementById('topImg').style.height;
	let topItem=document.getElementById('topItem');
	console.dir(topItem);
	

	let leftTop=left.offsetHeight;
	

	window.addEventListener("scroll",function(){
		console.dir(leftTop);
		if(topItem_groundHeight<=this.pageYOffset){
			topItem_ground.style.position="fixed";	
			topItem_ground.style.top="0px";
			topImg.style.marginBottom=topItem_ground.clientHeight+"px";
			
			left.style.position="fixed";	
			left.style.top=(leftTop-main.offsetTop)+"px";
		}else{
			topItem_ground.style.position="relative";	
			left.style.position="relative";	
			topImg.style.marginBottom=0+"px";
			left.style.top=0+"px";
	
		}
		
	})
	
	
	

});

