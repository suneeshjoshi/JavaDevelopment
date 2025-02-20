package com.example.listeners;

import com.example.events.UpsizeEvent;
import com.example.processor.TradeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UpsizeEventListener implements TradeEventListener{

    @Autowired
    private TradeProcessor tradeProcessor;

    @EventListener
    public void handleExecuteEvent(UpsizeEvent event) {
        tradeProcessor.process(event);
    }
}
