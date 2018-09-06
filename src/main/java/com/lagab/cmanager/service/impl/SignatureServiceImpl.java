package com.lagab.cmanager.service.impl;

import com.lagab.cmanager.service.SignatureService;
import com.lagab.cmanager.domain.Signature;
import com.lagab.cmanager.repository.SignatureRepository;
import com.lagab.cmanager.service.dto.SignatureDTO;
import com.lagab.cmanager.service.mapper.SignatureMapper;
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

    private final SignatureMapper signatureMapper;

    public SignatureServiceImpl(SignatureRepository signatureRepository, SignatureMapper signatureMapper) {
        this.signatureRepository = signatureRepository;
        this.signatureMapper = signatureMapper;
    }

    /**
     * Save a signature.
     *
     * @param signatureDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SignatureDTO save(SignatureDTO signatureDTO) {
        log.debug("Request to save Signature : {}", signatureDTO);
        Signature signature = signatureMapper.toEntity(signatureDTO);
        signature = signatureRepository.save(signature);
        return signatureMapper.toDto(signature);
    }

    /**
     * Get all the signatures.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SignatureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Signatures");
        return signatureRepository.findAll(pageable)
            .map(signatureMapper::toDto);
    }


    /**
     * Get one signature by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SignatureDTO> findOne(Long id) {
        log.debug("Request to get Signature : {}", id);
        return signatureRepository.findById(id)
            .map(signatureMapper::toDto);
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
