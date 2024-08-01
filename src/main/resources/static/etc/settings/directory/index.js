document.addEventListener("DOMContentLoaded",function(){
	let root = document.getElementById("root");
	let result=document.getElementById("result");


	root.addEventListener('input',function(){
		if(root.value==""){
			
		}else{
			let ajax = new class_ajax('/api/directory/');
			ajax.args('path',this.value);
			ajax.run();
			
			//console.dir(originalName[i]);
			ajax.xhr.onload = function() {
				console.dir(this.response);
				
				if(this.response!="true"){
					result.innerText="パスが存在しません。"
				}else{
					result.innerText="";
				}

			}
		}
		
	})

	let button =document.getElementById('button');
	button.addEventListener('click',function(){

		if(root.value==""){
			
		}else{
			let ajax2 = new class_ajax('/api/change-directory/');

			ajax2.args('path',root.value);
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
				    default:
				        alert("不明");
				        break;
				}
			}
		}
		
		
	})

})
