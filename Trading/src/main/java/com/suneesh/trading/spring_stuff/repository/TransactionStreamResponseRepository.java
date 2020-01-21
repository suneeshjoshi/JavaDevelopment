package com.suneesh.trading.spring_stuff.repository;

import com.suneesh.trading.models.responses.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionStreamResponseRepository extends CrudRepository<Transaction, Long> {

    List<Transaction> findByName(String name);

}
