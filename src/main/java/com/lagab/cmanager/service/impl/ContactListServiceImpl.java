package com.lagab.cmanager.service.impl;

import com.lagab.cmanager.service.ContactListService;
import com.lagab.cmanager.domain.ContactList;
import com.lagab.cmanager.repository.ContactListRepository;
import com.lagab.cmanager.service.dto.ContactListDTO;
import com.lagab.cmanager.service.mapper.ContactListMapper;
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

    private final ContactListMapper contactListMapper;

    public ContactListServiceImpl(ContactListRepository contactListRepository, ContactListMapper contactListMapper) {
        this.contactListRepository = contactListRepository;
        this.contactListMapper = contactListMapper;
    }

    /**
     * Save a contactList.
     *
     * @param contactListDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ContactListDTO save(ContactListDTO contactListDTO) {
        log.debug("Request to save ContactList : {}", contactListDTO);
        ContactList contactList = contactListMapper.toEntity(contactListDTO);
        contactList = contactListRepository.save(contactList);
        return contactListMapper.toDto(contactList);
    }

    /**
     * Get all the contactLists.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ContactListDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContactLists");
        return contactListRepository.findAll(pageable)
            .map(contactListMapper::toDto);
    }


    /**
     * Get one contactList by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ContactListDTO> findOne(Long id) {
        log.debug("Request to get ContactList : {}", id);
        return contactListRepository.findById(id)
            .map(contactListMapper::toDto);
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
