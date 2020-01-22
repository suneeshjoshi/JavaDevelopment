package com.suneesh.trading.database;

import com.suneesh.trading.utils.AutoTradingUtility;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.sql.Statement;
import java.util.*;

@Data
public class PostgreSQLDatabaseConnection implements DatabaseConnection {
    private static Logger logger = LogManager.getLogger();

    String URL;
    Connection connection;
    private List<String> listOfTables = Arrays.asList(
            "authorize",
            "balance",
            "candle",
            "portfolio_transaction",
            "tick",
            "transaction");

    private boolean dropAllTables = true;

    public PostgreSQLDatabaseConnection(String url) {
        this.URL =url;
        this.connection = createConnection();
    }

    @Override
    public Connection createConnection() {
        Connection conn = null;
        Properties props = new Properties();
        props.setProperty("user", "suneesh");
        props.setProperty("password", "suneesh");
        try {
            conn = DriverManager.getConnection(getUrl(), props);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
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
    public void executeNoResultSet(String createStatement) {
        Statement st = null;
        ArrayList list = new ArrayList(50);
        try {
            st = getConnection().createStatement();
            st.execute(createStatement);
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        @Override
    public List executeQuery(String statement) {
        Statement st = null;
        ResultSet rs = null;
        ArrayList list = new ArrayList(50);
        try {
            st = getConnection().createStatement();
            logger.info("Statement = {}", statement);
            rs = st.executeQuery(statement);
            if (rs != null) {
                ResultSetMetaData md = rs.getMetaData();
                int columns = md.getColumnCount();
                while (rs.next()){
                    HashMap row = new HashMap(columns);
                    for(int i=1; i<=columns; ++i){
                        row.put(md.getColumnName(i),rs.getObject(i));
                    }
                    list.add(row);
                }
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean checkTableExists(String tableName){
        DatabaseMetaData dbm = null;
        boolean result = false;
        try {
            dbm = getConnection().getMetaData();
            ResultSet tables = dbm.getTables(null, null, tableName, null);
            if (tables.next()) {
                result = true;
            }
            else {
                result = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void dropTables(){
        if(dropAllTables){
            listOfTables.stream().forEach(table -> {
                logger.info("Dropping table, {}", table);
                executeNoResultSet("DROP TABLE "+table);
            });
        }
    }

    @Override
    public void createDBSchema() {
        dropTables();

        listOfTables.stream().forEach(table -> {
            if(checkTableExists(table)){
                logger.info("Table {} Exists.", table);
            }
            else{
                logger.info("Table {} does not Exist.", table);
                File file = AutoTradingUtility.getFileFromResources(table+".sql");
                try {
                    executeNoResultSet(AutoTradingUtility.readFile(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                logger.info("Checking after Create table query...");
                if(checkTableExists(table)){
                    logger.info("Table {} Exists.", table);
                }
                else {
                    logger.info("Table {} still does not Exists.", table);
                }
            }
        });

    }

}
