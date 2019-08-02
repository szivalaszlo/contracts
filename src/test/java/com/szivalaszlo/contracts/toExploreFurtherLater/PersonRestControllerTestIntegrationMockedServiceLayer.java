package com.szivalaszlo.contracts.toExploreFurtherLater;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szivalaszlo.contracts.landon.business.domain.PersonForm;
import com.szivalaszlo.contracts.landon.business.service.PersonService;
import com.szivalaszlo.contracts.landon.web.application.PersonRestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonRestControllerTestIntegrationMockedServiceLayer {

    private static Logger logger = LogManager.getLogger();

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private PersonService personService;

    private JacksonTester<PersonForm> jsonPersonForm;

    @Before
    public void setup() {
        // Initializes the JacksonTester
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void it_should_return_person_by_id() {
        //given
        String urlWithPort = createURLWithPort("/contracts/api/persons/10");
        String urlApi = "http://localhost:"+port+"/contracts/api";

        given(personService.findById("10"))
                .willReturn(new PersonForm("10", "fname", "lname", "1990-01-01", "mfname", "mlname"));

        //when
        ResponseEntity<PersonForm> personByIdResponse = restTemplate.getForEntity(urlApi+"/persons/10", PersonForm.class);
        logger.debug("personFormResponse : " + personByIdResponse);
        HttpStatus responseStatus = personByIdResponse.getStatusCode();

        //then
        assertThat(responseStatus).isEqualTo(HttpStatus.CREATED);

    }
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }


}
