package com.suneesh.trading.repository;

import com.suneesh.trading.models.responses.Authorize;
import com.suneesh.trading.models.responses.AuthorizeResponse;
import com.suneesh.trading.models.responses.BalanceResponse;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthorizeResponseRepository extends CrudRepository<Authorize, Long> {

    List<Authorize> findByName(String name);

}
