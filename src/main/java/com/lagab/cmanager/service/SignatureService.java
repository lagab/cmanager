package com.lagab.cmanager.service;

import com.lagab.cmanager.domain.Signature;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Signature.
 */
public interface SignatureService {

    /**
     * Save a signature.
     *
     * @param signature the entity to save
     * @return the persisted entity
     */
    Signature save(Signature signature);

    /**
     * Get all the signatures.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Signature> findAll(Pageable pageable);


    /**
     * Get the "id" signature.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Signature> findOne(Long id);

    /**
     * Delete the "id" signature.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
