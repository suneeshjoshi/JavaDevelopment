package com.suneesh.trading.database;

import com.suneesh.trading.utils.AutoTradingUtility;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Data
public class PostgreSQLDatabaseConnection implements DatabaseConnection {
    String URL;
    Connection connection;
    private List<String> listOfTables;
    private List<String> tablesToPopulate;
    private List<String> tablesToDrop;
    private boolean dropDbTables;


    public PostgreSQLDatabaseConnection(String url) {
        this.URL =url;
        this.connection = createConnection();

        this.listOfTables = AutoTradingUtility.readListFromPropertyFile("ListOfDatabaseTablesToCreate");
        this.tablesToPopulate = AutoTradingUtility.readListFromPropertyFile("ListOfDatabaseTablesToPopulate");
        this.dropDbTables = Boolean.parseBoolean(AutoTradingUtility.getPropertyFromPropertyFile("DropDatabaseTables"));

        String listToDrop = AutoTradingUtility.getPropertyFromPropertyFile("ListOfDatabaseTablesToDrop");
        if(!listToDrop.isEmpty()) {
            this.tablesToDrop = AutoTradingUtility.readListFromPropertyFile(listToDrop);
        }
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
        if(dropDbTables){
            log.info("DROPPING DATABASE TABLES.");
            if(CollectionUtils.isNotEmpty(tablesToDrop)){
                tablesToDrop.stream().forEach(table -> {
                    log.info("Dropping table, {}", table);
                    executeNoResultSet("DROP TABLE "+table);
                });
            }
            else{
                log.info("No Tables defined to be dropped.");
            }
        }
        else{
            log.info("NOT DROPPING DATABASE TABLES.");
        }
    }

    @Override
    public void createDBSchema() {
        listOfTables.stream().forEach(table -> {
            if(checkTableExists(table)){
                log.info("Table {} Exists.", table);
            }
            else{
                log.info("Table {} does not Exist.", table);
                File file = AutoTradingUtility.getFileFromResources(table+".sql");
                try {
                    executeNoResultSet(AutoTradingUtility.readFile(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                log.info("Checking after Create table query...");
                if(checkTableExists(table)){
                    log.info("Table {} Exists.", table);
                }
                else {
                    log.info("Table {} still does not Exists.", table);
                }
            }
        });

    }

    @Override
    public void checkAndPopulateTables(){
        tablesToPopulate.forEach(table->{
            List<HashMap<String, String>> result = (List<HashMap<String,String>>)executeQuery("select * from "+table);

            if(CollectionUtils.isEmpty(result)){
                log.info("{} Table not populated.", table);
                File file = AutoTradingUtility.getFileFromResources("populate_"+table+".sql");
                try {
                    executeNoResultSet(AutoTradingUtility.readFile(file));
                    if( !CollectionUtils.isEmpty(executeQuery("select * from "+table) ))
                    {
                        log.info("{} tables populated.", table);
                    }
                    else{
                        log.error("ERROR! {} tables NOT populated.",table);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                log.info("{} table already populated.",table);
            }
        });
    }

    public void init(boolean backTestingMode){
        dropTables();
        createDBSchema();
        checkAndPopulateTables();
    }

    public String getFirstElementFromDBQuery(String query) {
        String result = null;
        List<HashMap<String, String>> dbResultList = executeQuery(query);
        if (CollectionUtils.isNotEmpty(dbResultList)) {
            result = dbResultList.get(0).entrySet().iterator().next().getValue();
        }
        return result;
    }

    @Override
    public ArrayList<String> getJsonResultDBQuery(String query) {
        ArrayList<String> result = new ArrayList<>();
        List<HashMap<String, String>> dbResultList = executeQuery("select row_to_json(t) as json_result from (" + query + " ) t");
        if (CollectionUtils.isNotEmpty(dbResultList)) {
            result = (ArrayList<String>) dbResultList.stream().map(ele -> ele.get("json_result")).collect(Collectors.toList());

        }
        return result;
    }

}
