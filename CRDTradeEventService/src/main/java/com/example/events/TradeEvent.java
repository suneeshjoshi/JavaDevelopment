package com.example.events;
public interface TradeEvent {
    String tradeId();
    String eventType();
    String eventPayload();
}
