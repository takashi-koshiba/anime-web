package com.example.web.etc.db.extendVideoRank;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class ExtendVideoService {
    private final ExtendVideoJDBC extendVideoJDBC;

    // コンストラクタインジェクション
 
    public ExtendVideoService(ExtendVideoJDBC extendVideoJDBC) {
        this.extendVideoJDBC = extendVideoJDBC;
    }
/*
    public boolean IsExistItem(String item) {
        int count = animeJDBC.countRow(item);
        return count==0? false:true;
    }
    
    public boolean insert(Anime anime) {
    	int row=animeJDBC.insert(anime);
    	
    	return row>0?true:false;
    }
  */  
    public List<ExtendVideo> selectAll(String year,String season){

    	return extendVideoJDBC.selectAll(Integer.parseInt(year),Integer.parseInt(season));
    }
    
    public List<ExtendVideo> selectAll(String year){
    	
    	return extendVideoJDBC.selectAll(Integer.parseInt(year));
    	
    }
    public List<ExtendVideo> selectAll(){

    	return extendVideoJDBC.selectAll();
    	
    }

}
