package com.suneesh.trading.repository;

import com.suneesh.trading.models.responses.AuthorizeResponse;

import java.util.List;
import java.util.Optional;

public class AuthorizeResponseRepositoryImpl implements  AuthorizeResponseRepository {
    @Override
    public List<AuthorizeResponse> findByName(String name) {
        return null;
    }

    @Override
    public <S extends AuthorizeResponse> S save(S entity) {
        return null;
    }

    @Override
    public <S extends AuthorizeResponse> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<AuthorizeResponse> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<AuthorizeResponse> findAll() {
        return null;
    }

    @Override
    public Iterable<AuthorizeResponse> findAllById(Iterable<Long> longs) {
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
    public void delete(AuthorizeResponse entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends AuthorizeResponse> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
