package com.leap.datastore.Data;

import java.io.Serializable;
import org.json.simple.JSONObject;

// Class for storing key-value pair with properties

public class Data implements Serializable{
    
    private static final long serialVersionUID=1L;
    private String key;
    private int timeToLive;
    private JSONObject value;
    
    public String getKey()
    {
        return key;
    }
    public void setKey(String key)
    {
        this.key=key;
    }
    public int getTimeToLive(){
        return this.timeToLive;
    }
    public void setTimeToLive(int timeToLive)
    {
        this.timeToLive=timeToLive;
    }
    public JSONObject getValue(){
        return this.value;
    }
    public void setValue(JSONObject value){
        this.value=value;
    }
}