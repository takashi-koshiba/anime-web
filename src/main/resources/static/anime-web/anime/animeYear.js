
document.addEventListener("DOMContentLoaded",function(){
	let year=document.getElementById('animeyear');
	
	let url=location.protocol+"api/db/getYear/";
	getData(url).then((result) =>{
		for(let i=0;i<result.length;i++){
			let option = document.createElement("option");
			option.text = result[i]['year']+"/"+toSeason(result[i]['season']-1);
			option.value = (result[i]['year']).toString()+(result[i]['season']).toString();
			if(i==result.length-1){
				option.setAttribute("selected", "selected");
			}
			
			year.appendChild(option);
		}
		
		
	})
	

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


