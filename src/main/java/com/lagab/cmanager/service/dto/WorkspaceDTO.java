package com.lagab.cmanager.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.lagab.cmanager.domain.enumeration.Visibility;

/**
 * A DTO for the Workspace entity.
 */
public class WorkspaceDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String slug;

    private String description;

    private String avatar;

    private Boolean requestAcess;

    private Visibility visibility;

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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Boolean isRequestAcess() {
        return requestAcess;
    }

    public void setRequestAcess(Boolean requestAcess) {
        this.requestAcess = requestAcess;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WorkspaceDTO workspaceDTO = (WorkspaceDTO) o;
        if (workspaceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), workspaceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WorkspaceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", slug='" + getSlug() + "'" +
            ", description='" + getDescription() + "'" +
            ", avatar='" + getAvatar() + "'" +
            ", requestAcess='" + isRequestAcess() + "'" +
            ", visibility='" + getVisibility() + "'" +
            "}";
    }
}
