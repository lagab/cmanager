package com.lagab.cmanager.service;

import com.lagab.cmanager.service.dto.ContactFolderDTO;

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
     * @param contactFolderDTO the entity to save
     * @return the persisted entity
     */
    ContactFolderDTO save(ContactFolderDTO contactFolderDTO);

    /**
     * Get all the contactFolders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ContactFolderDTO> findAll(Pageable pageable);


    /**
     * Get the "id" contactFolder.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ContactFolderDTO> findOne(Long id);

    /**
     * Delete the "id" contactFolder.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
