package com.example.web.etc.sta.que;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Que {

    static final BlockingQueue<ArgsData> encodingQueue = new LinkedBlockingQueue<>();
    static boolean isRunning = false;

    
    protected abstract void process(ArgsData args);

    
    public static synchronized void addToQueue(ArgsData args) {
        encodingQueue.add(args);
        processQueue(); // キューの処理を開始
    }

    // キューの処理
    private static synchronized void processQueue() {
        if (isRunning || encodingQueue.isEmpty()) {
            return; 
        }

        isRunning = true;

        
        new Thread(() -> {
            try {
                ArgsData args = encodingQueue.take();
                Que currentInstance = args.getQueInstance(); 
                if (currentInstance != null) {
                    currentInstance.process(args); 
                } else {
                    System.err.println("Que instance is null!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            	isRunning = false;
                processQueue(); 
            }
        }).start();
    }
}
