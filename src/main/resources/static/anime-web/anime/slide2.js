
document.addEventListener("DOMContentLoaded",function(){
	let rank=document.getElementsByClassName('rank');
	
	//ランキングの数だけ繰り返す
	for(let i=0;i<rank.length;i++){
		
		let rank_scroll = rank[i].children[1];
		let anime_child=rank_scroll.children[0].children[0];
		let anime_childW=anime_child.clientWidth;
		
		
		let button_left = rank[i].children[0];
		let button_right = rank[i].children[2];
		

		let margin=regulation(anime_childW,0,rank_scroll);
		
		resizeView(i,rank,rank_scroll,anime_child,anime_childW,margin);
		
		
		button_left.addEventListener('click',function(){
			scrollTo(rank,rank_scroll,anime_child,anime_childW,margin,-1);
		});
		button_right.addEventListener('click',function(){
			scrollTo(rank,rank_scroll,anime_child,anime_childW,margin,1);
		});
		
		//scrollTo(left_main_childId,rank,anime_child,anime_childW,margin);
	}

});

function scrollTo(left_main_childId,rank,anime_child,anime_childW,margin,direction){
	let c=Math.floor(rank.offsetWidth/anime_childW);
	let moveValue=((anime_childW+margin)*c*direction)+rank.scrollLeft ;
	
	for(let i=0;i<rank.children[0].children.length;i++){
		//rank.children[0].children[i].style.transform = `translateX(${moveValue*-1}px)`;
		rank.scrollTo({
			left:moveValue,
			behavior:'smooth'
		});
	}
}

function resizeView(index,left_main_childId,rank,anime_child,anime_childW,margin){

	
	margin=regulation(anime_childW,margin,rank);
	
	//要素余白を調整
	window.addEventListener('resize',function(){
		margin=regulation(anime_childW,margin,rank);
	})
		
	rank.addEventListener('scrollend',function(){
		//scroll_count=Math.floor(rank.scrollLeft/(anime_childW+margin));

		
		let scrolledElem=((Math.round(rank.scrollLeft/(anime_childW+margin))));
		let moveValue=((anime_childW+margin)*scrolledElem)-rank.scrollLeft ;
		
		for(let i=0;i<rank.children[0].children.length;i++){
			rank.children[0].children[i].style.transform = `translateX(${moveValue*-1}px)`;
		}
		margin=regulation(anime_childW,margin,rank);
	})
}

function regulation(anime_childW,margin,rank){
	let rankWidth=rank.offsetWidth;
	let c=Math.floor(rankWidth/anime_childW);
	margin=(rankWidth/c)-anime_childW;
	for(let i=0;i<rank.children[0].children.length;i++){
		rank.children[0].children[i].children[0].style.marginLeft=margin/2+"px";
		rank.children[0].children[i].children[0].style.marginRight=margin/2+"px";
	}	
	return margin;
	
}