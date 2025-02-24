package com.example.web.etc.sta.que.resize;

import com.example.web.etc.sta.que.ArgsData;
import com.example.web.etc.sta.que.Que;

public class Resize_Args extends ArgsData {

    public Resize_Args(String inputPath,String outputPath, Integer imgSize, String mimeType, Que queInstance) {
        super();
        setArgs(inputPath,outputPath,imgSize,mimeType);
        setQueInstance(queInstance); 
    }

    private void setArgs(String inputPath,String outputPath, Integer imgSize, String mimeType) {
        setArgument("input", inputPath);
        setArgument("output", outputPath);
        setArgument("size", imgSize);
        setArgument("mime", mimeType);
       
    }
}
