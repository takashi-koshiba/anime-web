package com.example.web.etc.sta.que.resize;

import java.io.File;
import java.nio.file.Paths;

import com.example.web.etc.sta.Img;
import com.example.web.etc.sta.que.ArgsData;
import com.example.web.etc.sta.que.Que;

public class Resize_que extends Que  {
	
	public  Resize_que() {
		super();
	}

	@Override
	protected void process(ArgsData argsdata) {
		// ExecProcess.main(argsdata.getArgument("cmd").toString());
		String input = Paths.get(argsdata.getArgument("input").toString()).normalize().toString();
		//System.out.println(p.toString());
		 Img img=new Img(new File(input));
		System.out.println(argsdata.getArgument("output").toString());
		img.Resize(argsdata.getArgument("output").toString(), Integer.valueOf(argsdata.getArgument("size").toString()), argsdata.getArgument("mime").toString());
	}
}
