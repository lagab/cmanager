package com.lagab.cmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.lagab.cmanager.domain.enumeration.Status;

/**
 * A Signature.
 */
@Entity
@Table(name = "signature")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Signature extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "jhi_order", nullable = false)
    private Integer order;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "decline_reason")
    private String declineReason;

    @NotNull
    @Column(name = "last_viewed_at", nullable = false)
    private LocalDate lastViewedAt;

    @NotNull
    @Column(name = "last_reminded_at", nullable = false)
    private LocalDate lastRemindedAt;

    @ManyToOne
    @JsonIgnoreProperties("signatures")
    private SignatureRequest request;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public Signature email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public Signature name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public Signature order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Status getStatus() {
        return status;
    }

    public Signature status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDeclineReason() {
        return declineReason;
    }

    public Signature declineReason(String declineReason) {
        this.declineReason = declineReason;
        return this;
    }

    public void setDeclineReason(String declineReason) {
        this.declineReason = declineReason;
    }

    public LocalDate getLastViewedAt() {
        return lastViewedAt;
    }

    public Signature lastViewedAt(LocalDate lastViewedAt) {
        this.lastViewedAt = lastViewedAt;
        return this;
    }

    public void setLastViewedAt(LocalDate lastViewedAt) {
        this.lastViewedAt = lastViewedAt;
    }

    public LocalDate getLastRemindedAt() {
        return lastRemindedAt;
    }

    public Signature lastRemindedAt(LocalDate lastRemindedAt) {
        this.lastRemindedAt = lastRemindedAt;
        return this;
    }

    public void setLastRemindedAt(LocalDate lastRemindedAt) {
        this.lastRemindedAt = lastRemindedAt;
    }

    public SignatureRequest getRequest() {
        return request;
    }

    public Signature request(SignatureRequest signatureRequest) {
        this.request = signatureRequest;
        return this;
    }

    public void setRequest(SignatureRequest signatureRequest) {
        this.request = signatureRequest;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Signature signature = (Signature) o;
        if (signature.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), signature.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Signature{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", name='" + getName() + "'" +
            ", order=" + getOrder() +
            ", status='" + getStatus() + "'" +
            ", declineReason='" + getDeclineReason() + "'" +
            ", lastViewedAt='" + getLastViewedAt() + "'" +
            ", lastRemindedAt='" + getLastRemindedAt() + "'" +
            "}";
    }
}
