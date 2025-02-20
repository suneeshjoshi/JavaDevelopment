package com.suneesh.events;
public record UpsizeEvent(
        String tradeId,
        int additionalQuantity
) implements TradeEvent {
    @Override
    public String eventType() {
        return "UpsizeEvent";
    }
    @Override
    public String eventPayload() {
        return "Additional Quantity: " + additionalQuantity;
    }
}
