package com.szivalaszlo.contracts.landon.business.exception;

public class EntityAlreadyExistsException extends  RuntimeException {
    String entityType;
    Object entityForm;
    String message;

    public EntityAlreadyExistsException(String entityType, Object entityForm){
        this.entityType = entityType;
        this.entityForm = entityForm;
        this.message = entityType + " already exists: " + entityForm.toString();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
