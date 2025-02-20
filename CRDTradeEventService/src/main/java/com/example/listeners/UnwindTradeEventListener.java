package com.example.listeners;
import com.example.events.UnwindEvent;
import com.example.processor.TradeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UnwindTradeEventListener implements TradeEventListener {

    @Autowired
    private TradeProcessor tradeProcessor;

    @EventListener
    public void handleExecuteEvent(UnwindEvent event) {
        tradeProcessor.process(event);
    }
}
