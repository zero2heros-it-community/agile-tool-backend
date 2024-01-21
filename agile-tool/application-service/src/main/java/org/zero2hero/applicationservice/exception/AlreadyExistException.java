package org.zero2hero.applicationservice.exception;


public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(String message) {
        super(message);
    }
}