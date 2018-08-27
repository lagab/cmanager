package com.lagab.cmanager.persistance.resolver;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

/**
 * @author gabriel
 * @since 30/07/2018.
 */
/**
 * Resolve {@link com.fasterxml.jackson.annotation.JsonIdentityInfo} with {@link EntityManager}.
 *
 * Objects are resolved by their name.
 */
@Component
@Scope("prototype")
public class EntityNameResolver extends SimpleObjectIdResolver{

    private EntityManager entityManager;

    public EntityNameResolver(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Object resolveId(ObjectIdGenerator.IdKey idKey) {
        Object resolved = super.resolveId(idKey);
        if (resolved == null) {
            resolved = entityManager.createQuery("SELECT o FROM " + idKey.scope.getName() + " o WHERE o.name = '" + idKey.key + "'", idKey.scope).getSingleResult();
            bindItem(idKey, resolved);
        }
        return resolved;
    }

    @Override
    public ObjectIdResolver newForDeserialization(Object o) {
        return new EntityNameResolver(entityManager);
    }

}
