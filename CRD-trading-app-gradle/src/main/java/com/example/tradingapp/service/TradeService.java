package com.example.tradingapp.service;

import com.example.tradingapp.model.Trade;
import com.example.tradingapp.repository.TradeRepository;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TradeService {
    
    private final TradeRepository tradeRepository;
    private final Cache<String, Trade> tradeCache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();
    private final ObjectMapper objectMapper;

    public Trade saveTrade(String tradeJson) throws Exception {
        Trade trade = objectMapper.readValue(tradeJson, Trade.class);
        trade.setTimestamp(LocalDateTime.now());
        trade.setTradeJson(tradeJson);
        tradeRepository.save(trade);
        tradeCache.put(trade.getTradeId() + "-" + trade.getVersion(), trade);
        return trade;
    }

    public Optional<Trade> getTrade(String tradeId, int version) {
        String cacheKey = tradeId + "-" + version;
        Trade trade = tradeCache.getIfPresent(cacheKey);
        if (trade == null) {
            trade = tradeRepository.findByTradeIdAndVersion(tradeId, version).orElse(null);
            if (trade != null) {
                tradeCache.put(cacheKey, trade);
            }
        }
        return Optional.ofNullable(trade);
    }

    public void deleteTrade(String tradeId, int version) {
        tradeRepository.findByTradeIdAndVersion(tradeId, version).ifPresent(tradeRepository::delete);
        tradeCache.invalidate(tradeId + "-" + version);
    }

    public List<Trade> getTradeVersions(String tradeId) {
        return tradeRepository.findByTradeIdOrderByVersionDesc(tradeId);
    }

    public void loadAllTradesIntoCache() {
        tradeRepository.findAll().forEach(trade ->
                tradeCache.put(trade.getTradeId() + "-" + trade.getVersion(), trade));
    }
}