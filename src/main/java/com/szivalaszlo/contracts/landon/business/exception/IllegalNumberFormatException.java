package com.szivalaszlo.contracts.landon.business.exception;

public class IllegalNumberFormatException extends NumberFormatException {
    private final String numberAsString;

    public IllegalNumberFormatException (String numberAsString){
        this.numberAsString = numberAsString;
    }

    public String getNumberAsString() {
        return numberAsString;
    }
}
