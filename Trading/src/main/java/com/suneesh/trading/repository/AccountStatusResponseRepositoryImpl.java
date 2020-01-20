package com.suneesh.trading.repository;

import com.suneesh.trading.models.responses.AccountStatusResponse;

import java.util.List;
import java.util.Optional;

public class AccountStatusResponseRepositoryImpl implements AccountStatusResponseRepository {
    @Override
    public List<AccountStatusResponse> findByName(String name) {
        return null;
    }

    @Override
    public <S extends AccountStatusResponse> S save(S entity) {
        return null;
    }

    @Override
    public <S extends AccountStatusResponse> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<AccountStatusResponse> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<AccountStatusResponse> findAll() {
        return null;
    }

    @Override
    public Iterable<AccountStatusResponse> findAllById(Iterable<Long> longs) {
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
    public void delete(AccountStatusResponse entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends AccountStatusResponse> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
