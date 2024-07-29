document.addEventListener("DOMContentLoaded",function(){
	let buttonExecute=document.getElementById('exe');
	let selectFileButton=document.getElementById('selectFile');
	
	let fileEdit=document.getElementById('fileEdit');
	let inputEdit=document.getElementById('inputEdit');
	
	const pattern = /data:(image\/jpeg|image\/png)/;
	let upload=new class_upload(pattern);
	
	//エラーがあれば出力
	let error=document.getElementById('error');
	
	
	buttonExecute.addEventListener("click",function(){
		let files=selectFileButton.files;
		let fileCount=files.length;
		
		document.getElementById('result').innerText="アップロード中";
		
		
	
		if(fileCount!=0){
			let ajax = [];
			let flag=false;
			
			for (let i = 0; i < fileCount; i++) {
			    ajax[i] = new class_ajax('/api/edit');
				
			    ajax[i].args('filename', files[i].name);
				
				//console.dir(files[i].target);
				
				
				
			    ajax[i].xhr.onload = function() {
					//結果を代入
					let json=JSON.parse(this.response);
					//console.dir(json);
					let fname=json.fname;//画像のファイル名
					let folderName=json.foldername;//フォルダ名
					let title=json.title;//タイトル
					let IsExist=json.isExist;
					
					let getFile=upload.GetFile(files[i],pattern);
					
					
					getFile.then(function(result){
						let resultGetFile=result;
						
						//指定した拡張子か
						console.dir(resultGetFile);
						if(resultGetFile[0] != null && !IsExist){
							//テンプレからコピーする
							flag=true;
							
							let inputEditTemp=inputEdit.cloneNode(true);
							inputEditTemp.removeAttribute("id");//id削除
							inputEditTemp.setAttribute("class","inputEdit");
							inputEditTemp.children[0].innerText=title;//
							inputEditTemp.children[0].setAttribute("class","originalName");
							
							inputEditTemp.children[1].value=folderName;
							inputEditTemp.children[1].setAttribute("class","foldername");
							
							
							//バイナリ
							
							inputEditTemp.children[2].value=resultGetFile[0];	
							inputEditTemp.children[2].setAttribute("class","base64");
							
							//拡張子
							inputEditTemp.children[3].value=resultGetFile[2];
							inputEditTemp.children[3].setAttribute("class","extension");
							fileEdit.appendChild(inputEditTemp);	
							
							
						}
						
						else if(IsExist){
							error.value=error.value+"「"+title+"」はすでに登録されています。\n";
						}else{
							//error.value=error.value+"OK";
							error.value=error.value+"「"+resultGetFile[1]+"」はアップロードできません\n";
						}
						
						
						
						if (i + 1 < fileCount) {
							document.getElementById('result').innerText=i+"/"+fileCount;
							
							//次のファイルを送信
						    ajax[i + 1].run();
							
							
						}else{//すべて終了したとき
							
							if(flag){
								imgUpload.style.display="block";
								document.getElementById('result').innerText="完了";
							}else{
								document.getElementById('result').innerText="エラー";
								alert("そのファイルはアップロードできません。");
								
							}
							
							selectFileButton.value="";
							

						}
					});
					
			    };
			}
			
			ajax[0].run();

		}else{
			result.innerText="0/0";
		}

	})
	
	
	
}) 






