package com.lagab.cmanager.service;

import com.lagab.cmanager.domain.SignatureRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing SignatureRequest.
 */
public interface SignatureRequestService {

    /**
     * Save a signatureRequest.
     *
     * @param signatureRequest the entity to save
     * @return the persisted entity
     */
    SignatureRequest save(SignatureRequest signatureRequest);

    /**
     * Get all the signatureRequests.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SignatureRequest> findAll(Pageable pageable);


    /**
     * Get the "id" signatureRequest.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SignatureRequest> findOne(Long id);

    /**
     * Delete the "id" signatureRequest.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
