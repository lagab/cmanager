package com.lagab.cmanager.service.dto;

import java.io.Serializable;
import com.lagab.cmanager.domain.enumeration.Status;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the Contract entity. This class is used in ContractResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /contracts?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContractCriteria implements Serializable {
    /**
     * Class for filtering Status
     */
    public static class StatusFilter extends Filter<Status> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter uuid;

    private StringFilter name;

    private StringFilter subject;

    private StatusFilter status;

    private StringFilter description;

    private StringFilter content;

    private BooleanFilter requestAcess;

    private LocalDateFilter lastActivityAt;

    private LocalDateFilter expiresAt;

    private StringFilter type;

    private LongFilter requestsId;

    private LongFilter projectId;

    public ContractCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUuid() {
        return uuid;
    }

    public void setUuid(StringFilter uuid) {
        this.uuid = uuid;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getSubject() {
        return subject;
    }

    public void setSubject(StringFilter subject) {
        this.subject = subject;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public void setStatus(StatusFilter status) {
        this.status = status;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getContent() {
        return content;
    }

    public void setContent(StringFilter content) {
        this.content = content;
    }

    public BooleanFilter getRequestAcess() {
        return requestAcess;
    }

    public void setRequestAcess(BooleanFilter requestAcess) {
        this.requestAcess = requestAcess;
    }

    public LocalDateFilter getLastActivityAt() {
        return lastActivityAt;
    }

    public void setLastActivityAt(LocalDateFilter lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }

    public LocalDateFilter getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateFilter expiresAt) {
        this.expiresAt = expiresAt;
    }

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public LongFilter getRequestsId() {
        return requestsId;
    }

    public void setRequestsId(LongFilter requestsId) {
        this.requestsId = requestsId;
    }

    public LongFilter getProjectId() {
        return projectId;
    }

    public void setProjectId(LongFilter projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "ContractCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (uuid != null ? "uuid=" + uuid + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (subject != null ? "subject=" + subject + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (content != null ? "content=" + content + ", " : "") +
                (requestAcess != null ? "requestAcess=" + requestAcess + ", " : "") +
                (lastActivityAt != null ? "lastActivityAt=" + lastActivityAt + ", " : "") +
                (expiresAt != null ? "expiresAt=" + expiresAt + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (requestsId != null ? "requestsId=" + requestsId + ", " : "") +
                (projectId != null ? "projectId=" + projectId + ", " : "") +
            "}";
    }

}
