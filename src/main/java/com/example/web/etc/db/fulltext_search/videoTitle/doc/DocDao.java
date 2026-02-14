package com.example.web.etc.db.fulltext_search.videoTitle.doc;

import java.util.List;

public interface DocDao {
	public List<Doc> selectById(long parentId);
	public long insert(long  parentId );
	public void del(long parentId);
}
