package com.suneesh.trading.database;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class DatabaseApplication {

    // get file from classpath, resources folder
    private static File getFileFromResources(String fileName) {

        ClassLoader classLoader = DatabaseApplication.class.getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

    }

    private static String readFile(File file) throws IOException {
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

    public static void main(String[] args) {

        PostgreSQLDatabaseConnection postgreSQLDatabaseConnection = new PostgreSQLDatabaseConnection("jdbc:postgresql://localhost/automated_trading");
        postgreSQLDatabaseConnection.createConnection();

        List<String> tableNameList = Arrays.asList("account_status",
                "authorize",
                "balance",
                "candle",
                "portfolio_transaction",
                "tick",
                "transaction");

        tableNameList.stream().forEach(table -> {
            if(postgreSQLDatabaseConnection.checkTableExists(table)){
                log.info("Table {} Exists.", table);
            }
            else{
                log.info("Table {} does not Exist.", table);
                File file = getFileFromResources(table+".sql");
                try {
                    postgreSQLDatabaseConnection.executeCreateTableQuery(readFile(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                log.info("Checking after Create table query...");
                if(postgreSQLDatabaseConnection.checkTableExists(table)){
                    log.info("Table {} Exists.", table);
                }
                else {
                    log.info("Table {} still does not Exists.", table);
                }

            }


        });




            List list = postgreSQLDatabaseConnection.executeQuery("select * from book");

        list.forEach(f->log.info(String.valueOf(f)));

    }

}
