package com.example.web.etc.sta.que.upFileDel;

import java.nio.file.Path;

import com.example.web.etc.sta.que.ArgsData;
import com.example.web.etc.sta.que.Que;

public class DelArgs extends ArgsData {

    public DelArgs(Path p, Que queInstance) {
        super();
        setArgs(p);
        setQueInstance(queInstance); 
    }

    private void setArgs(Path p) {
        setArgument("delPath",p.toString());

    }
}
