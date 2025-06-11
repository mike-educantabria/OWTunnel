package com.owtunnel.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String name, Object id) {
        super(String.format("%s with ID %s not found.", name, id));
    }
}
