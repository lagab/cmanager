package com.lagab.cmanager.service.dto;

import java.io.Serializable;
import com.lagab.cmanager.domain.enumeration.Gender;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Contact entity. This class is used in ContactResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /contacts?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContactCriteria implements Serializable {
    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter email;

    private GenderFilter gender;

    private StringFilter fullName;

    private StringFilter lastName;

    private StringFilter firstName;

    private StringFilter attributes;

    private LongFilter listId;

    public ContactCriteria() {
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

    public GenderFilter getGender() {
        return gender;
    }

    public void setGender(GenderFilter gender) {
        this.gender = gender;
    }

    public StringFilter getFullName() {
        return fullName;
    }

    public void setFullName(StringFilter fullName) {
        this.fullName = fullName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getAttributes() {
        return attributes;
    }

    public void setAttributes(StringFilter attributes) {
        this.attributes = attributes;
    }

    public LongFilter getListId() {
        return listId;
    }

    public void setListId(LongFilter listId) {
        this.listId = listId;
    }

    @Override
    public String toString() {
        return "ContactCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (gender != null ? "gender=" + gender + ", " : "") +
                (fullName != null ? "fullName=" + fullName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (attributes != null ? "attributes=" + attributes + ", " : "") +
                (listId != null ? "listId=" + listId + ", " : "") +
            "}";
    }

}
