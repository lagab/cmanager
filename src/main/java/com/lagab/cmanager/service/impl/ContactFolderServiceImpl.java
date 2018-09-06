package com.lagab.cmanager.service.impl;

import com.lagab.cmanager.service.ContactFolderService;
import com.lagab.cmanager.domain.ContactFolder;
import com.lagab.cmanager.repository.ContactFolderRepository;
import com.lagab.cmanager.service.dto.ContactFolderDTO;
import com.lagab.cmanager.service.mapper.ContactFolderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing ContactFolder.
 */
@Service
@Transactional
public class ContactFolderServiceImpl implements ContactFolderService {

    private final Logger log = LoggerFactory.getLogger(ContactFolderServiceImpl.class);

    private final ContactFolderRepository contactFolderRepository;

    private final ContactFolderMapper contactFolderMapper;

    public ContactFolderServiceImpl(ContactFolderRepository contactFolderRepository, ContactFolderMapper contactFolderMapper) {
        this.contactFolderRepository = contactFolderRepository;
        this.contactFolderMapper = contactFolderMapper;
    }

    /**
     * Save a contactFolder.
     *
     * @param contactFolderDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ContactFolderDTO save(ContactFolderDTO contactFolderDTO) {
        log.debug("Request to save ContactFolder : {}", contactFolderDTO);
        ContactFolder contactFolder = contactFolderMapper.toEntity(contactFolderDTO);
        contactFolder = contactFolderRepository.save(contactFolder);
        return contactFolderMapper.toDto(contactFolder);
    }

    /**
     * Get all the contactFolders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ContactFolderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContactFolders");
        return contactFolderRepository.findAll(pageable)
            .map(contactFolderMapper::toDto);
    }


    /**
     * Get one contactFolder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ContactFolderDTO> findOne(Long id) {
        log.debug("Request to get ContactFolder : {}", id);
        return contactFolderRepository.findById(id)
            .map(contactFolderMapper::toDto);
    }

    /**
     * Delete the contactFolder by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContactFolder : {}", id);
        contactFolderRepository.deleteById(id);
    }
}
