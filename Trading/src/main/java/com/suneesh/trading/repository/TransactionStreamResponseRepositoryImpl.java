package com.suneesh.trading.repository;

import com.suneesh.trading.models.responses.TransactionsStreamResponse;

import java.util.List;
import java.util.Optional;

public class TransactionStreamResponseRepositoryImpl implements TransactionStreamResponseRepository{
    @Override
    public List<TransactionsStreamResponse> findByName(String name) {
        return null;
    }

    @Override
    public <S extends TransactionsStreamResponse> S save(S entity) {
        return null;
    }

    @Override
    public <S extends TransactionsStreamResponse> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<TransactionsStreamResponse> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<TransactionsStreamResponse> findAll() {
        return null;
    }

    @Override
    public Iterable<TransactionsStreamResponse> findAllById(Iterable<Long> longs) {
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
    public void delete(TransactionsStreamResponse entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends TransactionsStreamResponse> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
