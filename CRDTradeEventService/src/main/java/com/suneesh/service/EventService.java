package com.suneesh.service;

import com.suneesh.events.ExecuteEvent;
import com.suneesh.events.UnwindEvent;
import com.suneesh.events.UpsizeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class EventService {
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public EventService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishExecuteEvent(String tradeId, int quantity) {
        ExecuteEvent event = new ExecuteEvent(tradeId, quantity);
        eventPublisher.publishEvent(event);
        System.out.println("Published Execute Event: " + event.eventPayload());
    }

    public void publishUnwindEvent(String tradeId, int quantity) {
        UnwindEvent event = new UnwindEvent(tradeId, quantity);
        eventPublisher.publishEvent(event);
        System.out.println("Published Unwind Event: " + event.eventPayload());
    }

    public void publishUpsizeEvent(String tradeId, int additionalQuantity) {
        UpsizeEvent event = new UpsizeEvent(tradeId, additionalQuantity);
        eventPublisher.publishEvent(event);
        System.out.println("Published Upsize Event: " + event.eventPayload());
    }
}
