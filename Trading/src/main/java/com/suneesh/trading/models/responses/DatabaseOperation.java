package com.suneesh.trading.models.responses;

public interface DatabaseOperation {
    default String databaseInsertString(){ return null; };
}
