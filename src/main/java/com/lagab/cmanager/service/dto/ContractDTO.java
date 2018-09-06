package com.lagab.cmanager.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.lagab.cmanager.domain.enumeration.Status;

/**
 * A DTO for the Contract entity.
 */
public class ContractDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String uuid;

    @NotNull
    private String name;

    @NotNull
    private String subject;

    private Status status;

    private String description;

    private String content;

    private Boolean requestAcess;

    @NotNull
    private LocalDate lastActivityAt;

    private LocalDate expiresAt;

    private String type;

    private Long projectId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean isRequestAcess() {
        return requestAcess;
    }

    public void setRequestAcess(Boolean requestAcess) {
        this.requestAcess = requestAcess;
    }

    public LocalDate getLastActivityAt() {
        return lastActivityAt;
    }

    public void setLastActivityAt(LocalDate lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }

    public LocalDate getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDate expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

        ContractDTO contractDTO = (ContractDTO) o;
        if (contractDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contractDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ContractDTO{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", name='" + getName() + "'" +
            ", subject='" + getSubject() + "'" +
            ", status='" + getStatus() + "'" +
            ", description='" + getDescription() + "'" +
            ", content='" + getContent() + "'" +
            ", requestAcess='" + isRequestAcess() + "'" +
            ", lastActivityAt='" + getLastActivityAt() + "'" +
            ", expiresAt='" + getExpiresAt() + "'" +
            ", type='" + getType() + "'" +
            ", project=" + getProjectId() +
            "}";
    }
}
