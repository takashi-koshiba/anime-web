package com.example.web.etc.db.rankedNewAnime;

import java.util.List;

public interface RankedNewAnimeDao {
	//public int insert(Anime anime);
	//public int countRow(String text);
	public List<RankedNewAnime> selectAll(Integer year,Integer season);
	public List<RankedNewAnime> selectAll(Integer year);
	public List<RankedNewAnime> selectAll();
}
