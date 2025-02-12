package com.example.web.etc.db.Animetable;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class AnimeService {
    private final AnimeJDBC animeJDBC;

    // コンストラクタインジェクション
 
    public AnimeService(AnimeJDBC animeJDBC) {
        this.animeJDBC = animeJDBC;
    }
    public void insertAlias(Integer id,String alias) {
    	animeJDBC.insertAlias(id,alias);
    	
    	
    }public void insertAlias(Integer id) {
    	animeJDBC.delAlias(id);

    }

    public boolean IsExistItem(String item) {
        int count = animeJDBC.countRow(item);
        return count==0? false:true;
    }
    
    public boolean insert(Anime anime) {
    	int row=animeJDBC.insert(anime);
    	
    	return row>0?true:false;
    }
    public List<Anime> selectGt(Integer length){
    	return animeJDBC.selectGt(length);
    }
    public List<Anime> selectAll(){
    	return animeJDBC.selectAll();
    }
    public List<Anime> selectOne(Integer id){
    	return animeJDBC.selectOne(id);
    }
    public List<prefix> selectPrefixAll(){
    	return animeJDBC.selectPrefixAll();
    }
    public List<AnimeSort>selectAllSortByText(Integer charId){
		return animeJDBC.selectAllSortByText(charId);
    	
    }
    public List<Anime> selectAliasOne(Integer id){
    	return animeJDBC.selectAliasOne(id);
    }


}
