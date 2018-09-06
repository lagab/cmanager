package com.lagab.cmanager.service.impl;

import com.lagab.cmanager.service.WorkspaceService;
import com.lagab.cmanager.domain.Workspace;
import com.lagab.cmanager.repository.WorkspaceRepository;
import com.lagab.cmanager.service.dto.WorkspaceDTO;
import com.lagab.cmanager.service.mapper.WorkspaceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Workspace.
 */
@Service
@Transactional
public class WorkspaceServiceImpl implements WorkspaceService {

    private final Logger log = LoggerFactory.getLogger(WorkspaceServiceImpl.class);

    private final WorkspaceRepository workspaceRepository;

    private final WorkspaceMapper workspaceMapper;

    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository, WorkspaceMapper workspaceMapper) {
        this.workspaceRepository = workspaceRepository;
        this.workspaceMapper = workspaceMapper;
    }

    /**
     * Save a workspace.
     *
     * @param workspaceDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public WorkspaceDTO save(WorkspaceDTO workspaceDTO) {
        log.debug("Request to save Workspace : {}", workspaceDTO);
        Workspace workspace = workspaceMapper.toEntity(workspaceDTO);
        workspace = workspaceRepository.save(workspace);
        return workspaceMapper.toDto(workspace);
    }

    /**
     * Get all the workspaces.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WorkspaceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Workspaces");
        return workspaceRepository.findAll(pageable)
            .map(workspaceMapper::toDto);
    }


    /**
     * Get one workspace by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<WorkspaceDTO> findOne(Long id) {
        log.debug("Request to get Workspace : {}", id);
        return workspaceRepository.findById(id)
            .map(workspaceMapper::toDto);
    }

    /**
     * Delete the workspace by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Workspace : {}", id);
        workspaceRepository.deleteById(id);
    }
}
