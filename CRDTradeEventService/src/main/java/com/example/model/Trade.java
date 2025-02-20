package com.example.model;

import com.example.events.TradeEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trade {
    private String tradeId;
    private int quantity;
    List<TradeEvent> eventHistory;

    public Trade(String tradeId){
        this(tradeId,0,new java.util.ArrayList<>());
    }

    public Trade(String tradeId, int quantity) {
        this(tradeId,quantity,new java.util.ArrayList<>());
    }

    public void addEvent(TradeEvent event) {
        eventHistory.add(event);
    }

}
