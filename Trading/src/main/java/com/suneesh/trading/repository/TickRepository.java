package com.suneesh.trading.repository;

import com.suneesh.trading.engine.Book;
import com.suneesh.trading.models.responses.Tick;
import com.suneesh.trading.models.responses.TickResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TickRepository extends CrudRepository<Tick, Long> {

    List<Tick> findByName(String name);

}
