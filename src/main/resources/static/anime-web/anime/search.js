
document.addEventListener("DOMContentLoaded",function(){
	let search= document.getElementById("search");
	searchResult.style.display='none';
	
	let searchCancel =document.getElementById("searchCancel");
	
	search.addEventListener('input',function(){
		execute(search);


	})
	search.addEventListener('focus',function(){
		execute(search);

	})
	
	searchCancel.addEventListener('click',function(){
		
		let searchResult=document.getElementById('searchResult');
		searchResult.style.display='none';
		this.style.display='none';
	})
	
	function execute(elem){
		if(elem.value.length>0){
					searchCancel.style.display="block";
		}else{
			searchCancel.style.display="none";
		}

		getDistanceData(elem.value).then(result => {
			setResult(result);
		})
	}
	
	function setResult(result){
		let searchResult=document.getElementById('searchResult');
		delSearchElem(searchResult);
		if(result){
			let len=result.length>10?10:result.length;
			for(let i=0;i<len;i++){
				if(result[i]['distance']<=0){
					return 
				}
				
				createSearchElem(searchResult,result[i]['original'],result[i]['id']);
				searchResult.style.display='block';
			}
			console.dir(result);
		}

	}

	
	async function getDistanceData(param) {
		
		
		if(!param || isOnlyBlank(param)){	
			return;
		}
		console.dir(param);
		let url=location.protocol+"api/db/animeLen/";
		let ajax_search = new class_ajax(url);
		ajax_search.args('txt', param);
		
		return new Promise((resolve, reject) => {
	        ajax_search.xhr.onload = function() {
	            if (this.status >= 200 && this.status < 300) {
	                resolve(JSON.parse(this.response));
	            } else {
	                // エラーハンドリング
	                reject(`Error: ${this.statusText}`);
	            }
	        };
	        
	        ajax_search.xhr.onerror = function() {
	            reject("Ajax request failed");
	        };

	        // AJAXリクエストを実行
	        ajax_search.run();
	    });
		
		
		
		//let data = await getData(url);
		//return data
	}
});

function isOnlyBlank(txt) {
    txt = txt.replace(/[\s　]+/g, '');
    return txt.length === 0;
}

function delSearchElem(searchResult){

	while (searchResult.firstChild) {
	    searchResult.removeChild(searchResult.firstChild);
	}
	searchResult.style.display='none';
}

function createSearchElem(searchResult,text,id){
	let a= document.createElement('a');
	a.setAttribute('href','/anime-web/anime/video/'+id);
	
	
	let div= document.createElement('div');
	div.setAttribute('class','searchChild');
	a.appendChild(div);
	
	let p= document.createElement('p');
	p.innerText=text;
	div.appendChild(p);
	searchResult.appendChild(a);
}

