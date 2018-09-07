package com.lagab.cmanager.service.impl;

import com.lagab.cmanager.service.SignatureRequestService;
import com.lagab.cmanager.domain.SignatureRequest;
import com.lagab.cmanager.repository.SignatureRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing SignatureRequest.
 */
@Service
@Transactional
public class SignatureRequestServiceImpl implements SignatureRequestService {

    private final Logger log = LoggerFactory.getLogger(SignatureRequestServiceImpl.class);

    private final SignatureRequestRepository signatureRequestRepository;

    public SignatureRequestServiceImpl(SignatureRequestRepository signatureRequestRepository) {
        this.signatureRequestRepository = signatureRequestRepository;
    }

    /**
     * Save a signatureRequest.
     *
     * @param signatureRequest the entity to save
     * @return the persisted entity
     */
    @Override
    public SignatureRequest save(SignatureRequest signatureRequest) {
        log.debug("Request to save SignatureRequest : {}", signatureRequest);        return signatureRequestRepository.save(signatureRequest);
    }

    /**
     * Get all the signatureRequests.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SignatureRequest> findAll(Pageable pageable) {
        log.debug("Request to get all SignatureRequests");
        return signatureRequestRepository.findAll(pageable);
    }


    /**
     * Get one signatureRequest by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SignatureRequest> findOne(Long id) {
        log.debug("Request to get SignatureRequest : {}", id);
        return signatureRequestRepository.findById(id);
    }

    /**
     * Delete the signatureRequest by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SignatureRequest : {}", id);
        signatureRequestRepository.deleteById(id);
    }
}
