package com.lagab.cmanager.service;

import com.lagab.cmanager.service.dto.ContactListDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ContactList.
 */
public interface ContactListService {

    /**
     * Save a contactList.
     *
     * @param contactListDTO the entity to save
     * @return the persisted entity
     */
    ContactListDTO save(ContactListDTO contactListDTO);

    /**
     * Get all the contactLists.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ContactListDTO> findAll(Pageable pageable);


    /**
     * Get the "id" contactList.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ContactListDTO> findOne(Long id);

    /**
     * Delete the "id" contactList.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
