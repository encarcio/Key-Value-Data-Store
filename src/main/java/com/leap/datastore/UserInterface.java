package com.leap.datastore;

import java.util.Scanner;
import org.json.simple.JSONObject;

/*
    User Interface to test DataStore library
*/

public class UserInterface {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DataStore dataStore;
        System.out.println("------------------INTERFACE FOR ACCESSING KEY-VALUE DATASTORE----------------");
        System.out.println("Do you wish to specify the location where DataStore will store key-value pairs?[Y/N]");
        String response = scanner.nextLine();
        if (response.equals("Y")) {
            System.out.println(
                    "Please enter the full path of the location (Don't give file name, it will be automatically generated)");
            String filePath = scanner.nextLine();
            dataStore = new DataStore(filePath);
        } else {
            dataStore = new DataStore();
        }
        String key;
        while (true) {

            System.out.println("Select options from menu");
            System.out.println("Press 1 : Create Key-Value pair");
            System.out.println("Press 2 : Read Key-Value pair");
            System.out.println("Press 3 : Delete by Key");
            System.out.println("Press 4 : Exit");
            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
            
            //Execute Create Operation
            case 1:               
                System.out.println("Enter the key");
                key = scanner.nextLine();
                System.out.println("Enter the value");
                String value = scanner.nextLine();
                JSONObject jsonValue = new JSONObject();
                jsonValue.put(key, value); 
                System.out.println(dataStore.create(key, jsonValue));
                break;
            
            //Execute Read Operation
            case 2:
                System.out.println("Enter the key");
                key = scanner.nextLine();
                JSONObject json = dataStore.read(key);
                if (json != null) {
                    System.out.println("Value associated with key:" + json.get(key));
                }
                break;

            //Execute Delete Operation    
            case 3:
                System.out.println("Enter the key");
                key = scanner.nextLine();
                System.out.println(dataStore.delete(key));
                break;

            //Close User Interface    
            case 4:
                System.out.println("DataStore closed");
                System.exit(0);
                break;

            //Option Not Recognised    
            default:
                System.err.println("FAILURE: Please give the correct option");
                break;
            }
        }
    }
}
