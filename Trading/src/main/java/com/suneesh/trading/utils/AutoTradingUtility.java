package com.suneesh.trading.utils;


import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class AutoTradingUtility {

    protected static Properties applicationProperties;

    // get file from classpath, resources folder
    public static InputStreamReader getFileFromResources(String fileName) {
        return new InputStreamReader(AutoTradingUtility.class.getClassLoader().getResourceAsStream(fileName));
    }

    public static String readFile(InputStreamReader file) throws IOException {
        StringBuilder result = new StringBuilder();
        if (file == null) return null;

        try (BufferedReader br = new BufferedReader(file)) {

            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
        }
        return String.valueOf(result);
    }

    public static String readFile(InputStream file) throws IOException {
        StringBuilder result = new StringBuilder();
        if (file == null) return null;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file))) {

            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
        }
        return String.valueOf(result);
    }

    public static String quotedString(Object str){
        String result = null;
        if(str!=null){
            result ="'"+String.valueOf(str)+"'";
        }
        return result;
    }

    public static String getTimeStampString(long epochTime){
        return "TIMESTAMP  'epoch' + "+epochTime+"  * INTERVAL '1 second' " ;
    }

    public static Properties readProperties(){
        Properties appProps = new Properties();
        try( InputStreamReader fileInputStream = getFileFromResources("application.properties") ){
            appProps.load(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return appProps;
    }

    public static String getPropertyFromPropertyFile(String property){
        if(applicationProperties==null){
            applicationProperties = readProperties();
        }
        return applicationProperties.getProperty(property).trim();
    }


    public static void sleep(int milliSeconds){
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static List<String> readListFromPropertyFile(String property){
        String listAsString = AutoTradingUtility.getPropertyFromPropertyFile(property);
        List<String> list= Arrays.asList(listAsString.split(","));
        return(list.stream().map(m -> m.trim()).collect(Collectors.toList()));
    }
}
