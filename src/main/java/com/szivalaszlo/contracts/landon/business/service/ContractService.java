package com.szivalaszlo.contracts.landon.business.service;

import com.szivalaszlo.contracts.landon.business.domain.ContractForm;
import com.szivalaszlo.contracts.landon.business.domain.ContractFormConverter;
import com.szivalaszlo.contracts.landon.business.domain.PersonForm;
import com.szivalaszlo.contracts.landon.business.exception.EntityNotFoundException;
import com.szivalaszlo.contracts.landon.business.exception.IllegalNumberFormatException;
import com.szivalaszlo.contracts.landon.business.exception.NoSellerOrBuyerDefinedException;
import com.szivalaszlo.contracts.landon.business.exception.SamePersonIsSellerAndBuyerException;
import com.szivalaszlo.contracts.landon.data.entity.Contract;
import com.szivalaszlo.contracts.landon.data.entity.Person;
import com.szivalaszlo.contracts.landon.data.repository.ContactdetailsRepository;
import com.szivalaszlo.contracts.landon.data.repository.ContractRepository;
import com.szivalaszlo.contracts.landon.data.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContractService {

    private static Logger logger = LogManager.getLogger();

    private PersonRepository personRepository;
    private ContactdetailsRepository contactdetailsRepository;
    private ContractRepository contractRepository;
    private PersonService personService;

    @Autowired
    public ContractService(PersonRepository personRepository, ContactdetailsRepository contactdetailsRepository, ContractRepository contractRepository, PersonService personService) {
        this.personRepository = personRepository;
        this.contactdetailsRepository = contactdetailsRepository;
        this.contractRepository = contractRepository;
    }

    private List<ContractForm> convertContractsToContractForms(List<Contract> contracts) {
        List<ContractForm> contractForms = new ArrayList<>();
        for (Contract currentContract : contracts) {
            contractForms.add(new ContractFormConverter(currentContract).getContractFormInstance());
        }
        return contractForms;
    }

    private ContractForm convertContractToContractForm(Contract contract) {
        return new ContractFormConverter(contract).getContractFormInstance();
    }

    public List<ContractForm> findAll() {
        return this.convertContractsToContractForms(contractRepository.findAll());
    }

    @Transactional
    public int createContract(ContractForm contractForm) {
        checkForInputCompletenessAndThrowExceptionIfNeeded(contractForm);
        LocalDate startDateParsed = LocalDate.parse(contractForm.getStartDate());
        LocalDate endDateParsed = LocalDate.parse(contractForm.getEndDate());
        Contract contract = new Contract(contractForm.getDocumentNumber(), startDateParsed, endDateParsed, contractForm.getContent());
        List<Person> sellers = this.findPersonsById(contractForm.getSellerIds());
        contract.setSellers(sellers);
        List<Person> buyers = this.findPersonsById(contractForm.getBuyerIds());
        contract.setBuyers(buyers);
        List<Person> buyersAndSellers = new ArrayList<>();
        buyersAndSellers.addAll(buyers);
        buyersAndSellers.addAll(sellers);

        if (contractAlreadyExistsInDb(contractForm.getDocumentNumber())) {
            logger.debug("Same contract already found in Db. Contract: " + contract.toString());
            return -1;
        } else {
            contractRepository.save(contract);
            // TODO: 3/11/19 check if saving buyers and sellers are needed or not. If not, delete buyersAndSellers list as of no need.
            //personRepository.save(seller);
            //personRepository.save(buyer);
            return contract.getId();
        }
    }

    private List<Person> findPersonsById(List<String> personIds) {
        List<Person> persons = new ArrayList<>();
        for (String currentId : personIds) {
            Person currentPerson = personRepository.findById(Integer.parseInt(currentId));
            if (currentPerson == null) {
                throw new EntityNotFoundException("Person", currentId);
            }
            persons.add(currentPerson);
        }
        return persons;
    }

    private void checkForInputCompletenessAndThrowExceptionIfNeeded(ContractForm contractForm) {
        List<String> personIdWhoAreBuyerAndSeller = createListOfPersonsWhoAreBothBuyerAndSeller(contractForm.getSellerIds(), contractForm.getBuyerIds());
        if (contractForm.getSellerIds().size() == 0) {
            logger.debug("no sellers defined! Throwing exception.");
            throw new NoSellerOrBuyerDefinedException("Seller", contractForm);
        }

        if (contractForm.getBuyerIds().size() == 0) {
            logger.debug("no buyers defined! Throwing exception!");
            throw new NoSellerOrBuyerDefinedException("Buyer", contractForm);
        }

        if (personIdWhoAreBuyerAndSeller.size() > 0) {
            logger.debug("Same buyer(s) and seller(s) defined. Throwing exception!");
            throw new SamePersonIsSellerAndBuyerException(personIdWhoAreBuyerAndSeller, contractForm);
        }

        logger.debug("ContractForm input: buyers and sellers defined OK.");
    }

    private boolean contractAlreadyExistsInDb(String documentNumber) {
        try {
            ContractForm foundContractFromInDb = findByDocumentId(documentNumber);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    private List<String> createListOfPersonsWhoAreBothBuyerAndSeller(List<String> sellers, List<String> buyers) {
        List<String> personsWhoAreBothBuyersAndSellers = new ArrayList<>();
        for (String currentSeller : sellers) {
            for (String currentBuyer : buyers) {
                if (currentSeller.equals(currentBuyer)) {
                    personsWhoAreBothBuyersAndSellers.add(currentBuyer);
                }
            }
        }
        return personsWhoAreBothBuyersAndSellers;
    }


    public ContractForm findByDocumentId(String documentNumber) {
        Contract currentContract = contractRepository.findByDocumentNumber(documentNumber);
        if (currentContract == null) {
            throw new EntityNotFoundException("Contract document number", documentNumber);
        }
        return this.convertContractToContractForm(currentContract);
    }

    public ContractForm findById(int id) {
        Contract currentContract = contractRepository.findById(id);
        if (currentContract == null) {
            throw new EntityNotFoundException("Contract", id);
        }
        return this.convertContractToContractForm(currentContract);
    }


    public ContractForm findById(String id) {
        try {
            int contractId = Integer.parseInt(id);
            return this.findById(contractId);
        } catch (NumberFormatException e) {
            throw new IllegalNumberFormatException(id);
        }
    }

    public List<ContractForm> findByStartDate(String startDate) {
        List<ContractForm> contractForms = new ContractFormConverter(contractRepository.findByStartDate(LocalDate.parse(startDate))).getContractForms();
        if (contractForms.size() == 0) {
            throw new EntityNotFoundException("Contract start date", startDate);
        }
        return contractForms;
    }

    public List<ContractForm> findByEndDate(String endDate) {
        List<ContractForm> contractForms = new ContractFormConverter(contractRepository.findByEndDate(LocalDate.parse(endDate))).getContractForms();
        if (contractForms.size() == 0) {
            throw new EntityNotFoundException("Contract end date", endDate);
        }
        return contractForms;
    }

    public List<ContractForm> findActiveContractsOnDate(String targetDate) {
        LocalDate target = LocalDate.parse(targetDate);
        List<Contract> contractsWithStartDateBefore = contractRepository.findByStartDateBefore(target.plusDays(1));
        List<Contract> contractsWithEndDateAfter = contractRepository.findByEndDateAfter(target.minusDays(1));
        List<Contract> contractsAliveAtTargetDate = new ArrayList<>();
        for (Contract currentContract : contractsWithStartDateBefore) {
            if (contractsWithEndDateAfter.contains(currentContract)) {
                contractsAliveAtTargetDate.add(currentContract);
            }
        }

        if (contractsAliveAtTargetDate.size() == 0) {
            throw new EntityNotFoundException("Contract active on target date", targetDate);
        }
        return new ContractFormConverter(contractsAliveAtTargetDate).getContractForms();
    }


    public List<ContractForm> findByLastName(String lastName) {
        List<Contract> contractsFoundByLastname = new ArrayList<>();
        List<Person> personFoundByLastName = personRepository.findByLastName(lastName);
        for (Person currentPerson : personFoundByLastName) {
            contractsFoundByLastname.addAll(currentPerson.getBuyerContracts());
            contractsFoundByLastname.addAll(currentPerson.getSellerContracts());
        }
        if (contractsFoundByLastname.size() == 0) {
            throw new EntityNotFoundException("Contract lastname", lastName);
        }
        return new ContractFormConverter(contractsFoundByLastname).getContractForms();
    }
}








