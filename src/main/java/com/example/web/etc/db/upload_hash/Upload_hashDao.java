package com.example.web.etc.db.upload_hash;

public interface Upload_hashDao {
	public int  count(Integer  id,String hash);
	public Boolean insertHash(String name,String pw);

}
