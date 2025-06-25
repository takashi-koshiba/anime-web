package com.example.web.etc.sta.que.LoundNorm;

import com.example.web.etc.sta.que.ArgsData;
import com.example.web.etc.sta.que.Que;

public class Lound_Args extends ArgsData {

    public Lound_Args(String input, String Output,Que queInstance) {
        super();
        setArgs(input,Output);
        setQueInstance(queInstance); 
    }

    private void setArgs(String input,String Output) {

        setArgument("input", input);
        setArgument("output", Output);
       
    }
}
