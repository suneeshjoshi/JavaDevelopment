package com.suneesh.trading.repository;

import com.suneesh.trading.models.responses.BalanceResponse;

import java.util.List;
import java.util.Optional;

public class BalanceResponseRepositoryImpl implements BalanceResponseRepository {
    @Override
    public List<BalanceResponse> findByName(String name) {
        return null;
    }

    @Override
    public <S extends BalanceResponse> S save(S entity) {
        return null;
    }

    @Override
    public <S extends BalanceResponse> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<BalanceResponse> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<BalanceResponse> findAll() {
        return null;
    }

    @Override
    public Iterable<BalanceResponse> findAllById(Iterable<Long> longs) {
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
    public void delete(BalanceResponse entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends BalanceResponse> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
