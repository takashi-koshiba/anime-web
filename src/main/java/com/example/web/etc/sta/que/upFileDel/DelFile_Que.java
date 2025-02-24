package com.example.web.etc.sta.que.upFileDel;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.example.web.etc.sta.DeleteR;
import com.example.web.etc.sta.que.ArgsData;
import com.example.web.etc.sta.que.Que;

public class DelFile_Que extends Que {

	
	public DelFile_Que() {
		super();
	}

	@Override
	protected void process(ArgsData argsdata) {
		Path p=Paths.get(argsdata.getArgument("delPath").toString()).normalize();
		DeleteR.main(p,true);
	
	}
	

}
