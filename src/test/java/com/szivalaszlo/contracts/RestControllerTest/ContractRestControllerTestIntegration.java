package com.szivalaszlo.contracts.RestControllerTest;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.szivalaszlo.contracts.ContractsApplication;
import com.szivalaszlo.contracts.landon.business.domain.*;
import com.szivalaszlo.contracts.landon.business.service.ContractService;
import com.szivalaszlo.contracts.landon.business.service.PersonService;
import com.szivalaszlo.contracts.landon.data.repository.ContactdetailsRepository;
import com.szivalaszlo.contracts.landon.data.repository.ContractRepository;
import com.szivalaszlo.contracts.landon.data.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.json.JsonObject;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ContractsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ContractRestControllerTestIntegration {

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

    private HttpHeaders headers = new HttpHeaders();

    Random rand = new Random();
    int randomNumber = rand.nextInt(89999) + 1000;
    int randomPersonId = rand.nextInt(99) + 1;

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

    private ContractForm testContractForm;
    private JsonObject testContractFormJsonJavax;
    private ObjectNode testContractFormJsonJackson;
    JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;

    @Before
    public void setup() throws JSONException {
//        List<String> sellerIds = personIdListGenerator(1, 60, 5);
//        List<String> buyerIds = personIdListGenerator(61, 100, 5);

        List<String> sellerIds = personIdListGeneratorNumbersInSequence(1, 45, 5);
        List<String> buyerIds = personIdListGeneratorNumbersInSequence(50, 95, 5);


        testContractForm = new ContractForm("0", sellerIds, buyerIds, documentNumber, startDate, endDate, content);

//        JsonArrayBuilder sellerJsonArray = Json.createArrayBuilder();
//        for (String currentId : sellerIds) {
//            sellerJsonArray.add(currentId);
//        }
//
//        JsonArrayBuilder buyerJsonArray = Json.createArrayBuilder();
//        for (String currentId : buyerIds) {
//            buyerJsonArray.add(currentId);
//        }
//
//        testContractFormJsonJavax = Json.createObjectBuilder()
//                .add("documentNumber", documentNumber)
//                .add("startDate", startDate)
//                .add("endDate", endDate)
//                .add("content", content)
//                .add("sellerIds", sellerJsonArray)
//                .add("buyerIds", buyerJsonArray)
//                .build();

        testContractFormJsonJackson = jsonNodeFactory.objectNode();
        testContractFormJsonJackson.put("documentNumber", documentNumber);
        testContractFormJsonJackson.put("startDate", startDate);
        testContractFormJsonJackson.put("endDate", endDate);
        testContractFormJsonJackson.put("content", content);

        ArrayNode sellerIdsArrayNode = testContractFormJsonJackson.putArray("sellerIds");
        for (String currentId : sellerIds) {
            sellerIdsArrayNode.add(currentId);
        }
        ArrayNode buyerIdsArrayNode = testContractFormJsonJackson.putArray("buyerIds");
        for (String currentId : buyerIds) {
            buyerIdsArrayNode.add(currentId);
        }
    }

    public List<String> personIdListGenerator(int from, int to, int numberOfIds) {
        List<String> personIdList = new ArrayList<>();
        for (int i = 0; i < numberOfIds; i++) {
            int randomPersonId = rand.nextInt(to - from) + from;
            personIdList.add(Integer.toString(randomPersonId));
        }
        return personIdList;
    }

    public List<String> personIdListGeneratorNumbersInSequence(int from, int to, int numberOfIds) {
        List<String> personIdList = new ArrayList<>();
        int randomPersonId = rand.nextInt(to - from) + from;
        for (int i = 0; i < numberOfIds; i++) {
            personIdList.add(Integer.toString(randomPersonId+i));
        }
        return personIdList;
    }



    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + "/contracts/api" + uri;
    }

    //GET

    @Test
    @Transactional
    public void contract_id_GET_should_return_EXISTING_contract_with_OK() {
        String urlWithPort = createURLWithPort("/contracts/id/10");

        ResponseEntity<ContractForm> actualResponse = restTemplate.getForEntity(urlWithPort, ContractForm.class);
        HttpStatus responseStatus = actualResponse.getStatusCode();
        ContractForm expectedResponse = new ContractFormConverter(contractRepository.findById(10)).getContractFormInstance();

        assertThat(responseStatus).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(expectedResponse);
        logger.debug("Actual response body: " + actualResponse.getBody());
        logger.debug("PersonRepository findbyId expected: " + expectedResponse);
    }

    @Test
    public void contract_id_GET_should_return_NOT_FOUND_for_not_existing_contract() {
        String urlWithPort = createURLWithPort("/contracts/id/-1");

        ResponseEntity<ContractForm> actualResponse = restTemplate.getForEntity(urlWithPort, ContractForm.class);
        HttpStatus responseStatus = actualResponse.getStatusCode();

        assertThat(responseStatus).isEqualTo(HttpStatus.NOT_FOUND);
        logger.debug("Actual response body: " + actualResponse.getBody());
    }

    @Test
    public void contract_id_GET_should_return_BAD_REQUEST_for_not_number_person_id() {
        String urlWithPort = createURLWithPort("/contracts/id/AAA");

        ResponseEntity<ContractForm> actualResponse = restTemplate.getForEntity(urlWithPort, ContractForm.class);
        HttpStatus responseStatus = actualResponse.getStatusCode();

        assertThat(responseStatus).isEqualTo(HttpStatus.BAD_REQUEST);
        logger.debug("Actual response body: " + actualResponse.getBody());
    }

    @Test
    @Transactional
    public void contract_target_date_GET_should_return_EXISTING_contract_list_with_OK() {
        String targetDate = "2000-01-01";
        String urlWithPort = createURLWithPort("/contracts/activeondate/" + targetDate);

        ResponseEntity<List<ContractForm>> actualResponse = restTemplate.exchange(
                urlWithPort,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ContractForm>>() {
                });
        HttpStatus responseStatus = actualResponse.getStatusCode();

        List<ContractForm> expectedResponse = contractService.findActiveContractsOnDate(targetDate);

        assertThat(responseStatus).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(expectedResponse);
        logger.debug("Actual response body: " + actualResponse.getBody());
        logger.debug("PersonRepository findbyId expected: " + expectedResponse);
    }

    @Test
    public void contract_target_date_GET_should_return_NOT_FOUND_for_not_existing_contract() {
        String targetDate = "2200-01-01";
        String urlWithPort = createURLWithPort("/contracts/activeondate/" + targetDate);

        ResponseEntity<ContractForm> actualResponse = restTemplate.getForEntity(urlWithPort, ContractForm.class);
        HttpStatus responseStatus = actualResponse.getStatusCode();

        assertThat(responseStatus).isEqualTo(HttpStatus.NOT_FOUND);
        logger.debug("Actual response body: " + actualResponse.getBody());
    }

    @Test
    @Transactional
    public void contract_last_name_GET_should_return_EXISTING_contract_list_with_OK() {
        String lastName = "Mills";
        String urlWithPort = createURLWithPort("/contracts/lastname/" + lastName);

        ResponseEntity<List<ContractForm>> actualResponse = restTemplate.exchange(
                urlWithPort,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ContractForm>>() {
                });
        HttpStatus responseStatus = actualResponse.getStatusCode();

        List<ContractForm> expectedResponse = contractService.findByLastName(lastName);

        assertThat(responseStatus).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(expectedResponse);
        logger.debug("Actual response body: " + actualResponse.getBody());
        logger.debug("PersonRepository findbyId expected: " + expectedResponse);
    }

    @Test
    public void contract_last_name_GET_should_return_NOT_FOUND_for_not_existing_contract() {
        String lastName = "noname";
        String urlWithPort = createURLWithPort("/contracts/lastname/" + lastName);

        ResponseEntity<ContractForm> actualResponse = restTemplate.getForEntity(urlWithPort, ContractForm.class);
        HttpStatus responseStatus = actualResponse.getStatusCode();

        assertThat(responseStatus).isEqualTo(HttpStatus.NOT_FOUND);
        logger.debug("Actual response body: " + actualResponse.getBody());
    }

    //POST
    @Test
    @Transactional
    public void contract_POST_should_return_CREATED_when_fully_defined_and_new() throws JSONException {
        String urlWithPort = createURLWithPort("/contracts/add");
        logger.debug("Test Json: " + testContractFormJsonJackson);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpRequest = new HttpEntity<>(testContractFormJsonJackson.toString(), headers);

        ResponseEntity<ContractForm> actualResponse = restTemplate.exchange(urlWithPort, HttpMethod.POST, httpRequest, ContractForm.class);
        HttpStatus responseStatus = actualResponse.getStatusCode();

        ContractForm actualBody = actualResponse.getBody();
        logger.debug("actual body: " + actualBody);

        assertThat(actualBody.getDocumentNumber().equals(testContractFormJsonJackson.get("documentNumber")));
        assertThat(actualBody.getStartDate().equals(testContractFormJsonJackson.get("startDate")));
        assertThat(actualBody.getEndDate().equals(testContractFormJsonJackson.get("endDate")));
        assertThat(actualBody.getContent().equals(testContractFormJsonJackson.get("content")));
        assertThat(actualBody.getSellerIds().equals(testContractFormJsonJackson.get("sellerIds")));
        assertThat(actualBody.getBuyerIds().equals(testContractFormJsonJackson.get("buyerIds")));
    }

    @Test
    public void contract_POST_should_return_UNPROCESSABLE_ENTITY_when_any_field_is_missing() {
        String urlWithPort = createURLWithPort("/contracts/add");
        List<String> contractFormFields = new ArrayList<>();

        contractFormFields.add("documentNumber");
        contractFormFields.add("startDate");
        contractFormFields.add("endDate");
        contractFormFields.add("content");
        contractFormFields.add("buyerIds");
        contractFormFields.add("sellerIds");

        for (String currentField : contractFormFields) {
            testContractFormJsonJackson.remove(currentField);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> httpRequest = new HttpEntity<>(testContractFormJsonJackson.toString(), headers);
            ResponseEntity<ContractForm> actualResponse = restTemplate.exchange(urlWithPort, HttpMethod.POST, httpRequest, ContractForm.class);
            HttpStatus responseStatus = actualResponse.getStatusCode();

            assertThat(responseStatus).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
            logger.debug("Actual response body: " + actualResponse.getBody());
        }
    }

    @Test
    public void contract_POST_should_return_UNPROCESSABLE_ENTITY_when_sellerIds_or_buyerIds_list_is_empty() {
        String urlWithPort = createURLWithPort("/contracts/add");

        List<String> contractFormFields = new ArrayList<>();
        contractFormFields.add("buyerIds");
        contractFormFields.add("sellerIds");

        for (String currentField : contractFormFields) {

            testContractFormJsonJackson.remove(currentField);
            ArrayNode sellerIdsArrayNode = testContractFormJsonJackson.putArray(currentField); //empty array

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> httpRequest = new HttpEntity<>(testContractFormJsonJackson.toString(), headers);

            ResponseEntity<ContractForm> actualResponse = restTemplate.exchange(urlWithPort, HttpMethod.POST, httpRequest, ContractForm.class);
            HttpStatus responseStatus = actualResponse.getStatusCode();

            assertThat(responseStatus).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
            logger.debug("Actual response body: " + actualResponse.getBody());
        }
    }

    @Test
    public void contract_POST_should_return_UNPROCESSABLE_ENTITY_when_sellerIds_and_buyerIds_have_same_elements() {
        String urlWithPort = createURLWithPort("/contracts/add");

        ArrayNode sellerIdsArrayNode = testContractFormJsonJackson.withArray("sellerIds");
        sellerIdsArrayNode.add("999");
        ArrayNode buyerIdsArrayNode = testContractFormJsonJackson.withArray("buyerIds");
        buyerIdsArrayNode.add("999");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpRequest = new HttpEntity<>(testContractFormJsonJackson.toString(), headers);

        ResponseEntity<ContractForm> actualResponse = restTemplate.exchange(urlWithPort, HttpMethod.POST, httpRequest, ContractForm.class);
        HttpStatus responseStatus = actualResponse.getStatusCode();

        assertThat(responseStatus).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        logger.debug("Actual response body: " + actualResponse.getBody());
    }

    @Test
    public void contract_POST_should_return_UNPROCESSABLE_ENTITY_when_start_or_end_date_is_not_legal_date_format() {
        String urlWithPort = createURLWithPort("/contracts/add");
        String dateFormatTester = "01-12-1990";
        List<String> contractFormFields = new ArrayList<>();
        contractFormFields.add("startDate");
        contractFormFields.add("endDate");
        for (String currentField : contractFormFields) {

            testContractFormJsonJackson.remove(currentField);
            testContractFormJsonJackson.put(currentField, dateFormatTester);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> httpRequest = new HttpEntity<>(testContractFormJsonJackson.toString(), headers);

            ResponseEntity<ContractForm> actualResponse = restTemplate.exchange(urlWithPort, HttpMethod.POST, httpRequest, ContractForm.class);
            HttpStatus responseStatus = actualResponse.getStatusCode();

            assertThat(responseStatus).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
            logger.debug("Actual response body: " + actualResponse.getBody());
        }
    }

