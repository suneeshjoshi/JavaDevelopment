package com.example.processor;

import com.example.events.ExecuteEvent;
import com.example.events.TradeEvent;
import com.example.events.UnwindEvent;
import com.example.events.UpsizeEvent;
import com.example.model.Trade;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TradeProcessor {
    private final Map<String, Trade> trades = new HashMap<>();

    public void process(ExecuteEvent event) {
        Trade trade = new Trade(event.tradeId(), event.quantity());
        trade.addEvent(event);
        trades.put(event.tradeId(), trade);
        System.out.println("Processed Execute Event: " + trade);
    }

    public void process(UnwindEvent event) {
        Trade trade = trades.get(event.tradeId());
        if (trade != null) {
            trade.setQuantity(trade.getQuantity() - event.quantity());
            trade.addEvent(event);
            System.out.println("Processed Unwind Event: " + trade);
        }
    }

    public void process(UpsizeEvent event) {
        Trade trade = trades.get(event.tradeId());
        if (trade != null) {
            trade.setQuantity(trade.getQuantity() + event.additionalQuantity());
            trade.addEvent(event);
            System.out.println("Processed Upsize Event: " + trade);
        }
    }

    public void getTradeEventHistory(String tradeId) {
        Trade trade = trades.get(tradeId);
        if (trade != null) {
            int count=0;
            System.out.println("Trade Event History for Trade ID: " + tradeId);
            for (TradeEvent event : trade.getEventHistory()) {
                System.out.println(++count + " : " + event.eventType() +" : "+ event.eventPayload());
            }
        }
    }
}
