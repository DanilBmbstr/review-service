package org.example.reviewservice.exception;

public class IllegalRatingException extends IllegalArgumentException{
    public IllegalRatingException(String message) {
        super(message);
    }

    public IllegalRatingException(String message,Throwable cause) {
        super(message, cause);
    }
}
