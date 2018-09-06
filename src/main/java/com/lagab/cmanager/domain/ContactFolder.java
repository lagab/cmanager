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
 * A ContactFolder.
 */
@Entity
@Table(name = "contact_folder")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ContactFolder extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "folder")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ContactList> lists = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("contactFolders")
    private Project project;

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

    public ContactFolder name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ContactList> getLists() {
        return lists;
    }

    public ContactFolder lists(Set<ContactList> contactLists) {
        this.lists = contactLists;
        return this;
    }

    public ContactFolder addLists(ContactList contactList) {
        this.lists.add(contactList);
        contactList.setFolder(this);
        return this;
    }

    public ContactFolder removeLists(ContactList contactList) {
        this.lists.remove(contactList);
        contactList.setFolder(null);
        return this;
    }

    public void setLists(Set<ContactList> contactLists) {
        this.lists = contactLists;
    }

    public Project getProject() {
        return project;
    }

    public ContactFolder project(Project project) {
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
        ContactFolder contactFolder = (ContactFolder) o;
        if (contactFolder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contactFolder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ContactFolder{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
