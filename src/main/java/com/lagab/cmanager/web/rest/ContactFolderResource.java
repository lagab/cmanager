package com.lagab.cmanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lagab.cmanager.service.ContactFolderService;
import com.lagab.cmanager.web.rest.errors.BadRequestAlertException;
import com.lagab.cmanager.web.rest.util.HeaderUtil;
import com.lagab.cmanager.web.rest.util.PaginationUtil;
import com.lagab.cmanager.service.dto.ContactFolderDTO;
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
 * REST controller for managing ContactFolder.
 */
@RestController
@RequestMapping("/api")
public class ContactFolderResource {

    private final Logger log = LoggerFactory.getLogger(ContactFolderResource.class);

    private static final String ENTITY_NAME = "contactFolder";

    private final ContactFolderService contactFolderService;

    public ContactFolderResource(ContactFolderService contactFolderService) {
        this.contactFolderService = contactFolderService;
    }

    /**
     * POST  /contact-folders : Create a new contactFolder.
     *
     * @param contactFolderDTO the contactFolderDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contactFolderDTO, or with status 400 (Bad Request) if the contactFolder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contact-folders")
    @Timed
    public ResponseEntity<ContactFolderDTO> createContactFolder(@Valid @RequestBody ContactFolderDTO contactFolderDTO) throws URISyntaxException {
        log.debug("REST request to save ContactFolder : {}", contactFolderDTO);
        if (contactFolderDTO.getId() != null) {
            throw new BadRequestAlertException("A new contactFolder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactFolderDTO result = contactFolderService.save(contactFolderDTO);
        return ResponseEntity.created(new URI("/api/contact-folders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contact-folders : Updates an existing contactFolder.
     *
     * @param contactFolderDTO the contactFolderDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contactFolderDTO,
     * or with status 400 (Bad Request) if the contactFolderDTO is not valid,
     * or with status 500 (Internal Server Error) if the contactFolderDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contact-folders")
    @Timed
    public ResponseEntity<ContactFolderDTO> updateContactFolder(@Valid @RequestBody ContactFolderDTO contactFolderDTO) throws URISyntaxException {
        log.debug("REST request to update ContactFolder : {}", contactFolderDTO);
        if (contactFolderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ContactFolderDTO result = contactFolderService.save(contactFolderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contactFolderDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contact-folders : get all the contactFolders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of contactFolders in body
     */
    @GetMapping("/contact-folders")
    @Timed
    public ResponseEntity<List<ContactFolderDTO>> getAllContactFolders(Pageable pageable) {
        log.debug("REST request to get a page of ContactFolders");
        Page<ContactFolderDTO> page = contactFolderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contact-folders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /contact-folders/:id : get the "id" contactFolder.
     *
     * @param id the id of the contactFolderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contactFolderDTO, or with status 404 (Not Found)
     */
    @GetMapping("/contact-folders/{id}")
    @Timed
    public ResponseEntity<ContactFolderDTO> getContactFolder(@PathVariable Long id) {
        log.debug("REST request to get ContactFolder : {}", id);
        Optional<ContactFolderDTO> contactFolderDTO = contactFolderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactFolderDTO);
    }

    /**
     * DELETE  /contact-folders/:id : delete the "id" contactFolder.
     *
     * @param id the id of the contactFolderDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contact-folders/{id}")
    @Timed
    public ResponseEntity<Void> deleteContactFolder(@PathVariable Long id) {
        log.debug("REST request to delete ContactFolder : {}", id);
        contactFolderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
