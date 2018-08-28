package com.lagab.cmanager.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author gabriel
 * @since 31/07/2018.
 */
public interface DefaultService<T, K extends Serializable> {

    long count();

    long count(Specification specification);

    K create(T entity);

    void update(K id, T entity);

    Iterable<T> findAll();
    Page<T> findAll(Specification specification, Pageable pageable);

    Optional<T> findOne(Specification specification);

    boolean existsById(K id);

    Optional<T> findById(K id);

    void delete(K id);

}

