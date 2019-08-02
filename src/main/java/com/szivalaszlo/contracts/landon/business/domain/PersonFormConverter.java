package com.szivalaszlo.contracts.landon.business.domain;

import com.szivalaszlo.contracts.landon.data.entity.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

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