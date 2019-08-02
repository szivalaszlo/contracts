package com.szivalaszlo.contracts.landon.data.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Contract")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "document_number")
    private String documentNumber;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "content")
    private String content;

    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name = "buyer_contract",
            joinColumns = @JoinColumn(name = "contractid"),
            inverseJoinColumns = @JoinColumn(name = "person_buyerid"))
    List<Person> buyers;

    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name = "seller_contract",
            joinColumns = @JoinColumn(name = "contractid"),
            inverseJoinColumns = @JoinColumn(name = "person_sellerid"))
    List<Person> sellers;

    public Contract(){

    }

    public Contract(String documentNumber, LocalDate startDate, LocalDate endDate, String content){
        this.documentNumber = documentNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Person> getBuyers() {
        return buyers;
    }

    public void setBuyers(List<Person> buyers) {
        this.buyers = buyers;
    }

    public void addBuyer(Person person){
        if(null == buyers){
            buyers = new ArrayList<>();
        }
        buyers.add(person);
    }

    public List<Person> getSellers() {
        return sellers;
    }

    public void setSellers(List<Person> sellers) {
        this.sellers = sellers;
    }

    public void addSeller(Person person){
        if(null == sellers){
            sellers = new ArrayList<>();
        }
        sellers.add(person);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Contract))
            return false;
        if (obj == this)
            return true;
        return Objects.equals(this.getId(), ((Contract) obj).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", documentNumber='" + documentNumber + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", content='" + content + '\'' +
                ", buyers=" + buyers +
                ", sellers=" + sellers +
                '}';
    }
}
