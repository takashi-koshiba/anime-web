

document.addEventListener("DOMContentLoaded",function(){
	let imgUploadButton=document.getElementById('imgUpload');
	let selectFileButton=document.getElementById('selectFile');
	
	//結果を取得
	let result=document.getElementById("result");
	
	
	let foldername =document.getElementsByClassName('foldername'); 
	let originalName = document.getElementsByClassName('originalName'); 
	let base64=document.getElementsByClassName('base64'); 
	
	let extension=document.getElementsByClassName('extension'); 
	let error=document.getElementById('error');
		

	
	imgUploadButton.addEventListener("click",function(){
		//let files=selectFile.files;
		let fileCount=base64.length;
		
		
	
		if(fileCount>0){
			let ajax = [];

			for (let i = 0; i < fileCount; i++) {
			    ajax[i] = new class_ajax('/anime-web/api/upload');
				
			    ajax[i].args('img', base64[i].value);
				ajax[i].args('foldername',foldername[i].value)
				ajax[i].args('originalName',originalName[i].innerText);
				ajax[i].args('extension',extension[i].value);
				
				//console.dir(originalName[i]);
			    ajax[i].xhr.onload = function() {
					let result=JSON.parse(this.response);
					
					console.dir(i);
					if (i + 1 < fileCount) {//次のファイルを送信
						document.getElementById('result').innerText=i+"/"+fileCount;
						ajax[i + 1].run();
					}else{
						document.getElementById('result').innerText="完了";
						selectFileButton.value="";//終了したらファイル選択をクリアする
						alert("アップロードが完了しました。");
					}
					
					
					if(result!="ok"){
						//エラーを出力
						
						error.value=error.value+foldername[i].value+"は"+this.response+"\n";
				
						
					}
			    };
				/*
				ajax[i].xhr.onload = function() {
					let result=JSON.parse(this.response);
					if(result!="ok")console.error(result);
				}
				*/
			}

			ajax[0].run();
/*			
			ajax[0].xhr.onload = function() {
				if(this.response[0]!="ok")console.error(this.response);
				
			}
	*/							
								
			
		}else{
			document.getElementById('result').innerText="0/0";
			
						
		}
		


	})
	//番組説明
	let remainingCount=document.getElementById('remainingCount');
	let progAjaxButton =document.getElementById('progAjaxButton');
	let vectorArea=document.getElementById('vectorArea');
	progAjaxButton.addEventListener("click",function(){
		let progcountAjax = new class_ajax('/anime-web/api/db/vectorAPI/progInsert/showCount');
		progcountAjax.run();
		progcountAjax.xhr.onload = function() {
			if (this.status !== 200) {
			    vectorArea.value += `HTTPエラー: ${this.status}`+"\n";
			    return;
			}
			
			let res = this.response;
			
			let progInsertAjax = [];
			if (!isNaN(res) && res>0) {
				vectorArea.value="実行開始"+"\n";
				for(let i=0;i<res;i++){
					progInsertAjax[i] = new class_ajax('/anime-web/api/db/vectorAPI/progInsert/1');
					progInsertAjax[i].xhr.onload = function() {
						if (this.status !== 200) {
						    vectorArea.value += `HTTPエラー: ${this.status}`+"\n";
						    return;
						}
						
						remainingCount.innerText=(res-i);
						if (i + 1 < res) {
							progInsertAjax[i+1].run();
						}
						else{
							
						}
											
						
					}
					progInsertAjax[i].xhr.addEventListener('error', function() {
								vectorArea.value+="通信に失敗しました。"+"\n";
					});
				}
				progInsertAjax[0].run();
				progInsertAjax[0].xhr.addEventListener('error', function() {
							vectorArea.value+="通信に失敗しました。"+"\n";
				});
			} else {
			    vectorArea.value+="エラー"+"\n"
			}
			
		}
		progcountAjax.xhr.addEventListener('error', function() {
					vectorArea.value="通信に失敗しました。"+"\n";
		});
		
	})
	
	//番組名検索
	let remainingTitleCount=document.getElementById('remainingTitleCount');
	let titleAjaxButton =document.getElementById('titleAjaxButton');
	let vectorTitleArea=document.getElementById('vectorTitleArea');
	titleAjaxButton.addEventListener("click",function(){
			let titlecountAjax = new class_ajax('/anime-web/api/db/vectorAPI/AnimeVector/showCount');
			titlecountAjax.run();
			titlecountAjax.xhr.onload = function() {
				if (this.status !== 200) {
				    vectorTitleArea.value += `HTTPエラー: ${this.status}`+"\n";
				    return;
				}
				
				let res = this.response;
				
				let titleInsertAjax = [];
				console.dir(res);
				if (!isNaN(res) && res>0) {
					vectorTitleArea.value="実行開始"+"\n";
					for(let i=0;i<res;i++){
						titleInsertAjax[i] = new class_ajax('/anime-web/api/db/vectorAPI/AnimeVector/1');
						titleInsertAjax[i].xhr.onload = function() {
							if (this.status !== 200) {
							    vectorTitleArea.value += `HTTPエラー: ${this.status}`+"\n";
							    return;
							}
							
							remainingTitleCount.innerText=(res-i);
							if (i + 1 < res) {
								titleInsertAjax[i+1].run();
							}
							else{
								remainingTitleCount.innerText=0;
							}
												
							
						}
						titleInsertAjax[i].xhr.addEventListener('error', function() {
									vectorTitleArea.value+="通信に失敗しました。"+"\n";
						});
					}
					titleInsertAjax[0].run();
					if(res==1){
						titleInsertAjax[0].xhr.onload = function() {
						    if (this.status !== 200) {
							    vectorTitleArea.value += `HTTPエラー: ${this.status}`+"\n";
								    return;
							 }
							 remainingTitleCount.innerText=0;
						}
					}
					
					
					titleInsertAjax[0].xhr.addEventListener('error', function() {
								vectorTitleArea.value+="通信に失敗しました。"+"\n";
					});
				} else {
				    vectorTitleArea.value+="エラー"+"\n"
				}
				
			}
			titlecountAjax.xhr.addEventListener('error', function() {
						vectorTitleArea.value="通信に失敗しました。"+"\n";
			});
			
		})
}) 






