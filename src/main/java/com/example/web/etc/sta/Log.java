package com.example.web.etc.sta;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
	private static Logger logger;
	private static String currentLogDate = "";
	private synchronized static  Logger write() throws IOException  {
		 String today = printNow();
        if (logger != null  && today.equals(currentLogDate)) {
            return logger; // すでに作成済みなら再利用
        }

        logger = Logger.getLogger(Log.class.getName());
        String fullPath = Setting.getRoot();
        Path logDir = Paths.get(fullPath, "content", "anime-web", "logs");

        currentLogDate = today;

        try {
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
            }
            
            FileHandler fHandler = new FileHandler(logDir.resolve(printNow()+".log").toString(), true);
            fHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fHandler);
            logger.setUseParentHandlers(false); 
            return logger;
            
        } catch (SecurityException | IOException e) {
            throw new IOException("ログファイルを作成できませんでした: " + e.getMessage(), e);
        }
    }
	public static void log(Level lev, String txt) {
		try {
			Logger logger=write();
			logger.log(lev,txt);
			System.out.println(lev.toString()+":"+txt);
			
			
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
	}
	public static void detail(Level lev,String txt,Exception e2) {
		 try {
		        Logger logger = write();
		        
		        StringWriter sw = new StringWriter();
		        PrintWriter pw = new PrintWriter(sw);
		        e2.printStackTrace(pw);
		        
		        logger.log(lev, txt + "\n" + sw.toString());
		        System.out.println(lev.toString()+":"+txt);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		
	}
	private static String printNow() {

			Date now = new Date();
		 
			SimpleDateFormat f= new SimpleDateFormat("yyyyMMdd");
			return f.format(now);
		 
	}
	
}
