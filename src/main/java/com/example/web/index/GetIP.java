package com.example.web.index;

import jakarta.servlet.http.HttpServletRequest;

import com.example.web.classes.main01;

public class GetIP {
	
	//IPから名前を検索します。
	public static BeanUser GetNameAndIp(HttpServletRequest request) {
		BeanUser[] user = {
				new BeanUser("172.0.0.1","管理者",true),
				new BeanUser("0:0:0:0:0:0:0:1","管理者",true),
		};
		
		String ip=main01.getIp(request);
		
		
		for(int i=0;i<user.length;i++) {
			if(user[i].getIp().equals(ip)) {
				return user[i];
			}
		}
		return new BeanUser(ip,"名無し",false);
	}


	

	
	
}
