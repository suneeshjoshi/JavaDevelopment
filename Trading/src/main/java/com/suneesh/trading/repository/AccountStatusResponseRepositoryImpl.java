package com.suneesh.trading.repository;

import com.suneesh.trading.models.responses.AccountStatus;
import com.suneesh.trading.models.responses.AccountStatusResponse;

import java.util.List;
import java.util.Optional;

public class AccountStatusResponseRepositoryImpl implements AccountStatusResponseRepository {
    @Override
    public List<AccountStatus> findByName(String name) {
        return null;
    }

    @Override
    public <S extends AccountStatus> S save(S entity) {
        return null;
    }

    @Override
    public <S extends AccountStatus> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<AccountStatus> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<AccountStatus> findAll() {
        return null;
    }

    @Override
    public Iterable<AccountStatus> findAllById(Iterable<Long> longs) {
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
    public void delete(AccountStatus entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends AccountStatus> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
