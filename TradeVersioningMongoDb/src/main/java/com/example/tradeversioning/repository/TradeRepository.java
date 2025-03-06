package com.example.tradeversioning.repository;

import com.example.tradeversioning.model.Trade;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TradeRepository extends MongoRepository<Trade, String> {
    List<Trade> findByTradeIdOrderByVersionAsc(String tradeId);
}
