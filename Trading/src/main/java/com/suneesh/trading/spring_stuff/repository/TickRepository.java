package com.suneesh.trading.spring_stuff.repository;

import com.suneesh.trading.models.responses.Tick;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TickRepository extends CrudRepository<Tick, Long> {

    List<Tick> findByName(String name);

}
