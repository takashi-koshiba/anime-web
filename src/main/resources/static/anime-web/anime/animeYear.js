
document.addEventListener("DOMContentLoaded",function(){
	let year=document.getElementById('animeyear');
	
	let url=location.protocol+"api/db/getYear/";
	getData(url).then((result) =>{
		let IsSelected=false;
		for(let i=0;i<result.length;i++){
			let option = document.createElement("option");
			option.text = result[i]['year']+"/"+toSeason(result[i]['season']-1);
			option.value = (result[i]['year']).toString()+(result[i]['season']).toString();
			
			
			option.selected = false;

			if(getParam()[0]==result[i]['year']&&getParam()[1]==result[i]['season']){
				option.selected = true;
				IsSelected=true;
			}else if(i==result.length-1 &&!IsSelected){
				option.selected = true;
				IsSelected=true;
			}
			
			year.appendChild(option);
		}

		
		
		
		
	})
	
	year.addEventListener('change',function(){
		let url=new URL(window.location.href);
		let result=splitValue(this.value);
		url=new URL(getPrefixUrl(url,'year',result[0]));
		
		url=getPrefixUrl(url,'season',result[1]);
		document.location= url;
	});
	function getParam(){
		let url=new URL(window.location.href);
		let year=url.searchParams.get('year');
		let season=url.searchParams.get('season');
		
		return [year,season];
	}
	
	function splitValue(value){
		let year=value.substr(0,4);
		let season=value.substr(4,1);
		return [year,season];
	}
	
	
	function toSeason(index){
		let t=['冬','春','夏','秋'];
		let result=t[index];
		return result;
	}	
	
	async function  api(url) {
		const response = await fetch(url);
		const json = await response.json(); 

		return json;
	}
	
	async function getData(url){
		let data=await api(url);
		return data;
		
	}

});


