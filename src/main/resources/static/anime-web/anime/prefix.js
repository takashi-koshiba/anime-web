
document.addEventListener("DOMContentLoaded",function(){
	getPrefix().then(result => {
		let kana= result.slice(0,46);
		let en=result.slice(46);
		let txtArr=[kana,en];
		let tab=document.getElementsByClassName('tab');
		
		selectInitTab(txtArr,tab);
		
		AddEventSwitchTab(tab,txtArr);
		
		
	})
	

	

});

function AddEventSwitchTab(tab,txtArr){
	let url=new URL(window.location.href).toString();
	for(let i=0;i<tab.length;i++){
		tab[i].addEventListener('click',function(){
			for(let ii=0;ii<tab.length;ii++){
				tab[ii].style.borderBottom ="0px solid";
			}
			
			tab[i].style.borderBottom ="1px solid"; 
			
			let param=i<2?txtArr[i]:"";

		
			if(i==2){//その他を押下したとき
				document.location= addUrl('prefix',0,url);
			}else if(i==3){//全部を押下したとき
				document.location= addUrl('prefix',-1,url);
			}else{
				setButtons(param);
			}

		})
		
	}
}

function selectInitTab(txtArr,tab){
	let url=new URL(window.location.href)
	let selectedButtonIndex=getParamFromUrl(url,'prefix');
	let selectedIndex=selectedButtonIndex<47?0:1;
	
	tab[selectedIndex].style.borderBottom ="1px solid"; 
	setButtons(txtArr[selectedIndex]);
	
}

function getParamFromUrl(url,param){
	let value=url.searchParams;
	return value.get(param);
}

function setButtons(prefixArr){
	delButtons();
	
	if(!prefixArr){
		return;
	}
	let prefixButtons=document.getElementById('prefixButtons');
	

	let prefixFirstIndex=prefixArr[0]['id']-1;
	let index=0;
	let url=new URL(window.location.href).toString();
	for(let i=prefixFirstIndex;i<prefixArr[prefixArr.length-1]['id'];i++){
		
		
		let b =document.createElement('button');
		b.innerText=prefixArr[index]['txt'];
		b.setAttribute('class','prefixButton');
		
		let a=document.createElement('a');
		

		a.setAttribute('href',addUrl('prefix',prefixArr[index]['id'],url));
		a.appendChild(b);
		//やゆよで改行
		if(i==38){
			let br =document.createElement('br');
			prefixButtons.appendChild(br);
		}
		
		prefixButtons.appendChild(a);
		

		//選択したボタンの色を変更
		if(i+1==getParamFromUrl(new URL(window.location.href),'prefix')){		
			changeSelectedColor(b,230, 230, 230);
		
		} 
		
		index++;
	}
}
function changeSelectedColor(elem,r,g,b){
	elem.style.backgroundColor=`rgb(${r},${g},${b})`;
}

function addUrl(param,value,url){
	let urlObj=new URL(url);
	let txt=urlObj.searchParams.get(param);
	
	if(txt){
		urlObj.searchParams.set(param,value);
		return urlObj.toString();
	}
	if(urlObj.searchParams.size==0){
		urlObj+="?";
	}else{
		urlObj+="&"
	}
	return urlObj+param+"="+value;
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