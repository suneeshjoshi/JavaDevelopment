package com.suneesh.trading.repository;

import com.suneesh.trading.models.responses.PortfolioResponse;

import java.util.List;
import java.util.Optional;

public class PortfolioResponseRepositoryImpl implements PortfolioResponseRepository {
    @Override
    public List<PortfolioResponse> findByName(String name) {
        return null;
    }

    @Override
    public <S extends PortfolioResponse> S save(S entity) {
        return null;
    }

    @Override
    public <S extends PortfolioResponse> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<PortfolioResponse> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<PortfolioResponse> findAll() {
        return null;
    }

    @Override
    public Iterable<PortfolioResponse> findAllById(Iterable<Long> longs) {
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
    public void delete(PortfolioResponse entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends PortfolioResponse> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
