package com.lagab.cmanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lagab.cmanager.service.SignatureRequestService;
import com.lagab.cmanager.web.rest.errors.BadRequestAlertException;
import com.lagab.cmanager.web.rest.util.HeaderUtil;
import com.lagab.cmanager.web.rest.util.PaginationUtil;
import com.lagab.cmanager.service.dto.SignatureRequestDTO;
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
 * REST controller for managing SignatureRequest.
 */
@RestController
@RequestMapping("/api")
public class SignatureRequestResource {

    private final Logger log = LoggerFactory.getLogger(SignatureRequestResource.class);

    private static final String ENTITY_NAME = "signatureRequest";

    private final SignatureRequestService signatureRequestService;

    public SignatureRequestResource(SignatureRequestService signatureRequestService) {
        this.signatureRequestService = signatureRequestService;
    }

    /**
     * POST  /signature-requests : Create a new signatureRequest.
     *
     * @param signatureRequestDTO the signatureRequestDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new signatureRequestDTO, or with status 400 (Bad Request) if the signatureRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/signature-requests")
    @Timed
    public ResponseEntity<SignatureRequestDTO> createSignatureRequest(@Valid @RequestBody SignatureRequestDTO signatureRequestDTO) throws URISyntaxException {
        log.debug("REST request to save SignatureRequest : {}", signatureRequestDTO);
        if (signatureRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new signatureRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SignatureRequestDTO result = signatureRequestService.save(signatureRequestDTO);
        return ResponseEntity.created(new URI("/api/signature-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /signature-requests : Updates an existing signatureRequest.
     *
     * @param signatureRequestDTO the signatureRequestDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated signatureRequestDTO,
     * or with status 400 (Bad Request) if the signatureRequestDTO is not valid,
     * or with status 500 (Internal Server Error) if the signatureRequestDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/signature-requests")
    @Timed
    public ResponseEntity<SignatureRequestDTO> updateSignatureRequest(@Valid @RequestBody SignatureRequestDTO signatureRequestDTO) throws URISyntaxException {
        log.debug("REST request to update SignatureRequest : {}", signatureRequestDTO);
        if (signatureRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SignatureRequestDTO result = signatureRequestService.save(signatureRequestDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, signatureRequestDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /signature-requests : get all the signatureRequests.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of signatureRequests in body
     */
    @GetMapping("/signature-requests")
    @Timed
    public ResponseEntity<List<SignatureRequestDTO>> getAllSignatureRequests(Pageable pageable) {
        log.debug("REST request to get a page of SignatureRequests");
        Page<SignatureRequestDTO> page = signatureRequestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/signature-requests");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /signature-requests/:id : get the "id" signatureRequest.
     *
     * @param id the id of the signatureRequestDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the signatureRequestDTO, or with status 404 (Not Found)
     */
    @GetMapping("/signature-requests/{id}")
    @Timed
    public ResponseEntity<SignatureRequestDTO> getSignatureRequest(@PathVariable Long id) {
        log.debug("REST request to get SignatureRequest : {}", id);
        Optional<SignatureRequestDTO> signatureRequestDTO = signatureRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(signatureRequestDTO);
    }

    /**
     * DELETE  /signature-requests/:id : delete the "id" signatureRequest.
     *
     * @param id the id of the signatureRequestDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/signature-requests/{id}")
    @Timed
    public ResponseEntity<Void> deleteSignatureRequest(@PathVariable Long id) {
        log.debug("REST request to delete SignatureRequest : {}", id);
        signatureRequestService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
