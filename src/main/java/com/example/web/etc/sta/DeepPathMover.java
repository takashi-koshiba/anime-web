package com.example.web.etc.sta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

//ファイルを階層を変更します。
//  /test.txt→/test/test.txt
public class DeepPathMover {
	public static void move(Path p) {
		
		String fullName = p.getFileName().toString();
		String fname=fullName;
		//拡張子を排除
		if(fname.indexOf(".")>=0) {
			fname=RemoveExtension.main(fullName);
		}

		//Path to = p.toAbsolutePath().resolve(fname);  // 例: /a/b/test.txt/test.txt

		if (!Files.isRegularFile(p)) {
		    Log.log(Level.INFO, "ファイルではありません: " + p);
		    return;
		}
		
		try {
			
			//同名のフォルダは作れないので退避先を作成
			Path tempDir = p.getParent().resolve("filestemp");
			
			//Log.log(Level.INFO, "ディレクトリを作成します。: " + tempDir.toString());
			
			Files.createDirectories(tempDir);
			
			//一時的な移動先
			Path tempPath = tempDir.resolve(fullName);  // filestemp/元のファイル名
			Files.move(p, tempPath, StandardCopyOption.REPLACE_EXISTING);

			
			//移動先のディレクトリ
			Path movePath =p.getParent().resolve(fname);
			Log.log(Level.INFO, "ディレクトリを作成します。: " + movePath.toString());
		    Files.createDirectories(movePath);  
		    
		    //System.out.println("from: " + p + " to: " + to);
		    Files.move(tempPath, movePath.resolve(fullName), StandardCopyOption.REPLACE_EXISTING);
		    Log.log(Level.INFO, "移動しました: " + movePath.resolve(fullName));

		} catch (IOException e) {
			System.out.println("ha");
		    Log.detail(Level.WARNING, "ファイルの移動に失敗", e);
		}

	}

}
