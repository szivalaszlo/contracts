package com.szivalaszlo.contracts.landon.web.application;


import com.szivalaszlo.contracts.landon.business.domain.ContractForm;
import com.szivalaszlo.contracts.landon.business.domain.ContractFormConverter;
import com.szivalaszlo.contracts.landon.business.domain.PersonForm;
import com.szivalaszlo.contracts.landon.business.domain.PersonFormConverter;
import com.szivalaszlo.contracts.landon.business.exception.FieldValidationErrorException;
import com.szivalaszlo.contracts.landon.business.service.ContractService;
import com.szivalaszlo.contracts.landon.data.entity.Contract;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ContractRestController {

    private static Logger logger = LogManager.getLogger();

    private ContractService contractService;

    @Autowired
    public ContractRestController(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping("/contracts/id/{contractId}")
    @Transactional
    public ResponseEntity<ContractForm> findById(@PathVariable String contractId, UriComponentsBuilder ucb) {
        ContractForm currentContractForm = contractService.findById(contractId);
        HttpHeaders headers = new HttpHeaders();
        URI locationUri = ucb.path("/contracts/id/").path(contractId).build().toUri();
        headers.setLocation(locationUri);
        return new ResponseEntity<ContractForm>(currentContractForm, headers, HttpStatus.OK);
    }

    @GetMapping("/contracts/activeondate/{targetDate}")
    @Transactional
    public ResponseEntity<List<ContractForm>> findActiveContractsOnDate(@PathVariable String targetDate, UriComponentsBuilder ucb) {
        List<ContractForm> allMatchingFromDb = contractService.findActiveContractsOnDate(targetDate);
        HttpHeaders headers = new HttpHeaders();
        URI locationUri = ucb.path("/activeondate/").path(targetDate).build().toUri();
        headers.setLocation(locationUri);
        return new ResponseEntity<List<ContractForm>>(allMatchingFromDb, headers, HttpStatus.OK);
    }

    @GetMapping("/contracts/lastname/{lastName}")
    @Transactional
    public ResponseEntity<List<ContractForm>> findContractsForLastname(@PathVariable String lastName, UriComponentsBuilder ucb) {
        List<ContractForm> allMatchingFromDb = contractService.findByLastName(lastName);
        HttpHeaders headers = new HttpHeaders();
        URI locationUri = ucb.path("/lastname/").path(lastName).build().toUri();
        headers.setLocation(locationUri);
        return new ResponseEntity<List<ContractForm>>(allMatchingFromDb, headers, HttpStatus.OK);
    }

    // TODO: 3/17/19 how to catch JSON parse error: Cannot deserialize instance of `java.util.ArrayList` if not array is passed in for sellers and buyers

    @PostMapping("/contracts/add")
    public ResponseEntity<ContractForm> addContract(@Valid @RequestBody ContractForm contractForm, BindingResult bindingResult, UriComponentsBuilder ucb) {
        if (bindingResult.hasErrors()) {
            logger.debug("ContractForm validation error(s) found: \n");
            throw new FieldValidationErrorException(bindingResult);
        } else {
            logger.debug("No errors found during parsing ContractForm to Contract");
            int newContractId = contractService.createContract(contractForm);
            ContractForm savedNewContractForm = contractService.findById(newContractId);
            HttpHeaders headers = new HttpHeaders();
            URI locationUri = ucb.path("/contracts/add/").path(String.valueOf(newContractId)).build().toUri();
            headers.setLocation(locationUri);
            return new ResponseEntity<ContractForm>(savedNewContractForm, headers, HttpStatus.CREATED);
        }
    }
}
