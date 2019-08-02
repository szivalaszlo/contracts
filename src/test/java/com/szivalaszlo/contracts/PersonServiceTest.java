package com.szivalaszlo.contracts;


import com.szivalaszlo.contracts.landon.business.domain.ContactdetailsForm;
import com.szivalaszlo.contracts.landon.business.domain.ContactdetailsFormConverter;
import com.szivalaszlo.contracts.landon.business.domain.PersonForm;
import com.szivalaszlo.contracts.landon.business.exception.EntityAlreadyExistsException;
import com.szivalaszlo.contracts.landon.business.service.PersonService;
import com.szivalaszlo.contracts.landon.data.entity.Contactdetails;
import com.szivalaszlo.contracts.landon.data.entity.Contract;
import com.szivalaszlo.contracts.landon.data.entity.Person;
import com.szivalaszlo.contracts.landon.data.repository.ContactdetailsRepository;
import com.szivalaszlo.contracts.landon.data.repository.ContractRepository;
import com.szivalaszlo.contracts.landon.data.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonServiceTest {

    private static Logger logger = LogManager.getLogger();

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ContactdetailsRepository contactdetailsRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private PersonService personService;

    Random rand = new Random();
    int randomNumber = rand.nextInt(89999) + 1000;

    private String firstName = "TestJhonFirst " + randomNumber;
    private String lastName = "TestSmithLast " + randomNumber;
    private String birthDay = "1990-11-11";
    private String firstNameMother = "TestJackieMotherFirst " + randomNumber;
    private String lastNameMother = "TestSmithMotherLast " + randomNumber;

    private String address = "test address street 1 in City " + randomNumber;
    private String email = "testemail@example.com " + randomNumber;
    private String phone = "+41 12 345 78 90 " + randomNumber;

    private PersonForm testPersonForm;

    @Before
    public void setup() {
        testPersonForm = new PersonForm("0", firstName, lastName, birthDay, firstNameMother, lastNameMother);
    }


    @Test
    public void it_should_save_person() {
        int testPersonId = personService.createPerson(testPersonForm);
        Person personReadFromDatabase = (Person) personRepository.findById(testPersonId);
        assertThat(personReadFromDatabase).hasFieldOrPropertyWithValue("firstName", testPersonForm.getFirstName());
        assertThat(personReadFromDatabase).hasFieldOrPropertyWithValue("lastName", testPersonForm.getLastName());
        assertThat(personReadFromDatabase).hasFieldOrPropertyWithValue("firstNameMother", testPersonForm.getFirstNameMother());
        assertThat(personReadFromDatabase).hasFieldOrPropertyWithValue("lastNameMother", testPersonForm.getLastNameMother());
        assertThat(personReadFromDatabase).hasFieldOrPropertyWithValue("dateOfBirth", LocalDate.parse(testPersonForm.getDateOfBirth()));
    }

    @Test
    public void it_should_not_recreate_already_existing_person() {
        personService.createPerson(testPersonForm);
        List<Person> allPersonFromDbBeforeTest = personRepository.findAll();
        int numberOfStoredPersonBeforeTest = allPersonFromDbBeforeTest.size();
        try {
            for (int i = 0; i < 9; i++) {
                personService.createPerson(testPersonForm);
            }
        } catch (EntityAlreadyExistsException e) {
            logger.debug(e.getMessage() + " exception caught. OK!");
        }
        List<Person> allPersonFromDbAfterTest = personRepository.findAll();
        int numberOfStoredPersonAfterTest = allPersonFromDbAfterTest.size();
        assertThat(numberOfStoredPersonBeforeTest).isEqualTo(numberOfStoredPersonAfterTest);
    }


    @Test
    public void it_should_find_all_persons() {
        List<PersonForm> allPersonsFromDb = personService.findAll();
        allPersonsFromDb.forEach(personForm -> logger.debug(personForm.toString()));
        assertThat(allPersonsFromDb, is(not(empty())));
    }

    @Test
    public void it_should_find_person_by_lastname() {
        String lastNameToFind = "Jones";
        List<PersonForm> personsByLastNameList = personService.findByLastName(lastNameToFind);
        personsByLastNameList.forEach(person -> logger.debug(person.toString()));
        personsByLastNameList.forEach(person -> Assert.assertEquals(lastNameToFind, person.getLastName()));
    }

    @Test
    public void it_should_find_person_by_firstname() {
        String firstNameToFind = "Bruce";
        List<PersonForm> personsByFirstnameList = personService.findByFirstName(firstNameToFind);
        personsByFirstnameList.forEach(person -> logger.debug(person.toString()));
        personsByFirstnameList.forEach(person -> Assert.assertEquals(firstNameToFind, person.getFirstName()));
    }

    @Test
    public void it_should_find_person_by_firstname_and_lastname() {
        String firstNameToFind = "Bruce";
        String lastNameToFind = "Donnelly";
        List<PersonForm> personsByFirstAndLastNameList = personService.findByFirstNameAndLastName(firstNameToFind, lastNameToFind);
        personsByFirstAndLastNameList.forEach(person -> logger.debug(person.toString()));
        personsByFirstAndLastNameList.forEach(person -> Assert.assertEquals(firstNameToFind, person.getFirstName()));
        personsByFirstAndLastNameList.forEach(person -> Assert.assertEquals(lastNameToFind, person.getLastName()));
    }


    @Test
    public void it_should_find_person_by_birthday() {
        String birthdayToFind = "1997-12-12";
        List<PersonForm> personsByBirthdayList = personService.findByDateOfBirth(birthdayToFind);
        personsByBirthdayList.forEach(person -> logger.debug(person.toString()));
        personsByBirthdayList.forEach(person -> Assert.assertEquals(person.getDateOfBirth().toString(), birthdayToFind));
    }

    @Test
    @Transactional
    public void it_should_list_person_by_contract_id() {
        String contractIdToFind = "11";
        List<PersonForm> personsFormsForContractId = personService.findByContractId(contractIdToFind);
        personsFormsForContractId.forEach(person -> logger.debug(person.toString()));
        for (PersonForm currentPersonForm : personsFormsForContractId) {
            Person currentPerson = personRepository.findById(Integer.parseInt(currentPersonForm.getId()));
            List<Contract> buyerContracts = currentPerson.getBuyerContracts();
            List<Contract> sellerContracts = currentPerson.getSellerContracts();
            List<String> buyerContractIds = fillListWithContractIds(buyerContracts);
            List<String> sellerContractIds = fillListWithContractIds(sellerContracts);
            Assert.assertTrue(buyerContractIds.contains(contractIdToFind) || sellerContractIds.contains(contractIdToFind));
        }
    }

    private List<String> fillListWithContractIds(List<Contract> contracts) {
        List<String> contractIds = new ArrayList<>();
        for (Contract currentContract : contracts) {
            contractIds.add(Integer.toString(currentContract.getId()));
        }
        return contractIds;
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @Rollback(false)
    public void it_should_save_contactdetails_to_person() {
        int testPersonId = personService.createPerson(testPersonForm);
        String testpersonIdString = Integer.toString(testPersonId);

        ContactdetailsForm testContactDetailsFrom = new ContactdetailsForm("0", testpersonIdString, address, email, phone);
        Person testPerson = personRepository.findById(testPersonId);
        personService.createContactdetailsForPerson(testContactDetailsFrom);
        Contactdetails testContactdetails = testPerson.getContactdetails().get(0);
        ContactdetailsForm contactdetailsFormReadFromDb = new ContactdetailsFormConverter(testContactdetails).getContactdetailsFormInstance();

        logger.debug("testperson: " + testPerson);
        logger.debug("testContactdetailsFormReadFromDb: " + contactdetailsFormReadFromDb);

        assertThat(testContactdetails).hasFieldOrPropertyWithValue("address", "test address street 1 in City " + randomNumber);
        assertThat(testContactdetails).hasFieldOrPropertyWithValue("email", "testemail@example.com " + randomNumber);
        assertThat(testContactdetails).hasFieldOrPropertyWithValue("phone", "+41 12 345 78 90 " + randomNumber);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @Rollback(false)
    public void it_should_save__several_contactdetails_to_person() {
        int testPersonId = personService.createPerson(testPersonForm);
        String testpersonIdString = Integer.toString(testPersonId);
        logger.debug("testPersonId : " + testpersonIdString);
        List<Contactdetails> allContactdetailsFromDbBeforeTest = contactdetailsRepository.findAll();
        int numberOfStoredContactDetailsBeforeTest = allContactdetailsFromDbBeforeTest.size();
        logger.debug("Number of contactdetails before: " + numberOfStoredContactDetailsBeforeTest);
        for (int i = 1000; i <= 3000; i += 1000) {
            ContactdetailsForm testContactDetailsFrom = new ContactdetailsForm("0", testpersonIdString, address + i, email + i, phone + i);
            personService.createContactdetailsForPerson(testContactDetailsFrom);
        }
        List<Contactdetails> allContactdetailsFromDbAfterTest = contactdetailsRepository.findAll();
        int numberOfStoredContactDetailsAfterTest = allContactdetailsFromDbAfterTest.size();
        logger.debug("Number of contactdetails after: " + numberOfStoredContactDetailsAfterTest);
        assertThat(numberOfStoredContactDetailsBeforeTest + 3).isEqualTo(numberOfStoredContactDetailsAfterTest);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @Rollback(true)  //otherwise tries to create person and won't create contactdetails -> error with rollback setting
    public void it_should_not_recreate_already_existing_contactdetails() {
        int testPersonId = personService.createPerson(testPersonForm);
        String testpersonIdString = Integer.toString(testPersonId);
        ContactdetailsForm testContactDetailsFrom = new ContactdetailsForm("0", testpersonIdString, address, email, phone);
        Person testPerson = personRepository.findById(testPersonId);
        List<Contactdetails> allContactdetailsFromDbBeforeTest = contactdetailsRepository.findAll();
        int numberOfStoredContactDetailsBeforeTest = allContactdetailsFromDbBeforeTest.size();
        logger.debug("Number of contactdetails before: " + numberOfStoredContactDetailsBeforeTest);
        try {
            for (int i = 0; i < 10; i++) {
                personService.createContactdetailsForPerson(testContactDetailsFrom);
            }
        } catch (EntityAlreadyExistsException e) {
            logger.debug(e.getMessage() + " exception caught. OK!");
        }
        List<Contactdetails> allContactdetailsFromDbAfterTest = contactdetailsRepository.findAll();
        int numberOfStoredContactDetailsAfterTest = allContactdetailsFromDbAfterTest.size();
        logger.debug("Number of contactdetails after: " + numberOfStoredContactDetailsAfterTest);
        assertThat(numberOfStoredContactDetailsBeforeTest + 1).isEqualTo(numberOfStoredContactDetailsAfterTest);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @Rollback(false)
    public void it_should_find_all_contactdetails_for_person() {
        int testPersonId = personService.createPerson(testPersonForm);
        String testpersonIdString = Integer.toString(testPersonId);
        List<Contactdetails> allContactdetailsFromDbBeforeTest = contactdetailsRepository.findAll();
        List<ContactdetailsForm> addedContactdetailsList = new ArrayList<>();
        for (int i = 1000; i <= 3000; i += 1000) {
            ContactdetailsForm testContactDetailsFrom = new ContactdetailsForm("0", testpersonIdString, address + i, email + i, phone + i);
            personService.createContactdetailsForPerson(testContactDetailsFrom);
            addedContactdetailsList.add(testContactDetailsFrom);
        }
        List<ContactdetailsForm> foundContactdetailsForPersonList = personService.findContactdetailsForPerson(testpersonIdString);
        logger.debug("created contactdetails list: " + addedContactdetailsList);
        logger.debug("found contactdetails list: " + foundContactdetailsForPersonList);
        assertThat(foundContactdetailsForPersonList.containsAll(addedContactdetailsList));
    }
}
