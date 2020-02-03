package com.suneesh.trading.core;

import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.models.requests.*;
import com.suneesh.trading.models.responses.AuthorizeResponse;
import com.suneesh.trading.utils.AutoTradingUtility;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;


import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class ErrorManager {
    private BlockingQueue<RequestBase> commandQueue = new LinkedBlockingQueue<>();
    private float startValue;
    private ApiWrapper api;

    public ErrorManager(BlockingQueue<RequestBase> inputMessageQueue, ApiWrapper apiWrapper) {
        this.commandQueue = inputMessageQueue;
        this.api = apiWrapper;
    }

    private void checkErrorsRecordedInDbTable(){
        DatabaseConnection dbConnection = api.getDatabaseConnection();
        String dbErrorMessage="WrongResponse";
        String updateString = null;
        List<Map<String,String>> wrongResponseErrorList = dbConnection.executeQuery("SELECT * from error_table WHERE status = 'ACTIVE' and error_message = "+AutoTradingUtility.quotedString(dbErrorMessage)+" and fix_time is null");
        if(!CollectionUtils.isEmpty(wrongResponseErrorList)){
            updateString= "UPDATE trade SET result='OPEN' WHERE result is null";
            dbConnection.executeNoResultSet(updateString);
            dbConnection.executeNoResultSet("UPDATE error_table SET status ='FIXED' AND fix_time = now() WHERE status = 'ACTIVE' and error_message = "+AutoTradingUtility.quotedString(dbErrorMessage)+" and fix_time is null");
        }
    }

    public void threadWork(){
        log.info("Thread work ... ");
        while(true){
            try {
                checkErrorsRecordedInDbTable();
                AutoTradingUtility.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
