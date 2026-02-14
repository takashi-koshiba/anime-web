package com.example.web.etc.db.fulltext_search.prog.parent;

import java.util.List;

public interface progParentDao {
	public List<progParent> selectById(int id);
	public long insert(long id );
	public void delete(int animeid);

	public List<video_prog> selectUnhashedParentId(boolean isLimit);
}
