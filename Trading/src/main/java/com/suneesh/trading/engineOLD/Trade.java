package com.suneesh.trading.engineOLD;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Trade {
    String PutOrCall;
    float startPrice;
    LocalDateTime tradeTime;
    float amount;
    String status;

}