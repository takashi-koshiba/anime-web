//スクロールしたときに左のパネルを固定する
document.addEventListener("DOMContentLoaded",function(){
	let topItem_ground=document.getElementById('topItem_ground');
	let topItem_groundHeight=topItem_ground.offsetTop;
	let leftTop=left.offsetTop;

	window.addEventListener("scroll",function(){
		console.dir(leftTop-topItem_groundHeight);
		if(topItem_groundHeight<=this.pageYOffset){
			left.style.position="fixed";	
			left.style.top=(leftTop-topItem_groundHeight)+"px";
		}else{
			left.style.position="relative";	
			left.style.top=0+"px";
	
		}
		
	})

});


