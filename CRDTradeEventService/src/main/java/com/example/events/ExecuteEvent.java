package com.example.events;
public record ExecuteEvent(
        String tradeId,
        int quantity) implements TradeEvent {
    @Override
    public String eventType() {
        return "ExecuteEvent";
    }
    @Override
    public String eventPayload() {
        return "Executed Quantity: " + quantity ;
    }
}
