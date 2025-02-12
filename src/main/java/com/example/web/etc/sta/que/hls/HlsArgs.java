package com.example.web.etc.sta.que.hls;

import java.nio.file.Path;

import com.example.web.etc.sta.que.ArgsData;
import com.example.web.etc.sta.que.Que;

public class HlsArgs extends ArgsData {

    public HlsArgs(String input, Path output, String url, Que queInstance) {
        super();
        setArgs(input, output, url);
        setQueInstance(queInstance); 
    }

    private void setArgs(String input, Path output, String url) {
        setArgument("input", input);
        setArgument("output", output.toString());
        setArgument("url", url);
    }
}
