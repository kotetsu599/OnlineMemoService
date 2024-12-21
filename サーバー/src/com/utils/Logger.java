package com.utils;
import java.sql.Timestamp;
import java.text.MessageFormat;

public class Logger{
    public static void Log(String EventType,String EventContent,String IpAddress){
        
        Long datetime = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(datetime);

        System.out.println(MessageFormat.format("[{0}] {1} {2} {3}",EventType,timestamp,IpAddress,EventContent));

    }
}