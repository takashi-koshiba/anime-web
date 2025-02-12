document.addEventListener("DOMContentLoaded",function(){
	let excludeform = document.getElementById('list');
	let formlen=excludeform.children.length;
	
	exec=function(){
		
		addToList(excludeform);
	}

	
	excludeform.children[formlen-1].addEventListener('click',exec);
	

	

	
	function addToList(value){
		console.dir(value.children.length);
		value.children[value.children.length-1].removeEventListener('click',exec);
			
		let input= document.createElement('input');
		input.setAttribute('class','inputText');
		input.setAttribute('name','title');
		
		let br=document.createElement('br');
		value.appendChild(br);
		value.appendChild(input);
		
		
		input.addEventListener('click',exec);
		

	}
	
})