//    @Test
//    @Transactional
//    public void contract_POST_should_return_CREATED_when_fully_defined_and_new() throws JSONException {
//        String urlWithPort = createURLWithPort("/contracts/add");
//        logger.debug("Test Json: " + testContractFormJsonJavax);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> httpRequest = new HttpEntity<>(testContractFormJsonJavax.toString(), headers);
//
//        ResponseEntity<ContractForm> actualResponse = restTemplate.exchange(urlWithPort, HttpMethod.POST, httpRequest, ContractForm.class);
//        HttpStatus responseStatus = actualResponse.getStatusCode();
//
//        ContractForm actualBody = actualResponse.getBody();
//        logger.debug("actual body: " + actualBody);
//
//        assertThat(actualBody).hasFieldOrPropertyWithValue("documentNumber", testContractFormJsonJavax.getString("documentNumber"));
//        assertThat(actualBody).hasFieldOrPropertyWithValue("startDate", testContractFormJsonJavax.getString("startDate"));
//        assertThat(actualBody).hasFieldOrPropertyWithValue("endDate", testContractFormJsonJavax.getString("endDate"));
//        assertThat(actualBody).hasFieldOrPropertyWithValue("content", testContractFormJsonJavax.getString("content"));
//
//        Set<Integer> sellersJson = convertIdStringToIntegerSet(testContractFormJsonJavax.get("sellerIds").toString());
//        Set<Integer> sellerActual = convertIdStringToIntegerSet(actualBody.getSellerIds().toString());
//        Set<Integer> buyersJson = convertIdStringToIntegerSet(testContractFormJsonJavax.get("buyerIds").toString());
//        Set<Integer> buyerActual = convertIdStringToIntegerSet(actualBody.getBuyerIds().toString());
//
//        logger.debug("sellerJson: " + sellersJson);
//        logger.debug("sellerActual: " + sellerActual);
//        logger.debug("buyerJson: " + buyersJson);
//        logger.debug("buyerActual: " + buyerActual);
//
//        assertThat(sellersJson).isEqualTo(sellerActual);
//        assertThat(buyersJson).isEqualTo(buyerActual);
//    }
//
//    private Set<Integer> convertIdStringToIntegerSet(String ids) {
//        Set<Integer> integerIds = new HashSet<>();
//        String filtered = ids.replaceAll("[^0-9,]", "");
////        logger.debug("filtered string with numbers only: " + filtered);
//        String[] numbersStringArray = filtered.split(",");
//        for (int i = 0; i < numbersStringArray.length; i++) {
//            integerIds.add(Integer.parseInt(numbersStringArray[i]));
//        }
//        return integerIds;
//    }
}





