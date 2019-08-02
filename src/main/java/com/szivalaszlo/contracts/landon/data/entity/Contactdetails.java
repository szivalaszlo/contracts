package com.szivalaszlo.contracts.landon.data.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="contactdetails")
public class Contactdetails {

    private static Logger logger = LogManager.getLogger();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "address")
    private String address;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;

    @ManyToOne
    @JoinColumn(name = "personid", nullable = false)
    private Person person;

    public Contactdetails(){

    }

    public Contactdetails(String address, String email, String phone) {
        this.address = address;
        this.email = email;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
//        logger.debug("Person is set for contactdetail: " + this.toString() + " person: " + this.person.toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Contactdetails))
            return false;
        if (obj == this)
            return true;
        return Objects.equals(this.getId(), ((Contactdetails) obj).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Contactdetails{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", person=" + person +
                '}';
    }
}