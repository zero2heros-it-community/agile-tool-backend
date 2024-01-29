package org.zero2hero.applicationservice.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NameFormatException.class)
    public ResponseEntity<?> wrongFormat(NameFormatException nameFormatException) {
        ErrorResponse errorResponse = new ErrorResponse(nameFormatException.getMessage(),  HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<?> alreadyExist(AlreadyExistException alreadyExistException){
        ErrorResponse errorResponse = new ErrorResponse(alreadyExistException.getMessage(), HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFound(NotFoundException notFoundException){
        ErrorResponse errorResponse = new ErrorResponse(notFoundException.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> forbidden(ForbiddenException forbiddenException){
        ErrorResponse errorResponse = new ErrorResponse(forbiddenException.getMessage(), HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(IdFormatException.class)
    public ResponseEntity<?> idFormat(IdFormatException idFormatException){
        ErrorResponse errorResponse = new ErrorResponse(idFormatException.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}