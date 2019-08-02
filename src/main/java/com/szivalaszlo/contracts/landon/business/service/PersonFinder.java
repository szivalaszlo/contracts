package com.szivalaszlo.contracts.landon.business.service;


import com.szivalaszlo.contracts.landon.business.domain.PersonForm;
import com.szivalaszlo.contracts.landon.business.domain.PersonFormPatch;
import com.szivalaszlo.contracts.landon.business.exception.EntityNotFoundException;
import com.szivalaszlo.contracts.landon.data.entity.Person;
import com.szivalaszlo.contracts.landon.data.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class PersonFinder {

    private static Logger logger = LogManager.getLogger();

    @Autowired
    PersonService personService;

    @Autowired
    PersonRepository personRepository;

    // TODO: 3/19/19 make this class work for personService put and patch
    public PersonFinder() {

    }

    public PersonFinder(PersonService personService, PersonRepository personRepository) {
        this.personService = personService;
        this.personRepository = personRepository;
    }

    public Person getPerson(PersonForm personForm) {
        int personId = Integer.parseInt(personForm.getId());
        logger.debug("Inside finder personForm. PersonId : " + personId);
        return findPersonById(personId);
    }

    public Person getPerson(PersonFormPatch personFormPatch) {
        int personId = Integer.parseInt(personFormPatch.getId());
        logger.debug("Inside finder personFormPatch. PersonId : " + personId);
        return findPersonById(personId);
    }

    private Person findPersonById(int personId) {
        if (personService.findById(personId) == null) {
            throw new EntityNotFoundException("Person", personId);
        }
        return personRepository.findById(personId);
    }
}
