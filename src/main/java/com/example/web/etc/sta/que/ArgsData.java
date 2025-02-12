package com.example.web.etc.sta.que;

import java.util.HashMap;
import java.util.Map;

public abstract class ArgsData {
    private Map<String, Object> arguments; 
    private Que queInstance; 

    public ArgsData() {
        this.arguments = new HashMap<>();
    }

    public Map<String, Object> getArguments() {
        return arguments;
    }

    public void setArgument(String key, Object value) {
        arguments.put(key, value);
    }

    public Object getArgument(String key) {
        return arguments.get(key);
    }

    public void setQueInstance(Que queInstance) {
        this.queInstance = queInstance;
    }

    public Que getQueInstance() {
        return queInstance;
    }
}
