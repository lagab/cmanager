package com.lagab.cmanager.service.mapper;

import com.lagab.cmanager.domain.*;
import com.lagab.cmanager.service.dto.ContactFolderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ContactFolder and its DTO ContactFolderDTO.
 */
@Mapper(componentModel = "spring", uses = {ProjectMapper.class})
public interface ContactFolderMapper extends EntityMapper<ContactFolderDTO, ContactFolder> {

    @Mapping(source = "project.id", target = "projectId")
    ContactFolderDTO toDto(ContactFolder contactFolder);

    @Mapping(target = "lists", ignore = true)
    @Mapping(source = "projectId", target = "project")
    ContactFolder toEntity(ContactFolderDTO contactFolderDTO);

    default ContactFolder fromId(Long id) {
        if (id == null) {
            return null;
        }
        ContactFolder contactFolder = new ContactFolder();
        contactFolder.setId(id);
        return contactFolder;
    }
}
