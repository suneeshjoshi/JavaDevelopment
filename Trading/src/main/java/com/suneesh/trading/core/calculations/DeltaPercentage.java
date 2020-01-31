package com.suneesh.trading.core.calculations;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Data
@Slf4j
public class DeltaPercentage {
    HashMap<String,Double> inputMap;
    final double epsilon = 0.0000001D;
    double delta;

    public DeltaPercentage(HashMap<String, Double> inputMap) {
        this.inputMap = inputMap;
    }

    public double calculate(){
        BlackScholes bs1 = new BlackScholes(inputMap);
        double callPrice1 = bs1.getCall();

        HashMap<String, Double> inputMap2 = new HashMap<>(inputMap);
        inputMap2.put("stock",  inputMap.get("stock")+epsilon);

        BlackScholes bs2 = new BlackScholes(inputMap2);
        double callPrice2 = bs2.getCall();

        double priceDiff = callPrice2 - callPrice1;
        delta = priceDiff/epsilon;

        double deltaPercentage = ((delta*2)*100) - 100;
        return deltaPercentage;
    }
}
