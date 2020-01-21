package com.suneesh.trading.spring_stuff.repository;

import com.suneesh.trading.models.responses.Authorize;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthorizeResponseRepository extends CrudRepository<Authorize, Long> {

    List<Authorize> findByName(String name);

}
