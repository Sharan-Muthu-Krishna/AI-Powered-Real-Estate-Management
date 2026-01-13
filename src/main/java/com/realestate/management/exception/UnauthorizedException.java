// UnauthorizedException.java - src/main/java/com/realestate/management/exception/UnauthorizedException.java
package com.realestate.management.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}