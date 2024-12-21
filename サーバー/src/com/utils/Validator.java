package com.utils;

import java.io.BufferedReader;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


public class Validator{

    private static boolean isLengthInvalid(String input, int minLength, int maxLength) {
        return input == null || input.length() < minLength || input.length() > maxLength;
    }

    private static boolean containsWhitespace(String input) {
        return input != null && input.matches(".*\\s.*");
    }


    public static boolean IsValidateRegister(String UserName,String Password){
        Map<String,String> UserInfo = new HashMap<>();
        UserInfo = loadFromFile("com/data/UserInfo.txt");

        if (UserInfo.containsKey(UserName)){//ユーザーネームの重複を確認
            return false;
        }

        if (!containsWhitespace(UserName)&&!isLengthInvalid(UserName, 5, 30)) {//ユーザーネームに空白が含まれていないかつ 五字以上30字以下
            return true;
        }else{
            return false;
        }
    }

    public static boolean IsValidateLogin(String UserName,String Password){
        Map<String,String> UserInfo = new HashMap<>();
        UserInfo = loadFromFile("com/data/UserInfo.txt");

        if (UserInfo.containsKey(UserName)){
            if (UserInfo.get(UserName).equals(Password)){ //UserNameのパスワードが提供されたパスワードと等しいとき
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }

    }

    public static boolean IsValidateMemo(String Memo){
        if (Memo.length()>4000){
            return false;
        }else{
            return true;
        }
    }

    public static Map<String, String> loadFromFile(String filename) {
    Map<String, String> map = new HashMap<>();
    Path filePath = Paths.get(filename); 
    try (BufferedReader reader = Files.newBufferedReader(filePath)) {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains(":")) {
                String[] keyValue = line.split(":", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();
                    map.put(key, value);
                } else {
                    System.err.println("無効な行をスキップ: " + line);
                }
            } else {
                System.err.println("コロンが含まれていない行をスキップ: " + line);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
    return map;
}

}
