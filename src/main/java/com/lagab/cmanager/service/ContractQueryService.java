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

import com.lagab.cmanager.domain.Contract;
import com.lagab.cmanager.domain.*; // for static metamodels
import com.lagab.cmanager.repository.ContractRepository;
import com.lagab.cmanager.service.dto.ContractCriteria;


/**
 * Service for executing complex queries for Contract entities in the database.
 * The main input is a {@link ContractCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Contract} or a {@link Page} of {@link Contract} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContractQueryService extends QueryService<Contract> {

    private final Logger log = LoggerFactory.getLogger(ContractQueryService.class);

    private final ContractRepository contractRepository;

    public ContractQueryService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    /**
     * Return a {@link List} of {@link Contract} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Contract> findByCriteria(ContractCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Contract> specification = createSpecification(criteria);
        return contractRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Contract} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Contract> findByCriteria(ContractCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Contract> specification = createSpecification(criteria);
        return contractRepository.findAll(specification, page);
    }

    /**
     * Function to convert ContractCriteria to a {@link Specification}
     */
    private Specification<Contract> createSpecification(ContractCriteria criteria) {
        Specification<Contract> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Contract_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUuid(), Contract_.uuid));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Contract_.name));
            }
            if (criteria.getSubject() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubject(), Contract_.subject));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Contract_.status));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Contract_.description));
            }
            if (criteria.getContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContent(), Contract_.content));
            }
            if (criteria.getRequestAcess() != null) {
                specification = specification.and(buildSpecification(criteria.getRequestAcess(), Contract_.requestAcess));
            }
            if (criteria.getLastActivityAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastActivityAt(), Contract_.lastActivityAt));
            }
            if (criteria.getExpiresAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpiresAt(), Contract_.expiresAt));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Contract_.type));
            }
            if (criteria.getRequestsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getRequestsId(), Contract_.requests, SignatureRequest_.id));
            }
            if (criteria.getProjectId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProjectId(), Contract_.project, Project_.id));
            }
        }
        return specification;
    }

}
