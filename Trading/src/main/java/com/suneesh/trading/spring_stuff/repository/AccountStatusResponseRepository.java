package com.suneesh.trading.spring_stuff.repository;

import com.suneesh.trading.models.responses.AccountStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountStatusResponseRepository extends CrudRepository<AccountStatus, Long> {

    List<AccountStatus> findByName(String name);

}
