package com.suneesh.trading.spring_stuff.repository;

import com.suneesh.trading.models.responses.Authorize;

import java.util.List;
import java.util.Optional;

public class AuthorizeResponseRepositoryImpl implements  AuthorizeResponseRepository {
    @Override
    public List<Authorize> findByName(String name) {
        return null;
    }

    @Override
    public <S extends Authorize> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Authorize> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Authorize> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<Authorize> findAll() {
        return null;
    }

    @Override
    public Iterable<Authorize> findAllById(Iterable<Long> longs) {
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
    public void delete(Authorize entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends Authorize> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
