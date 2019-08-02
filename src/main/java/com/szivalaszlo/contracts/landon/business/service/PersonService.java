package com.szivalaszlo.contracts.landon.business.service;

import com.szivalaszlo.contracts.landon.business.domain.*;
import com.szivalaszlo.contracts.landon.business.exception.EntityAlreadyExistsException;
import com.szivalaszlo.contracts.landon.business.exception.EntityNotFoundException;
import com.szivalaszlo.contracts.landon.business.exception.IllegalNumberFormatException;
import com.szivalaszlo.contracts.landon.data.entity.Contactdetails;
import com.szivalaszlo.contracts.landon.data.entity.Contract;
import com.szivalaszlo.contracts.landon.data.entity.Person;
import com.szivalaszlo.contracts.landon.data.repository.ContactdetailsRepository;
import com.szivalaszlo.contracts.landon.data.repository.ContractRepository;
import com.szivalaszlo.contracts.landon.data.repository.PersonRepository;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PersonService {

    private static Logger logger = LogManager.getLogger();

    private PersonRepository personRepository;
    private ContactdetailsRepository contactdetailsRepository;
    private ContractRepository contractRepository;

    @Autowired
    public PersonService(PersonRepository personRepository, ContactdetailsRepository contactdetailsRepository, ContractRepository contractRepository) {
        this.personRepository = personRepository;
        this.contactdetailsRepository = contactdetailsRepository;
        this.contractRepository = contractRepository;
    }

    public int createPerson(PersonForm personForm) {
        Person person = new Person(personForm.getFirstName(), personForm.getLastName(), personForm.getDateOfBirth(), personForm.getFirstNameMother(), personForm.getLastNameMother());
        if (rowWithSameEntriesAlreadyExistsInDbFor(personForm)) {
            logger.debug("Same person already found in Db. Person: " + personForm.toString());
            throw new EntityAlreadyExistsException("Person", personForm);
        } else {
            personRepository.save(person);
            return person.getId();
        }
    }

    private boolean rowWithSameEntriesAlreadyExistsInDbFor(PersonForm personForm) {
        try {
            List<PersonForm> allPersonFormByFirstAndLastNameFromDb = this.findByFirstNameAndLastName(personForm.getFirstName(), personForm.getLastName());
            String birthdayAndMotherNameOfNewPerson = personForm.getDateOfBirth().toString() + personForm.getFirstNameMother() + personForm.getLastNameMother();
            for (PersonForm currentPersonForm : allPersonFormByFirstAndLastNameFromDb) {
                String birthdayAndMotherNameOfExistingPerson = currentPersonForm.getDateOfBirth().toString() + currentPersonForm.getFirstNameMother() + currentPersonForm.getLastNameMother();
                if (birthdayAndMotherNameOfNewPerson.equals(birthdayAndMotherNameOfExistingPerson)) {
                    return true;
                }
            }
        } catch (EntityNotFoundException e) {
            return false;
        }
        return false;
    }

    public int putPerson(PersonForm personForm) {
        int personId = Integer.parseInt(personForm.getId());
        if (this.findById(personId) == null) {
            throw new EntityNotFoundException("Person", personId);
        }
        Person personToUpdate = personRepository.findById(personId);

        Map<String, Object> fieldsToPatch = personForm.extractAllFields();
        PatchMethodInvoker fieldPatcher = new PatchMethodInvoker(personToUpdate, fieldsToPatch);
        fieldPatcher.updateFields();

        personRepository.save(personToUpdate);
        return personId;
    }

    public int patchPerson(PersonFormPatch personFormPatch) {
        int personId = Integer.parseInt(personFormPatch.getId());
        if (this.findById(personId) == null) {
            throw new EntityNotFoundException("Person", personId);
        }
        Person personToPatch = personRepository.findById(personId);

        Map<String, Object> fieldsToPatch = personFormPatch.getFieldsToPatch();
        PatchMethodInvoker fieldPatcher = new PatchMethodInvoker(personToPatch, fieldsToPatch);
        fieldPatcher.updateFields();

        personRepository.save(personToPatch);
        return personId;
    }




    @Transactional
    public List<PersonForm> findAll() {
        return new PersonFormConverter(personRepository.findAll()).getPersonFormList();
    }

    private PersonForm convertPersonToPersonForm(Person person) {
        return new PersonFormConverter(person).getPersonFormInstance();
    }


    public PersonForm findById(int id) {
        Person currentPerson = personRepository.findById(id);
        if (currentPerson == null) {
            throw new EntityNotFoundException("Person", id);
        }
        return this.convertPersonToPersonForm(currentPerson);
    }

    public PersonForm findById(String id) {
        try {
            int personId = Integer.parseInt(id);
            return this.findById(personId);
        } catch (NumberFormatException e) {
            // TODO: 3/20/19 don't throw a new exception
            throw new IllegalNumberFormatException(id);
        }
    }

    public Person findPersonEntityById(String id) {
        try {
            int personId = Integer.parseInt(id);
            return personRepository.findById(personId);
        } catch (NumberFormatException e) {
            throw new IllegalNumberFormatException(id);
        }
    }

    public List<PersonForm> findByLastName(String lastName) {
        List<PersonForm> personForms = new PersonFormConverter(personRepository.findByLastName(lastName)).getPersonFormList();
        if (personForms.size() == 0) {
            throw new EntityNotFoundException("Person lastname", lastName);
        }
        return personForms;
    }

    public List<PersonForm> findByFirstName(String firstName) {
        List<PersonForm> personForms = new PersonFormConverter(personRepository.findByFirstName(firstName)).getPersonFormList();
        if (personForms.size() == 0) {
            throw new EntityNotFoundException("Person firstName", firstName);
        }
        return personForms;
    }

    public List<PersonForm> findByDateOfBirth(String dateOfBirth) {
        LocalDate birthDay = LocalDate.parse(dateOfBirth);
        List<PersonForm> personForms = new PersonFormConverter(personRepository.findByDateOfBirth(birthDay)).getPersonFormList();
        if (personForms.size() == 0) {
            throw new EntityNotFoundException("Person date of birth", dateOfBirth);
        }
        return personForms;
    }

    public List<PersonForm> findByFirstNameAndLastName(String firstName, String lastName) {
        List<PersonForm> personForms = new PersonFormConverter(personRepository.findByFirstNameAndLastName(firstName, lastName)).getPersonFormList();
        if (personForms.size() == 0) {
            throw new EntityNotFoundException("Person firstName, lastName", firstName + ", " + lastName);
        }
        return personForms;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<PersonForm> findByContractId(int contractId) {
        Contract contractFoundById = contractRepository.findById(contractId);
        List<Person> buyersAndSellers = new ArrayList<Person>(contractFoundById.getSellers());
        buyersAndSellers.addAll(contractFoundById.getBuyers());
        return new PersonFormConverter(buyersAndSellers).getPersonFormList();
    }

    public List<PersonForm> findByContractId(String id) {
        try {
            int contractId = Integer.parseInt(id);
            return this.findByContractId(contractId);
        } catch (NumberFormatException e) {
            throw new IllegalNumberFormatException(id);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int createContactdetailsForPerson(ContactdetailsForm contactdetailsForm) {
        int personId = Integer.parseInt(contactdetailsForm.getPersonId());
        Person person = personRepository.findById(personId);
        if (person == null) {
            throw new EntityNotFoundException("Person from adding contact details request", personId);
        }
        Contactdetails contactdetails = new Contactdetails(contactdetailsForm.getAddress(), contactdetailsForm.getEmail(), contactdetailsForm.getPhone());
        if (rowWithSameEntriesAlreadyExistsInDbFor(contactdetails, person)) {
            logger.debug("Same contactdetails for person already found " + person.toString() + " " + contactdetails.toString());
            throw new EntityAlreadyExistsException("Contactdetails", contactdetailsForm);
        } else {
            contactdetails.setPerson(person);
            contactdetailsRepository.save(contactdetails); //needed otherwise getId() will return 0. Important it has to be before saving the person (cascade) otherwise it will duplicate the entry in the db
            person.addContactdetail(contactdetails);
            personRepository.save(person);
        }
        return contactdetails.getId();
    }

    private boolean rowWithSameEntriesAlreadyExistsInDbFor(Contactdetails contactdetails, Person person) {
        List<Contactdetails> contactdetailsList = new ArrayList<>();
        if (null != person.getContactdetails()) {
            contactdetailsList = person.getContactdetails();
        }
        String addressPhoneEmailOfNewContactdetails = contactdetails.getAddress() + contactdetails.getPhone() + contactdetails.getEmail();
        for (Contactdetails currentContactdetails : contactdetailsList) {
            String addressPhoneEmailOfCurrentContactdetails = currentContactdetails.getAddress() + currentContactdetails.getPhone() + currentContactdetails.getEmail();
            if (addressPhoneEmailOfCurrentContactdetails.equals(addressPhoneEmailOfNewContactdetails)) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public List<ContactdetailsForm> findContactdetailsForPerson(String personId) {
        List<ContactdetailsForm> contactdetailsForms = new ArrayList<>();

        PersonForm personForm = this.findById(personId);
        Person person = this.findPersonEntityById(personId);

        List<Contactdetails> contactdetails = person.getContactdetails();

        for (Contactdetails currentContactdetails : contactdetails) {
            contactdetailsForms.add(new ContactdetailsFormConverter(currentContactdetails).getContactdetailsFormInstance());
        }
        if (contactdetailsForms.size() == 0) {
            throw new EntityNotFoundException("Contact details for person", personId);
        }
        return contactdetailsForms;
    }

    public ContactdetailsForm findContactdetailsById(int id) {
        Contactdetails currentContactdetails = contactdetailsRepository.findById(id);
        if (currentContactdetails == null) {
            throw new EntityNotFoundException("Contact details", id);
        }
        return new ContactdetailsFormConverter(currentContactdetails).getContactdetailsFormInstance();
    }

    public ContactdetailsForm findContactdetailsById(String id) {
        try {
            int contactdetailsId = Integer.parseInt(id);
            return this.findContactdetailsById(contactdetailsId);
        } catch (NumberFormatException e) {
            throw new IllegalNumberFormatException(id);
        }
    }
}