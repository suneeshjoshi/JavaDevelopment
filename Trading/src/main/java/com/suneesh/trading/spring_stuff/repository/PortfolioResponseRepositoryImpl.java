package com.suneesh.trading.spring_stuff.repository;

import com.suneesh.trading.models.responses.PortfolioTransaction;

import java.util.List;
import java.util.Optional;

public class PortfolioResponseRepositoryImpl implements PortfolioResponseRepository {
    @Override
    public List<PortfolioTransaction> findByName(String name) {
        return null;
    }

    @Override
    public <S extends PortfolioTransaction> S save(S entity) {
        return null;
    }

    @Override
    public <S extends PortfolioTransaction> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<PortfolioTransaction> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<PortfolioTransaction> findAll() {
        return null;
    }

    @Override
    public Iterable<PortfolioTransaction> findAllById(Iterable<Long> longs) {
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
    public void delete(PortfolioTransaction entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends PortfolioTransaction> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
