package com.leap.datastore;


import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import com.leap.datastore.Utilities.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.leap.datastore.Data.*;

/* 
    Class implementing Data Store and its Operations
*/


public class DataStore implements Serializable {



    private String fileName; //Stores Filename
    private String filePath; //Stores file path which data store acesses for operations

    /* 
        Map which stores key-expiry pairfor efficiently checking if key has been already
        used or not.
        Value is being used to check if key has expired i.e lived pass its specified time
        according to its time-to-live property
    */
    private Map<String, Long> keyTimeMap = new ConcurrentHashMap<String, Long>(); // Making the class thread-safe
    

    //Intialisation with default location
    public DataStore() {
        
        setFileName();
        this.filePath = Constants.defaultLocation+"\\"+this.fileName+".txt";
        FileOperations.createFile(filePath);
    }

    //Intialisation with given location
    public DataStore(String filePath) {
        setFileName();
        if (FileOperations.validFilePath(filePath)) {
            this.filePath = filePath+"\\"+fileName+".txt";
            FileOperations.createFile(filePath);
        } else {
            System.out.println(Message.INVALID_FILEPATH.value);
        }
    }

    //Generates a unique filename for each instance of datastore
    public void setFileName()
    {
        UUID id=UUID.randomUUID();
        this.fileName="db-"+id.toString();
    }

    //Create Operation( if not TTL is given)
    public synchronized String create(String key, JSONObject value) {
        try {
            return create(key, value, -1);
        } catch (Exception e) {
            return Message.CREATE_FAIL.value;
        }
    }

    //Create operation (if TTL is given) - creates a new key-value pair and stores it in file
    public synchronized String create(String key, JSONObject value, int timeToLive) {
        try {

            //Empty Key
            if (key.equals("")) 
            {
                return Message.KEY_MISSING.value;
            }
            //Key has more than 32 chars
            if (key.length() > 32) 
            {
                return Message.KEY_LENGTH_EXCEEDED.value;
            }
            long currentTime = Calendar.getInstance().getTimeInMillis();
            
            //If key exists
            if (keyTimeMap.containsKey(key)) {
                
                //Check if it has expired or not.If yes,remove the key-value pair.
                if (currentTime > keyTimeMap.get(key)) {
                    keyTimeMap.remove(key);
                    FileOperations.deleteFromFile(key, filePath);

                } 
                //If key has not expired, throw error for duplicate key is used
                else {
                    return Message.DUPLICATE_KEY.value;
                }
            }
            Data data = new Data();
            data.setKey(key);
            
            if (timeToLive > 0) {
                data.setTimeToLive(timeToLive);
            } else {
                data.setTimeToLive(Constants.defaultTTL);
            }
            long expiryTime = Calendar.getInstance().getTimeInMillis();
            expiryTime += (data.getTimeToLive() * 60 * Constants.ONE_SEC_IN_MILLI);
            keyTimeMap.put(key, expiryTime);
            data.setValue(value);
            
            if (!FileOperations.validFileSize(filePath)) {
                return Message.FILE_SIZE_EXCEED.value;
            }
            FileOperations.writeToFile(this.filePath, data);
            FileOperations.readFromFile(key, filePath);
            return Message.CREATE_SUCCESS.value;
        } catch (Exception e) {
            e.printStackTrace();
            return Message.CREATE_FAIL.value;
        }
    }

    //Read Operation - returns value(JSON Object) associated with a valid key
    public synchronized JSONObject read(String key) {

        //Instantiate a JSON Object
        JSONObject jsonValue = new JSONObject();
        try {

            // Empty Key
            if (key.equals("")) 
            {
                System.out.println(Message.KEY_MISSING.value);
                return jsonValue;
            }
            //Key has more than 32 chars
            if (key.length() > 32) 
            {
                System.out.println(Message.KEY_LENGTH_EXCEEDED.value);
                return jsonValue;
            }
            //If key exists
            if (keyTimeMap.containsKey(key)) {
                long currentTime = Calendar.getInstance().getTimeInMillis();
                
                //Check if it has expired or not.If yes,remove the key-value pair.
                if (currentTime > keyTimeMap.get(key)) {
                    keyTimeMap.remove(key);
                    FileOperations.deleteFromFile(key, filePath);
                    System.out.println(Message.KEY_NOT_AVAILABLE.value);
                    return jsonValue;
                } 
                //If key has not expired, return value as JSON Object
                else {
                    String json = FileOperations.readFromFile(key, this.filePath);
                    JSONParser parser = new JSONParser();
                    jsonValue = (JSONObject) parser.parse(json);
                    return jsonValue;
                }
            } else {
                System.out.println(Message.KEY_NOT_AVAILABLE.value);
                return jsonValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print(Message.READ_FAIL.value);
        }
        return jsonValue;
    }

    //Delete Operation - deletes a key-value pair from datastore
    public synchronized String delete(String key) {
        try {
            // Empty Key
            if (key.equals("")) {
                return Message.KEY_MISSING.value;
            }
            //Key Does not exists 
            if (!keyTimeMap.containsKey(key)) {
                return Message.KEY_NOT_AVAILABLE.value;
            }
            //Key has more than 32 chars
            if(key.length()>32)
            {
                return Message.KEY_LENGTH_EXCEEDED.value;
            }
            keyTimeMap.remove(key);
            boolean res = FileOperations.deleteFromFile(key, filePath);
            if (res) {
                return Message.DELETE_SUCCESS.value;

            } else {
                return Message.DELETE_FAIL.value;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Message.DELETE_FAIL.value;
        }
    }
}
