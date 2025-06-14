package com.example.web.etc.sta.que;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;

import com.example.web.etc.sta.Log;

public abstract class Que {

    static final BlockingQueue<ArgsData> encodingQueue = new LinkedBlockingQueue<>();
    static boolean isRunning = false;

    
    protected abstract void process(ArgsData args);

    
    public static synchronized void addToQueue(ArgsData args) {
    	Log.log(Level.INFO, "===キューに追加されました。===");
    	writeLog(args);
    	
        encodingQueue.add(args);
        Log.log(Level.INFO, "===キューの追加が終わりました。===");
        
        processQueue(); // キューの処理を開始
    }

    // キューの処理
    protected static synchronized void processQueue() {
        if (isRunning || encodingQueue.isEmpty()) {
            return; 
        }

        isRunning = true;

        
        new Thread(() -> {
        	ArgsData args = null;
            try {
                args = encodingQueue.take();
                Que currentInstance = args.getQueInstance(); 
                
                if (currentInstance != null) {
                	Log.log(Level.INFO, "===プロセスを実行します。===");
                	writeLog(args);
                	Log.log(Level.INFO, "===ここから===");
                    currentInstance.process(args); 
                } else {
                	Log.log(Level.WARNING,"Que is null! ");
                }
            } catch (Exception e) {
            	Log.detail(Level.WARNING, "unknow err!", e);
                e.printStackTrace();
            } finally {
            	isRunning = false;
                processQueue(); 																																																													
            }
        }).start();
    }
    private static void writeLog(ArgsData args) {
    	  Map<String, Object> map = args.getArguments();
    	
        for (String key : map.keySet() ) {
        	Log.log(Level.INFO,key + ":" + map.get(key));
        }
    	
    }
}
