# README

## Spring boot with REST tutorial

## Objective

This tutorial covers a Spring boot project with REST API definition. The exercise is to build a web application in which multiple persons can have multiple contracts signed between them. This is a classic many to many relationship problem between the main entities.

This tutorial will help you understand the basic concepts of database relationships, hibernate queries and Spring boot. 

[Getting started](https://app.gitbook.com/@szivalaszlo/s/contracts-web-app/~/drafts/-LlIiIaBM5eigIxAZ7O9/primary/#getting-started)  


## Background and recommended online courses

I decided to pivot my career and become a junior software engineer after practicing mechanical engineering for 10 years. First I accomplished core java language targeted online courses and then I moved to learn Spring, Hibernate and Database fundamentals. Furthermore I luckily found a mentor who gave me tricky and useful programming exercises and a number of coaching lessons to be able to enter the job market in my new career.

I enrolled for the online courses as follows: 

1. [Java Programming and Software Engineering Fundamentals - Coursera - Duke University ](https://www.coursera.org/specializations/java-programming?)
2. [Object Oriented Java Programming: Data Structures and Beyond  - Coursera - UC San Diego](https://www.coursera.org/specializations/java-object-oriented?)
3. [Spring & Hibernate for Beginners - Udemy](https://www.udemy.com/spring-hibernate-tutorial/)
4. [Spring: Framework In Depth - Lynda Learning](https://www.lynda.com/Spring-tutorials/Spring-Framework-Depth/606088-2.html)
5. [Learning Spring with Spring Boot - Lynda Learning](https://www.lynda.com/Spring-Framework-tutorials/Learning-Spring-Spring-Boot/550572-2.html?srchtrk=index%3a2%0alinktypeid%3a2%0aq%3aspring+boot%0apage%3a1%0as%3arelevance%0asa%3atrue%0aproducttypeid%3a2)
6. [Programming Foundations: Databases - Lynda Learning](https://www.lynda.com/Programming-Foundations-training-tutorials/1351-0.html)

## Recommended software setup with install tutorial links \(Linux\):

* Ubuntu operating system \(I personally use Linux Mint distribution which runs very well on a low resource SSD upgraded laptop.\)
* [Java Open JDK](https://www.digitalocean.com/community/tutorials/how-to-install-java-with-apt-on-ubuntu-18-04) 
* [Maven](https://linuxize.com/post/how-to-install-apache-maven-on-ubuntu-18-04/)
* [Spring Boot CLI](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#getting-started-installing-the-cli)
* [MySQL](https://www.digitalocean.com/community/tutorials/how-to-install-mysql-on-ubuntu-18-04)
* [IntelliJ IDEA](https://www.jetbrains.com/idea/) 
* [Postman](https://linuxize.com/post/how-to-install-postman-on-ubuntu-18-04/)
* [Git](https://www.digitalocean.com/community/tutorials/how-to-install-git-on-ubuntu-18-04)

  \*\*\*\*

## **Getting started**

First a Maven project shall be generated using [https://start.spring.io/](https://start.spring.io/) Add group and artifact name, plus select the 3 dependencies shown in the screenshot using the search bar.

![spring starter](https://i.imgur.com/LZXVgV9.png)

After this step the fresh “empty” project can be imported to IntelliJ as a Maven project.

If you are not familiar with Maven you may look into this [Wikipedia article](https://en.wikipedia.org/wiki/Apache_Maven) and find more material on it if needed.

For this simple project we are going to set a few dependencies in the pom.xml file in addition to the default generated ones.

![POM](https://i.imgur.com/IWFnl2i.png)

See the actual pom.xml file for the additional dependencies with descriptive comments.

## Laying out the database schema

According to the task, a contract can have one or more sellers and buyers as well. This is a many to many relationship. I decided to build the following schema.

![SQL schema](https://i.imgur.com/p7Q4RGT.png)

I know that this database design is not good, because on the seller\_contract and buyer\_contract the same contract is referenced in two different tables. Ideally there should be only one table at the top instead of two. This table should be a reference table listing in one line the contract FK a seller FK and a buyer FK. I chose the current design over this one because the current design allows for using the @JoinTable keyword which makes this project easier to understand and work with.

A person can have multiple contracts \(as buyer, or seller\) and also a contract can have multiple persons. This is a many-to-many relationship.

I have used @ManyToMany, @JoinTable and @JoinColumn annotations to map the many-to-many association. See this [article](https://www.baeldung.com/hibernate-many-to-many) for details.

### Creating and filling up tables

The tables can be created in no time with SQL commands. If you are not familiar with SQL you may look into the online training mentioned earlier. 

I decided to fill the MySQL database with dummy data to exercise on. I wanted to see how the tables are written by hibernate and what changes when. 

You may generate your dummy data using this [site](http://filldb.info/dummy). 

If you decide to use dummy data, the tables will be created by copy-pasting the code given by the online data generator. So that is pretty fast. 

## Entity - basics

Before going into details, the relation between the database tables and the variables have to be clear. If we look into the person table, we see, that we have _id_ as an integer, a bunch of string values and a date. The _id_ is generated by the database. The _@Id_ is telling, that this is a primary key and the _@GeneratedValue_ ... sets the generation automatic by the database. _@Column_ is reflecting the column name in the table of the database. The annotations have to be located right before the particular variable declaration.

I suggest you to follow the table and column naming pattern as shown below, as hibernate anyways uses this naming convention. 

```java
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
```

### What entities I need



