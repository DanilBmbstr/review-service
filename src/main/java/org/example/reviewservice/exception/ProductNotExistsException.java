package org.example.reviewservice.exception;

public class ProductNotExistsException extends IllegalArgumentException {
    public ProductNotExistsException(String message) {
        super(message);
    }
    public ProductNotExistsException(String message,Throwable cause) {
        super(message, cause);
    }
}
