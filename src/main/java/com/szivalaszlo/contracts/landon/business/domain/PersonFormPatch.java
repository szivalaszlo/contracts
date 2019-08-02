package com.szivalaszlo.contracts.landon.business.domain;

import com.szivalaszlo.contracts.landon.business.exception.DateFormatConstraint;
import com.szivalaszlo.contracts.landon.business.exception.DateInPastConstraint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

public class PersonFormPatch {

    private static Logger logger = LogManager.getLogger();

    @NotNull
    private String id;

    @Size(min = 2, max = 30)
    private String firstName;


    @Size(min = 2, max = 30)
    private String lastName;


    @DateFormatConstraint //custom annotation
    @DateInPastConstraint //custom annotation
    private String dateOfBirth;


    @Size(min = 2, max = 30)
    private String firstNameMother;

    @Size(min = 2, max = 30)
    private String lastNameMother;

    private Map<String, Object> notNullFields;

    public PersonFormPatch() {
        notNullFields = new HashMap<>();
        id = null;
        firstName = null;
        lastName = null;
        dateOfBirth = null;
        firstNameMother = null;
        lastNameMother = null;
    }

    public Map<String, Object> getFieldsToPatch() {
        if (firstName != null) {
            notNullFields.put("firstName", firstName);
        }
        if (lastName != null) {
            notNullFields.put("lastName", lastName);
        }
        if (dateOfBirth != null) {
            notNullFields.put("dateOfBirth", dateOfBirth);
        }
        if (firstNameMother != null) {
            notNullFields.put("firstNameMother", firstNameMother);
        }
        if (lastNameMother != null) {
            notNullFields.put("lastNameMother", lastNameMother);
        }
        return notNullFields;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getDateOfBirth() {
        return dateOfBirth;
    }


    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    public String getFirstNameMother() {
        return firstNameMother;
    }


    public void setFirstNameMother(String firstNameMother) {
        this.firstNameMother = firstNameMother;
    }


    public String getLastNameMother() {
        return lastNameMother;
    }


    public void setLastNameMother(String lastNameMother) {
        this.lastNameMother = lastNameMother;
    }

    @Override
    public String toString() {
        return "PersonFormPatch{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", firstNameMother='" + firstNameMother + '\'' +
                ", lastNameMother='" + lastNameMother + '\'' +
                '}';
    }
}
