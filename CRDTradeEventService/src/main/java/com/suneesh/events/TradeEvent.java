package com.suneesh.events;
public interface TradeEvent {
    String tradeId();
    String eventType();
    String eventPayload();
}
