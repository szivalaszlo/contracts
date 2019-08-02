package com.szivalaszlo.contracts.landon.business.exception;

import com.szivalaszlo.contracts.landon.business.domain.ContractForm;

import java.util.List;

public class SamePersonIsSellerAndBuyerException extends RuntimeException {
    List<String> personIds;
    private ContractForm contractForm;
    private String message;

    public SamePersonIsSellerAndBuyerException(List<String> personIds, ContractForm contractForm) {
        this.personIds = personIds;
        this.contractForm = contractForm;
        message = "The following personIds are defined both as seller and buyer: " + personIds +
                "\n in contract input: " + contractForm;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
