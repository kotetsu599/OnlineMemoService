package com.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Map;
import java.util.HashMap;

public class DataManager{
    public static void SaveUserInfo(String UserName,String Password){

        Map<String,String> UserInfo = new HashMap<>();
        UserInfo.put(UserName,Password);

        saveToTxt(UserInfo, "com/data/UserInfo.txt");
        
    }

    public static void SaveUserMemos(String UserName,String Memo){
        Map<String,List<String>> UserMemos = new HashMap<>(); // {username:memo,memo1...}
        UserMemos = parseTextFileToMap("com/data/UserMemos.txt");

        if (UserMemos.containsKey(UserName)){
            try{
                List<String> Memos = new ArrayList<>();
                Memos = UserMemos.get(UserName);
                Memos.add(Memo);
                UserMemos.put(UserName, Memos);

                saveToTxtForMemos(UserMemos, "com/data/UserMemos.txt");

            }catch (Exception e){
                e.printStackTrace();
            }
           
        }else{
            List<String> newMemo = new ArrayList<>();
            newMemo.add("Thanks For Using This App.");
            newMemo.add(Memo);
            UserMemos.put(UserName,newMemo);
            
            saveToTxtForMemos(UserMemos, "com/data/UserMemos.txt");
        }
    }

    public static String GetUserMemos(String UserName){
        Map<String,List<String>> UserMemos = new HashMap<>();
        UserMemos = parseTextFileToMap("com/data/UserMemos.txt");
        List<String> Memos = new ArrayList<>();
        if (UserMemos.containsKey(UserName)){
            Memos = UserMemos.get(UserName);
            String MemosString = "";
            for(String Memo:Memos){
                MemosString = MemosString + Memo + "&"; //メモ内に&があったらぶっ壊れるから要修正
            }
            return MemosString;
        }else{
            return null;
        }
        
    }

    public static void saveToTxt(Map<String, String> data, String filename) {
    Path filePath = Paths.get(filename);
    try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String line = entry.getKey() + ":" + entry.getValue();
            writer.write(line);
            writer.newLine();
        }
    } catch (IOException e) {
        Logger.Log("ERROR", e.getMessage(), "Unknown");
    }
}

    public static void saveToTxtForMemos(Map<String, List<String>> map, String filename) {
        Path filePath = Paths.get(filename);
        try (BufferedWriter writer = Files.newBufferedWriter(filePath,StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                String line = entry.getKey() + ":" + String.join(",", entry.getValue());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            Logger.Log("ERROR", e.getMessage(), "Unknown");
        }
    }

    public static Map<String, List<String>> parseTextFileToMap(String filename) {
        Map<String, List<String>> dataMap = new HashMap<>();
        Path filePath = Paths.get(filename);

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String[] values = parts[1].split(",");
                    dataMap.put(key, new ArrayList<>(Arrays.asList(values)));//この行のせいで一時間無駄にした。Arrays.asListはサイズが固定されているから、新たな要素を追加しようとするとエラーが起きる。
                }
            }
        } catch (IOException e) {
            Logger.Log("ERROR", e.getMessage(), "Unknown");
        }

        return dataMap;
    }


}