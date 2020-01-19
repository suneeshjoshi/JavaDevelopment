package com.suneesh.trading.repository;

import com.suneesh.trading.models.responses.AuthorizeResponse;
import com.suneesh.trading.models.responses.BalanceResponse;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthorizeResponseRepository extends CrudRepository<AuthorizeResponse, Long> {

    List<AuthorizeResponse> findByName(String name);

}
