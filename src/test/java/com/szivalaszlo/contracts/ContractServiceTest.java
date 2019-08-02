package com.szivalaszlo.contracts;

import com.szivalaszlo.contracts.landon.business.domain.ContractForm;
import com.szivalaszlo.contracts.landon.business.domain.PersonForm;
import com.szivalaszlo.contracts.landon.business.service.ContractService;
import com.szivalaszlo.contracts.landon.business.service.PersonService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ContractServiceTest {

    private static Logger logger = LogManager.getLogger();

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ContactdetailsRepository contactdetailsRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    ContractService contractService;

    Random rand = new Random();
    int randomNumber = rand.nextInt(899) + 100;

    private String firstName = "TestContractJhonFirst " + randomNumber;
    private String lastName = "TestContractSmithLast " + randomNumber;
    private String birthDay = "1990-11-11";
    private String firstNameMother = "TestJackieMotherFirst " + randomNumber;
    private String lastNameMother = "TestSmithMotherLast " + randomNumber;

    private String address = "test address street 1 in City " + randomNumber;
    private String email = "testemail@example.com " + randomNumber;
    private String phone = "+41 12 345 78 90 " + randomNumber;

    private String documentNumber = "C 1234 " + randomNumber;
    private String startDate = "2001-01-01";
    private String endDate = "2002-01-01";
    private String content = "This is a contract content. " + randomNumber;

    List<String> sellerIdsTest;
    List<String> buyerIdsTest;
    ContractForm contractFormTest;

    @Before
    public void setup() {
        sellerIdsTest = new ArrayList<>();
        buyerIdsTest = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            PersonForm currentSellerForm = new PersonForm("0", firstName + i, lastName + i, birthDay, firstNameMother + i, lastNameMother + i);
            PersonForm currentBuyerForm = new PersonForm("0", firstName + i + 10, lastName + i + 10, birthDay, firstNameMother + i + 10, lastNameMother + i + 10);
            sellerIdsTest.add(Integer.toString(personService.createPerson(currentSellerForm)));
            buyerIdsTest.add(Integer.toString(personService.createPerson(currentBuyerForm)));
        }
        contractFormTest = new ContractForm("0", sellerIdsTest, buyerIdsTest, documentNumber, startDate, endDate, content);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @Rollback(false)
    public void it_should_save_contract() {
        int contractId = contractService.createContract(contractFormTest);
        Contract contractFromDb = contractRepository.findById(contractId);
        logger.debug("New Contract: " + contractFromDb);
        logger.debug("Sellers stored in Contract: " + contractFromDb.getSellers());
        logger.debug("Buyers stored in Contract: " + contractFromDb.getBuyers());
        List<String> sellersFromDb = getListOfIdsOfPersons(contractFromDb.getSellers());
        logger.debug("sellerIds: " + sellersFromDb);
        List<String> buyersFromDb = getListOfIdsOfPersons(contractFromDb.getBuyers());
        logger.debug("buyerIds: " + buyersFromDb);
        assertThat(sellersFromDb.containsAll(sellerIdsTest));
        assertThat(buyersFromDb.containsAll(buyerIdsTest));
        assertThat(contractFromDb).hasFieldOrPropertyWithValue("documentNumber", "C 1234 " + randomNumber);
        assertThat(contractFromDb).hasFieldOrPropertyWithValue("startDate", LocalDate.parse(startDate));
        assertThat(contractFromDb).hasFieldOrPropertyWithValue("endDate", LocalDate.parse(endDate));
        assertThat(contractFromDb).hasFieldOrPropertyWithValue("content", "This is a contract content. " + randomNumber);
    }

    private List<String> getListOfIdsOfPersons(List<Person> persons) {
        List<String> listOfIds = new ArrayList<>();
        for (Person currentPerson : persons) {
            listOfIds.add(Integer.toString(currentPerson.getId()));
        }
        return listOfIds;
    }


    @Test
    public void it_should_not_recreate_already_existing_contract() {
        List<Contract> allContractFromDbBeforeTest = contractRepository.findAll();
        int numberOfStoredContractBeforeTest = allContractFromDbBeforeTest.size();
        for (int i = 0; i < 9; i++) {
            contractService.createContract(contractFormTest);
        }
        List<Contract> allContractFromDbAfterTest = contractRepository.findAll();
        int numberOfStoredContractAfterTest = allContractFromDbAfterTest.size();
        assertThat(numberOfStoredContractBeforeTest + 1).isEqualTo(numberOfStoredContractAfterTest);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @Rollback(false)
    public void it_should_find_contract_with_start_date() {
        String targetDate = "1975-05-06";
        List<ContractForm> foundContracts = contractService.findByStartDate(targetDate);
        foundContracts.forEach(contractForm -> logger.debug(contractForm.toString()));
        foundContracts.forEach(contractForm -> assertThat(contractForm.getStartDate().equals(targetDate)));
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @Rollback(false)
    public void it_should_find_contract_with_end_date() {
        String targetDate = "2015-12-04";
        List<ContractForm> foundContracts = contractService.findByEndDate(targetDate);
        foundContracts.forEach(contractForm -> logger.debug(contractForm.toString()));
        foundContracts.forEach(contractForm -> assertThat(contractForm.getEndDate().equals(targetDate)));
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @Rollback(false)
    public void it_should_find_contract_active_on_date() {
        String targetDate = "2004-04-24";
        LocalDate target = LocalDate.parse(targetDate);
        List<ContractForm> foundContracts = contractService.findActiveContractsOnDate(targetDate);
        foundContracts.forEach(contractFrom -> logger.debug(contractFrom.toString()));
        foundContracts.forEach(contractFrom -> assertThat(LocalDate.parse(contractFrom.getStartDate()).isBefore(target)));
        foundContracts.forEach(contractFrom -> assertThat(LocalDate.parse(contractFrom.getEndDate()).isAfter(target)));
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    @Rollback(false)
    public void it_should_find_contract_by_lastname() {
        String lastNameToFind = "Mills";
        List<PersonForm> personFormsFromPersonQuery = personService.findByLastName(lastNameToFind);
        List<String> personIdsFromPersonQuery = new ArrayList<>();
        for (PersonForm currentPersonform : personFormsFromPersonQuery) {
            personIdsFromPersonQuery.add(currentPersonform.getId());
        }
        logger.debug("Ids from lastname query: " + personIdsFromPersonQuery);
        List<ContractForm> foundContractForms = contractService.findByLastName(lastNameToFind);
        foundContractForms.forEach(contract -> logger.debug(contract.toString()));
        for (ContractForm currentContractForm : foundContractForms) {
            List<String> allBuyerAndSellerIds = new ArrayList<>();
            allBuyerAndSellerIds.addAll(currentContractForm.getBuyerIds());
            allBuyerAndSellerIds.addAll(currentContractForm.getSellerIds());
            assertThat(atLeastOnePersonIdIsPresentInAllBuyerAndSellerIdsList(allBuyerAndSellerIds, personIdsFromPersonQuery));
        }
    }

    private boolean atLeastOnePersonIdIsPresentInAllBuyerAndSellerIdsList(List<String> buyerAndSellerIds, List<String> personIds) {
        for (String currentPersonId : personIds) {
            if (buyerAndSellerIds.contains(currentPersonId)) {
                return true;
            }
        }
        return false;
    }
}
