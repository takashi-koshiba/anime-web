class class_upload{


	//引数
	//files[i].name
	//パターン
	
	//戻り値
	//[0] base64
	//[1] //MIMEタイプ
	//[2]//拡張子
	 GetFile(file) {
	    return new Promise((resolve, reject) => {
	        // バイナリを取得
	        let reader = new FileReader();
	        reader.readAsDataURL(file);

	        // バイナリを代入
	        reader.onload = function(e) {
	            let MIMEIndex = this.result.indexOf(",");
	            
	           
	            let base64Data = this.result.substring(MIMEIndex + 1);
	            let result;

				//console.dir(this.result);
				//console.dir(base64Data);
	            
	            result = [base64Data,this.result];
	            

				
	            resolve(result);
	        };

	        reader.onerror = function(error) {
	            reject(error);
	        };
	    });
	}
}
        