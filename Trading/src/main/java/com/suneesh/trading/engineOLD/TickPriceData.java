package com.suneesh.trading.engineOLD;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TickPriceData {
    private float price;
    private float change;
    private LocalDateTime timestamp;
    private Long id = 0L;
    private String direction;
    private static Long idCounter = 1L;

    TickPriceData(float newPrice, float change){
        this.price = newPrice;
        this.change = change;
        this.timestamp = LocalDateTime.now();
        this.id = TickPriceData.getNextID();
        this.direction = (change<0?"DOWN":"UP");
    }

    private static Long getNextID() {
        return idCounter++;
    }

}
