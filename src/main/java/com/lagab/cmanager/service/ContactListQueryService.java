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

import com.lagab.cmanager.domain.ContactList;
import com.lagab.cmanager.domain.*; // for static metamodels
import com.lagab.cmanager.repository.ContactListRepository;
import com.lagab.cmanager.service.dto.ContactListCriteria;


/**
 * Service for executing complex queries for ContactList entities in the database.
 * The main input is a {@link ContactListCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContactList} or a {@link Page} of {@link ContactList} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContactListQueryService extends QueryService<ContactList> {

    private final Logger log = LoggerFactory.getLogger(ContactListQueryService.class);

    private final ContactListRepository contactListRepository;

    public ContactListQueryService(ContactListRepository contactListRepository) {
        this.contactListRepository = contactListRepository;
    }

    /**
     * Return a {@link List} of {@link ContactList} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContactList> findByCriteria(ContactListCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ContactList> specification = createSpecification(criteria);
        return contactListRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ContactList} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactList> findByCriteria(ContactListCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ContactList> specification = createSpecification(criteria);
        return contactListRepository.findAll(specification, page);
    }

    /**
     * Function to convert ContactListCriteria to a {@link Specification}
     */
    private Specification<ContactList> createSpecification(ContactListCriteria criteria) {
        Specification<ContactList> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ContactList_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ContactList_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ContactList_.description));
            }
            if (criteria.getContactsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getContactsId(), ContactList_.contacts, Contact_.id));
            }
            if (criteria.getFolderId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getFolderId(), ContactList_.folder, ContactFolder_.id));
            }
        }
        return specification;
    }

}
