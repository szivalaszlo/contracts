package com.szivalaszlo.contracts.toExploreFurtherLater;
import com.szivalaszlo.contracts.landon.business.domain.PersonForm;
import com.szivalaszlo.contracts.landon.business.domain.PersonFormConverter;
import com.szivalaszlo.contracts.landon.business.service.PersonService;
import com.szivalaszlo.contracts.landon.data.entity.Person;
import com.szivalaszlo.contracts.landon.data.repository.ContactdetailsRepository;
import com.szivalaszlo.contracts.landon.data.repository.ContractRepository;
import com.szivalaszlo.contracts.landon.data.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonLightConverterTest {

    private static Logger logger = LogManager.getLogger();

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ContactdetailsRepository contactdetailsRepository;

    @Autowired
    private ContractRepository contractRepository;


//    @Test
//    @Transactional(propagation = Propagation.REQUIRED)
//    @Rollback(false)
//    public void it_should_create_all_person_light(){
//        PersonService testPersonService = new PersonService(personRepository, contactdetailsRepository, contractRepository);
//        List<Person> allPersonFromDb = testPersonService.findAll();
//        List<PersonForm> allPersonFormFromDb = new ArrayList<>();
//
//        for(Person currentPerson : allPersonFromDb){
//            PersonForm curretnPersonForm = new PersonFormConverter(currentPerson).getPersonFormInstance();
//            allPersonFormFromDb.add(curretnPersonForm);
//            logger.debug(curretnPersonForm.toString());
//        }
//
//    }


}
