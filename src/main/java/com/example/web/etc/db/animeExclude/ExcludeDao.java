package com.example.web.etc.db.animeExclude;

import java.util.List;

public interface ExcludeDao {
	public List<Exclude> selectAll();
	public void insert(String title);
	public void del();
}
