package com.lagab.cmanager.service.util;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

/**
 * @author gabriel
 * @since 03/10/2018.
 * Resolve {@link com.fasterxml.jackson.annotation.JsonIdentityInfo} with {@link EntityManager}.
 *
 * Objects are resolved by their id.
 */
@Component
@Scope("prototype")
public class EntityIdResolver extends SimpleObjectIdResolver{
    private EntityManager entityManager;

    public EntityIdResolver(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Object resolveId(ObjectIdGenerator.IdKey idKey) {
        Object resolved = super.resolveId(idKey);
        if (resolved == null) {
            resolved = entityManager.find(idKey.scope, idKey.key);
            bindItem(idKey, resolved);
        }
        return resolved;
    }

    @Override
    public ObjectIdResolver newForDeserialization(Object o) {
        return new EntityIdResolver(entityManager);
    }
}
