package com.example.web.etc.db.upload;

import java.util.List;

public interface UploadDao {
	public Upload selectOne(String name);
	public List<Upload> selectUserAll();
	public Upload login(Integer id ,String pw);
	public Boolean insertUser(String name,String pw);

}
