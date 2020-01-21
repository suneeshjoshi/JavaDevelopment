package com.suneesh.trading.spring_stuff.repository;

import com.suneesh.trading.models.responses.Tick;

import java.util.List;
import java.util.Optional;

public class TickRepositoryImpl implements  TickRepository{
    @Override
    public List<Tick> findByName(String name) {
        return null;
    }

    @Override
    public <S extends Tick> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Tick> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Tick> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<Tick> findAll() {
        return null;
    }

    @Override
    public Iterable<Tick> findAllById(Iterable<Long> longs) {
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
    public void delete(Tick entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends Tick> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
