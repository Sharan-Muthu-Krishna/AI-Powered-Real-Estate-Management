// FileStorageException.java - src/main/java/com/realestate/management/exception/FileStorageException.java
package com.realestate.management.exception;

public class FileStorageException extends RuntimeException {
    public FileStorageException(String message) {
        super(message);
    }
    
    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}