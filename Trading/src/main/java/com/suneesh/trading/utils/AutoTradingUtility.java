package com.suneesh.trading.utils;


import java.io.*;
import java.net.URL;
import java.util.Properties;

public class AutoTradingUtility {

    protected static Properties applicationProperties;

    // get file from classpath, resources folder
    public static File getFileFromResources(String fileName) {

        ClassLoader classLoader = AutoTradingUtility.class.getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

    }

    public static String readFile(File file) throws IOException {
        StringBuilder result = new StringBuilder();
        if (file == null) return null;

        try (FileReader reader = new FileReader(file);
             BufferedReader br = new BufferedReader(reader)) {

            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
        }
        return String.valueOf(result);
    }

    public static String quotedString(Object str){
        return "'"+String.valueOf(str)+"'";
    }

    public static Properties readProperties(){
        File applicationPropertiesFile = getFileFromResources("application.properties");
        Properties appProps = new Properties();
        try(FileInputStream fileInputStream = new FileInputStream(applicationPropertiesFile)){
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

}
