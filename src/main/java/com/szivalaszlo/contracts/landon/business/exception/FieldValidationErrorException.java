package com.szivalaszlo.contracts.landon.business.exception;

import org.springframework.validation.BindingResult;

public class FieldValidationErrorException extends RuntimeException {
    private final BindingResult bindingResult;

    public FieldValidationErrorException(BindingResult bindingResult){
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult(){
        return bindingResult;
    }
}
