package com.skillshare.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.mongodb.MongoException;
import com.mongodb.MongoTimeoutException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class DatabaseExceptionHandler {

    @ExceptionHandler({
        MongoException.class,
        MongoTimeoutException.class,
        DataAccessResourceFailureException.class,
        UncategorizedMongoDbException.class,
        DataAccessException.class
    })
    public ResponseEntity<?> handleDatabaseConnectionException(Exception ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", new Date());
        response.put("status", "error");
        response.put("code", "DATABASE_ERROR");
        
        if (ex instanceof MongoTimeoutException) {
            response.put("message", "Database connection timed out. Please try again later.");
        } else if (ex instanceof DataAccessResourceFailureException) {
            response.put("message", "Cannot connect to the database. Please check your connection and try again.");
        } else {
            response.put("message", "A database error occurred. Please try again later.");
        }
        
        response.put("details", ex.getMessage());
        response.put("path", request.getDescription(false));
        
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }
}