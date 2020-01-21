package com.suneesh.trading.spring_stuff.repository;

import com.suneesh.trading.models.responses.Candle;

import java.util.List;
import java.util.Optional;

public class TickHistoryResponseRepositoryImpl implements TickHistoryResponseRepository{
    @Override
    public List<Candle> findByName(String name) {
        return null;
    }

    @Override
    public <S extends Candle> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Candle> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Candle> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<Candle> findAll() {
        return null;
    }

    @Override
    public Iterable<Candle> findAllById(Iterable<Long> longs) {
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
    public void delete(Candle entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends Candle> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
