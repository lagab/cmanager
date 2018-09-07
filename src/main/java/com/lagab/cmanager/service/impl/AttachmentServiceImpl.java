package com.lagab.cmanager.service.impl;

import com.lagab.cmanager.service.AttachmentService;
import com.lagab.cmanager.domain.Attachment;
import com.lagab.cmanager.repository.AttachmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Attachment.
 */
@Service
@Transactional
public class AttachmentServiceImpl implements AttachmentService {

    private final Logger log = LoggerFactory.getLogger(AttachmentServiceImpl.class);

    private final AttachmentRepository attachmentRepository;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    /**
     * Save a attachment.
     *
     * @param attachment the entity to save
     * @return the persisted entity
     */
    @Override
    public Attachment save(Attachment attachment) {
        log.debug("Request to save Attachment : {}", attachment);        return attachmentRepository.save(attachment);
    }

    /**
     * Get all the attachments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Attachment> findAll(Pageable pageable) {
        log.debug("Request to get all Attachments");
        return attachmentRepository.findAll(pageable);
    }


    /**
     * Get one attachment by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Attachment> findOne(Long id) {
        log.debug("Request to get Attachment : {}", id);
        return attachmentRepository.findById(id);
    }

    /**
     * Delete the attachment by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Attachment : {}", id);
        attachmentRepository.deleteById(id);
    }
}
