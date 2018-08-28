package com.lagab.cmanager.persistance.repository;

import com.lagab.cmanager.persistance.model.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


/**
 * @author gabriel
 * @since 13/07/2018.
 */
public interface UserRepository extends CrudRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsername(String username);
}
