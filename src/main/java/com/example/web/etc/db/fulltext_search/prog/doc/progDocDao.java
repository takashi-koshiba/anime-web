package com.example.web.etc.db.fulltext_search.prog.doc;

import java.util.List;

public interface progDocDao {
	public List<progDoc> selectById(long parentId);
	public long insert(long  parentId );
	public void del(long parentId);
}
