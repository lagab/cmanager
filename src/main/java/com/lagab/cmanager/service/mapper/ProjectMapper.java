package com.lagab.cmanager.service.mapper;

import com.lagab.cmanager.domain.*;
import com.lagab.cmanager.service.dto.ProjectDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Project and its DTO ProjectDTO.
 */
@Mapper(componentModel = "spring", uses = {WorkspaceMapper.class})
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {

    @Mapping(source = "workspace.id", target = "workspaceId")
    ProjectDTO toDto(Project project);

    @Mapping(target = "contracts", ignore = true)
    @Mapping(target = "contactFolders", ignore = true)
    @Mapping(source = "workspaceId", target = "workspace")
    Project toEntity(ProjectDTO projectDTO);

    default Project fromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }
}
