package com.suneesh.trading.repository;

import com.suneesh.trading.models.responses.BalanceResponse;
import com.suneesh.trading.models.responses.Transaction;
import com.suneesh.trading.models.responses.TransactionsStreamResponse;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionStreamResponseRepository extends CrudRepository<Transaction, Long> {

    List<Transaction> findByName(String name);

}
