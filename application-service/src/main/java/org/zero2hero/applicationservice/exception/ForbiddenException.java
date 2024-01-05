package org.zero2hero.applicationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created By Hasan-Murat Kücüközdemir
 * Date : 29.12.2023
 */
// ForbiddenException.java
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

    private final String message;

    public ForbiddenException(String message) {
        super(message);
        this.message = message;
    }


}
