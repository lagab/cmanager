package com.lagab.cmanager.ws.controller;

import com.lagab.cmanager.services.DefaultService;
import com.lagab.cmanager.services.exception.NotFoundException;
import org.springframework.core.GenericTypeResolver;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.URI;

/**
 * @author gabriel
 * @since 25/07/2018.
 */
public abstract  class AbstractCrudController<T, K extends Serializable> implements CrudRestController<T, K > {

    /**
     *
     */
    private Type entityType;

    /**
     *
     */
    private DefaultService<T, K> service;

    /**
     * Constructor.
     * @param service the crud service.
     */
    public AbstractCrudController(DefaultService<T, K> service) {
        this.entityType = GenericTypeResolver.resolveType(getClass(), AbstractCrudController.class);
        this.service = service;
    }

    /**
     *
     * @param entity
     * @param validator
     */
    /*private void check(T entity, Validator<T> validator) {
        Errors errors = new Errors();
        if (validator != null && !validator.validate(entity, errors)) {
            throw new BadRequestException(errors);
        }
    }*/

    /**
     *
     * @param specification
     * @param paging
     * @param defaultSorting
     * @return
     */
    /*public Page<Resource<T>> findAll(Specification specification, Paging paging, String defaultSorting) {
        if (paging.getSize() <= 0) {
            paging.setSize((int) service.count());
        }
        if (Strings.isNullOrEmpty(paging.getSortBy())) {
            paging.setSortBy(defaultSorting == null ? "id" : defaultSorting);
        }
        return new PageResource<>(service.findAll(specification, PageRequestUtil.of(paging))).map(this::asResource);
    }*/

    /**
     *
     * @param id
     * @return
     */
    public T findById(K id) {
        return service.findById(id).orElseThrow(() -> new NotFoundException(entityType.getClass(), id));
    }



    /**
     *
     * @param entity
     * @return
     */
    public ResponseEntity<K> checkAndCreate(T entity) {
        K id = service.create(entity);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/" + id).build().toUri();
        return ResponseEntity.created(uri).build();
    }

    /**
     *
     * @param id
     * @param entity
     * @return
     */
    public ResponseEntity<Void> checkAndUpdate(K id, T entity) {
        service.update(id, entity);
        return ResponseEntity.noContent().build();
    }

    /**
     *
     * @param content
     * @return
     */
    public Resource<T> asResource(T content) {
        return new Resource<>(content);
    }


    /**
     *
     * @param id
     * @return
     */
    public ResponseEntity<Void> delete(K id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


}
