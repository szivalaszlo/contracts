package com.szivalaszlo.contracts.landon.business.domain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class ContactdetailsForm {

    private String id;

    @NotNull
    private String personId;

    @Size(min = 2, max = 50)
    private String address;

    @Email
    private String email;

    @Size(min = 2, max = 50)
    private String phone;

    public ContactdetailsForm() {

    }

    public ContactdetailsForm(String id, String personId, String address, String email, String phone) {
        this.id = id;
        this.personId = personId;
        this.address = address;
        this.email = email;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof ContactdetailsForm))
            return false;
        if (obj == this)
            return true;
        return Objects.equals(this.getId(), ((ContactdetailsForm) obj).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ContactdetailsForm{" +
                "id='" + id + '\'' +
                ", personId='" + personId + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
