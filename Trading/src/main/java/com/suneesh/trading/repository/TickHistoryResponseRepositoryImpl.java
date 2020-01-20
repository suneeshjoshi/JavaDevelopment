package com.suneesh.trading.repository;

import com.suneesh.trading.models.responses.TickHistoryResponse;

import java.util.List;
import java.util.Optional;

public class TickHistoryResponseRepositoryImpl implements TickHistoryResponseRepository{
    @Override
    public List<TickHistoryResponse> findByName(String name) {
        return null;
    }

    @Override
    public <S extends TickHistoryResponse> S save(S entity) {
        return null;
    }

    @Override
    public <S extends TickHistoryResponse> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<TickHistoryResponse> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<TickHistoryResponse> findAll() {
        return null;
    }

    @Override
    public Iterable<TickHistoryResponse> findAllById(Iterable<Long> longs) {
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
    public void delete(TickHistoryResponse entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends TickHistoryResponse> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
