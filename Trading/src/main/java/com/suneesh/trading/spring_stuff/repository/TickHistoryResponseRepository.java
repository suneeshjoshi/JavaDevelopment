package com.suneesh.trading.spring_stuff.repository;

import com.suneesh.trading.models.responses.Candle;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TickHistoryResponseRepository extends CrudRepository<Candle, Long> {

    List<Candle> findByName(String name);

}
