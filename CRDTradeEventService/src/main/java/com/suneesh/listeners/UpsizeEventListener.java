package com.suneesh.listeners;

import com.suneesh.events.UpsizeEvent;
import com.suneesh.processor.TradeProcessor;
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
