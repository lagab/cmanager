package com.lagab.cmanager.ws.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

/**
 * @author gabriel
 * @since 31/07/2018.
 */
public interface CrudRestController<T, K extends Serializable> {

    /**
     *
     * @param entity
     * @return
     */
    ResponseEntity<K> create(T entity);

    /**
     *
     * @param id
     * @param entity
     * @return
     */
    ResponseEntity<Void> update(K id, T entity);

    /**
     *
     * @param id
     * @return
     */
    Resource<T> get(K id);

    /**
     *
     * @param specification
     * @param paging
     * @return
     */
   // Page<Resource<T>> getAll(Specification specification, Paging paging);


    /**
     *
     * @param id
     * @return
     */
    ResponseEntity<Void> delete(K id);
}
