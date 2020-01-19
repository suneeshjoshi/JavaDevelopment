package com.suneesh.trading.database;

import java.sql.Connection;
import java.util.List;

public interface DatabaseConnection {
    String URL = null;
    Connection connection = null;
    void createConnection();
    String getUrl();
    Connection getConnection();
    List executeQuery(String statement);
}
