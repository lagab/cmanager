package com.lagab.cmanager.service.impl;

import com.lagab.cmanager.service.SignatureService;
import com.lagab.cmanager.domain.Signature;
import com.lagab.cmanager.repository.SignatureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Signature.
 */
@Service
@Transactional
public class SignatureServiceImpl implements SignatureService {

    private final Logger log = LoggerFactory.getLogger(SignatureServiceImpl.class);

    private final SignatureRepository signatureRepository;

    public SignatureServiceImpl(SignatureRepository signatureRepository) {
        this.signatureRepository = signatureRepository;
    }

    /**
     * Save a signature.
     *
     * @param signature the entity to save
     * @return the persisted entity
     */
    @Override
    public Signature save(Signature signature) {
        log.debug("Request to save Signature : {}", signature);        return signatureRepository.save(signature);
    }

    /**
     * Get all the signatures.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Signature> findAll(Pageable pageable) {
        log.debug("Request to get all Signatures");
        return signatureRepository.findAll(pageable);
    }


    /**
     * Get one signature by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Signature> findOne(Long id) {
        log.debug("Request to get Signature : {}", id);
        return signatureRepository.findById(id);
    }

    /**
     * Delete the signature by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Signature : {}", id);
        signatureRepository.deleteById(id);
    }
}
