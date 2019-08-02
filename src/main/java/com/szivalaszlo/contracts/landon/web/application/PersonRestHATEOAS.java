package com.szivalaszlo.contracts.landon.web.application;


import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Controller;
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

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Controller
@RequestMapping("/api/hateoas")
public class PersonRestHATEOAS {

    private static Logger logger = LogManager.getLogger();

    private PersonService personService;
    private PersonRepository personRepository;
    private ContactdetailsRepository contactdetailsRepository;

    public PersonRestHATEOAS() {
    }

    @Autowired
    public PersonRestHATEOAS(PersonService personService, PersonRepository personRepository, ContactdetailsRepository contactdetailsRepository) {
        this.personService = personService;
        this.personRepository = personRepository;
        this.contactdetailsRepository = contactdetailsRepository;
    }

    @GetMapping("/persons/{personId}")
    public Resource<PersonForm> findById(@PathVariable String personId) {
        PersonForm currentPersonForm = personService.findById(personId);
        logger.debug("inside HATEOAS controller. Found PersonForm: " + currentPersonForm);
        Link selfLink = linkTo(PersonRestHATEOAS.class).slash(currentPersonForm.getId()).withSelfRel();
//        example for link to method
//        Link link = linkTo(methodOn(PersonRestHATEOAS.class).show(2L)).withSelfRel();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(PersonRestHATEOAS.class).slash(currentPersonForm).toUri());
//        return new Resource<PersonForm>(currentPersonForm, headers, HttpStatus.OK);
        Resource <PersonForm> resource = new Resource<>(currentPersonForm);

        resource.add(selfLink);
        logger.debug("created resource: " +resource);
        return resource;
    }
}














