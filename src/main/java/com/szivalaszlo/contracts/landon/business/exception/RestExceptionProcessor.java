package com.szivalaszlo.contracts.landon.business.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestExceptionProcessor {

    private static Logger logger = LogManager.getLogger();

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorInfo entityNotFound(HttpServletRequest request, EntityNotFoundException exception) {
        List<String> messages = new ArrayList<>();
        messages.add(exception.getMessage());
        logger.debug("Error message: " + messages);
        return new ErrorInfo(HttpStatus.NOT_FOUND, request.getRequestURI(), messages);
    }


    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ErrorInfo entityAlreadyExists(HttpServletRequest request, EntityAlreadyExistsException exception) {
        List<String> messages = new ArrayList<>();
        messages.add(exception.getMessage());
        logger.debug("Error message: " + messages);
        return new ErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY, request.getRequestURI(), messages);
    }

    @ExceptionHandler(IllegalNumberFormatException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorInfo personNotFound(HttpServletRequest request, IllegalNumberFormatException exception) {
        List<String> messages = new ArrayList<>();
        messages.add("String cannot be parsed to Integer:   " + exception.getNumberAsString());
        logger.debug("Error message: " + messages);
        return new ErrorInfo(HttpStatus.BAD_REQUEST, request.getRequestURI(), messages);
    }


    @ExceptionHandler(FieldValidationErrorException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ErrorInfo fieldValidationFailed(HttpServletRequest request, FieldValidationErrorException exception) {
        BindingResult bindingResultOfFieldValidation = exception.getBindingResult();
        List<String> messages = new ArrayList<>();
        messages.add("Rejected field(s) : ");
        List<FieldError> fieldErrors = bindingResultOfFieldValidation.getFieldErrors();
        for (FieldError currentFieldError : fieldErrors) {
            messages.add(currentFieldError.getField() + " : " + currentFieldError.getRejectedValue() + "  *** Reason : " + currentFieldError.getDefaultMessage());
        }
        logger.debug("Found field validation errors: " + messages);
        return new ErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY, request.getRequestURI(), messages);
    }

    @ExceptionHandler(SamePersonIsSellerAndBuyerException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ErrorInfo SameBuyerAndSellerDefinedInContract(HttpServletRequest request, SamePersonIsSellerAndBuyerException exception) {
        List<String> messages = new ArrayList<>();
        messages.add(exception.getMessage());
        logger.debug("Error message: " + messages);
        return new ErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY, request.getRequestURI(), messages);
    }

    @ExceptionHandler(NoSellerOrBuyerDefinedException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ErrorInfo NoSellerOrBuyerDefinedInContract(HttpServletRequest request, NoSellerOrBuyerDefinedException exception) {
        List<String> messages = new ArrayList<>();
        messages.add(exception.getMessage());
        logger.debug("Error message: " + messages);
        return new ErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY, request.getRequestURI(), messages);
    }
}
