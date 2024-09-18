
document.addEventListener("DOMContentLoaded",function(){
	let search= document.getElementById("search");
	searchResult.style.display='none';
	search.addEventListener('input',function(){

		getDistanceData(this.value).then(result => {
			setResult(result);
		})

	})
	
	
	

	
	function setResult(result){
		let searchResult=document.getElementById('searchResult');
		delSearchElem(searchResult);
		if(result){
			let len=result.length>10?10:result.length;
			for(let i=0;i<len;i++){
				if(result[i]['distance']<=0){
					return 
				}
				createSearchElem(searchResult,result[i]['original']);
				searchResult.style.display='block';
			}
		}

	}

	
	async function getDistanceData(paramIndex) {
		
		
		if(!paramIndex){	
			return;
		}
		let url=location.protocol+"api/db/animeLen/"+paramIndex;
		let data = await getData(url);
		return data
	}
});

function delSearchElem(searchResult){

	while (searchResult.firstChild) {
	    searchResult.removeChild(searchResult.firstChild);
	}
	searchResult.style.display='none';
}

function createSearchElem(searchResult,text){
	let div= document.createElement('div');
	div.setAttribute('class','searchChild');

	let p= document.createElement('p');
	p.innerText=text;
	div.appendChild(p);
	searchResult.appendChild(div);
}

