package com.example.web.etc.db.Animetable;

import java.util.List;

public interface AnimeDao {
	public int insert(Anime anime);
	public int countRow(String text);
	public List<Anime> selectAll();
	public List<Anime> selectOne(Integer id);
	public List<AnimeSort> selectAllSortByText(Integer charId);
	public List<prefix> selectPrefixAll();
	
}
