package com.suneesh.trading.database;

import com.suneesh.trading.utils.AutoTradingUtility;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
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
    private List<String> listOfTables;
    private List<String> tablesToPopulate;
    private List<String> tablesToDrop;

    public PostgreSQLDatabaseConnection(String url) {
        this.URL =url;
        this.connection = createConnection();

        this.listOfTables = AutoTradingUtility.readListFromPropertyFile("ListOfDatabaseTables");
        this.tablesToPopulate = AutoTradingUtility.readListFromPropertyFile("DatabaseTablesToPopulate");
        this.tablesToDrop = AutoTradingUtility.readListFromPropertyFile("DatabaseTablesToDrop");
    }

    @Override
    public Connection createConnection() {
        Connection conn = null;
        Properties props = new Properties();
        props.setProperty("user", AutoTradingUtility.getPropertyFromPropertyFile("DatabaseUsername"));
        props.setProperty("password", AutoTradingUtility.getPropertyFromPropertyFile("DatabasePassword"));
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
    public List<HashMap<String,String>> executeQuery(String statement) {
        Statement st = null;
        ResultSet rs = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<>(50);
        try {
            st = getConnection().createStatement();
//            logger.info("Statement = {}", statement);
            rs = st.executeQuery(statement);
            if (rs != null) {
                ResultSetMetaData md = rs.getMetaData();
                int columns = md.getColumnCount();
                while (rs.next()){
                    HashMap<String,String> row = new HashMap<>(columns);
                    for(int i=1; i<=columns; ++i){
                        row.put(md.getColumnName(i),String.valueOf(rs.getObject(i)));
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
        if(CollectionUtils.isNotEmpty(tablesToDrop)){
            tablesToDrop.stream().forEach(table -> {
                logger.info("Dropping table, {}", table);
                executeNoResultSet("DROP TABLE "+table);
            });
        }
    }

    @Override
    public void createDBSchema() {
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

    @Override
    public void checkAndPopulateTables(){
        tablesToPopulate.forEach(table->{
            List<HashMap<String, String>> result = (List<HashMap<String,String>>)executeQuery("select * from "+table);

            if(CollectionUtils.isEmpty(result)){
                logger.info("{} Table not populated.", table);
                File file = AutoTradingUtility.getFileFromResources("populate_"+table+".sql");
                try {
                    executeNoResultSet(AutoTradingUtility.readFile(file));
                    if( !CollectionUtils.isEmpty(executeQuery("select * from "+table) ))
                    {
                        logger.info("{} tables populated.", table);
                    }
                    else{
                        logger.error("ERROR! {} tables NOT populated.",table);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                logger.info("{} table already populated.",table);
            }
        });
    }

    public void init(boolean backTestingMode){
        if(!backTestingMode) {
            dropTables();
        }
        else{
            logger.info("DROPPInG TABLE NOT ALLOWED IN BACKTESTING MODE.");
        }
        createDBSchema();
        checkAndPopulateTables();
    }

}
