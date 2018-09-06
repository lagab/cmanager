package com.lagab.cmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A SignatureRequest.
 */
@Entity
@Table(name = "signature_request")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SignatureRequest extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "requester_email", nullable = false)
    private String requesterEmail;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "subject")
    private String subject;

    @Column(name = "message")
    private String message;

    @Column(name = "cc_email")
    private String ccEmail;

    @OneToMany(mappedBy = "request")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Signature> signatures = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("requests")
    private Contract contract;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequesterEmail() {
        return requesterEmail;
    }

    public SignatureRequest requesterEmail(String requesterEmail) {
        this.requesterEmail = requesterEmail;
        return this;
    }

    public void setRequesterEmail(String requesterEmail) {
        this.requesterEmail = requesterEmail;
    }

    public String getTitle() {
        return title;
    }

    public SignatureRequest title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public SignatureRequest subject(String subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public SignatureRequest message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCcEmail() {
        return ccEmail;
    }

    public SignatureRequest ccEmail(String ccEmail) {
        this.ccEmail = ccEmail;
        return this;
    }

    public void setCcEmail(String ccEmail) {
        this.ccEmail = ccEmail;
    }

    public Set<Signature> getSignatures() {
        return signatures;
    }

    public SignatureRequest signatures(Set<Signature> signatures) {
        this.signatures = signatures;
        return this;
    }

    public SignatureRequest addSignatures(Signature signature) {
        this.signatures.add(signature);
        signature.setRequest(this);
        return this;
    }

    public SignatureRequest removeSignatures(Signature signature) {
        this.signatures.remove(signature);
        signature.setRequest(null);
        return this;
    }

    public void setSignatures(Set<Signature> signatures) {
        this.signatures = signatures;
    }

    public Contract getContract() {
        return contract;
    }

    public SignatureRequest contract(Contract contract) {
        this.contract = contract;
        return this;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
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
        SignatureRequest signatureRequest = (SignatureRequest) o;
        if (signatureRequest.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), signatureRequest.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SignatureRequest{" +
            "id=" + getId() +
            ", requesterEmail='" + getRequesterEmail() + "'" +
            ", title='" + getTitle() + "'" +
            ", subject='" + getSubject() + "'" +
            ", message='" + getMessage() + "'" +
            ", ccEmail='" + getCcEmail() + "'" +
            "}";
    }
}
