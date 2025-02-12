class class_ajax{
            xhr = new XMLHttpRequest();
			xhr_up = this.xhr.upload;
            post_data = new FormData();
            constructor(path) {
                this.path = path;//送信先のphpのパス
            }
            //送信する値
            //i はキー
            //str　はその値
            args(i,str){
                this.post_data.append(i, str);
            }
		

            run(){
                this.xhr.open('POST', this.path, true);
                
                //this.post_data.append('mode', 1);
				//console.dir(this.post_data);
                this.xhr.send(this.post_data);

                //var a =document.getElementById('a');
                /*
                //xhrがすべて読み込んだら
                this.xhr.addEventListener('load', function () {
                    //console.log(this.response);
                    a.innerText=this.response;
                    

                });
                */
				
            }
			
        }
        