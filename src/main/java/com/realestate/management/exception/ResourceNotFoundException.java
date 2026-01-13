// ResourceNotFoundException.java - src/main/java/com/realestate/management/exception/ResourceNotFoundException.java
package com.realestate.management.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}