package com.szivalaszlo.contracts.landon.business.domain;

import com.szivalaszlo.contracts.landon.data.entity.Contactdetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContactdetailsFormConverter {

    private static Logger logger = LogManager.getLogger();

    public Contactdetails contactdetails;
    public ContactdetailsForm contactdetailsForm;

    public ContactdetailsFormConverter(Contactdetails contactdetails){
        this.contactdetails = contactdetails;
        logger.debug("Inside ContactdetailsFormConverter. Current Contactdetails to convert: " + contactdetails);
        contactdetailsForm = new ContactdetailsForm();
        setAllAttributesOnContactdetailsForm();
    }

    private void setAllAttributesOnContactdetailsForm(){
        contactdetailsForm.setId(Integer.toString(contactdetails.getId()));
        contactdetailsForm.setPersonId(Integer.toString(contactdetails.getPerson().getId()));
        contactdetailsForm.setAddress(contactdetails.getAddress());
        contactdetailsForm.setEmail(contactdetails.getEmail());
        contactdetailsForm.setPhone(contactdetails.getPhone());
    }

    public ContactdetailsForm getContactdetailsFormInstance(){
        return contactdetailsForm;
    }
}
