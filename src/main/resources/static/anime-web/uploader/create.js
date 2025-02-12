document.addEventListener("DOMContentLoaded",function(){
	let submit = document.getElementById('submit');
	submit.addEventListener('click',function(){
	
		let name = document.getElementById('name');
		let pw = document.getElementById('pw');
		
		let ajax2 = new class_ajax('/anime-web/uploader/login/addUser');
		ajax2.args('name',name.value);
		ajax2.args('pw',pw.value);
		
		ajax2.xhr.onload = function() {
			let result=this.response;
			console.dir(this.response);
			if(result==1){
				alert("入力内容に不備があります。");
			}else if(result==2){
				alert("書き込みに失敗しました。");
			}else if(result==3){
				alert("すでにそのユーザー名は使用されています。");
			}else if(result==0){
				alert("アカウントが作成できました。");
			}
		}
		ajax2.run();
	})
})


