package com.lagab.cmanager.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.lagab.cmanager.domain.enumeration.Status;

/**
 * A DTO for the Signature entity.
 */
public class SignatureDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private Integer order;

    private Status status;

    private String declineReason;

    @NotNull
    private LocalDate lastViewedAt;

    @NotNull
    private LocalDate lastRemindedAt;

    private Long requestId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDeclineReason() {
        return declineReason;
    }

    public void setDeclineReason(String declineReason) {
        this.declineReason = declineReason;
    }

    public LocalDate getLastViewedAt() {
        return lastViewedAt;
    }

    public void setLastViewedAt(LocalDate lastViewedAt) {
        this.lastViewedAt = lastViewedAt;
    }

    public LocalDate getLastRemindedAt() {
        return lastRemindedAt;
    }

    public void setLastRemindedAt(LocalDate lastRemindedAt) {
        this.lastRemindedAt = lastRemindedAt;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long signatureRequestId) {
        this.requestId = signatureRequestId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SignatureDTO signatureDTO = (SignatureDTO) o;
        if (signatureDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), signatureDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SignatureDTO{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", name='" + getName() + "'" +
            ", order=" + getOrder() +
            ", status='" + getStatus() + "'" +
            ", declineReason='" + getDeclineReason() + "'" +
            ", lastViewedAt='" + getLastViewedAt() + "'" +
            ", lastRemindedAt='" + getLastRemindedAt() + "'" +
            ", request=" + getRequestId() +
            "}";
    }
}
