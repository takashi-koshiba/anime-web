document.addEventListener("DOMContentLoaded",function(){
	let submit = document.getElementById('submit');
	submit.addEventListener('click',function(){
	
		let select = document.getElementById('user_select');
		let selected_id=select.value;

		let pw = document.getElementById('pw');
		
		let ajax2 = new class_ajax('/anime-web/uploader/login');
		ajax2.args('id',selected_id);
		ajax2.args('pw',pw.value);
		
		ajax2.xhr.onload = function() {
			let result=this.response;
			console.dir(this.response);
			if(result==1){
				alert("入力内容に不備があります。");
			}else if(result==2){
				alert("ログインに失敗しました。");
			}else if(result==0){
				alert("ログインに成功しました。");
				document.location = '/anime-web/uploader/view';
			}
		}
		ajax2.run();
		
	})
})


