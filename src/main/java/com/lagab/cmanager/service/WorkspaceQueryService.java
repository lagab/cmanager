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

import com.lagab.cmanager.domain.Workspace;
import com.lagab.cmanager.domain.*; // for static metamodels
import com.lagab.cmanager.repository.WorkspaceRepository;
import com.lagab.cmanager.service.dto.WorkspaceCriteria;


/**
 * Service for executing complex queries for Workspace entities in the database.
 * The main input is a {@link WorkspaceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Workspace} or a {@link Page} of {@link Workspace} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WorkspaceQueryService extends QueryService<Workspace> {

    private final Logger log = LoggerFactory.getLogger(WorkspaceQueryService.class);

    private final WorkspaceRepository workspaceRepository;

    public WorkspaceQueryService(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    /**
     * Return a {@link List} of {@link Workspace} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Workspace> findByCriteria(WorkspaceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Workspace> specification = createSpecification(criteria);
        return workspaceRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Workspace} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Workspace> findByCriteria(WorkspaceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Workspace> specification = createSpecification(criteria);
        return workspaceRepository.findAll(specification, page);
    }

    /**
     * Function to convert WorkspaceCriteria to a {@link Specification}
     */
    private Specification<Workspace> createSpecification(WorkspaceCriteria criteria) {
        Specification<Workspace> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Workspace_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Workspace_.name));
            }
            if (criteria.getSlug() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlug(), Workspace_.slug));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Workspace_.description));
            }
            if (criteria.getAvatar() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAvatar(), Workspace_.avatar));
            }
            if (criteria.getRequestAcess() != null) {
                specification = specification.and(buildSpecification(criteria.getRequestAcess(), Workspace_.requestAcess));
            }
            if (criteria.getVisibility() != null) {
                specification = specification.and(buildSpecification(criteria.getVisibility(), Workspace_.visibility));
            }
            if (criteria.getProjectsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProjectsId(), Workspace_.projects, Project_.id));
            }
        }
        return specification;
    }

}
