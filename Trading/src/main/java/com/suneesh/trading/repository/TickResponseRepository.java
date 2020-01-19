package com.suneesh.trading.repository;

import com.suneesh.trading.engine.Book;
import com.suneesh.trading.models.responses.TickResponse;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TickResponseRepository extends CrudRepository<TickResponse, Long> {

    List<TickResponse> findByName(String name);

}
