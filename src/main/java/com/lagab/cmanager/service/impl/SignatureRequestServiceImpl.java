package com.lagab.cmanager.service.impl;

import com.lagab.cmanager.service.SignatureRequestService;
import com.lagab.cmanager.domain.SignatureRequest;
import com.lagab.cmanager.repository.SignatureRequestRepository;
import com.lagab.cmanager.service.dto.SignatureRequestDTO;
import com.lagab.cmanager.service.mapper.SignatureRequestMapper;
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

    private final SignatureRequestMapper signatureRequestMapper;

    public SignatureRequestServiceImpl(SignatureRequestRepository signatureRequestRepository, SignatureRequestMapper signatureRequestMapper) {
        this.signatureRequestRepository = signatureRequestRepository;
        this.signatureRequestMapper = signatureRequestMapper;
    }

    /**
     * Save a signatureRequest.
     *
     * @param signatureRequestDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SignatureRequestDTO save(SignatureRequestDTO signatureRequestDTO) {
        log.debug("Request to save SignatureRequest : {}", signatureRequestDTO);
        SignatureRequest signatureRequest = signatureRequestMapper.toEntity(signatureRequestDTO);
        signatureRequest = signatureRequestRepository.save(signatureRequest);
        return signatureRequestMapper.toDto(signatureRequest);
    }

    /**
     * Get all the signatureRequests.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SignatureRequestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SignatureRequests");
        return signatureRequestRepository.findAll(pageable)
            .map(signatureRequestMapper::toDto);
    }


    /**
     * Get one signatureRequest by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SignatureRequestDTO> findOne(Long id) {
        log.debug("Request to get SignatureRequest : {}", id);
        return signatureRequestRepository.findById(id)
            .map(signatureRequestMapper::toDto);
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
