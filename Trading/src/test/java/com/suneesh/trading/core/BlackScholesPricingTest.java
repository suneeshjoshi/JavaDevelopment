package com.suneesh.trading.core;

import com.suneesh.trading.core.calculations.BlackScholes;
import com.suneesh.trading.core.calculations.DeltaPercentage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@Slf4j
public class BlackScholesPricingTest {

    @Test
    public void testBlackScholes() {
        HashMap<String, Double> inputMap = new HashMap<>();

        // Instantaneous price
        inputMap.put("stock", 188.6181);
        // Barrier
        inputMap.put("strike", 188.5267);
        inputMap.put("volatility", 0.05D);
        inputMap.put("interest", 0.01D);

        Double aDouble = Double.valueOf(((4 * 60) + 47) / 365);
        log.info("aDouble = {}", aDouble);
//        inputMap.put("timehorizon",Double.valueOf(((4*60)+47)/365));
        inputMap.put("timehorizon", 1.5863);

        BlackScholes bs = new BlackScholes(inputMap);
        double call = bs.getCall();

        log.info("call = {}", call);
    }

    @Test
    public void EpochTest() {
        log.info("{}", Math.toIntExact(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) % 60));
        log.info("{}", Math.toIntExact(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)));
        log.info("{}", LocalDateTime.now());

        int epochTime = Math.toIntExact(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        log.info("{}", epochTime);
        log.info("{}", epochTime % 60);

    }

    @Test
    public void calcDeltaPercentage() {
        HashMap<String, Double> inputMap = new HashMap<>();
        final double VOLATILITY = 0.03D;
        final double INTEREST = 0.01D;
        final double TRADE_DURATION = 600D;

//        double timeLeftOnTrade = TRADE_DURATION - 21;
        double timeHorizon = (555.0 / 365.0) / 365.0;


        inputMap.put("stock", 189.2041);
        inputMap.put("strike", 189.3979);
        inputMap.put("volatility", VOLATILITY);
        inputMap.put("interest", INTEREST);
        inputMap.put("timehorizon", timeHorizon);

        log.info(inputMap.toString());

        DeltaPercentage dp = new DeltaPercentage(inputMap);
        double tradeDeltaPercentage = dp.calculate();
        log.info("Trade's Delta Percentage = {}", tradeDeltaPercentage);


    }
}