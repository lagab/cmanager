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
 * Criteria class for the Project entity. This class is used in ProjectResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /projects?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProjectCriteria implements Serializable {
    /**
     * Class for filtering Visibility
     */
    public static class VisibilityFilter extends Filter<Visibility> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private StringFilter path;

    private VisibilityFilter visibility;

    private BooleanFilter archived;

    private LongFilter contractsId;

    private LongFilter contactFoldersId;

    private LongFilter workspaceId;

    public ProjectCriteria() {
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

    public StringFilter getPath() {
        return path;
    }

    public void setPath(StringFilter path) {
        this.path = path;
    }

    public VisibilityFilter getVisibility() {
        return visibility;
    }

    public void setVisibility(VisibilityFilter visibility) {
        this.visibility = visibility;
    }

    public BooleanFilter getArchived() {
        return archived;
    }

    public void setArchived(BooleanFilter archived) {
        this.archived = archived;
    }

    public LongFilter getContractsId() {
        return contractsId;
    }

    public void setContractsId(LongFilter contractsId) {
        this.contractsId = contractsId;
    }

    public LongFilter getContactFoldersId() {
        return contactFoldersId;
    }

    public void setContactFoldersId(LongFilter contactFoldersId) {
        this.contactFoldersId = contactFoldersId;
    }

    public LongFilter getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(LongFilter workspaceId) {
        this.workspaceId = workspaceId;
    }

    @Override
    public String toString() {
        return "ProjectCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (path != null ? "path=" + path + ", " : "") +
                (visibility != null ? "visibility=" + visibility + ", " : "") +
                (archived != null ? "archived=" + archived + ", " : "") +
                (contractsId != null ? "contractsId=" + contractsId + ", " : "") +
                (contactFoldersId != null ? "contactFoldersId=" + contactFoldersId + ", " : "") +
                (workspaceId != null ? "workspaceId=" + workspaceId + ", " : "") +
            "}";
    }

}
