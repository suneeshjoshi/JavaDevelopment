package com.suneesh.trading.database;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class DatabaseApplication {
    public static void main(String[] args) {

        PostgreSQLDatabaseConnection postgreSQLDatabaseConnection = new PostgreSQLDatabaseConnection("jdbc:postgresql://localhost/automated_trading");
        postgreSQLDatabaseConnection.createConnection();

        String tableName = "book2";
        if(postgreSQLDatabaseConnection.checkTableExists(tableName)){
            log.info("Table {} Exists.", tableName);
        }
        else{
            log.info("Table {} does not Exist.", tableName);
        }

        List list = postgreSQLDatabaseConnection.executeQuery("select * from book");

        list.forEach(f->log.info(String.valueOf(f)));

    }
}
