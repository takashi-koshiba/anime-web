package com.example.web.etc.db.fulltext_search.videoTitle.selectDB;

import java.util.List;

public interface AnimeHashDao {

	public List<AnimeHash> selectByHash(String str);
}
