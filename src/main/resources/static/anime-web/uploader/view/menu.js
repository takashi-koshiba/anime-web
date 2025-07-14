document.addEventListener("DOMContentLoaded",function(){
	let button=document.getElementById('menu-button');
	let menuCancel=document.getElementById('menuCancel');
	let menuCancelDisplay=menuCancel.style.display;
	let menu=document.getElementById('menu');
	let delButton=document.getElementById('delButton');
	
	let itemCheck = document.getElementById('itemCheck');
	let itemCheckedClear = document.getElementById('itemCheckedClear');

	function setAllCheckboxes(checked) {
	    const fileElems = document.getElementsByClassName('fileElem');
	    Array.from(fileElems).forEach(child => {
	        const checkBox = child.querySelector('div > div:nth-child(3) input[type="checkbox"]');
	        if (checkBox) checkBox.checked = checked;
	    });
	}

	//チェックボックスのチェックとクリア
	itemCheck.addEventListener('click', () => setAllCheckboxes(true));
	itemCheckedClear.addEventListener('click', () => setAllCheckboxes(false));

	
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
	async function fileDel() {
	    let fileElem = document.getElementsByClassName('fileElem');
	    if (fileElem.length <= 1) {
	        alert("ファイルがありません");
	        return;
	    }
		
	    let promises = [];
	    let isChecked = false;

	    for (let i = 1; i < fileElem.length; i++) {
	        let elem = fileElem[i].children[0].children[2].children[0].children[0];
	        let elemChecked = elem.checked;
	        if (!elemChecked) continue;

	        isChecked = true;
	        let alias = elem.getAttribute("alias");

	        let p = new Promise((resolve, reject) => {
	            let ajax = new class_ajax("/anime-web/getFile/view/del/elem");
	            ajax.args("alias", alias);
	            ajax.run();
	            ajax.xhr.addEventListener('loadend', function () {
	                if (this.status === 403) {
	                    alert("セッションが切れました");
	                    reject("session");
	                } else if (this.status !== 200) {
	                    console.error("エラーが発生しました。ステータス：" + this.status);
	                    reject("error");
	                } else {
	                    resolve();
	                }
	            });
	        });

	        promises.push(p);
	    }

	    if (!isChecked) {
	        alert("選択されているファイルがありません");
	        return;
	    }

	    try {
	        await Promise.all(promises);
	        alert("削除しました。");
	        
	    } catch (e) {
			alert("エラーが発生しました。詳細はコンソール確認してください。");
	    }finally{
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
					qltSelect[3].classList.remove("selectedButton");
					this.classList.add("selectedButton");
				})
			}
	}
	//transform:translateX(620px) ;
})


