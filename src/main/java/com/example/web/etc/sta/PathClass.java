package com.example.web.etc.sta;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class PathClass {
	public static boolean IsExistFolder(String path) {
		Path p=Paths.get(path);
		return Files.exists(p);
	}
}