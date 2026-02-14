package com.example.web.etc.db.fulltext_search.prog.hash;


import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class progHashJDBC implements progHashDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public void  insert(long doc_id,long hash,int pos ) {
    	ArrayList<Object> param  = new ArrayList<>();
    	param.add(doc_id);
    	param.add(hash);
    	param.add(pos);
        jdbc.update("INSERT INTO proghash(doc_id,hash_int,gram_pos) VALUES(?,?,?)",param.toArray());

       
    }

}
