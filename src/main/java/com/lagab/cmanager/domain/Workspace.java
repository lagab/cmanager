package com.lagab.cmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
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
 * Workpace entity.
 * @author The JHipster team.
 */
@ApiModel(description = "Workpace entity. @author The JHipster team.")
@Entity
@Table(name = "workspace")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Workspace extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "slug", nullable = false)
    private String slug;

    @Column(name = "description")
    private String description;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "request_acess")
    private Boolean requestAcess;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility")
    private Visibility visibility;

    @OneToMany(mappedBy = "workspace")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Project> projects = new HashSet<>();

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

    public Workspace name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public Workspace slug(String slug) {
        this.slug = slug;
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public Workspace description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatar() {
        return avatar;
    }

    public Workspace avatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Boolean isRequestAcess() {
        return requestAcess;
    }

    public Workspace requestAcess(Boolean requestAcess) {
        this.requestAcess = requestAcess;
        return this;
    }

    public void setRequestAcess(Boolean requestAcess) {
        this.requestAcess = requestAcess;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public Workspace visibility(Visibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public Workspace projects(Set<Project> projects) {
        this.projects = projects;
        return this;
    }

    public Workspace addProjects(Project project) {
        this.projects.add(project);
        project.setWorkspace(this);
        return this;
    }

    public Workspace removeProjects(Project project) {
        this.projects.remove(project);
        project.setWorkspace(null);
        return this;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
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
        Workspace workspace = (Workspace) o;
        if (workspace.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), workspace.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Workspace{" +
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
