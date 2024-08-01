

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
			    ajax[i] = new class_ajax('/api/upload');
				
			    ajax[i].args('img', base64[i].value);
				ajax[i].args('foldername',foldername[i].value)
				ajax[i].args('originalName',originalName[i].innerText);
				ajax[i].args('extension',extension[i].value);
				
				//console.dir(originalName[i]);
			    ajax[i].xhr.onload = function() {
					if (i + 1 < fileCount) {//次のファイルを送信
						document.getElementById('result').innerText=i+"/"+fileCount;
						ajax[i + 1].run();
					}else{
						document.getElementById('result').innerText="完了";
						selectFileButton.value="";//終了したらファイル選択をクリアする
						alert("アップロードが完了しました。");
					}
					
					
					if(this.response!="ok"){
						//エラーを出力
						
						error.value=error.value+foldername[i].value+"は"+this.response+"\n";
				
						
					}
			    };
			}

			ajax[0].run();

		}else{
			document.getElementById('result').innerText="0/0";
			
		}

	})
}) 






