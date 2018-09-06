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

import com.lagab.cmanager.domain.enumeration.Visibility;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Project extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "path", nullable = false)
    private String path;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility")
    private Visibility visibility;

    @NotNull
    @Column(name = "archived", nullable = false)
    private Boolean archived;

    @OneToMany(mappedBy = "project")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Contract> contracts = new HashSet<>();

    @OneToMany(mappedBy = "project")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ContactFolder> contactFolders = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("projects")
    private Workspace workspace;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Project name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public Project path(String path) {
        this.path = path;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public Project visibility(Visibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public Boolean isArchived() {
        return archived;
    }

    public Project archived(Boolean archived) {
        this.archived = archived;
        return this;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public Set<Contract> getContracts() {
        return contracts;
    }

    public Project contracts(Set<Contract> contracts) {
        this.contracts = contracts;
        return this;
    }

    public Project addContracts(Contract contract) {
        this.contracts.add(contract);
        contract.setProject(this);
        return this;
    }

    public Project removeContracts(Contract contract) {
        this.contracts.remove(contract);
        contract.setProject(null);
        return this;
    }

    public void setContracts(Set<Contract> contracts) {
        this.contracts = contracts;
    }

    public Set<ContactFolder> getContactFolders() {
        return contactFolders;
    }

    public Project contactFolders(Set<ContactFolder> contactFolders) {
        this.contactFolders = contactFolders;
        return this;
    }

    public Project addContactFolders(ContactFolder contactFolder) {
        this.contactFolders.add(contactFolder);
        contactFolder.setProject(this);
        return this;
    }

    public Project removeContactFolders(ContactFolder contactFolder) {
        this.contactFolders.remove(contactFolder);
        contactFolder.setProject(null);
        return this;
    }

    public void setContactFolders(Set<ContactFolder> contactFolders) {
        this.contactFolders = contactFolders;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public Project workspace(Workspace workspace) {
        this.workspace = workspace;
        return this;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
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
        Project project = (Project) o;
        if (project.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), project.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", path='" + getPath() + "'" +
            ", visibility='" + getVisibility() + "'" +
            ", archived='" + isArchived() + "'" +
            "}";
    }
}
