package com.example.web.etc.sta.que.cmd;

import com.example.web.etc.sta.que.ArgsData;
import com.example.web.etc.sta.que.Que;

public class Cmd_Args extends ArgsData {

    public Cmd_Args(String cmd, Que queInstance) {
        super();
        setArgs(cmd);
        setQueInstance(queInstance); 
    }

    private void setArgs(String cmd) {
        setArgument("cmd", cmd);
       
    }
}
