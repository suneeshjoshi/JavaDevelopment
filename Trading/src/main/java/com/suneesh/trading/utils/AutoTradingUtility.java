package com.suneesh.trading.utils;

import com.suneesh.trading.database.DatabaseApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class AutoTradingUtility {
    // get file from classpath, resources folder
    public static File getFileFromResources(String fileName) {

        ClassLoader classLoader = DatabaseApplication.class.getClassLoader();

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

}
