package com.szivalaszlo.contracts.landon.data.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="person")
public class Person {

    private static Logger logger = LogManager.getLogger();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column(name = "first_name_mother")
    private String firstNameMother;
    @Column(name = "last_name_mother")
    private String lastNameMother;

    @OneToMany(fetch=FetchType.LAZY, mappedBy = "person", cascade = CascadeType.ALL) // refers to person attribute of Contactdetails class
    private List<Contactdetails> contactdetails;

    @ManyToMany(fetch=FetchType.LAZY)
            @JoinTable(name = "buyer_contract",
            joinColumns = @JoinColumn(name = "person_buyerid"),
            inverseJoinColumns = @JoinColumn(name = "contractid"))
    List<Contract> buyerContracts;

    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name = "seller_contract",
            joinColumns = @JoinColumn(name = "person_sellerid"),
            inverseJoinColumns = @JoinColumn(name = "contractid"))
    List<Contract> sellerContracts;

    public Person(){

    }

    public Person(String firstName, String lastName, String dateOfBirth, String firstNameMother, String lastNameMother) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = LocalDate.parse(dateOfBirth);
        this.firstNameMother = firstNameMother;
        this.lastNameMother = lastNameMother;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

//    public void setDateOfBirth(LocalDate dateOfBirth) {
//        this.dateOfBirth = dateOfBirth;
//    }

    public void setDateOfBirth(String dateOfBirth){
        this.dateOfBirth = LocalDate.parse(dateOfBirth);
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

    public List<Contactdetails> getContactdetails() {
        return contactdetails;
    }

    public List<Contract> getBuyerContracts() {
        return buyerContracts;
    }

    public List<Contract> getSellerContracts() {
        return sellerContracts;
    }

    public void setContactdetails(List<Contactdetails> contactdetails) {
        this.contactdetails = contactdetails;
    }



    public void setBuyerContracts(List<Contract> buyerContracts) {
        this.buyerContracts = buyerContracts;
    }

    public void setABuyerContract(Contract aBuyerContract){
        if(null == buyerContracts){
            buyerContracts = new ArrayList<>();
        }
        buyerContracts.add(aBuyerContract);
    }

    public void setSellerContracts(List<Contract> sellerContracts) {
        this.sellerContracts = sellerContracts;
    }

    public void setASellerContract(Contract aSellerContract){
        if(null == sellerContracts){
            sellerContracts = new ArrayList<>();
        }
        sellerContracts.add(aSellerContract);
    }

    public void addContactdetail(Contactdetails contactdetail){
        if(null == contactdetails){
            contactdetails = new ArrayList<Contactdetails>();
        }
        contactdetails.add(contactdetail);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Person))
            return false;
        if (obj == this)
            return true;
        return Objects.equals(this.getId(), ((Person) obj).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", firstNameMother='" + firstNameMother + '\'' +
                ", lastNameMother='" + lastNameMother + '\'' +
                '}';
    }
}
