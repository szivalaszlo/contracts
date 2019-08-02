package com.szivalaszlo.contracts.toExploreFurtherLater;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szivalaszlo.contracts.landon.business.exception.RestExceptionProcessor;
import com.szivalaszlo.contracts.landon.business.service.PersonService;
import com.szivalaszlo.contracts.landon.data.entity.Person;
import com.szivalaszlo.contracts.landon.data.repository.PersonRepository;
import com.szivalaszlo.contracts.landon.web.application.PersonRestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;



@RunWith(MockitoJUnitRunner.class)
public class PersonRestControllerTestStandaloneNoContextLoaded {

    private static Logger logger = LogManager.getLogger();

    @Autowired
    MockMvc mockMvc;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonService personService;

    @InjectMocks  //injects the above @Mock (mocked) objects into this one
    private PersonRestController personRestController;

    private JacksonTester<Person> jsonPerson;

    @Before
    public void setup(){
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(PersonRestController.class)
                .setControllerAdvice(new RestExceptionProcessor())
                .build();
    }

    @Test
    public void it_should_find_person_by_id() throws Exception{
        // given
        given(personRepository.findById(1))
                .willReturn(new Person("fir", "sec", "1990-01-01", "mfir", "mlas"));

        // when
        MockHttpServletResponse response = mockMvc.perform(
                get("/persons/1")
                        .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        assertThat(response.getContentAsString()).isEqualTo(
                jsonPerson.write(new Person("fir", "sec", "1990-01-01", "mfir", "mlas")).getJson()
        );
    }




}
