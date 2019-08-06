# README

## Spring boot with REST tutorial

## Table of contents

* [Objective](https://github.com/szivalaszlo/contracts#objective)
* [Background and recommended online courses](https://github.com/szivalaszlo/contracts#background-and-recommended-online-courses)
* [Recommended software setup with install tutorial links](https://github.com/szivalaszlo/contracts#recommended-software-setup-with-install-tutorial-links-linux)
* [Getting started](https://github.com/szivalaszlo/contracts#getting-started)
* [Laying out the database schema](https://github.com/szivalaszlo/contracts#laying-out-the-database-schema)
* [Entities](https://github.com/szivalaszlo/contracts#entity---basics)



## Objective

This tutorial covers a Spring boot project with REST API definition. The exercise is to build a web application in which multiple persons can have multiple contracts signed between them. 

This tutorial will help you to build a web application on your local machine which uses Spring Boot and MySQL. 

The explanation is kept to minimum with listing relevant articles to read in order to gain in depth understanding. For me this type of research-read-code method works the best. 

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

First a Maven project shall be generated using [https://start.spring.io/](https://start.spring.io/). Add group and artifact name, plus select the 3 dependencies shown in the screenshot using the search bar.

![spring starter](https://i.imgur.com/LZXVgV9.png)

After this step the fresh “empty” project can be imported to IntelliJ as a Maven project.

If you are not familiar with Maven you may look into this [Wikipedia article](https://en.wikipedia.org/wiki/Apache_Maven) and find more material on it if needed.

For this simple project we are going to set a few dependencies in the pom.xml file in addition to the default generated ones.

![POM](https://i.imgur.com/IWFnl2i.png)

See the actual [pom.xml](https://github.com/szivalaszlo/contracts/blob/master/pom.xml) file for the additional dependencies with descriptive comments.

## Laying out the database schema

By definition a contract can have one or more sellers and also one or more buyers as well. This is a many to many relationship. I decided to build the following schema.

![SQL schema](https://i.imgur.com/p7Q4RGT.png)

A person can have many contactdetails defined. The seller\_contract and the buyer\_contract tables join the person and the contract. 

I know that this **database design is not good**, because the same contract is referenced \(twice\) in the seller\_contract and buyer\_contract tables. Ideally there should be only one table at the top instead of this two. In this 'better' setup this alone table should be a reference table listing in one line the contract FK a seller FK and a buyer FK. Still I chose the current design because the current design allows for using the basic jointable approach. Further read: 

[Many-To-Many Relationship in JPA](https://www.baeldung.com/jpa-many-to-many)

A person can have multiple contracts \(as buyer, or seller\) and also a contract can have multiple persons. This is a many-to-many relationship.

I have used @ManyToMany, @JoinTable and @JoinColumn annotations to map the many-to-many association. Further read:

[Hibernate Many to Many Annotation Tutorial](https://www.baeldung.com/hibernate-many-to-many)

### Creating and filling up tables

The tables can be created in no time with SQL commands. If you are not familiar with SQL you may look into the online training mentioned earlier. 

I decided to fill the MySQL database with dummy data to exercise on. I wanted to see how the tables are written by hibernate and what changes when. 

You may generate your dummy data using this [site](http://filldb.info/dummy). 

If you decide to use dummy data, the tables will be created by copy-pasting the code given by the online data generator. So that is pretty fast. 

## Entities

[Link to code](https://github.com/szivalaszlo/contracts/tree/master/src/main/java/com/szivalaszlo/contracts/landon/data)

Entities are POJOs used to persist data to the database. An entity represents a table and an entity instance a row of the table. If we look into the person table in the database, we see that we have _id_ as an integer, a bunch of string values and a date. The entity has to reflect the columns of the particular table. To map the entity and its variable to the table and the table columns, we us annotations. 

The _id_ is generated by the database. The _@Id_ is telling, that this is a primary key and the _@GeneratedValue_ ... sets the generation automatic by the database. _@Column_ is reflecting the column name in the table of the database. The annotations have to be located right before the particular variable declaration.

I suggest you to follow the table and column naming pattern as shown below, as hibernate anyways uses this naming convention. 

For reference:

* [JPA Entities - Baeldung](https://www.baeldung.com/jpa-entities)
* [Accessing Data with JPA - Spring guides](https://spring.io/guides/gs/accessing-data-jpa/)

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

Besides the Person we create a Contactedtails and a Contract entities. 

After defining the Entities, the Repositories have to be created as well. I chose JpaRepository and I defined a couple of searches. 

## Lazy loading and @Transactional

In the entities' code you may have noticed the _fetch = FetchType.LAZY_ definition. What it does and why it is important. 

When **eager** loading is used, associated data is loaded into the memory on the spot. In case of **lazy** loading the data will not be loaded until it is requested. 

Looking at our Person entity, if a person is loaded with eager strategy then all of its contactdetails \(one to many\) will be loaded when the person itself is initialized. If lazy loading is enabled, contactdetails data will not be initialized and loaded into memory unless me we make a specific call to it. It does not sound a big deal with small amount of data, but as the database expands it can consume resources. 

During lazy loading to keep the transaction open we use the _@Transactional_ annotation. When we try to load the contactdetails of the person hibernate will look if a transaction is already open. If so, it will use it. This topic needs in depth exploration so I strongly recommend to consult at least the following articles. 

Further reading: 

* [Eager/Lazy Loading In Hibernate](https://www.baeldung.com/hibernate-lazy-eager-loading)
* [Transaction propagation](https://docs.spring.io/spring/docs/current/spring-framework-reference/data-access.html#tx-propagation)
* [Transaction rollback](https://dzone.com/articles/spring-transactional-amp-exceptions)

## Service Layer

The service layer holds the business logic. This layer describes how and what data is read from and written into the database. 

Let's look at the PersonService.java file. At first glance it is visible, that the createPerson\(\) method accepts a PersonForm object as input. Generally it is a good idea to not to expose Entity instances through the REST API. Therefore I decided to convert each Entity to a lighter object which does not contain link to other Entities, but its core properties. 

For instance the PersonFromConverter turns a Person object into the lighter PersonFrom object.

```java
public class PersonFormConverter {

    private static Logger logger = LogManager.getLogger();

    private Person person;
    private PersonForm personForm;
    private List<Person> persons;
    private List<PersonForm> personForms;

    public PersonFormConverter(Person person) {
        this.person = person;
        personForm = new PersonForm();
        setAllAttributesOnPersonForm();
    }

    public PersonFormConverter(List<Person> persons) {
        this.persons = persons;
        personForms = new ArrayList<>();
        setAllAttributesOnEveryPersonForms();
    }

    private void setAllAttributesOnPersonForm() {
        personForm.setId(Integer.toString(person.getId()));
        personForm.setFirstName(person.getFirstName());
        personForm.setLastName(person.getLastName());
        personForm.setDateOfBirth(person.getDateOfBirth().toString());
        personForm.setFirstNameMother(person.getFirstNameMother());
        personForm.setLastNameMother(person.getLastNameMother());
    }

    private void setAllAttributesOnEveryPersonForms() {
        for (Person currentPerson : persons) {
            personForms.add(new PersonFormConverter(currentPerson).getPersonFormInstance());
        }
    }

    public PersonForm getPersonFormInstance() {
        return personForm;
    }

    public List<PersonForm> getPersonFormList() {
        return personForms;
    }
}
```

The PersonFrom.java follows.

```java
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
```

