package com.lagab.cmanager.persistance.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * @author gabriel
 * @since 13/07/2018.
 */
@Access(AccessType.PROPERTY)
@Entity
@Table(name = "namespace")
public class Namespace{

    private Long id;
    private String name;
    private String path;
    private Date createdAt;
    private Date updatedAt;
    private String type;
    private String description;
    private String avatar;
    private Integer visibilityLevel;
    private Byte requestAcess;
    private User owner;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

   
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   
    @Column(name = "path")
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

   
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

   
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

   
    @Column(name = "avatar")
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

   
    @Column(name = "visibility_level")
    public Integer getVisibilityLevel() {
        return visibilityLevel;
    }

    public void setVisibilityLevel(Integer visibilityLevel) {
        this.visibilityLevel = visibilityLevel;
    }

   
    @Column(name = "request_acess")
    public Byte getRequestAcess() {
        return requestAcess;
    }

    public void setRequestAcess(Byte requestAcess) {
        this.requestAcess = requestAcess;
    }

    @ManyToOne(
            targetEntity = User.class,
            cascade = CascadeType.MERGE
    )
    @JoinColumn(name = "owner_id")
    @JsonIdentityInfo
            (
            generator=ObjectIdGenerators.PropertyGenerator.class,
            property="id",
            scope = User.class
    )
    //@JsonIdentityReference(alwaysAsId=true)
    @JsonDeserialize(as = User.class)
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Namespace namespace = (Namespace) o;

        if (id != null ? !id.equals(namespace.id) : namespace.id != null) return false;
        if (name != null ? !name.equals(namespace.name) : namespace.name != null) return false;
        if (path != null ? !path.equals(namespace.path) : namespace.path != null) return false;
        if (createdAt != null ? !createdAt.equals(namespace.createdAt) : namespace.createdAt != null) return false;
        if (updatedAt != null ? !updatedAt.equals(namespace.updatedAt) : namespace.updatedAt != null) return false;
        if (type != null ? !type.equals(namespace.type) : namespace.type != null) return false;
        if (description != null ? !description.equals(namespace.description) : namespace.description != null)
            return false;
        if (avatar != null ? !avatar.equals(namespace.avatar) : namespace.avatar != null) return false;
        if (visibilityLevel != null ? !visibilityLevel.equals(namespace.visibilityLevel) : namespace.visibilityLevel != null)
            return false;
        if (requestAcess != null ? !requestAcess.equals(namespace.requestAcess) : namespace.requestAcess != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (avatar != null ? avatar.hashCode() : 0);
        result = 31 * result + (visibilityLevel != null ? visibilityLevel.hashCode() : 0);
        result = 31 * result + (requestAcess != null ? requestAcess.hashCode() : 0);
        return result;
    }


}
