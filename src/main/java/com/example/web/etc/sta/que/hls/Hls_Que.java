package com.example.web.etc.sta.que.hls;

import com.example.web.etc.sta.Hls;
import com.example.web.etc.sta.que.ArgsData;
import com.example.web.etc.sta.que.Que;

public class Hls_Que extends Que {

	
	public Hls_Que() {
		super();
	}

	@Override
	protected void process(ArgsData argsdata) {
		Hls.main(argsdata);
		
	}
	

}
