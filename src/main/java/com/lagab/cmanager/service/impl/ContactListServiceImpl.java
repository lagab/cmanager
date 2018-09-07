package com.lagab.cmanager.service.impl;

import com.lagab.cmanager.service.ContactListService;
import com.lagab.cmanager.domain.ContactList;
import com.lagab.cmanager.repository.ContactListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing ContactList.
 */
@Service
@Transactional
public class ContactListServiceImpl implements ContactListService {

    private final Logger log = LoggerFactory.getLogger(ContactListServiceImpl.class);

    private final ContactListRepository contactListRepository;

    public ContactListServiceImpl(ContactListRepository contactListRepository) {
        this.contactListRepository = contactListRepository;
    }

    /**
     * Save a contactList.
     *
     * @param contactList the entity to save
     * @return the persisted entity
     */
    @Override
    public ContactList save(ContactList contactList) {
        log.debug("Request to save ContactList : {}", contactList);        return contactListRepository.save(contactList);
    }

    /**
     * Get all the contactLists.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ContactList> findAll(Pageable pageable) {
        log.debug("Request to get all ContactLists");
        return contactListRepository.findAll(pageable);
    }


    /**
     * Get one contactList by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ContactList> findOne(Long id) {
        log.debug("Request to get ContactList : {}", id);
        return contactListRepository.findById(id);
    }

    /**
     * Delete the contactList by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContactList : {}", id);
        contactListRepository.deleteById(id);
    }
}
