package com.suneesh.trading.repository;

import com.suneesh.trading.models.responses.AccountStatus;
import com.suneesh.trading.models.responses.AccountStatusResponse;
import com.suneesh.trading.models.responses.BalanceResponse;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountStatusResponseRepository extends CrudRepository<AccountStatus, Long> {

    List<AccountStatus> findByName(String name);

}
