package com.szivalaszlo.contracts.landon.business.exception;

public class EntityNotFoundException extends RuntimeException {
    String entityType;
    String id;
    int idNo;
    String message;

    public EntityNotFoundException(String entityType, String id){
        this.entityType = entityType;
        this.id = id;
        this.message = entityType + " id: " + id + " not found.";
    }

    public EntityNotFoundException(String entityType, int id){
        this.entityType = entityType;
        this.idNo = id;
        this.message = entityType + " id: " + idNo + " not found.";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
