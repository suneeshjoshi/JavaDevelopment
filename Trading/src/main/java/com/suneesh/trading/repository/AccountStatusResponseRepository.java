package com.suneesh.trading.repository;

import com.suneesh.trading.models.responses.AccountStatusResponse;
import com.suneesh.trading.models.responses.BalanceResponse;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountStatusResponseRepository extends CrudRepository<AccountStatusResponse, Long> {

    List<AccountStatusResponse> findByName(String name);

}
