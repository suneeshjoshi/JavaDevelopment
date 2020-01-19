package com.suneesh.trading.repository;

import com.suneesh.trading.engine.Book;
import com.suneesh.trading.models.responses.Tick;
import com.suneesh.trading.models.responses.TickResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TickRepository extends JpaRepository {

    List<Tick> findByName(String name);

}
