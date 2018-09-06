package com.lagab.cmanager.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ContactFolder entity.
 */
public class ContactFolderDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Long projectId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContactFolderDTO contactFolderDTO = (ContactFolderDTO) o;
        if (contactFolderDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contactFolderDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ContactFolderDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", project=" + getProjectId() +
            "}";
    }
}
