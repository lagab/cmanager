package com.lagab.cmanager.service;

import com.lagab.cmanager.domain.Attachment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Attachment.
 */
public interface AttachmentService {

    /**
     * Save a attachment.
     *
     * @param attachment the entity to save
     * @return the persisted entity
     */
    Attachment save(Attachment attachment);

    /**
     * Get all the attachments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Attachment> findAll(Pageable pageable);


    /**
     * Get the "id" attachment.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Attachment> findOne(Long id);

    /**
     * Delete the "id" attachment.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
