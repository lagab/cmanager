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

import com.lagab.cmanager.domain.ContactFolder;
import com.lagab.cmanager.domain.*; // for static metamodels
import com.lagab.cmanager.repository.ContactFolderRepository;
import com.lagab.cmanager.service.dto.ContactFolderCriteria;


/**
 * Service for executing complex queries for ContactFolder entities in the database.
 * The main input is a {@link ContactFolderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContactFolder} or a {@link Page} of {@link ContactFolder} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContactFolderQueryService extends QueryService<ContactFolder> {

    private final Logger log = LoggerFactory.getLogger(ContactFolderQueryService.class);

    private final ContactFolderRepository contactFolderRepository;

    public ContactFolderQueryService(ContactFolderRepository contactFolderRepository) {
        this.contactFolderRepository = contactFolderRepository;
    }

    /**
     * Return a {@link List} of {@link ContactFolder} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContactFolder> findByCriteria(ContactFolderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ContactFolder> specification = createSpecification(criteria);
        return contactFolderRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ContactFolder} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactFolder> findByCriteria(ContactFolderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ContactFolder> specification = createSpecification(criteria);
        return contactFolderRepository.findAll(specification, page);
    }

    /**
     * Function to convert ContactFolderCriteria to a {@link Specification}
     */
    private Specification<ContactFolder> createSpecification(ContactFolderCriteria criteria) {
        Specification<ContactFolder> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ContactFolder_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ContactFolder_.name));
            }
            if (criteria.getListsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getListsId(), ContactFolder_.lists, ContactList_.id));
            }
            if (criteria.getProjectId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProjectId(), ContactFolder_.project, Project_.id));
            }
        }
        return specification;
    }

}
