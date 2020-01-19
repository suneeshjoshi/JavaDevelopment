package com.suneesh.trading.repository;

import com.suneesh.trading.engine.Book;
import com.suneesh.trading.models.responses.TickHistoryResponse;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TickHistoryResponseRepository extends CrudRepository<TickHistoryResponse, Long> {

    List<TickHistoryResponse> findByName(String name);

}
