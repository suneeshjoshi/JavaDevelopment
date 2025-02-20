package com.suneesh.processor;

import com.suneesh.events.ExecuteEvent;
import com.suneesh.events.UnwindEvent;
import com.suneesh.events.UpsizeEvent;
import com.suneesh.model.Trade;
import com.suneesh.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TradeProcessor {
    @Autowired
    private TradeRepository tradeRepository;

    public void process(ExecuteEvent event) {
        Trade trade = new Trade();
        trade.setTradeId(event.tradeId());
        trade.setQuantity(event.quantity());
        trade.addEvent(event);
        tradeRepository.save(trade);
        System.out.println("Processed Execute Event: " + trade);
    }

    public void process(UnwindEvent event) {
        tradeRepository.findByTradeId(event.tradeId()).ifPresent(trade -> {
            trade.setQuantity(trade.getQuantity() - event.quantity());
            trade.addEvent(event);
            tradeRepository.save(trade);
            System.out.println("Processed Unwind Event: " + trade);
        });
    }

    public void process(UpsizeEvent event) {
        tradeRepository.findByTradeId(event.tradeId()).ifPresent(trade -> {
            trade.setQuantity(trade.getQuantity() + event.additionalQuantity());
            trade.addEvent(event);
            tradeRepository.save(trade);
            System.out.println("Processed Upsize Event: " + trade);
        });
    }

//    public void getTradeEventHistory(String tradeId) {
//        Trade trade = trades.get(tradeId);
//        if (trade != null) {
//            int count=0;
//            System.out.println("Trade Event History for Trade ID: " + tradeId);
//            for (TradeEvent event : trade.getEventHistory()) {
//                System.out.println(++count + " : " + event.eventType() +" : "+ event.eventPayload());
//            }
//        }
//    }
}
