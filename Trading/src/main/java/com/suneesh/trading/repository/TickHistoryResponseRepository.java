package com.suneesh.trading.repository;

import com.suneesh.trading.engine.Book;
import com.suneesh.trading.models.responses.Candle;
import com.suneesh.trading.models.responses.TickHistoryResponse;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TickHistoryResponseRepository extends CrudRepository<Candle, Long> {

    List<Candle> findByName(String name);

}
