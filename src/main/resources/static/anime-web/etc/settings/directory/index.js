document.addEventListener("DOMContentLoaded",function(){
	let root = document.getElementById("root");
	let documentResult=document.getElementById("rootResult");
	let urlResult=document.getElementById("urlResult");
	let url=document.getElementById("url");


	root.addEventListener('input',function(){
		if(this.value==""){
			
		}else{
			let ajax = new class_ajax('/anime-web/api/directory/root/');
			ajax.args('path',this.value);
			ajax.run();
			
			//console.dir(originalName[i]);
			ajax.xhr.onload = function() {
				console.dir(this.response);
				
				if(this.response!="true"){
					rootResult.innerText="パスが存在しません。"
				}else{
					rootResult.innerText="";
				}

			}
		}
		
	})
	url.addEventListener('input',function(){
		if(this.value==""){
			
		}else{
			let ajax2 = new class_ajax('/anime-web/api/directory/url/');
			ajax2.args('url',this.value);
			ajax2.run();
			
			ajax2.xhr.onload = function() {
				console.dir(this.response);
				
				if(this.response!="true"){
					urlResult.innerText="urlの形式ではありません。"
				}else{
					urlResult.innerText="";
				}

			}
		}
	})

	let button =document.getElementById('button');
	button.addEventListener('click',function(){

		if(root.value==""){
			
		}else{
			let ajax2 = new class_ajax('/anime-web/api/change-directory/');

			ajax2.args('path',root.value);
			ajax2.args('url',url.value);
			ajax2.run();
			
			//console.dir(originalName[i]);
			ajax2.xhr.onload = function() {
				
				
				switch(this.response) {
				    case "0":
				        alert("ディレクトリの設定に成功しました。");
				        break;
				    case "1":
				        alert("指定されたディレクトリが存在しません。");
				        break;
				    case "2":
				        alert("ディレクトリの設定に失敗しました。");
				        break;
					case "3":
						alert("指定された値はurlの形式ではありません。");
						break;
				    default:
				        alert("不明");
				        break;
				}
			}
		}
		
		
	})

})
