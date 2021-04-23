package com.leap.datastore.Utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import com.leap.datastore.Data.*;

/*

    Class for performing file path validation,file creation and file operations

*/

public class FileOperations {

    // Checks if file path given is valid or not

    public static boolean validFilePath(String filePath) {
        File file = new File(filePath);
        // Abstract pathname exists or not
        if (!file.isDirectory()) {
            return false;
        }
        return true;
    }

    //Checks if file in which data is being entered has size <= 1GB

    public static boolean validFileSize(String filePath) {
        File file = new File(filePath);
        long fileSizeInBytes = file.length();
        // Convert the bytes to bytes (1 KB = 1024 Bytes)
        long fileSizeInKB = fileSizeInBytes / 1024;
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        long fileSizeInMB = fileSizeInKB / 1024;
        // Check if file size+(data to be written) < 1024 MB(1GB)
        if (fileSizeInMB + (16 / 1024) > 1024) {
            return false;
        }
        return true;

    }

    //Creates a new file in given file path location

    public static boolean createFile(String filePath) {
        boolean res = false;
        try {
            File file = new File(filePath);
            res = file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    //Writes a new key-value pair to file 

    public static boolean writeToFile(String filePath, Data data) {
        File file = new File(filePath);
        try {
            PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            pr.println(data.getKey() + "-" + data.getValue());
            pr.close();
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
        return true;
    }

    //Read value associated with a key from file if key exists

    public static String readFromFile(String key, String filePath) {
        File file = new File(filePath);
        String value = new String();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(key)) {
                    value = line.split("-")[1];
                    break;
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    //Deletes a key-value pair from file if it exists 

    public static boolean deleteFromFile(String key, String filePath) {
        File file = new File(filePath);
        List<String> list=new LinkedList<String>();
        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(file)); 
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(key)) {
                    list.add(line);
                }
            }
            reader.close();
            file.delete();
            File newFile=new File(filePath);
            newFile.createNewFile();
            PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter(newFile, true)));
            for(String data:list)
            {
                System.out.println(data);
                pr.println(data);
                pr.flush();
            }
            pr.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
