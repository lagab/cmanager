package com.lagab.cmanager.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the SignatureRequest entity.
 */
public class SignatureRequestDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String requesterEmail;

    @NotNull
    private String title;

    private String subject;

    private String message;

    private String ccEmail;

    private Long contractId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequesterEmail() {
        return requesterEmail;
    }

    public void setRequesterEmail(String requesterEmail) {
        this.requesterEmail = requesterEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCcEmail() {
        return ccEmail;
    }

    public void setCcEmail(String ccEmail) {
        this.ccEmail = ccEmail;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SignatureRequestDTO signatureRequestDTO = (SignatureRequestDTO) o;
        if (signatureRequestDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), signatureRequestDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SignatureRequestDTO{" +
            "id=" + getId() +
            ", requesterEmail='" + getRequesterEmail() + "'" +
            ", title='" + getTitle() + "'" +
            ", subject='" + getSubject() + "'" +
            ", message='" + getMessage() + "'" +
            ", ccEmail='" + getCcEmail() + "'" +
            ", contract=" + getContractId() +
            "}";
    }
}
