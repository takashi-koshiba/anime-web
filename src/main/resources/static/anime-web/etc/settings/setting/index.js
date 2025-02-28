document.addEventListener("DOMContentLoaded",function(){
	let root = document.getElementById("root");
	let documentResult=document.getElementById("rootResult");
	let urlResult=document.getElementById("urlResult");
	let url=document.getElementById("url");
	let encoder=document.getElementById("encoder");

	root.addEventListener('input',	async function(){
		rootResult.innerText=await isExistPath(this.value);		
	})
	


	let videoPath=document.getElementById('videoPath');
	let videoPathResult=document.getElementById('videoPathResult');
	videoPath.addEventListener('input',async function(){

		
		videoPathResult.innerText=await isExistPath(this.value);
		
	})
	


	
	let button =document.getElementById('button');
	button.addEventListener('click',function(){

		if(root.value=="" ||  videoPath.value==""){
			alert("未入力の欄があります。");
		}else{
			let ajax2 = new class_ajax('/anime-web/api/change-directory/');

			ajax2.args('path',root.value);

			ajax2.args('videoPath',videoPath.value);
			ajax2.args('encoder',encoder.value);
			
			console.dir(encoder.value);
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
					case "4":
						alert("指定された値は動画のディレクトは見つかりませんでした。");
						break;
				    default:
				        alert("不明");
				        break;
				}
			}
		}
		
		
	})
	function api(path,txt,param){
		if (txt == "") {
		    return "";
		}
		return new Promise((resolve, reject) => {


	        let ajax = new class_ajax(path);
	        ajax.args(param, txt);
	        ajax.run();

	        ajax.xhr.onload = function() {
	            if (this.response != "true") {
	                resolve(1);
	            } else {
	                resolve(0); 
	            }
	        }

	        ajax.xhr.onerror = function() {
	            reject(-1); 
	        }
	    });
	}
	
	async function  isUrl(txt) {
		result=await api("/anime-web/api/directory/url/",txt,"url");
		if(result==0){
			return ""
		}else if(result==1){
			return "urlの形式ではありません。"
		}else{
			return "エラーが発生しました。"
		}
	}
	
	async function  isExistPath(txt) {
		result=await api("/anime-web/api/directory/is-exist/",txt,"path");
	    if(result==0){
			return ""
		}else if(result==1){
			return "パスが存在しません"
		}else{
			return "エラーが発生しました。"
		}
		
	}
})
