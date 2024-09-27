package com.example.web.etc.db.rankedAnime;

import java.util.List;

public interface RankdAnimeDao {
	//public int insert(Anime anime);
	//public int countRow(String text);
	public List<RankedAnime> selectAll();
	public RankedAnime selectOne(Integer animeId);
	public Integer countRow();
}
