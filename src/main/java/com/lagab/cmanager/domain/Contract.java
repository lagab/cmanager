package com.lagab.cmanager.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.lagab.cmanager.service.util.EntityIdResolver;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.lagab.cmanager.domain.enumeration.Status;

/**
 * A Contract.
 */
@Entity
@Table(name = "contract")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Contract extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "uuid", nullable = false)
    private String uuid;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "subject", nullable = false)
    private String subject;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "description")
    private String description;

    @Column(name = "content")
    private String content;

    @Column(name = "request_acess")
    private Boolean requestAcess;

    @NotNull
    @Column(name = "last_activity_at", nullable = false)
    private LocalDate lastActivityAt;

    @Column(name = "expires_at")
    private LocalDate expiresAt;

    @Column(name = "jhi_type")
    private String type;

    @OneToMany(mappedBy = "contract")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SignatureRequest> requests = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("contracts")
    @JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        resolver = EntityIdResolver.class,
        scope = Project.class
    )
    private Project project;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public Contract uuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public Contract name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public Contract subject(String subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Status getStatus() {
        return status;
    }

    public Contract status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public Contract description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public Contract content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean isRequestAcess() {
        return requestAcess;
    }

    public Contract requestAcess(Boolean requestAcess) {
        this.requestAcess = requestAcess;
        return this;
    }

    public void setRequestAcess(Boolean requestAcess) {
        this.requestAcess = requestAcess;
    }

    public LocalDate getLastActivityAt() {
        return lastActivityAt;
    }

    public Contract lastActivityAt(LocalDate lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
        return this;
    }

    public void setLastActivityAt(LocalDate lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }

    public LocalDate getExpiresAt() {
        return expiresAt;
    }

    public Contract expiresAt(LocalDate expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    public void setExpiresAt(LocalDate expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getType() {
        return type;
    }

    public Contract type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<SignatureRequest> getRequests() {
        return requests;
    }

    public Contract requests(Set<SignatureRequest> signatureRequests) {
        this.requests = signatureRequests;
        return this;
    }

    public Contract addRequests(SignatureRequest signatureRequest) {
        this.requests.add(signatureRequest);
        signatureRequest.setContract(this);
        return this;
    }

    public Contract removeRequests(SignatureRequest signatureRequest) {
        this.requests.remove(signatureRequest);
        signatureRequest.setContract(null);
        return this;
    }

    public void setRequests(Set<SignatureRequest> signatureRequests) {
        this.requests = signatureRequests;
    }

    public Project getProject() {
        return project;
    }

    public Contract project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
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
        Contract contract = (Contract) o;
        if (contract.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contract.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Contract{" +
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
            "}";
    }
}
