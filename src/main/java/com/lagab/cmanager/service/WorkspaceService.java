package com.lagab.cmanager.service;

import com.lagab.cmanager.service.dto.WorkspaceDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Workspace.
 */
public interface WorkspaceService {

    /**
     * Save a workspace.
     *
     * @param workspaceDTO the entity to save
     * @return the persisted entity
     */
    WorkspaceDTO save(WorkspaceDTO workspaceDTO);

    /**
     * Get all the workspaces.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<WorkspaceDTO> findAll(Pageable pageable);


    /**
     * Get the "id" workspace.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<WorkspaceDTO> findOne(Long id);

    /**
     * Delete the "id" workspace.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
