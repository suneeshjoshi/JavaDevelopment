package com.suneesh.trading.database;

import com.suneesh.trading.utils.AutoTradingUtility;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class DatabaseApplicationTest {

    @Test
    public void databaseTest() {

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
                File file = AutoTradingUtility.getFileFromResources(table+".sql");
                try {
                    postgreSQLDatabaseConnection.executeCreateTableQuery(AutoTradingUtility.readFile(file));
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
