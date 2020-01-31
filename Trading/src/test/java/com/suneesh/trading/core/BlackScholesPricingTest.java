package com.suneesh.trading.core;

import com.suneesh.trading.core.calculations.BlackScholes;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;

@Slf4j
public class BlackScholesPricingTest {

    @Test
    public void testBlackScholes(){
        HashMap<String, Double> inputMap = new HashMap<>();

        // Instantaneous price
        inputMap.put("stock",181.8374);
        // Barrier
        inputMap.put("strike",181.756);
        inputMap.put("volatility",0.05D);
        inputMap.put("interest",0.01D);

        Double aDouble = Double.valueOf(((4 * 60) + 47) / 365);
        log.info("aDouble = {}",aDouble);
//        inputMap.put("timehorizon",Double.valueOf(((4*60)+47)/365));
        inputMap.put("timehorizon",7.86);

        BlackScholes bs = new BlackScholes(inputMap);
        double call = bs.getCall();

        log.info("call = {}", call);
    }

    @Test
    public void EpochTEst(){
        log.info("{}",Math.toIntExact(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)%60));
        log.info("{}",Math.toIntExact(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)));
        log.info("{}",LocalDateTime.now());

        int epochTime = Math.toIntExact(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        log.info("{}",epochTime);
        log.info("{}",epochTime%60);

    }
}
