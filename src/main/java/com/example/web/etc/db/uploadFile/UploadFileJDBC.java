package com.example.web.etc.db.uploadFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.web.etc.db.renban.renbanService;
import com.example.web.etc.sta.Kakasi;
import com.example.web.etc.sta.TextRep;
import com.example.web.uploader.sendFile.fileType;

@Repository

public class UploadFileJDBC implements UploadFileDao {

    @Autowired
    private JdbcTemplate jdbc;

	@Autowired
	renbanService renban;
	
	
	private Integer itemLimit;
	public  UploadFileJDBC() {
		itemLimit=100;
	}
	public Integer getItemLimit() {
		return this.itemLimit;
	}
	
    @Override
    @Transactional
    public String insertFile(String fname,String lname,String  mime,Integer id,Integer mimeId) {
    	try {
    		/*
    		for (int i =0;i<999999;i++) {
    			jdbc.update("INSERT INTO renban (alias) VALUES(?)","a"+i);
    		}
        	*/
    		String searchTxt=Kakasi.main(TextRep.main(fname+lname),"-KH");
    		String alias=renban.selectOne();
    		jdbc.update("INSERT INTO uploadfile (user_id,fname,lname,alias,extension,searchTxt,mimeId) VALUES(?,?,?,?,?,?,?)",id,fname,lname,alias,mime,searchTxt,mimeId);
    		
        	return alias;
    	}catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}

    }
    public void delete(String userId,String fid) {
    	String sql="delete from uploadfile where user_id=? and alias = ? ";
		jdbc.update(sql,userId,fid);
		
    }
    
    public List<FileInfo> selectFileOne(String userId,String fId){
    	try {

    		String sql = "select uploadfile.id,user_id,fname,lname,alias,extension,hiduke,extension.type  from uploadfile left join extension on uploadfile.mimeid=extension.id left join type on extension.type=type.id "
    				+ " WHERE user_id = ? "
    				+ " AND alias = ?";
    		
    		List<Map<String, Object>> result = jdbc.queryForList(sql, userId, fId);

    		
			List<FileInfo> list = new ArrayList<>();
			for(Map<String,Object>map:result) {
				FileInfo fileInfo = new FileInfo();
        	
				
				 fileInfo.setId((Integer)map.get("id"));
				 fileInfo.setUser_id((Integer)map.get("user_id"));
				 fileInfo.setFname((String)map.get("fname"));
				 fileInfo.setLname((String)map.get("lname"));
				
				 fileInfo.setAlias((String)map.get("alias"));
				 fileInfo.setExtension((String)map.get("extension"));
				// fileInfo.setSearchTxt((String)(map.get("searchTxt")));
				 fileInfo.setHiduke(null);
				 
				 fileType type=map.get("type")==null?fileType.OTHER:fileType.values()[(int) map.get("type")];
				 fileInfo.setType(type);

				list.add( fileInfo);
			}

			return list;
    	}catch (Exception e) {
    		e.printStackTrace();
    		List<FileInfo> list = new ArrayList<>();
			return list;
    	}
    }
    private List<Map<String, Object>>  getSqlTxt(String userId,Integer columnId,String order,Integer ftypeId) {
    	
    	
    	
    	Integer  typeId=ftypeId==fileType.OTHER.ordinal()?-2:ftypeId;
    	
    	//ファイルタイプ全部
    	if(typeId==-1) {
    		String sql="select uploadfile.id,user_id,fname,lname,alias,ex,hiduke,type,searchtxt from uploadfile "
    				+ "left join extension on uploadfile.mimeId=extension.id "
    				+ "WHERE user_id = ? "
    				+ "ORDER BY "+ columnId+" "+order ;

    		
    		List<Map<String, Object>> result = jdbc.queryForList(sql, userId);
    		return result ;
    	}
    	//その他のファイル
    	else if(typeId==-2) {
    		String sql = " select uploadfile.id,user_id,fname,lname,alias,ex,hiduke,type,searchtxt from uploadfile "
    				+ "				left join extension on uploadfile.mimeId=extension.id "
    				+ "				WHERE user_id = ?"
    				+ "		         AND ex is null "
    		           + "ORDER BY "+ columnId+" "+order;
    		
    		List<Map<String, Object>> result = jdbc.queryForList(sql, userId);
    		return result ;
    	}
    	//タイプを選択したとき
    		String sql = "select uploadfile.id,user_id,fname,lname,alias,ex,hiduke,type,searchtxt from uploadfile "
    				+ "join extension on uploadfile.mimeId=extension.id "
    				+ "WHERE user_id = ? "
    		          
    		           + "AND extension.type= ? "
    		           + "ORDER BY "+ columnId+" "+order;
    	
		
		List<Map<String, Object>> result = jdbc.queryForList(sql, userId, typeId);
		return result ;
    }
    @Override
    public Integer countRow(String userId, Integer ftypeId) {
    	

    	Integer  typeId=ftypeId==fileType.OTHER.ordinal()?-2:ftypeId;
    	String sql="";
    	//ファイルタイプ全部
    	if(typeId==-1) {
    		 sql = "select count(*) as rw from uploadfile "
    				 + "join extension on uploadfile.mimeId=extension.id "
				+ "WHERE user_id = ? ";
				
    	}
    	//その他のファイル
    	else if(typeId==-2) {
   		 	sql = "select count(*) as rw from uploadfile "
   		 		+ "join extension on uploadfile.mimeId=extension.id "
				+ "WHERE user_id = ? "
				+ "AND ex is null ";
    	}   	
    	//タイプを選択したとき
    	else {
    		sql = "select count(*) as rw from uploadfile "
        			+ "join extension on uploadfile.mimeId=extension.id "
    		+ "WHERE user_id = ? "
    		+ "AND extension.type= ? ";
    	}
    	
    	    
	
    	List<Map<String, Object>> result = jdbc.queryForList(sql, userId);
    	return Integer.valueOf(result.get(0).get("rw").toString()) ;
	
    }
    
    @Override
    public List<FileInfo> selectFile(String userId,Integer columnId,String order,Integer ftypeId){

    	    	try {
    	    		List<Map<String, Object>> result=getSqlTxt(userId,columnId,order,ftypeId);
    				List<FileInfo> list = new ArrayList<>();
    				for(Map<String,Object>map:result) {
    					FileInfo fileInfo = new FileInfo();
    	        	
    					
    					 fileInfo.setId((Integer)map.get("id"));
    					 fileInfo.setUser_id((Integer)map.get("user_id"));
    					 fileInfo.setFname((String)map.get("fname"));
    					 fileInfo.setLname((String)map.get("lname"));
    					
    					 fileInfo.setAlias((String)map.get("alias"));
    					 fileInfo.setExtension((String)map.get("extension"));
    					 fileInfo.setSearchTxt((String)(map.get("searchTxt")));
    					 
    					 fileType type=map.get("type")==null?fileType.OTHER:fileType.values()[(int) map.get("type")];
    					 fileInfo.setType(type);
    					 //fileInfo.setHiduke(null);
    					

    					list.add( fileInfo);
    				}

    				return list;
    	    	}catch (Exception e) {
    	    		e.printStackTrace();
    	    		List<FileInfo> list = new ArrayList<>();
    				return list;
    	    	}
    	    
    }
    
}
