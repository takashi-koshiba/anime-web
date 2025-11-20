package com.example.web.rest.settings.addProg;


import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.sta.Setting;
import com.example.web.index.BeanUser;
import com.example.web.index.GetIP;
@RestController

public class AddProg{

	
	@GetMapping("/anime-web/api/AddProg/")
	public ResponseEntity<Object> start(HttpServletRequest request)  {
		BeanUser user =GetIP.GetNameAndIp(request);
		if(!user.isAdmin()) {
		    return ResponseEntity
		            .status(HttpStatus.FORBIDDEN)
		            .body("権限がありません。");
		}
		
		
		return null;

		
		
	}
	
	private void readProg() {
		Path current = Path.of(Setting.getProgPath());
		if (!Files.isDirectory(current)) {
		    throw new InvalidPathException(current.toString(), "ディレクトリが存在しません");
		}

	}
	
	
}
