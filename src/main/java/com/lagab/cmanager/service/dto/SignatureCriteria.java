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
 * Criteria class for the Signature entity. This class is used in SignatureResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /signatures?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SignatureCriteria implements Serializable {
    /**
     * Class for filtering Status
     */
    public static class StatusFilter extends Filter<Status> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter email;

    private StringFilter name;

    private IntegerFilter order;

    private StatusFilter status;

    private StringFilter declineReason;

    private LocalDateFilter lastViewedAt;

    private LocalDateFilter lastRemindedAt;

    private LongFilter requestId;

    public SignatureCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public IntegerFilter getOrder() {
        return order;
    }

    public void setOrder(IntegerFilter order) {
        this.order = order;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public void setStatus(StatusFilter status) {
        this.status = status;
    }

    public StringFilter getDeclineReason() {
        return declineReason;
    }

    public void setDeclineReason(StringFilter declineReason) {
        this.declineReason = declineReason;
    }

    public LocalDateFilter getLastViewedAt() {
        return lastViewedAt;
    }

    public void setLastViewedAt(LocalDateFilter lastViewedAt) {
        this.lastViewedAt = lastViewedAt;
    }

    public LocalDateFilter getLastRemindedAt() {
        return lastRemindedAt;
    }

    public void setLastRemindedAt(LocalDateFilter lastRemindedAt) {
        this.lastRemindedAt = lastRemindedAt;
    }

    public LongFilter getRequestId() {
        return requestId;
    }

    public void setRequestId(LongFilter requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "SignatureCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (order != null ? "order=" + order + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (declineReason != null ? "declineReason=" + declineReason + ", " : "") +
                (lastViewedAt != null ? "lastViewedAt=" + lastViewedAt + ", " : "") +
                (lastRemindedAt != null ? "lastRemindedAt=" + lastRemindedAt + ", " : "") +
                (requestId != null ? "requestId=" + requestId + ", " : "") +
            "}";
    }

}
