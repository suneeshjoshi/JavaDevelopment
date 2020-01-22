package com.suneesh.trading.database;

import com.suneesh.trading.utils.AutoTradingUtility;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

public interface DatabaseConnection {
    String URL = null;
    Connection connection = null;
    List<String> listOfTables = null;
    boolean dropAllTables = false;

    Connection createConnection();
    String getUrl();
    Connection getConnection();

    void executeNoResultSet(String createStatement);

    List executeQuery(String statement);
    boolean checkTableExists(String tableName);

    void dropTables();

    void createDBSchema();

}
