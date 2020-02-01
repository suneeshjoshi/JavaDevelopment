package com.suneesh.trading.models.responses;

import java.util.List;

public interface DatabaseOperation {
    default List<String> databaseInsertStringList(){ return null; };
    default List<String> databaseUpdateStringList(){ return null; };
}
