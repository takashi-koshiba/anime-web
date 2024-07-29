//ウィンドウの横幅を変えてもレイアウトが変わらないようにする
document.addEventListener("DOMContentLoaded",function(){
	let main=document.getElementById('main');
	let left=document.getElementById('left');
	let left_main=document.getElementById('left_main');
	left_main.style.width=(main.offsetWidth-left.offsetWidth)-2+"px";
			
	window.addEventListener("resize",function(){
		left_main.style.width=((main.offsetWidth)-left.offsetWidth)-2+"px";
		
	})
});


