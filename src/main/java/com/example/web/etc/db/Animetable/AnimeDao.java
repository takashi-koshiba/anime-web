package com.example.web.etc.db.Animetable;

import java.util.List;

public interface AnimeDao {
	public int insert(Anime anime);
	public void insertAlias(Integer id,String alias);
	public void delAlias(Integer id);
	public int countRow(String text);
	public List<Anime> selectAll();
	public List<Anime> selectGt(Integer length);
	public List<Anime> selectOne(Integer id);
	public List<Anime> selectAliasOne(Integer id);
	public List<AnimeSort> selectAllSortByText(Integer charId);
	public List<prefix> selectPrefixAll();
	
}
