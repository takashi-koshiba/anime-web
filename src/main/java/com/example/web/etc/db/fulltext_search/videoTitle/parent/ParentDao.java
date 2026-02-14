package com.example.web.etc.db.fulltext_search.videoTitle.parent;

import java.util.List;

public interface ParentDao {
	public List<Parent> selectById(int id);
	public long insert(int id );
	public void delete(int animeid);
}
