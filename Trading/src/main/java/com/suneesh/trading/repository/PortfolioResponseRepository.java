package com.suneesh.trading.repository;

import com.suneesh.trading.engine.Book;
import com.suneesh.trading.models.responses.PortfolioResponse;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PortfolioResponseRepository extends CrudRepository<PortfolioResponse, Long> {

    List<PortfolioResponse> findByName(String name);

}
