
document.addEventListener("DOMContentLoaded",function(){
	

	
	getPrefix().then(result => {
		let kana= result.slice(0,46);
		let en=result.slice(46);
		
		let txtArr=[kana,en];
		
		let tab=document.getElementsByClassName('tab');
		setButtons(kana);
		
		
		tab[0].style.borderBottom ="1px solid"; 
		for(let i=0;i<tab.length;i++){
			tab[i].addEventListener('click',function(){
				for(let ii=0;ii<tab.length;ii++){
					
					tab[ii].style.borderBottom ="0px solid"; 
				}
				tab[i].style.borderBottom ="1px solid"; 
				
				let param=i<2?txtArr[i]:"";

				setButtons(param);
				if(i==2){
					let url=new URL(window.location.href);
					document.location= getPrefixUrl(url,'prefix',0);
				}else if(i==3){
					let url=new URL(window.location.href);
					document.location= getPrefixUrl(url,'prefix',-1);
				}
			})
			
		}
		
	})
	

	

});

function　setButtons(txt){
	delButtons();
	
	if(!txt){
		return;
	}
	let prefixButtons=document.getElementById('prefixButtons');
	for(let i=0;i<txt.length;i++){
		
		
		let b =document.createElement('button');
		b.innerText=txt[i]['txt'];
		b.setAttribute('class','prefixButton');
		
		let a=document.createElement('a');
		let url=new URL(window.location.href);

		a.setAttribute('href',getPrefixUrl(url,'prefix',txt[i]['id']));
		a.appendChild(b);
		//やゆよで改行
		if(i==38){
			let br =document.createElement('br');
			prefixButtons.appendChild(br);
		}
		
		
	
		
		prefixButtons.appendChild(a);
	}
}
	
function getPrefixUrl(url,param,value){

	//let url=new URL(window.location.href);

	return addUrl(url,param,value);
	
	//document.location= url+"?year="+result[0]+"&season="+result[1];
}
function addUrl(url,param,value){
	let txt=url.searchParams.get(param);
	if(txt){
		url.searchParams.set(param,value);
		return url.toString();
	}
	if(url.searchParams.size==0){
		url+="?";
	}else{
		url+="&"
	}
	return url+param+"="+value;
}
function delButtons(){
	let prefixButtons = document.getElementById('prefixButtons');
	while (prefixButtons.firstChild) {
	    prefixButtons.removeChild(prefixButtons.firstChild);
	}

}

async function getPrefix() {
	let url=location.protocol+"api/db/selectPrefix/";
	let data = await getData(url);
	return data
}