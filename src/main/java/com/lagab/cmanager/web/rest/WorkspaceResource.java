package com.lagab.cmanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lagab.cmanager.service.WorkspaceService;
import com.lagab.cmanager.web.rest.errors.BadRequestAlertException;
import com.lagab.cmanager.web.rest.util.HeaderUtil;
import com.lagab.cmanager.web.rest.util.PaginationUtil;
import com.lagab.cmanager.service.dto.WorkspaceDTO;
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
 * REST controller for managing Workspace.
 */
@RestController
@RequestMapping("/api")
public class WorkspaceResource {

    private final Logger log = LoggerFactory.getLogger(WorkspaceResource.class);

    private static final String ENTITY_NAME = "workspace";

    private final WorkspaceService workspaceService;

    public WorkspaceResource(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    /**
     * POST  /workspaces : Create a new workspace.
     *
     * @param workspaceDTO the workspaceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workspaceDTO, or with status 400 (Bad Request) if the workspace has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/workspaces")
    @Timed
    public ResponseEntity<WorkspaceDTO> createWorkspace(@Valid @RequestBody WorkspaceDTO workspaceDTO) throws URISyntaxException {
        log.debug("REST request to save Workspace : {}", workspaceDTO);
        if (workspaceDTO.getId() != null) {
            throw new BadRequestAlertException("A new workspace cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkspaceDTO result = workspaceService.save(workspaceDTO);
        return ResponseEntity.created(new URI("/api/workspaces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /workspaces : Updates an existing workspace.
     *
     * @param workspaceDTO the workspaceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workspaceDTO,
     * or with status 400 (Bad Request) if the workspaceDTO is not valid,
     * or with status 500 (Internal Server Error) if the workspaceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/workspaces")
    @Timed
    public ResponseEntity<WorkspaceDTO> updateWorkspace(@Valid @RequestBody WorkspaceDTO workspaceDTO) throws URISyntaxException {
        log.debug("REST request to update Workspace : {}", workspaceDTO);
        if (workspaceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WorkspaceDTO result = workspaceService.save(workspaceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, workspaceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /workspaces : get all the workspaces.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of workspaces in body
     */
    @GetMapping("/workspaces")
    @Timed
    public ResponseEntity<List<WorkspaceDTO>> getAllWorkspaces(Pageable pageable) {
        log.debug("REST request to get a page of Workspaces");
        Page<WorkspaceDTO> page = workspaceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/workspaces");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /workspaces/:id : get the "id" workspace.
     *
     * @param id the id of the workspaceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workspaceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/workspaces/{id}")
    @Timed
    public ResponseEntity<WorkspaceDTO> getWorkspace(@PathVariable Long id) {
        log.debug("REST request to get Workspace : {}", id);
        Optional<WorkspaceDTO> workspaceDTO = workspaceService.findOne(id);
        log.debug(workspaceDTO.toString());
        return ResponseUtil.wrapOrNotFound(workspaceDTO);
    }

    /**
     * DELETE  /workspaces/:id : delete the "id" workspace.
     *
     * @param id the id of the workspaceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/workspaces/{id}")
    @Timed
    public ResponseEntity<Void> deleteWorkspace(@PathVariable Long id) {
        log.debug("REST request to delete Workspace : {}", id);
        workspaceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
