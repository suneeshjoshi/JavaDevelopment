package com.suneesh.trading.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class PostgreSQLDatabaseConnection implements DatabaseConnection {

    String URL;
    Connection connection;

    public PostgreSQLDatabaseConnection(String url) {
        this.URL =url;
    }

    @Override
    public void createConnection() {
        Properties props = new Properties();
        props.setProperty("user", "suneesh");
        props.setProperty("password", "suneesh");
        try {
            this.connection = DriverManager.getConnection(getUrl(), props);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getUrl() {
        return this.URL;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public List executeQuery(String statement) {
        Statement st = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList(50);
        try {
            st = getConnection().createStatement();
            rs = st.executeQuery(statement);
            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();
            while (rs.next()){
                HashMap row = new HashMap(columns);
                for(int i=1; i<=columns; ++i){
                    row.put(md.getColumnName(i),rs.getObject(i));
                }
                list.add(row);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
