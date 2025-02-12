package com.example.web.etc.sta.que.cmd;

import com.example.web.etc.sta.ExecProcessget;
import com.example.web.etc.sta.que.ArgsData;
import com.example.web.etc.sta.que.Que;

public class Cmd_que extends Que  {
	
	public  Cmd_que() {
		super();
	}

	@Override
	protected void process(ArgsData argsdata) {
		// ExecProcess.main(argsdata.getArgument("cmd").toString());
		ExecProcessget.start(argsdata.getArgument("cmd").toString());
	}
}
