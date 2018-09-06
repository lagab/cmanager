package com.lagab.cmanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lagab.cmanager.service.ContactListService;
import com.lagab.cmanager.web.rest.errors.BadRequestAlertException;
import com.lagab.cmanager.web.rest.util.HeaderUtil;
import com.lagab.cmanager.web.rest.util.PaginationUtil;
import com.lagab.cmanager.service.dto.ContactListDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ContactList.
 */
@RestController
@RequestMapping("/api")
public class ContactListResource {

    private final Logger log = LoggerFactory.getLogger(ContactListResource.class);

    private static final String ENTITY_NAME = "contactList";

    private final ContactListService contactListService;

    public ContactListResource(ContactListService contactListService) {
        this.contactListService = contactListService;
    }

    /**
     * POST  /contact-lists : Create a new contactList.
     *
     * @param contactListDTO the contactListDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contactListDTO, or with status 400 (Bad Request) if the contactList has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contact-lists")
    @Timed
    public ResponseEntity<ContactListDTO> createContactList(@Valid @RequestBody ContactListDTO contactListDTO) throws URISyntaxException {
        log.debug("REST request to save ContactList : {}", contactListDTO);
        if (contactListDTO.getId() != null) {
            throw new BadRequestAlertException("A new contactList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactListDTO result = contactListService.save(contactListDTO);
        return ResponseEntity.created(new URI("/api/contact-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contact-lists : Updates an existing contactList.
     *
     * @param contactListDTO the contactListDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contactListDTO,
     * or with status 400 (Bad Request) if the contactListDTO is not valid,
     * or with status 500 (Internal Server Error) if the contactListDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contact-lists")
    @Timed
    public ResponseEntity<ContactListDTO> updateContactList(@Valid @RequestBody ContactListDTO contactListDTO) throws URISyntaxException {
        log.debug("REST request to update ContactList : {}", contactListDTO);
        if (contactListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ContactListDTO result = contactListService.save(contactListDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contactListDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contact-lists : get all the contactLists.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of contactLists in body
     */
    @GetMapping("/contact-lists")
    @Timed
    public ResponseEntity<List<ContactListDTO>> getAllContactLists(Pageable pageable) {
        log.debug("REST request to get a page of ContactLists");
        Page<ContactListDTO> page = contactListService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contact-lists");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /contact-lists/:id : get the "id" contactList.
     *
     * @param id the id of the contactListDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contactListDTO, or with status 404 (Not Found)
     */
    @GetMapping("/contact-lists/{id}")
    @Timed
    public ResponseEntity<ContactListDTO> getContactList(@PathVariable Long id) {
        log.debug("REST request to get ContactList : {}", id);
        Optional<ContactListDTO> contactListDTO = contactListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactListDTO);
    }

    /**
     * DELETE  /contact-lists/:id : delete the "id" contactList.
     *
     * @param id the id of the contactListDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contact-lists/{id}")
    @Timed
    public ResponseEntity<Void> deleteContactList(@PathVariable Long id) {
        log.debug("REST request to delete ContactList : {}", id);
        contactListService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
