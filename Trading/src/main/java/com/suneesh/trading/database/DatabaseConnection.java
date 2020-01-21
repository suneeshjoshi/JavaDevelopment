package com.suneesh.trading.database;

import java.sql.Connection;
import java.util.List;

public interface DatabaseConnection {
    String URL = null;
    Connection connection = null;
    Connection createConnection();
    String getUrl();
    Connection getConnection();

    void executeNoResultSet(String createStatement);

    List executeQuery(String statement);
    boolean checkTableExists(String tableName);
    void createDBSchema();

}
