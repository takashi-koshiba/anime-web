

document.addEventListener("DOMContentLoaded",function(){
	SetErrorSize();
	
	window.addEventListener("resize", function(){
		SetErrorSize();
	});
	
	

}) 


function SetErrorSize(){
	let windowHeight=window.innerHeight;
	let errorTop=error.offsetTop;
	
	error.style.height=windowHeight-errorTop+"px";
}





