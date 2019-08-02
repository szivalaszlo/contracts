package com.szivalaszlo.contracts.landon.business.exception;

import com.szivalaszlo.contracts.landon.business.domain.ContractForm;

public class NoSellerOrBuyerDefinedException extends RuntimeException {
    private String message;
    private String personType;
    private ContractForm contractForm;

    public NoSellerOrBuyerDefinedException(String personType, ContractForm contractForm){
        this.personType = personType;
        this.contractForm = contractForm;
        message = "No " + personType + " is defined in contract input: " + contractForm;
    }


    @Override
    public String getMessage() {
        return message;
    }

}
