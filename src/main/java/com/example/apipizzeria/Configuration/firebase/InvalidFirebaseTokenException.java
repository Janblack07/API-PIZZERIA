package com.example.apipizzeria.Configuration.firebase;

public class InvalidFirebaseTokenException extends RuntimeException {
    public InvalidFirebaseTokenException(String message) { super(message); }
    public InvalidFirebaseTokenException(String message, Throwable cause) { super(message, cause); }
}