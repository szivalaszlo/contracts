package com.szivalaszlo.contracts.landon.business.domain;

import com.szivalaszlo.contracts.landon.business.exception.DateFormatConstraint;
import com.szivalaszlo.contracts.landon.business.exception.DateInPastConstraint;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PersonForm {
    private String id;

    @NotNull
    @Size(min = 2, max = 30)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 30)
    private String lastName;

    @DateFormatConstraint //custom annotation
    @DateInPastConstraint //custom annotation
    @NotNull
    private String dateOfBirth;

    @NotNull
    @Size(min = 2, max = 30)
    private String firstNameMother;

    @NotNull
    @Size(min = 2, max = 30)
    private String lastNameMother;

    private List<ContactdetailsForm> contactDetails;

    public PersonForm() {
    }

    public PersonForm(String id, String firstName, String lastName, String dateOfBirth, String firstNameMother, String lastNameMother) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.firstNameMother = firstNameMother;
        this.lastNameMother = lastNameMother;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setFirstNameMother(String firstNameMother) {
        this.firstNameMother = firstNameMother;
    }

    public void setLastNameMother(String lastNameMother) {
        this.lastNameMother = lastNameMother;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getFirstNameMother() {
        return firstNameMother;
    }

    public String getLastNameMother() {
        return lastNameMother;
    }

    public Map<String, Object> extractAllFields() {
        Map<String, Object> allFields = new HashMap<>();
        allFields.put("firstName", firstName);
        allFields.put("lastName", lastName);
        allFields.put("dateOfBirth", dateOfBirth);
        allFields.put("firstNameMother", firstNameMother);
        allFields.put("lastNameMother", lastNameMother);
        return allFields;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof PersonForm))
            return false;
        if (obj == this)
            return true;
        return Objects.equals(this.getId(), ((PersonForm) obj).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        return "PersonForm{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", firstNameMother='" + firstNameMother + '\'' +
                ", lastNameMother='" + lastNameMother + '\'' +
                '}';
    }
}
