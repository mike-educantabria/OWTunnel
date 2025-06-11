package com.owtunnel.exception;

public class EntityAlreadyExistsException extends RuntimeException {

    public EntityAlreadyExistsException(String name, Object id) {
        super(String.format("%s with ID %s already exists.", name, id));
    }
}
