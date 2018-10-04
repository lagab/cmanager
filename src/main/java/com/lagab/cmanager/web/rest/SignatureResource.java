package com.lagab.cmanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lagab.cmanager.domain.Signature;
import com.lagab.cmanager.service.SignatureService;
import com.lagab.cmanager.web.rest.errors.BadRequestAlertException;
import com.lagab.cmanager.web.rest.util.HeaderUtil;
import com.lagab.cmanager.web.rest.util.PaginationUtil;
import com.lagab.cmanager.service.dto.SignatureCriteria;
import com.lagab.cmanager.service.SignatureQueryService;
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
 * REST controller for managing Signature.
 */
@RestController
@RequestMapping("/api")
public class SignatureResource {

    private final Logger log = LoggerFactory.getLogger(SignatureResource.class);

    private static final String ENTITY_NAME = "signature";

    private final SignatureService signatureService;

    private final SignatureQueryService signatureQueryService;

    public SignatureResource(SignatureService signatureService, SignatureQueryService signatureQueryService) {
        this.signatureService = signatureService;
        this.signatureQueryService = signatureQueryService;
    }

    /**
     * POST  /signatures : Create a new signature.
     *
     * @param signature the signature to create
     * @return the ResponseEntity with status 201 (Created) and with body the new signature, or with status 400 (Bad Request) if the signature has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/signatures")
    @Timed
    public ResponseEntity<Signature> createSignature(@Valid @RequestBody Signature signature) throws URISyntaxException {
        log.debug("REST request to save Signature : {}", signature);
        if (signature.getId() != null) {
            throw new BadRequestAlertException("A new signature cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Signature result = signatureService.save(signature);
        return ResponseEntity.created(new URI("/api/signatures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /signatures : Updates an existing signature.
     *
     * @param signature the signature to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated signature,
     * or with status 400 (Bad Request) if the signature is not valid,
     * or with status 500 (Internal Server Error) if the signature couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/signatures")
    @Timed
    public ResponseEntity<Signature> updateSignature(@Valid @RequestBody Signature signature) throws URISyntaxException {
        log.debug("REST request to update Signature : {}", signature);
        if (signature.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Signature result = signatureService.save(signature);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, signature.getId().toString()))
            .body(result);
    }

    /**
     * GET  /signatures : get all the signatures.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of signatures in body
     */
    @GetMapping("/signatures")
    @Timed
    public ResponseEntity<List<Signature>> getAllSignatures(SignatureCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Signatures by criteria: {}", criteria);
        Page<Signature> page = signatureQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/signatures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /signatures/:id : get the "id" signature.
     *
     * @param id the id of the signature to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the signature, or with status 404 (Not Found)
     */
    @GetMapping("/signatures/{id}")
    @Timed
    public ResponseEntity<Signature> getSignature(@PathVariable Long id) {
        log.debug("REST request to get Signature : {}", id);
        Optional<Signature> signature = signatureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(signature);
    }

    /**
     * DELETE  /signatures/:id : delete the "id" signature.
     *
     * @param id the id of the signature to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/signatures/{id}")
    @Timed
    public ResponseEntity<Void> deleteSignature(@PathVariable Long id) {
        log.debug("REST request to delete Signature : {}", id);
        signatureService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
