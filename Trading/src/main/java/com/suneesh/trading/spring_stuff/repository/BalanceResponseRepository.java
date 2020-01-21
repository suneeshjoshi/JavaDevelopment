package com.suneesh.trading.spring_stuff.repository;

import com.suneesh.trading.models.responses.Balance;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BalanceResponseRepository extends CrudRepository<Balance, Long> {

    List<Balance> findByName(String name);

}
