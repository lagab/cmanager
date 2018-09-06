package com.lagab.cmanager.service.mapper;

import com.lagab.cmanager.domain.*;
import com.lagab.cmanager.service.dto.ContactListDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ContactList and its DTO ContactListDTO.
 */
@Mapper(componentModel = "spring", uses = {ContactFolderMapper.class})
public interface ContactListMapper extends EntityMapper<ContactListDTO, ContactList> {

    @Mapping(source = "folder.id", target = "folderId")
    ContactListDTO toDto(ContactList contactList);

    @Mapping(target = "contacts", ignore = true)
    @Mapping(source = "folderId", target = "folder")
    ContactList toEntity(ContactListDTO contactListDTO);

    default ContactList fromId(Long id) {
        if (id == null) {
            return null;
        }
        ContactList contactList = new ContactList();
        contactList.setId(id);
        return contactList;
    }
}
