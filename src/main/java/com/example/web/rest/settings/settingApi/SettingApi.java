package com.example.web.rest.settings.settingApi;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController

public class SettingApi{

	
	@GetMapping("/anime-web/api/setting/")
	public SettingBean start()  {
		SettingBean settingBean = new SettingBean();
		settingBean.setDocumentRoot(com.example.web.etc.sta.Setting.getRoot());
		
		return settingBean;
		
		
	}
	
	
}
