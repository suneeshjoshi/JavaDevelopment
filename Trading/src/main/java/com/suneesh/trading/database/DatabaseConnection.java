package com.suneesh.trading.database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public interface DatabaseConnection {
    String URL = null;
    Connection connection = null;
    List<String> listOfTables = null;
    List<String> tablesToPopulate = null;
    List<String> tablesToDrop = null;

    Connection createConnection();
    String getUrl();
    Connection getConnection();

    void executeNoResultSet(String createStatement);

    List executeQuery(String statement);
    boolean checkTableExists(String tableName);

    void dropTables();

    void createDBSchema();

    void checkAndPopulateTables();

    void init(boolean backTestingMode);

    String getFirstElementFromDBQuery(String Query);

    ArrayList<String> getJsonResultDBQuery(String query);
}
