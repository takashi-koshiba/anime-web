package com.example.web.etc.db.RankedAnimeSeason;

import java.util.List;

public interface RankedAnimeSeasonDao {
	//public int insert(Anime anime);
	//public int countRow(String text);
	public List<RankedAnimeSeason> selectAll(Integer year,Integer season);
	public List<RankedAnimeSeason> selectAll(Integer year);
	public List<RankedAnimeSeason> selectAll();
}
