document.addEventListener("DOMContentLoaded",function(){
	let button=document.getElementById('menu-button');
	let menuCancel=document.getElementById('menuCancel');
	let menuCancelDisplay=menuCancel.style.display;
	let menu=document.getElementById('menu');
	let delButton=document.getElementById('delButton');
	
	
	delButton.addEventListener('click',function(){
		if(!window.confirm("ファイルを削除しますか?")){return}
		fileDel();
	})
	
	button.addEventListener('click',function(){
		
		switchMenu();
		
	})
	menuCancel.addEventListener	('click',function(){
			
		switchMenu();
			
	})

	addEventChangeQltButton();
	
	function switchMenu() {
	  
	    if (menuCancel.style.display === "block") {
	    
	        menu.style.transform = "translate(0px, 0px)";
	        menuCancel.style.display = "none";
	    } else {
	  
	        menu.style.transform = "translate(310px, 0px)";
	        menuCancel.style.display = "block";
	    }
	}
	function fileDel(){
		
		
		
		
		let fileElem = document.getElementsByClassName('fileElem');
		if(fileElem.length<=1){
			alert("ファイルがありません");
			return;
		}

		
		
		let isChecked=false;
		let error=false;
		for(let i=1;i<fileElem.length;i++){
			let elem = fileElem[i].children[0].children[2].children[0].children[0]
			let elemChecked=elem.checked;
			if(!elemChecked){
				continue;
				
			}	
			
			isChecked=true
			let alias=elem.getAttribute("alias");
			
			let ajax = new class_ajax("/anime-web/getFile/view/del/elem");
			ajax.args("alias",alias);
			
			
			ajax.run();
			ajax.xhr.addEventListener('loadend', function() {
				
				if(!error){
					if(this.status==403){
						alert("セッションが切れました");
						
					}else if(this.status!=200){
						alert("エラーが発生しました。ステータス："+this.status);
					}

				}

				error=true;
			});
			//console.dir(fileElem[i].children[0].children[2].children[0].children[0]);
		}

		if(!isChecked){
			alert("選択されているファイルがありません");
			return;
		}
		if(!error){
			alert("削除しました。");
			location.reload();
		}
	}
	

	function addEventChangeQltButton(){
			let qltSelect = document.getElementsByClassName("qltSelect");
			for(let i =0;i<qltSelect.length;i++){
				qltSelect[i].addEventListener('click',function(){
					qltSelect[0].classList.remove("selectedButton");
					qltSelect[1].classList.remove("selectedButton");
					qltSelect[2].classList.remove("selectedButton");
					this.classList.add("selectedButton");
				})
			}
	}
	//transform:translateX(620px) ;
})


