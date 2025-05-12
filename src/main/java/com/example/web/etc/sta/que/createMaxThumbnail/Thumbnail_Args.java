package com.example.web.etc.sta.que.createMaxThumbnail;

import com.example.web.etc.sta.que.ArgsData;
import com.example.web.etc.sta.que.Que;

public class Thumbnail_Args extends ArgsData {

    public Thumbnail_Args(String alias,String fname, Que queInstance) {
        super();
        setArgs(alias,fname);
        setQueInstance(queInstance); 
    }

    private void setArgs(String alias,String fname) {
        setArgument("alias", alias);
        setArgument("fname", fname);
       
    }
}
