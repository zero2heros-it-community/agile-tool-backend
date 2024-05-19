package org.zero2hero.applicationservice.exception;

public class BelongsToAnotherUserException extends RuntimeException {
        public BelongsToAnotherUserException(String message) {
            super(message);
        }

}
