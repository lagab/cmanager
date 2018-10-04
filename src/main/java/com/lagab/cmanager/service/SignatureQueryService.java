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

import com.lagab.cmanager.domain.Signature;
import com.lagab.cmanager.domain.*; // for static metamodels
import com.lagab.cmanager.repository.SignatureRepository;
import com.lagab.cmanager.service.dto.SignatureCriteria;


/**
 * Service for executing complex queries for Signature entities in the database.
 * The main input is a {@link SignatureCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Signature} or a {@link Page} of {@link Signature} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SignatureQueryService extends QueryService<Signature> {

    private final Logger log = LoggerFactory.getLogger(SignatureQueryService.class);

    private final SignatureRepository signatureRepository;

    public SignatureQueryService(SignatureRepository signatureRepository) {
        this.signatureRepository = signatureRepository;
    }

    /**
     * Return a {@link List} of {@link Signature} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Signature> findByCriteria(SignatureCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Signature> specification = createSpecification(criteria);
        return signatureRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Signature} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Signature> findByCriteria(SignatureCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Signature> specification = createSpecification(criteria);
        return signatureRepository.findAll(specification, page);
    }

    /**
     * Function to convert SignatureCriteria to a {@link Specification}
     */
    private Specification<Signature> createSpecification(SignatureCriteria criteria) {
        Specification<Signature> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Signature_.id));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Signature_.email));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Signature_.name));
            }
            if (criteria.getOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrder(), Signature_.order));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Signature_.status));
            }
            if (criteria.getDeclineReason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDeclineReason(), Signature_.declineReason));
            }
            if (criteria.getLastViewedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastViewedAt(), Signature_.lastViewedAt));
            }
            if (criteria.getLastRemindedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastRemindedAt(), Signature_.lastRemindedAt));
            }
            if (criteria.getRequestId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getRequestId(), Signature_.request, SignatureRequest_.id));
            }
        }
        return specification;
    }

}
