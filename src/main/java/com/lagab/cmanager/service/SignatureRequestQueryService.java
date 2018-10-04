package com.lagab.cmanager.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.lagab.cmanager.domain.SignatureRequest;
import com.lagab.cmanager.domain.*; // for static metamodels
import com.lagab.cmanager.repository.SignatureRequestRepository;
import com.lagab.cmanager.service.dto.SignatureRequestCriteria;


/**
 * Service for executing complex queries for SignatureRequest entities in the database.
 * The main input is a {@link SignatureRequestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SignatureRequest} or a {@link Page} of {@link SignatureRequest} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SignatureRequestQueryService extends QueryService<SignatureRequest> {

    private final Logger log = LoggerFactory.getLogger(SignatureRequestQueryService.class);

    private final SignatureRequestRepository signatureRequestRepository;

    public SignatureRequestQueryService(SignatureRequestRepository signatureRequestRepository) {
        this.signatureRequestRepository = signatureRequestRepository;
    }

    /**
     * Return a {@link List} of {@link SignatureRequest} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SignatureRequest> findByCriteria(SignatureRequestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SignatureRequest> specification = createSpecification(criteria);
        return signatureRequestRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SignatureRequest} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SignatureRequest> findByCriteria(SignatureRequestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SignatureRequest> specification = createSpecification(criteria);
        return signatureRequestRepository.findAll(specification, page);
    }

    /**
     * Function to convert SignatureRequestCriteria to a {@link Specification}
     */
    private Specification<SignatureRequest> createSpecification(SignatureRequestCriteria criteria) {
        Specification<SignatureRequest> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SignatureRequest_.id));
            }
            if (criteria.getRequesterEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRequesterEmail(), SignatureRequest_.requesterEmail));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), SignatureRequest_.title));
            }
            if (criteria.getSubject() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubject(), SignatureRequest_.subject));
            }
            if (criteria.getMessage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMessage(), SignatureRequest_.message));
            }
            if (criteria.getCcEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCcEmail(), SignatureRequest_.ccEmail));
            }
            if (criteria.getSignaturesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSignaturesId(), SignatureRequest_.signatures, Signature_.id));
            }
            if (criteria.getContractId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getContractId(), SignatureRequest_.contract, Contract_.id));
            }
        }
        return specification;
    }

}
