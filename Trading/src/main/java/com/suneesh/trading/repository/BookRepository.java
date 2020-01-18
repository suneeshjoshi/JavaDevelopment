package com.suneesh.trading.repository;

import com.suneesh.trading.engine.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {

    List<Book> findByName(String name);

}
