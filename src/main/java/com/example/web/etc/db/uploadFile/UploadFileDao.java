package com.example.web.etc.db.uploadFile;

import java.util.List;

public interface UploadFileDao {
	//public Upload selectOne(String name);
	//public List<Upload> selectAll();
	//public Upload login(Integer id ,String pw);
	public String insertFile(String fname,String lname,String mime,Integer id,Integer mimeId);
	public List<FileInfo> selectFile(String userId, Integer columnId, String order, Integer ftypeId);
	
    public List<FileInfo> selectFileOne(String userId,String fId);
    
    public Integer countRow(String userId, Integer ftypeId);
    
    public void delete(String userId,String fid);
}
