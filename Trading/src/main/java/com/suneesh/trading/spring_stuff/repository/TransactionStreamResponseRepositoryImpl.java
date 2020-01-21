package com.suneesh.trading.spring_stuff.repository;

import com.suneesh.trading.models.responses.Transaction;

import java.util.List;
import java.util.Optional;

public class TransactionStreamResponseRepositoryImpl implements TransactionStreamResponseRepository{
    @Override
    public List<Transaction> findByName(String name) {
        return null;
    }

    @Override
    public <S extends Transaction> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Transaction> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Transaction> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<Transaction> findAll() {
        return null;
    }

    @Override
    public Iterable<Transaction> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Transaction entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends Transaction> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
