package com.example.tradingapp.repository;

import com.example.tradingapp.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TradeRepository extends JpaRepository<Trade, Long> {
    List<Trade> findByTradeIdOrderByVersionDesc(String tradeId);
    Optional<Trade> findByTradeIdAndVersion(String tradeId, int version);
    List<Trade> findAll();
}