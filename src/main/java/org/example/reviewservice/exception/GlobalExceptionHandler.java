package org.example.reviewservice.exception;

import io.jsonwebtoken.io.SerialException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationExceptions(MethodArgumentNotValidException ex) {



        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Validation failed"
        );
        problemDetail.setTitle("Review publication error");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        problemDetail.setProperty("Errors: ", errors.toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleHttpNotReadableException(HttpMessageNotReadableException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Failed to read request"
        );
        problemDetail.setTitle("Review publication error");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        String cause = ex.getMostSpecificCause().toString();
        problemDetail.setProperty("Error: ", cause);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }



    @ExceptionHandler(ProductNotExistsException.class)
    public ResponseEntity<ProblemDetail> handleProductNotExistsException(ProductNotExistsException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "Product does not exist"
        );
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }


    @ExceptionHandler(SerialException.class)
    public ResponseEntity<ProblemDetail> handeKafkaProducerNotWorkingException(SerialException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Failed serialize review"
        );
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("Message", ex.getCause().getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);

    }


    @ExceptionHandler(KafkaProducerException.class)
    public ResponseEntity<ProblemDetail> handeKafkaProducerNotWorkingException(KafkaProducerException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Failed to send kafka message"
        );
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("Message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);

    }
        };



