package com.lagab.cmanager.service;

import com.lagab.cmanager.domain.ContactFolder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ContactFolder.
 */
public interface ContactFolderService {

    /**
     * Save a contactFolder.
     *
     * @param contactFolder the entity to save
     * @return the persisted entity
     */
    ContactFolder save(ContactFolder contactFolder);

    /**
     * Get all the contactFolders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ContactFolder> findAll(Pageable pageable);


    /**
     * Get the "id" contactFolder.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ContactFolder> findOne(Long id);

    /**
     * Delete the "id" contactFolder.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
