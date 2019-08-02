package com.szivalaszlo.contracts.RestControllerTest;

import com.szivalaszlo.contracts.ContractsApplication;
import com.szivalaszlo.contracts.landon.business.domain.ContactdetailsForm;
import com.szivalaszlo.contracts.landon.business.domain.PersonForm;
import com.szivalaszlo.contracts.landon.business.domain.PersonFormConverter;
import com.szivalaszlo.contracts.landon.business.exception.EntityNotFoundException;
import com.szivalaszlo.contracts.landon.business.service.ContractService;
import com.szivalaszlo.contracts.landon.business.service.PersonService;
import com.szivalaszlo.contracts.landon.data.entity.Contactdetails;
import com.szivalaszlo.contracts.landon.data.entity.Person;
import com.szivalaszlo.contracts.landon.data.repository.ContactdetailsRepository;
import com.szivalaszlo.contracts.landon.data.repository.ContractRepository;
import com.szivalaszlo.contracts.landon.data.repository.PersonRepository;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.json.JsonObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ContractsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonRestControllerTestIntegration {

    private static Logger logger = LogManager.getLogger();

    @LocalServerPort
    private int port;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ContactdetailsRepository contactdetailsRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ContractService contractService;

    @Autowired
    private PersonService personService;

    @Autowired
    private TestRestTemplate restTemplate;
    private RestTemplate patchRestTemplate;

    private HttpHeaders headers = new HttpHeaders();

//    @Test
//    public void it_should_return_person_by_id_when_exists_with_OK() {
//        HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);
//        String urlWithPort = createURLWithPort("/contracts/api/persons/10");
//
//        ResponseEntity<PersonForm> actualResponse = restTemplate.exchange(urlWithPort, HttpMethod.GET, httpEntity, PersonForm.class);
//
//        ResponseEntity<PersonForm> personByIdResponse = restTemplate.getForEntity(urlWithPort, PersonForm.class);
//        HttpStatus responseStatus = actualResponse.getStatusCode();
//        PersonForm expectedResponse = new PersonFormConverter(personRepository.findById(10)).getPersonFormInstance();
//        assertThat(responseStatus).isEqualTo(HttpStatus.OK);
//
//        assertThat(actualResponse.getBody()).isEqualTo(expectedResponse);
//        logger.debug("Actual response body: " + actualResponse.getBody());
//        logger.debug("PersonRepository findbyId expected: " + expectedResponse);
//
//    }

    Random rand = new Random();
    int randomNumber = rand.nextInt(8999) + 100;
    int randomPersonId = rand.nextInt(99) + 1;

    private String firstName = "TestJhonFirst " + randomNumber;
    private String lastName = "TestSmithLast " + randomNumber;
    private String birthDay = "1990-11-11";
    private String firstNameMother = "TestJackieMotherFirst " + randomNumber;
    private String lastNameMother = "TestSmithMotherLast " + randomNumber;


    private String personId = Integer.toString(randomPersonId);
    private String address = "test address street 1 in City " + randomNumber;
    private String email = "testemail@example.com";
    private String phone = "+41 12 345 78 90 " + randomNumber;

    private PersonForm testPersonForm;
    private JSONObject testPersonJson;
    private ContactdetailsForm testContactdetailsForm;
    private JSONObject testContactdetailsJson;


    @Before
    public void setup() throws JSONException {
        testPersonForm = new PersonForm("0", firstName, lastName, birthDay, firstNameMother, lastNameMother);
        while (testPersonFormAlreadyExistsInDb(testPersonForm)) {
            testPersonForm = new PersonForm("0", firstName, lastName, birthDay, firstNameMother, lastNameMother);
        }

        testPersonJson = new JSONObject();
        testPersonJson.put("firstName", firstName);
        testPersonJson.put("lastName", lastName);
        testPersonJson.put("dateOfBirth", birthDay);
        testPersonJson.put("firstNameMother", firstNameMother);
        testPersonJson.put("lastNameMother", lastNameMother);

        testContactdetailsForm = new ContactdetailsForm("0", personId, address, email, phone);
        testContactdetailsJson = new JSONObject();
        testContactdetailsJson.put("personId", personId);
        testContactdetailsJson.put("address", address);
        testContactdetailsJson.put("email", email);
        testContactdetailsJson.put("phone", phone);

        // Add Apache HttpClient as TestRestTemplate for testing PATCH - Spring RestTestTemplate cannot...
        this.patchRestTemplate = restTemplate.getRestTemplate();
        HttpClient httpClient = HttpClientBuilder.create().build();
        this.patchRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
    }

    private boolean testPersonFormAlreadyExistsInDb(PersonForm personForm) {
        try {
            List<PersonForm> foundPersonForms = personService.findByFirstNameAndLastName(personForm.getFirstName(), personForm.getLastName());
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + "/contracts/api" + uri;
    }

    //GET

    @Test
    public void person_id_GET_should_return_EXISTING_person_with_OK() {
        String urlWithPort = createURLWithPort("/persons/10");

        ResponseEntity<PersonForm> actualResponse = restTemplate.getForEntity(urlWithPort, PersonForm.class);
        HttpStatus responseStatus = actualResponse.getStatusCode();
        PersonForm expectedResponse = new PersonFormConverter(personRepository.findById(10)).getPersonFormInstance();

        assertThat(responseStatus).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(expectedResponse);
        logger.debug("Actual response body: " + actualResponse.getBody());
        logger.debug("PersonRepository findbyId expected: " + expectedResponse);
    }

    @Test
    public void person_id_GET_should_return_NOT_FOUND_for_NOT_EXISTING_person() {
        String urlWithPort = createURLWithPort("/persons/-1");

        ResponseEntity<PersonForm> actualResponse = restTemplate.getForEntity(urlWithPort, PersonForm.class);
        HttpStatus responseStatus = actualResponse.getStatusCode();

        assertThat(responseStatus).isEqualTo(HttpStatus.NOT_FOUND);
        logger.debug("Actual response body: " + actualResponse);
    }

    @Test
    public void person_id_GET_should_return_BAD_REQUEST_for_not_number_person_id() {
        String urlWithPort = createURLWithPort("/persons/AAA");

        ResponseEntity<PersonForm> actualResponse = restTemplate.getForEntity(urlWithPort, PersonForm.class);
        HttpStatus responseStatus = actualResponse.getStatusCode();

        assertThat(responseStatus).isEqualTo(HttpStatus.BAD_REQUEST);
        logger.debug("Actual response body: " + actualResponse);
    }

    @Test
    public void person_lastname_GET_should_return_EXISTING_person_list_with_OK() {
        String lastName = "jones";
        String urlWithPort = createURLWithPort("/persons/lastname/" + lastName);

        ResponseEntity<List<PersonForm>> actualResponse = restTemplate.exchange(urlWithPort,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PersonForm>>() {
                });
        HttpStatus responseStatus = actualResponse.getStatusCode();

        List<PersonForm> expectedResponse = personService.findByLastName(lastName);

        assertThat(responseStatus).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(expectedResponse);
        logger.debug("Actual response body: " + actualResponse.getBody());
        logger.debug("PersonRepository findbyId expected: " + expectedResponse);
    }

    @Test
    public void person_lastname_GET_should_return_NOT_FOUND_for_NOT_EXISTING_person() {
        String urlWithPort = createURLWithPort("/persons/lastname/anonymousPerson");

        ResponseEntity<PersonForm> actualResponse = restTemplate.getForEntity(urlWithPort, PersonForm.class);
        HttpStatus responseStatus = actualResponse.getStatusCode();

        assertThat(responseStatus).isEqualTo(HttpStatus.NOT_FOUND);
        logger.debug("Actual response body: " + actualResponse);
    }

    @Test
    public void contactDetails_for_personId_GET_should_return_EXISTING_contactDetails_list_with_OK() {
        String personId = "1";
        String urlWithPort = createURLWithPort("/persons/contactdetails/personid/" + personId);

        ResponseEntity<List<ContactdetailsForm>> actualResponse = restTemplate.exchange(urlWithPort,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ContactdetailsForm>>() {
                });
        HttpStatus responseStatus = actualResponse.getStatusCode();

        List<ContactdetailsForm> expectedResponse = personService.findContactdetailsForPerson(personId);

        assertThat(responseStatus).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(expectedResponse);
        logger.debug("Actual response body: " + actualResponse.getBody());
        logger.debug("PersonRepository findbyId expected: " + expectedResponse);
    }

    @Test
    public void contactDetails_for_personId_GET_should_return_NOT_FOUND_for_NOT_EXISTING_personId() {
        String personId = "100000";
        String urlWithPort = createURLWithPort("/persons/contactdetails/personid/" + personId);

        ResponseEntity<ContactdetailsForm> actualResponse = restTemplate.getForEntity(urlWithPort, ContactdetailsForm.class);
        HttpStatus responseStatus = actualResponse.getStatusCode();

        assertThat(responseStatus).isEqualTo(HttpStatus.NOT_FOUND);
        logger.debug("Actual response body: " + actualResponse);
    }

    @Test
    public void contactDetails_id_GET_should_return_NOT_FOUND_for_NOT_EXISTING_contactDetails() {
        String contactDetailsId = "100000";
        String urlWithPort = createURLWithPort("/persons/contactdetails/id/" + contactDetailsId);

        ResponseEntity<ContactdetailsForm> actualResponse = restTemplate.getForEntity(urlWithPort, ContactdetailsForm.class);
        HttpStatus responseStatus = actualResponse.getStatusCode();

        assertThat(responseStatus).isEqualTo(HttpStatus.NOT_FOUND);
        logger.debug("Actual response body: " + actualResponse);
    }

    @Test
    public void contactDetails_id_GET_should_return_BAD_REQUEST_for_not_number_contactDetails_id() {
        String contactDetailsId = "AAA";
        String urlWithPort = createURLWithPort("/persons/contactdetails/id/" + contactDetailsId);

        ResponseEntity<ContactdetailsForm> actualResponse = restTemplate.getForEntity(urlWithPort, ContactdetailsForm.class);
        HttpStatus responseStatus = actualResponse.getStatusCode();

        assertThat(responseStatus).isEqualTo(HttpStatus.BAD_REQUEST);
        logger.debug("Actual response body: " + actualResponse);
    }

    //POST
    @Test
    public void person_POST_should_return_CREATED_when_fully_defined_and_new() throws JSONException {
        String urlWithPort = createURLWithPort("/persons/add");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpRequest = new HttpEntity<>(testPersonJson.toString(), headers);

        ResponseEntity<PersonForm> actualResponse = restTemplate.exchange(urlWithPort, HttpMethod.POST, httpRequest, PersonForm.class);
        HttpStatus responseStatus = actualResponse.getStatusCode();

        PersonForm actualBody = actualResponse.getBody();
        logger.debug("actual body: " + actualBody);

        assertThat(actualBody).hasFieldOrPropertyWithValue("firstName", testPersonJson.get("firstName"));
        assertThat(actualBody).hasFieldOrPropertyWithValue("lastName", testPersonJson.get("lastName"));
        assertThat(actualBody).hasFieldOrPropertyWithValue("firstNameMother", testPersonJson.get("firstNameMother"));
        assertThat(actualBody).hasFieldOrPropertyWithValue("lastNameMother", testPersonJson.get("lastNameMother"));
        assertThat(actualBody).hasFieldOrPropertyWithValue("dateOfBirth", testPersonJson.get("dateOfBirth"));

        Person personReadFromDatabase = (Person) personRepository.findById(Integer.parseInt(actualBody.getId()));
        assertThat(personReadFromDatabase).hasFieldOrPropertyWithValue("firstName", testPersonJson.get("firstName"));
        assertThat(personReadFromDatabase).hasFieldOrPropertyWithValue("lastName", testPersonJson.get("lastName"));
        assertThat(personReadFromDatabase).hasFieldOrPropertyWithValue("firstNameMother", testPersonJson.get("firstNameMother"));
        assertThat(personReadFromDatabase).hasFieldOrPropertyWithValue("lastNameMother", testPersonJson.get("lastNameMother"));
        assertThat(personReadFromDatabase).hasFieldOrPropertyWithValue("dateOfBirth", LocalDate.parse(testPersonJson.get("dateOfBirth").toString()));
    }

    @Test
    public void person_POST_should_return_UNPROCESSABLE_ENTITY_when_any_field_is_missing() {
        String urlWithPort = createURLWithPort("/persons/add");
        List<String> personFormFields = new ArrayList<>();
        personFormFields.add("firstName");
        personFormFields.add("lastName");
        personFormFields.add("firstNameMother");
        personFormFields.add("lastNameMother");
        personFormFields.add("dateOfBirth");

        for (String currentField : personFormFields) {
            testPersonJson.remove(currentField);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> httpRequest = new HttpEntity<>(testPersonJson.toString(), headers);
            ResponseEntity<PersonForm> actualResponse = restTemplate.exchange(urlWithPort, HttpMethod.POST, httpRequest, PersonForm.class);
            HttpStatus responseStatus = actualResponse.getStatusCode();

            assertThat(responseStatus).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
            logger.debug("Actual response body: " + actualResponse.getBody());
        }
    }

    @Test
    public void person_POST_should_return_UNPROCESSABLE_ENTITY_when_any_name_is_not_between_2_and_30_chars() throws JSONException {
        String urlWithPort = createURLWithPort("/persons/add");
        List<String> personFormFields = new ArrayList<>();
        personFormFields.add("firstName");
        personFormFields.add("lastName");
        personFormFields.add("firstNameMother");
        personFormFields.add("lastNameMother");

        List<String> stringLengthTester = new ArrayList<>();
        stringLengthTester.add("a");
        String maxLengthString = "";
        for (int i = 0; i < 31; i++) {
            maxLengthString += "a";
        }
        stringLengthTester.add(maxLengthString);
        ;

        for (String currentTesterString : stringLengthTester) {
            for (String currentField : personFormFields) {
                testPersonJson.remove(currentField);
                testPersonJson.put(currentField, currentTesterString);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> httpRequest = new HttpEntity<>(testPersonJson.toString(), headers);
                ResponseEntity<PersonForm> actualResponse = restTemplate.exchange(urlWithPort, HttpMethod.POST, httpRequest, PersonForm.class);
                HttpStatus responseStatus = actualResponse.getStatusCode();

                assertThat(responseStatus).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
                logger.debug("Actual response body: " + actualResponse.getBody());
            }
        }
    }

    @Test
    public void person_POST_should_return_UNPROCESSABLE_ENTITY_when_birthDay_is_not_legal_date_format_or_is_in_future() throws JSONException {
        String urlWithPort = createURLWithPort("/persons/add");
        List<String> dateFormatTester = new ArrayList<>();
        dateFormatTester.add("01-12-1990");
        String tomorrow = LocalDate.now().plusDays(1).toString();
        dateFormatTester.add(tomorrow);

        for (String currentDateTester : dateFormatTester) {
            testPersonJson.remove("dateOfBirth");
            testPersonJson.put("dateOfBirth", currentDateTester);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> httpRequest = new HttpEntity<>(testPersonJson.toString(), headers);

            ResponseEntity<PersonForm> actualResponse = restTemplate.exchange(urlWithPort, HttpMethod.POST, httpRequest, PersonForm.class);
            HttpStatus responseStatus = actualResponse.getStatusCode();

            assertThat(responseStatus).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
            logger.debug("Actual response body: " + actualResponse.getBody());
        }
    }

    @Test
    public void contactDetails_POST_should_return_CREATED_when_fully_defined_and_new() throws JSONException {
        String urlWithPort = createURLWithPort("/persons/contactdetails/add");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpRequest = new HttpEntity<>(testContactdetailsJson.toString(), headers);
        logger.debug("Attempt to add contactDetails: " + testContactdetailsJson);

        ResponseEntity<ContactdetailsForm> actualResponse = restTemplate.exchange(urlWithPort, HttpMethod.POST, httpRequest, ContactdetailsForm.class);
        HttpStatus responseStatus = actualResponse.getStatusCode();

        ContactdetailsForm actualBody = actualResponse.getBody();
        logger.debug("actual body: " + actualBody);

        assertThat(actualBody).hasFieldOrPropertyWithValue("personId", testContactdetailsJson.get("personId"));
        assertThat(actualBody).hasFieldOrPropertyWithValue("address", testContactdetailsJson.get("address"));
        assertThat(actualBody).hasFieldOrPropertyWithValue("email", testContactdetailsJson.get("email"));
        assertThat(actualBody).hasFieldOrPropertyWithValue("phone", testContactdetailsJson.get("phone"));

        Contactdetails contactdetailsReadFromDatabase = (Contactdetails) contactdetailsRepository.findById(Integer.parseInt(actualBody.getId()));
        assertThat(Integer.toString(contactdetailsReadFromDatabase.getPerson().getId())).isEqualTo(testContactdetailsJson.get("personId"));
        assertThat(contactdetailsReadFromDatabase).hasFieldOrPropertyWithValue("address", testContactdetailsJson.get("address"));
        assertThat(contactdetailsReadFromDatabase).hasFieldOrPropertyWithValue("email", testContactdetailsJson.get("email"));
        assertThat(contactdetailsReadFromDatabase).hasFieldOrPropertyWithValue("phone", testContactdetailsJson.get("phone"));
    }

    @Test
    public void contactDetails_POST_should_return_NOT_FOUND_when_not_existing_person_id_is_used() throws JSONException {
        String urlWithPort = createURLWithPort("/persons/contactdetails/add");
        testContactdetailsJson.remove("personId");
        testContactdetailsJson.put("personId", "-1");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpRequest = new HttpEntity<>(testContactdetailsJson.toString(), headers);
        logger.debug("Attempt to add contactDetails: " + testContactdetailsJson);

        ResponseEntity<ContactdetailsForm> actualResponse = restTemplate.exchange(urlWithPort, HttpMethod.POST, httpRequest, ContactdetailsForm.class);
        HttpStatus responseStatus = actualResponse.getStatusCode();
        ContactdetailsForm actualBody = actualResponse.getBody();

        assertThat(responseStatus).isEqualTo(HttpStatus.NOT_FOUND);
        logger.debug("Actual response body: " + actualResponse.getBody());
    }

    @Test
    public void contactDetails_POST_should_return_UNPROCESSABLE_ENTITY_when_person_id_is_missing() {
        String urlWithPort = createURLWithPort("/persons/contactdetails/add");
        testContactdetailsJson.remove("personId");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpRequest = new HttpEntity<>(testContactdetailsJson.toString(), headers);
        logger.debug("Attempt to add contactDetails: " + testContactdetailsJson);

        ResponseEntity<ContactdetailsForm> actualResponse = restTemplate.exchange(urlWithPort, HttpMethod.POST, httpRequest, ContactdetailsForm.class);
        HttpStatus responseStatus = actualResponse.getStatusCode();
        ContactdetailsForm actualBody = actualResponse.getBody();

        assertThat(responseStatus).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        logger.debug("Actual response body: " + actualResponse.getBody());
    }

    @Test
    public void contactDetails_POST_should_return_UNPROCESSABLE_ENTITY_when_address_and_phone_are_not_between_2_and_50() throws JSONException {
        String urlWithPort = createURLWithPort("/persons/contactdetails/add");

        List<String> contactdetailsFormFields = new ArrayList<>();
        contactdetailsFormFields.add("phone");
        contactdetailsFormFields.add("address");

        List<String> stringLengthTester = new ArrayList<>();
        stringLengthTester.add("a");
        String maxLengthString = "";
        for (int i = 0; i < 51; i++) {
            maxLengthString += "a";
        }
        stringLengthTester.add(maxLengthString);

        for (String currentTesterString : stringLengthTester) {
            for (String currentField : contactdetailsFormFields) {
                testContactdetailsJson.remove(currentField);
                testContactdetailsJson.put(currentField, currentTesterString);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> httpRequest = new HttpEntity<>(testContactdetailsJson.toString(), headers);
                logger.debug("Attempt to add contactDetails: " + testContactdetailsJson);

                ResponseEntity<ContactdetailsForm> actualResponse = restTemplate.exchange(urlWithPort, HttpMethod.POST, httpRequest, ContactdetailsForm.class);
                HttpStatus responseStatus = actualResponse.getStatusCode();
                ContactdetailsForm actualBody = actualResponse.getBody();

                assertThat(responseStatus).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
                logger.debug("Actual response body: " + actualResponse.getBody());
            }
        }
    }

    @Test
    public void contactDetails_POST_should_return_UNPROCESSABLE_ENTITY_when_not_well_formed_email_address() throws JSONException {
        String urlWithPort = createURLWithPort("/persons/contactdetails/add");
        testContactdetailsJson.remove("email");
        testContactdetailsJson.put("email", "this.email.com");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpRequest = new HttpEntity<>(testContactdetailsJson.toString(), headers);
        logger.debug("Attempt to add contactDetails: " + testContactdetailsJson);

        ResponseEntity<ContactdetailsForm> actualResponse = restTemplate.exchange(urlWithPort, HttpMethod.POST, httpRequest, ContactdetailsForm.class);
        HttpStatus responseStatus = actualResponse.getStatusCode();
        ContactdetailsForm actualBody = actualResponse.getBody();

        assertThat(responseStatus).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        logger.debug("Actual response body: " + actualResponse.getBody());
    }

    //PATCH
    @Test
    public void person_PATCH_should_return_OK_when_input_well_defined() throws JSONException {
        //creating a new entity
        String urlWithPort = createURLWithPort("/persons/add");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpRequest = new HttpEntity<>(testPersonJson.toString(), headers);

        ResponseEntity<PersonForm> actualResponse = restTemplate.exchange(urlWithPort, HttpMethod.POST, httpRequest, PersonForm.class);
        HttpStatus responseStatus = actualResponse.getStatusCode();

        PersonForm actualBody = actualResponse.getBody();
        String personToBePatchedId = actualBody.getId();

        assertThat(actualBody).hasFieldOrPropertyWithValue("firstName", testPersonJson.get("firstName"));
        assertThat(actualBody).hasFieldOrPropertyWithValue("lastName", testPersonJson.get("lastName"));
        assertThat(actualBody).hasFieldOrPropertyWithValue("firstNameMother", testPersonJson.get("firstNameMother"));
        assertThat(actualBody).hasFieldOrPropertyWithValue("lastNameMother", testPersonJson.get("lastNameMother"));
        assertThat(actualBody).hasFieldOrPropertyWithValue("dateOfBirth", testPersonJson.get("dateOfBirth"));

        Person personReadFromDatabase = (Person) personRepository.findById(Integer.parseInt(actualBody.getId()));
        assertThat(personReadFromDatabase).hasFieldOrPropertyWithValue("firstName", testPersonJson.get("firstName"));
        assertThat(personReadFromDatabase).hasFieldOrPropertyWithValue("lastName", testPersonJson.get("lastName"));
        assertThat(personReadFromDatabase).hasFieldOrPropertyWithValue("firstNameMother", testPersonJson.get("firstNameMother"));
        assertThat(personReadFromDatabase).hasFieldOrPropertyWithValue("lastNameMother", testPersonJson.get("lastNameMother"));
        assertThat(personReadFromDatabase).hasFieldOrPropertyWithValue("dateOfBirth", LocalDate.parse(testPersonJson.get("dateOfBirth").toString()));

        logger.debug("TestPerson created: " + actualBody + "\n ready to be patched");

        // defining patch to above created entity
        JSONObject testPersonJsonPatch = new JSONObject();
        testPersonJsonPatch.put("id", personToBePatchedId);
        testPersonJsonPatch.put("firstName", "PATCHED firstname");
        testPersonJsonPatch.put("dateOfBirth", "1970-12-31");
        logger.debug("patch json: " + testPersonJsonPatch.toString());

        String urlWithPortPatch = createURLWithPort("/persons/" + personToBePatchedId);

        HttpHeaders headersPatch = new HttpHeaders();
        headersPatch.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpRequestPatch = new HttpEntity<>(testPersonJsonPatch.toString(), headersPatch);

        ResponseEntity<PersonForm> actualResponsePatch = patchRestTemplate.exchange(urlWithPortPatch, HttpMethod.PATCH, httpRequestPatch, PersonForm.class);

        HttpStatus responseStatusPatch = actualResponsePatch.getStatusCode();

        PersonForm actualBodyPatch = actualResponsePatch.getBody();
        logger.debug("Patched person response created: " + actualBodyPatch);

        assertThat(actualBodyPatch).hasFieldOrPropertyWithValue("firstName", testPersonJsonPatch.get("firstName"));
        assertThat(actualBodyPatch).hasFieldOrPropertyWithValue("lastName", testPersonJson.get("lastName"));
        assertThat(actualBodyPatch).hasFieldOrPropertyWithValue("firstNameMother", testPersonJson.get("firstNameMother"));
        assertThat(actualBodyPatch).hasFieldOrPropertyWithValue("lastNameMother", testPersonJson.get("lastNameMother"));
        assertThat(actualBodyPatch).hasFieldOrPropertyWithValue("dateOfBirth", testPersonJsonPatch.get("dateOfBirth"));

        Person personReadFromDatabasePatch = (Person) personRepository.findById(Integer.parseInt(actualBody.getId()));
        assertThat(personReadFromDatabasePatch).hasFieldOrPropertyWithValue("firstName", testPersonJsonPatch.get("firstName"));
        assertThat(personReadFromDatabasePatch).hasFieldOrPropertyWithValue("lastName", testPersonJson.get("lastName"));
        assertThat(personReadFromDatabasePatch).hasFieldOrPropertyWithValue("firstNameMother", testPersonJson.get("firstNameMother"));
        assertThat(personReadFromDatabasePatch).hasFieldOrPropertyWithValue("lastNameMother", testPersonJson.get("lastNameMother"));
        assertThat(personReadFromDatabasePatch).hasFieldOrPropertyWithValue("dateOfBirth", LocalDate.parse(testPersonJsonPatch.get("dateOfBirth").toString()));

        logger.debug("Patched person: " + actualBodyPatch);
    }

    //PUT
    @Test
    public void person_PUT_should_return_OK_when_input_well_defined() throws JSONException {
        //creating a new entity
        String urlWithPort = createURLWithPort("/persons/add");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpRequest = new HttpEntity<>(testPersonJson.toString(), headers);

        ResponseEntity<PersonForm> actualResponse = restTemplate.exchange(urlWithPort, HttpMethod.POST, httpRequest, PersonForm.class);
        HttpStatus responseStatus = actualResponse.getStatusCode();

        PersonForm actualBody = actualResponse.getBody();
        String personToBePatchedId = actualBody.getId();

        assertThat(actualBody).hasFieldOrPropertyWithValue("firstName", testPersonJson.get("firstName"));
        assertThat(actualBody).hasFieldOrPropertyWithValue("lastName", testPersonJson.get("lastName"));
        assertThat(actualBody).hasFieldOrPropertyWithValue("firstNameMother", testPersonJson.get("firstNameMother"));
        assertThat(actualBody).hasFieldOrPropertyWithValue("lastNameMother", testPersonJson.get("lastNameMother"));
        assertThat(actualBody).hasFieldOrPropertyWithValue("dateOfBirth", testPersonJson.get("dateOfBirth"));

        Person personReadFromDatabase = (Person) personRepository.findById(Integer.parseInt(actualBody.getId()));
        assertThat(personReadFromDatabase).hasFieldOrPropertyWithValue("firstName", testPersonJson.get("firstName"));
        assertThat(personReadFromDatabase).hasFieldOrPropertyWithValue("lastName", testPersonJson.get("lastName"));
        assertThat(personReadFromDatabase).hasFieldOrPropertyWithValue("firstNameMother", testPersonJson.get("firstNameMother"));
        assertThat(personReadFromDatabase).hasFieldOrPropertyWithValue("lastNameMother", testPersonJson.get("lastNameMother"));
        assertThat(personReadFromDatabase).hasFieldOrPropertyWithValue("dateOfBirth", LocalDate.parse(testPersonJson.get("dateOfBirth").toString()));

        logger.debug("TestPerson created: " + actualBody + "\n ready to be patched");

        // defining put
        JSONObject testPersonJsonPut = new JSONObject();
        testPersonJsonPut.put("id", personToBePatchedId);
        testPersonJsonPut.put("firstName", firstName + 1000);
        testPersonJsonPut.put("lastName", lastName + 1000);
        testPersonJsonPut.put("dateOfBirth", "2000-12-12");
        testPersonJsonPut.put("firstNameMother", firstNameMother + 1000);
        testPersonJsonPut.put("lastNameMother", lastNameMother + 1000);
        logger.debug("put json: " + testPersonJsonPut.toString());

        String urlWithPortPatch = createURLWithPort("/persons/" + personToBePatchedId);

        HttpHeaders headersPut = new HttpHeaders();
        headersPut.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpRequestPut = new HttpEntity<>(testPersonJsonPut.toString(), headersPut);

//        ResponseEntity<PersonForm> actualResponsePut = patchRestTemplate.exchange(urlWithPortPatch, HttpMethod.PATCH, httpRequestPut, PersonForm.class);
        ResponseEntity<PersonForm> actualResponsePut = restTemplate.exchange(urlWithPortPatch, HttpMethod.PUT, httpRequestPut, PersonForm.class);

        HttpStatus responseStatusPut = actualResponsePut.getStatusCode();

        PersonForm actualBodyPut = actualResponsePut.getBody();
        logger.debug("Patched person response created: " + actualBodyPut);

        assertThat(actualBodyPut).hasFieldOrPropertyWithValue("firstName", testPersonJsonPut.get("firstName"));
        assertThat(actualBodyPut).hasFieldOrPropertyWithValue("lastName", testPersonJsonPut.get("lastName"));
        assertThat(actualBodyPut).hasFieldOrPropertyWithValue("firstNameMother", testPersonJsonPut.get("firstNameMother"));
        assertThat(actualBodyPut).hasFieldOrPropertyWithValue("lastNameMother", testPersonJsonPut.get("lastNameMother"));
        assertThat(actualBodyPut).hasFieldOrPropertyWithValue("dateOfBirth", testPersonJsonPut.get("dateOfBirth"));

        logger.debug("Put person: " + actualBodyPut);
    }
}
