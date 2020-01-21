package com.suneesh.trading.spring_stuff.repository;

import com.suneesh.trading.models.responses.Balance;

import java.util.List;
import java.util.Optional;

public class BalanceResponseRepositoryImpl implements BalanceResponseRepository {
    @Override
    public List<Balance> findByName(String name) {
        return null;
    }

    @Override
    public <S extends Balance> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Balance> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Balance> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<Balance> findAll() {
        return null;
    }

    @Override
    public Iterable<Balance> findAllById(Iterable<Long> longs) {
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
    public void delete(Balance entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends Balance> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
