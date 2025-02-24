document.addEventListener("DOMContentLoaded",function(){
	let submit = document.getElementById('submit');
	let error=document.getElementById('error');
	let per_p=document.getElementById('per_p');
	let result_p=document.getElementById('result');
	let bar_txt=document.getElementById('bar_txt');
	let bar=document.getElementById('bar');
	
	submit.addEventListener('click',function(){

		let upload=new class_upload();
				
		
		let file = document.getElementById('file');
		
		
		
		let files=file.files;
		let fileCount=files.length;
		if(fileCount==0){
			return;
		}
		
		bar_txt.innerText="("+0+"/"+fileCount+")";
		result_p.innerText="アップロード中";
		
		per_p.innerText="0%";
		bar.value=0;
		
		let ajax2 = [];
		
		
		let Isduplicate = document.getElementById('duplicate').checked;
		uploadFile(0, ajax2,fileCount,Isduplicate); // 最初のファイルのアップロードを開始

		// 2番目以降のファイルの処理
		for (let i = 1; i < fileCount; i++) {
			try{
				
				uploadFile(i, ajax2,fileCount,Isduplicate);
			}catch(e){
				error.value=error.value+e; 
			}
		    
		}
		function uploadFile(index, ajax2,fileCount,Isduplicate) {
		    //let getFile = upload.GetFile(files[index]);
		    ajax2[index] = new class_ajax('/anime-web/uploader/sendFile');
			console.dir(ajax2[index]);
			console.dir("送信");

			console.dir(Isduplicate);
	        ajax2[index].args('name', files[index].name);
			ajax2[index].args('file', files[index]);
			ajax2[index].args('Isduplicate', Isduplicate);
			
			// 最初のファイルの処理
	        if (index == 0) {
				
				progress(fileCount,index);
	            ajax2[index].xhr.onloadend = function() {
					console.dir(ajax2[index].xhr.status);
					
					showErr(ajax2[index].xhr.status,files[index].name);
					
	                if (fileCount > 1) {
	                    ajax2[index + 1].run(); //2個目を実行
						if(this.response=="false"&& ajax2[index].xhr!=403){
							error.value=error.value+"書き込みに失敗しました。 "+files[index].name+"\r\n"; 
	                    }
	                }
					
					bar_txt.innerText="("+(1+index)+"/"+fileCount+")";
					if(index+1==fileCount){
						finish();
						
					}
	            }
				ajax2[index].xhr.onerror = function () {
					
					error.value = error.value + "エラーが発生しました。 " + files[index].name + "\r\n";
				}
				
				
				try{
					ajax2[index].run();
				}catch(e){
					error.value=error.value+e; 
				}
	           return;
	        }
			
			
			
	        if (index + 1 < fileCount) {
				progress(fileCount,index);
				
	            ajax2[index].xhr.onloadend = function() {
	                //error.value=error.value+this.response; 
					//result_p.innerText=1+index+"/"+files.length
					if(this.response=="false"){
						error.value=error.value+"書き込みに失敗しました。 "+files[index].name+"\r\n"; 
					}
					showErr(ajax2[index].xhr.status,files[index].name);
					
					try{
						 ajax2[index + 1].run(); 
					}catch(e){
						error.value=error.value+e; 
					}
					
					bar_txt.innerText="("+(1+index)+"/"+fileCount+")";
					if(index+1==fileCount){
						finish();
					}
	            }
				ajax2[index].xhr.onerror = function () {
					error.value=error.value+"エラーが発生しました。"+files[index].name+"\r\n"; 
				}
				
				
	        } else {
				progress(fileCount,index);
	            ajax2[index].xhr.onloadend = function() {
					showErr(ajax2[index].xhr.status,files[index].name);
	                //error.value=error.value+this.response; 
					if(this.response=="false"){
						error.value=error.value+"書き込みに失敗しました。 "+files[index].name+"\r\n"; 
					}
					bar_txt.innerText="("+(1+index)+"/"+fileCount+")";
					if(index+1==fileCount){
						finish();
	            	
					}
					ajax2[index].xhr.onerror = function () {
						error.value = error.value + "エラーが発生しました。 " + files[index].name + "\r\n";
					}
				}
	        }
	        
	        
		 
		}
		function showErr(status,fname){
			if(status==403){
				error.value=error.value+"セッションが切断されています。 "+fname+"\r\n"; 
				result_p.innerText="アップロードに失敗しました。";
				alert("セッションが切れました");
				return;			
								
			}else if(status==400){
				error.value=error.value+"アップロードできませんでした。"; 
				result_p.innerText="アップロードに失敗しました。";
				alert("アップロードできませんでした。");
				return;			
			}else if(status==409){

				error.value=error.value+"ファイルはすでにアップロード済みです。"+fname+"\r\n";
				result_p.innerText="アップロードに失敗しました。";
			}
		}
		function finish(){

			result_p.innerText="アップロードが終了しました。";
			alert("アップロードが終了しました。");
			file.value=null;
		}
		function progress(count,index){
			console.dir(index);
			console.dir(count);
			ajax2[index].xhr_up.onprogress=function(e){
				console.dir(((e.loaded/e.total)*100/count)+((index/count)*100));
				let per=Math.floor((((e.loaded/e.total)*100/count)+((index/count)*100))*100)/100;
				per_p.innerText=per+"%";
				bar.value=per;
			}
		}

		

	
	})
})


