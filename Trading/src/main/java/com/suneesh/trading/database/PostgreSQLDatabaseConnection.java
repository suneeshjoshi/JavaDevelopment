package com.suneesh.trading.database;

import com.suneesh.trading.models.responses.*;

import java.sql.*;
import java.sql.Statement;
import java.util.*;

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
    public void createDBSchema() {
//        protected static Map<Long, TickResponse> tickCache;
//        protected static Map<Long, TickHistoryResponse> tickHistoryCache;
//        protected static Map<Long, AuthorizeResponse> authorizeResponseCache;
//        protected static Map<Long, BalanceResponse> balanceResponseCache;
//        protected static Map<Long, TransactionsStreamResponse> transactionsStreamResponseCache;
//        protected static Map<Long, PortfolioResponse> portfolioResponseCache;
//        protected static Map<Long, AccountStatusResponse> accountStatusResponseCache;

//        if(!checkTableExists("Tick")){
//            createTickTable();
//        }
//        if(!checkTableExists("TickHistory")){
//            createTickTable();
//        }
//        if(!checkTableExists("Authorize")){
//            createTickTable();
//        }
//        if(!checkTableExists("Balance")){
//            createTickTable();
//        }
//        if(!checkTableExists("Transaction")){
//            createTickTable();
//        }
//        if(!checkTableExists("Portfolio")){
//            createTickTable();
//        }
//        if(!checkTableExists("AccountStatus")){
//            createTickTable();
//        }

    }

}
