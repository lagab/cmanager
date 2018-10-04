package com.lagab.cmanager.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the SignatureRequest entity. This class is used in SignatureRequestResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /signature-requests?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SignatureRequestCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter requesterEmail;

    private StringFilter title;

    private StringFilter subject;

    private StringFilter message;

    private StringFilter ccEmail;

    private LongFilter signaturesId;

    private LongFilter contractId;

    public SignatureRequestCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getRequesterEmail() {
        return requesterEmail;
    }

    public void setRequesterEmail(StringFilter requesterEmail) {
        this.requesterEmail = requesterEmail;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getSubject() {
        return subject;
    }

    public void setSubject(StringFilter subject) {
        this.subject = subject;
    }

    public StringFilter getMessage() {
        return message;
    }

    public void setMessage(StringFilter message) {
        this.message = message;
    }

    public StringFilter getCcEmail() {
        return ccEmail;
    }

    public void setCcEmail(StringFilter ccEmail) {
        this.ccEmail = ccEmail;
    }

    public LongFilter getSignaturesId() {
        return signaturesId;
    }

    public void setSignaturesId(LongFilter signaturesId) {
        this.signaturesId = signaturesId;
    }

    public LongFilter getContractId() {
        return contractId;
    }

    public void setContractId(LongFilter contractId) {
        this.contractId = contractId;
    }

    @Override
    public String toString() {
        return "SignatureRequestCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (requesterEmail != null ? "requesterEmail=" + requesterEmail + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (subject != null ? "subject=" + subject + ", " : "") +
                (message != null ? "message=" + message + ", " : "") +
                (ccEmail != null ? "ccEmail=" + ccEmail + ", " : "") +
                (signaturesId != null ? "signaturesId=" + signaturesId + ", " : "") +
                (contractId != null ? "contractId=" + contractId + ", " : "") +
            "}";
    }

}
