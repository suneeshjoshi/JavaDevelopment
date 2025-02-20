package com.example.events;
public record UnwindEvent(
        String tradeId,
        int quantity
) implements TradeEvent {
    @Override
    public String eventType() {
        return "UnwindEvent";
    }
    @Override
    public String eventPayload() {
        return "Unwound Quantity: " + quantity;
    }
}
