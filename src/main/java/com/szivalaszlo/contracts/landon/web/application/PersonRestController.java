package com.szivalaszlo.contracts.landon.web.application;

import com.szivalaszlo.contracts.landon.business.domain.*;
import com.szivalaszlo.contracts.landon.business.exception.FieldValidationErrorException;
import com.szivalaszlo.contracts.landon.business.service.PersonService;
import com.szivalaszlo.contracts.landon.data.repository.ContactdetailsRepository;
import com.szivalaszlo.contracts.landon.data.repository.PersonRepository;
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
import java.util.List;

@RestController
@RequestMapping("/api")
public class PersonRestController {

    private static Logger logger = LogManager.getLogger();

    private PersonService personService;
    private PersonRepository personRepository;
    private ContactdetailsRepository contactdetailsRepository;

    public PersonRestController() {
    }

    @Autowired
    public PersonRestController(PersonService personService, PersonRepository personRepository, ContactdetailsRepository contactdetailsRepository) {
        this.personService = personService;
        this.personRepository = personRepository;
        this.contactdetailsRepository = contactdetailsRepository;
    }

    @GetMapping("/persons")
    @Transactional
    public List<PersonForm> findAll() {
        List<PersonForm> allMatchingPersonForm = personService.findAll();
        return allMatchingPersonForm;
    }

    @GetMapping("/persons/{personId}")
    public ResponseEntity<PersonForm> findById(@PathVariable String personId, UriComponentsBuilder ucb) {
        PersonForm currentPersonForm = personService.findById(personId);
        HttpHeaders headers = new HttpHeaders();
        URI locationUri = ucb.path("/persons/").path(personId).build().toUri();
        headers.setLocation(locationUri);
        return new ResponseEntity<PersonForm>(currentPersonForm, headers, HttpStatus.OK);
    }


    @GetMapping("/persons/lastname/{lastName}")
    public ResponseEntity<List<PersonForm>> findLastName(@PathVariable String lastName, UriComponentsBuilder ucb) {
        List<PersonForm> allMatchingPersonFromDb = personService.findByLastName(lastName);
        HttpHeaders headers = new HttpHeaders();
        URI locationUri = ucb.path("/persons/lastname/").path(lastName).build().toUri();
        headers.setLocation(locationUri);
        return new ResponseEntity<List<PersonForm>>(allMatchingPersonFromDb, headers, HttpStatus.OK);
    }


    @GetMapping("/persons/contactdetails/personid/{personId}")
    public List<ContactdetailsForm> findContactdetailsByPersonId(@PathVariable String personId) {
        List<ContactdetailsForm> allMatchingContactdetailsFromDb = personService.findContactdetailsForPerson(personId);
        return allMatchingContactdetailsFromDb;
    }

    @GetMapping("/persons/contactdetails/id/{contactdetailsId}")
    public ContactdetailsForm findContactdetailsById(@PathVariable String contactdetailsId) {
        return personService.findContactdetailsById(contactdetailsId);
    }


    @PostMapping("/persons/add")
    public ResponseEntity<PersonForm> addPerson(@Valid @RequestBody PersonForm personForm, BindingResult bindingResult, UriComponentsBuilder ucb) {
        if (bindingResult.hasErrors()) {
            logger.debug("PersonForm validation error(s) found: \n");
            throw new FieldValidationErrorException(bindingResult);
        } else {
            logger.debug("No errors found during parsing PersonForm to Person");
            int newPersonId = personService.createPerson(personForm);
            PersonForm savedNewPersonFrom = new PersonFormConverter(personRepository.findById(newPersonId)).getPersonFormInstance();
            HttpHeaders headers = new HttpHeaders();
            URI locationUri = ucb.path("/persons/").path(String.valueOf(newPersonId)).build().toUri();
            headers.setLocation(locationUri);
            return new ResponseEntity<PersonForm>(savedNewPersonFrom, headers, HttpStatus.CREATED);
        }
    }

    @PatchMapping("/persons/{personId}")
    public ResponseEntity<?> patchPerson(@PathVariable String personId, @Valid @RequestBody PersonFormPatch personFormPatch, BindingResult bindingResult, UriComponentsBuilder ucb) {
        if (bindingResult.hasErrors()) {
            logger.debug("PersonFormPatch validation error(s) found: \n");
            throw new FieldValidationErrorException(bindingResult);
        } else {
            logger.debug("No errors found during parsing PersonFormPatch to Person");
            int updatedPersonId = personService.patchPerson(personFormPatch);
            PersonForm savedPatchedPersonFrom = new PersonFormConverter(personRepository.findById(updatedPersonId)).getPersonFormInstance();
            HttpHeaders headers = new HttpHeaders();
            URI locationUri = ucb.path("/persons/").path(String.valueOf(updatedPersonId)).build().toUri();
            headers.setLocation(locationUri);
            return new ResponseEntity<PersonForm>(savedPatchedPersonFrom, headers, HttpStatus.CREATED);
        }
    }

    @PutMapping("/persons/{personId}")
    public ResponseEntity<PersonForm> putPerson(@PathVariable String personId, @Valid @RequestBody PersonForm personForm, BindingResult bindingResult, UriComponentsBuilder ucb) {
        if (bindingResult.hasErrors()) {
            logger.debug("PersonForm validation error(s) found: \n");
            throw new FieldValidationErrorException(bindingResult);
        } else {
            logger.debug("No errors found during parsing PersonForm to Person");
            int newPersonId = personService.putPerson(personForm);
            PersonForm savedNewPersonFrom = new PersonFormConverter(personRepository.findById(newPersonId)).getPersonFormInstance();
            HttpHeaders headers = new HttpHeaders();
            URI locationUri = ucb.path("/persons/").path(String.valueOf(newPersonId)).build().toUri();
            headers.setLocation(locationUri);
            return new ResponseEntity<PersonForm>(savedNewPersonFrom, headers, HttpStatus.CREATED);
        }
    }


    @PostMapping("/persons/contactdetails/add")
    public ResponseEntity<ContactdetailsForm> addContactdetails(@Valid @RequestBody ContactdetailsForm contactdetailsForm, BindingResult bindingResult, UriComponentsBuilder ucb) {
        if (bindingResult.hasErrors()) {
            logger.debug("ContactdetailsForm validation error(s) found: \n");
            throw new FieldValidationErrorException(bindingResult);
        } else {
            logger.debug("No errors found during parsing ContactdetailsForm to Contactdetails");
            int newContactdetailsId = personService.createContactdetailsForPerson(contactdetailsForm);
            ContactdetailsForm savedNewContactdetailsForm = new ContactdetailsFormConverter(contactdetailsRepository.findById(newContactdetailsId)).getContactdetailsFormInstance();
            HttpHeaders headers = new HttpHeaders();
            URI locationUri = ucb.path("/persons/contactdetails/add").path(String.valueOf(newContactdetailsId)).build().toUri();
            headers.setLocation(locationUri);
            return new ResponseEntity<ContactdetailsForm>(savedNewContactdetailsForm, headers, HttpStatus.OK);
        }
    }
}






























