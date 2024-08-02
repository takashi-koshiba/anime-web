class class_upload{


	//引数
	//files[i].name
	//パターン
	
	//戻り値
	//[0] base64
	//[1] //MIMEタイプ
	//[2]//拡張子
	 GetFile(file,pattern) {
	    return new Promise((resolve, reject) => {
	        // バイナリを取得
	        let reader = new FileReader();
	        reader.readAsDataURL(file);

	        // バイナリを代入
	        reader.onload = function(e) {
	            let MIMEIndex = this.result.indexOf(",");
	            let MIME = this.result.substring(0, MIMEIndex + 1);
	            let headerMatch = MIME.match(pattern);
	            let base64Data = this.result.substring(MIMEIndex + 1);
	            let result;

				//console.dir(this.result);
				//console.dir(base64Data);
	            if (headerMatch == null) {
				    let  text=MIME.substring(5);
					text=text.substring(0,text.length-8);

	                result =[null, text,null];
	            }else {

					let extensionIndex = headerMatch[1].indexOf("/");
					let extension=headerMatch[1].substring(extensionIndex+1);
	                result = [base64Data, headerMatch[1],extension];
	            }

				
	            resolve(result);
	        };

	        reader.onerror = function(error) {
	            reject(error);
	        };
	    });
	}
}
        