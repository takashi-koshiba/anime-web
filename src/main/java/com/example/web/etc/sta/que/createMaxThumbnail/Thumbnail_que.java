package com.example.web.etc.sta.que.createMaxThumbnail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

import com.example.web.etc.sta.Log;
import com.example.web.etc.sta.Setting;
import com.example.web.etc.sta.que.ArgsData;
import com.example.web.etc.sta.que.Que;

public class Thumbnail_que extends Que  {
	
	public  Thumbnail_que() {
		super();
	}

	@Override
	protected void process(ArgsData argsdata) {
		// ExecProcess.main(argsdata.getArgument("cmd").toString());
		String alias  = argsdata.getArgument("alias").toString();
		String fname = argsdata.getArgument("fname").toString();
		Path root = Paths.get(Setting.getRoot(), "content", "anime-web", "upload", "file", "maxSeek", alias);

		if (!Files.exists(root)) {
		    if (root.toFile().mkdirs()) {
		        Log.log(Level.INFO, "フォルダを作成しました。： " + root.toString());
		    } else {
		        Log.log(Level.WARNING, "フォルダの作成に失敗しました。： " + root.toString());
		        Log.log(Level.WARNING, "シーク画像の生成を中止しました。： " + root.toString());
		        System.err.print("フォルダの作成に失敗しました。");
		        return;
		    }
		}

		File getMaxImage = MaxImage(alias);
		try {
		    Files.copy(getMaxImage.toPath(), root.resolve(fname), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
		    Log.log(Level.WARNING, "failed to copy file from: " + getMaxImage.toPath() + " to " + root.toString());
		    e.printStackTrace();
		}

		
		
		//System.out.println(p.toString());
		 //Img img=new Img(new File(input));
		//System.out.println(argsdata.getArgument("output").toString());
		//img.Resize(argsdata.getArgument("output").toString(), Integer.valueOf(argsdata.getArgument("size").toString()), argsdata.getArgument("mime").toString());
	}
	
	private File MaxImage(String alias) {
		Path root = Paths.get(Setting.getRoot(),"content","anime-web","upload","file","seek-image",alias);
		File files[]=root.toFile().listFiles();


        long maxSize = -1;

        if (files==null) return null;
        
        long count  =0;
        long index = 0;
        for (File image : files) {
        	long size=image.length();
            if(maxSize<size) {
            	maxSize=size;
            	index=count;
            }
            count++;
        }
        File result = files[(int) index];
        
        return result;
		}
	}
