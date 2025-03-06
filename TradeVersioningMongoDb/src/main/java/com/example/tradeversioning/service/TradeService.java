package com.example.tradeversioning.service;

import com.example.tradeversioning.model.Trade;
import com.example.tradeversioning.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    @Transactional
    public Trade saveTradeVersion(String tradeId, Map<String, Object> tradeData) {
        List<Trade> existingTrades = tradeRepository.findByTradeIdOrderByVersionAsc(tradeId);
        existingTrades.forEach(trade -> 
            tradeRepository.save(new Trade(trade.id(), trade.tradeId(), trade.version() + 1, trade.status(), trade.timestamp(), trade.tradeData()))
        );

        Trade newTrade = Trade.builder()
                .tradeId(tradeId)
                .version(0)
                .status("NEW")
                .timestamp(Instant.now())
                .tradeData(tradeData)
                .build();

        return tradeRepository.save(newTrade);
    }

    public Trade executeTrade(String tradeId) {
        Trade latestTrade = getLatestTrade(tradeId);
        if (latestTrade == null) return null;

        Trade executedTrade = Trade.builder()
                .tradeId(tradeId)
                .version(0)
                .status("EXECUTED")
                .timestamp(Instant.now())
                .tradeData(latestTrade.tradeData())
                .build();

        return saveTradeVersion(tradeId, executedTrade.tradeData());
    }

    public Trade unwindTrade(String tradeId) {
        Trade latestTrade = getLatestTrade(tradeId);
        if (latestTrade == null || !latestTrade.status().equals("EXECUTED")) return null;

        Map<String, Object> reversedTradeData = new HashMap<>(latestTrade.tradeData());
        reversedTradeData.put("quantity", -(int) reversedTradeData.get("quantity"));

        Trade unwindTrade = Trade.builder()
                .tradeId(tradeId)
                .version(0)
                .status("UNWOUND")
                .timestamp(Instant.now())
                .tradeData(reversedTradeData)
                .build();

        return saveTradeVersion(tradeId, unwindTrade.tradeData());
    }

    public Trade upsizeTrade(String tradeId, int additionalQuantity) {
        Trade latestTrade = getLatestTrade(tradeId);
        if (latestTrade == null) return null;

        Map<String, Object> updatedTradeData = new HashMap<>(latestTrade.tradeData());
        int newQuantity = (int) updatedTradeData.get("quantity") + additionalQuantity;
        updatedTradeData.put("quantity", newQuantity);

        Trade upsizeTrade = Trade.builder()
                .tradeId(tradeId)
                .version(0)
                .status(latestTrade.status())
                .timestamp(Instant.now())
                .tradeData(updatedTradeData)
                .build();

        return saveTradeVersion(tradeId, upsizeTrade.tradeData());
    }

    public Trade getLatestTrade(String tradeId) {
        return tradeRepository.findByTradeIdOrderByVersionAsc(tradeId)
                .stream()
                .filter(trade -> trade.version() == 0)
                .findFirst()
                .orElse(null);
    }

    public Trade getPreviousVersion(String tradeId, int currentVersion) {
        return tradeRepository.findByTradeIdOrderByVersionAsc(tradeId)
                .stream()
                .filter(trade -> trade.version() == currentVersion + 1)
                .findFirst()
                .orElse(null);
    }
}
