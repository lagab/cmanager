package com.lagab.cmanager.service;

import com.lagab.cmanager.domain.ContactList;

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
     * @param contactList the entity to save
     * @return the persisted entity
     */
    ContactList save(ContactList contactList);

    /**
     * Get all the contactLists.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ContactList> findAll(Pageable pageable);


    /**
     * Get the "id" contactList.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ContactList> findOne(Long id);

    /**
     * Delete the "id" contactList.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
