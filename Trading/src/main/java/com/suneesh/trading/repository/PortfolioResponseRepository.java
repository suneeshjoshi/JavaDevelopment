package com.suneesh.trading.repository;

import com.suneesh.trading.engine.Book;
import com.suneesh.trading.models.responses.PortfolioResponse;
import com.suneesh.trading.models.responses.PortfolioTransaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PortfolioResponseRepository extends CrudRepository<PortfolioTransaction, Long> {

    List<PortfolioTransaction> findByName(String name);

}
