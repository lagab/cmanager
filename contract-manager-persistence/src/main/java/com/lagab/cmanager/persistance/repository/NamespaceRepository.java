package com.lagab.cmanager.persistance.repository;

import com.lagab.cmanager.persistance.model.Namespace;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author gabriel
 * @since 13/07/2018.
 */
public interface NamespaceRepository extends CrudRepository<Namespace, Long>, JpaSpecificationExecutor<Namespace> {

   // List<Namespace> findByNamespaceId(long namespaceId);
    Namespace findByPath(String path);
}
