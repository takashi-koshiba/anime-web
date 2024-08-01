

document.addEventListener("DOMContentLoaded",function(){
	let imgUpload=document.getElementById('imgUpload');
	let uploadButton=document.getElementById('uploadButton');
	let result=document.getElementById("result");
	
	
	let foldername =document.getElementsByClassName('foldername'); 
	let originalName = document.getElementsByClassName('originalName'); 

	
	
		
	imgUpload.addEventListener("click",function(){
		let files=uploadButton.files;
		let fileCount=files.length;
		let message="";
		result.innerText=message;
		
	
		if(foldername.length>0 && files.length){
			message="アップロード中";
			result.innerText=fileCount;
			let ajax = [];

			for (let i = 0; i < fileCount; i++) {
			    ajax[i] = new class_ajax('/api/upload');
				
			    ajax[i].args('upload', files[i]);
				ajax[i].args('folder', "C:\\Users\\muu4\\Documents\\新しいフォルダー\\");
				ajax[i].args('foldername',foldername[i].value)
				ajax[i].args('originalName',originalName[i].innerText);
				
				console.dir(originalName[i]);
			    ajax[i].xhr.onload = function() {
					if(this.response!="ok"){
						//エラーを出力
						message=this.response;
	
						
					}else if (i + 1 < fileCount) {//次のファイルを送信
						message=i+1+"/"+fileCount;
			            ajax[i + 1].run();
			        }else if(fileCount==i+1){
						message=i+1+"/"+fileCount;
						uploadButton.value="";//終了したらファイル選択をクリアする
					}
					else{

					}
					
					result.innerText=message;
			    };
			}

			ajax[0].run();

		}else{
			result.innerText="0/0";
			uploadButton.value="";//終了したらファイル選択をクリアする
		}

	})
}) 






