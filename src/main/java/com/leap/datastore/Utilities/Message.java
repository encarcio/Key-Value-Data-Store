package com.leap.datastore.Utilities;


/*
    Named Constants for Messages 
*/


public enum Message {

    

    //Error Messages
    KEY_LENGTH_EXCEEDED("FAILURE:Key length exceeds specified limit"),
    KEY_MISSING("FAILURE:No key is specified"),
    DUPLICATE_KEY("FAILURE:Key already used"),
    INVALID_FILEPATH("FAILURE:File Path is invalid"),
    
    
    CREATE_FAIL("FAILURE:Create operation fail"),
    READ_FAIL("FAILURE:Read Operation Fail"),
    DELETE_FAIL("FAILURE:Delete Operation Fail"),

    KEY_NOT_AVAILABLE("FAILURE:Key Not Found"),
    FILE_SIZE_EXCEED("FAILURE:File size exceeds 1GB"),
    
    //Success Messages
    CREATE_SUCCESS("SUCCESS:Create operation Successful"),
    DELETE_SUCCESS("SUCCESS:Delete Operation Successful");
    
    public String value;
    private Message(String value){
        this.value=value;
    }
}
