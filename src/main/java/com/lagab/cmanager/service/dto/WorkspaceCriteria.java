package com.lagab.cmanager.service.dto;

import java.io.Serializable;
import com.lagab.cmanager.domain.enumeration.Visibility;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Workspace entity. This class is used in WorkspaceResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /workspaces?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WorkspaceCriteria implements Serializable {
    /**
     * Class for filtering Visibility
     */
    public static class VisibilityFilter extends Filter<Visibility> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private StringFilter slug;

    private StringFilter description;

    private StringFilter avatar;

    private BooleanFilter requestAcess;

    private VisibilityFilter visibility;

    private LongFilter projectsId;

    public WorkspaceCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getSlug() {
        return slug;
    }

    public void setSlug(StringFilter slug) {
        this.slug = slug;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getAvatar() {
        return avatar;
    }

    public void setAvatar(StringFilter avatar) {
        this.avatar = avatar;
    }

    public BooleanFilter getRequestAcess() {
        return requestAcess;
    }

    public void setRequestAcess(BooleanFilter requestAcess) {
        this.requestAcess = requestAcess;
    }

    public VisibilityFilter getVisibility() {
        return visibility;
    }

    public void setVisibility(VisibilityFilter visibility) {
        this.visibility = visibility;
    }

    public LongFilter getProjectsId() {
        return projectsId;
    }

    public void setProjectsId(LongFilter projectsId) {
        this.projectsId = projectsId;
    }

    @Override
    public String toString() {
        return "WorkspaceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (slug != null ? "slug=" + slug + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (avatar != null ? "avatar=" + avatar + ", " : "") +
                (requestAcess != null ? "requestAcess=" + requestAcess + ", " : "") +
                (visibility != null ? "visibility=" + visibility + ", " : "") +
                (projectsId != null ? "projectsId=" + projectsId + ", " : "") +
            "}";
    }

}
