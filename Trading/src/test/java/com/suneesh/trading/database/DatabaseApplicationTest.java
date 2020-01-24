package com.suneesh.trading.database;

import com.suneesh.trading.utils.AutoTradingUtility;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class DatabaseApplicationTest {
    PostgreSQLDatabaseConnection postgreSQLDatabaseConnection = null;

    @Test
    public void databaseTest() {

        String databaseServer = AutoTradingUtility.getPropertyFromPropertyFile("DatabaseServerEngine");
        String dbURL = AutoTradingUtility.getPropertyFromPropertyFile("DatabaseURL");


        postgreSQLDatabaseConnection = new PostgreSQLDatabaseConnection("jdbc:postgresql://192.168.99.100:5432/automated_trading");
        postgreSQLDatabaseConnection.createConnection();

        List<String> tableNameList = Arrays.asList("authorize",
                "balance",
                "candle",
                "portfolio_transaction",
                "strategy",
                "strategy_steps",
                "tick",
                "transaction",
                "trade");

        tableNameList.stream().forEach(table -> {
            if(postgreSQLDatabaseConnection.checkTableExists(table)){
                log.info("Table {} Exists.", table);
            }
            else{
                log.info("Table {} does not Exist.", table);
                File file = AutoTradingUtility.getFileFromResources(table+".sql");
                try {
                    postgreSQLDatabaseConnection.executeNoResultSet(AutoTradingUtility.readFile(file));
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

        checkAndPopulateTables();
    }

    private void checkAndPopulateTables(){
        List<HashMap<String, String>> result = (List<HashMap<String,String>>)postgreSQLDatabaseConnection.executeQuery("select * from strategy");

        if(CollectionUtils.isEmpty(result)){
            log.info("Strategy Table not populated.");
            File file = AutoTradingUtility.getFileFromResources("populate_strategy_steps.sql");
            try {
                postgreSQLDatabaseConnection.executeNoResultSet(AutoTradingUtility.readFile(file));
                if( !CollectionUtils.isEmpty(postgreSQLDatabaseConnection.executeQuery("select * from strategy") ))
                {
                    log.info("strategy & strategy_steps tables populated.");
                }
                else{
                    log.error("ERROR! strategy & strategy_steps tables NOT populated.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else {
            result.forEach(f -> log.info(String.valueOf(f)));
        }

    }
}
