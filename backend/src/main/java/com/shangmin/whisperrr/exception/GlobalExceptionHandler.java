package com.shangmin.whisperrr.exception;

import com.shangmin.whisperrr.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.util.ConcurrentModificationException;

/**
 * Global exception handler for the application
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(FileValidationException.class)
    public ResponseEntity<ErrorResponse> handleFileValidationException(FileValidationException ex) {
        logger.warn("File validation error: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
            "FILE_VALIDATION_ERROR",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(error);
    }
    
    @ExceptionHandler(TranscriptionProcessingException.class)
    public ResponseEntity<ErrorResponse> handleTranscriptionProcessingException(TranscriptionProcessingException ex) {
        logger.error("Transcription processing error: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse(
            "TRANSCRIPTION_PROCESSING_ERROR",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        logger.warn("Validation error: {}", ex.getMessage());
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .findFirst()
            .orElse("Validation failed");
        
        ErrorResponse error = new ErrorResponse(
            "VALIDATION_ERROR",
            message,
            LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(error);
    }
    
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        logger.warn("File size exceeded limit: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
            "FILE_SIZE_EXCEEDED",
            "File size exceeds the maximum allowed limit of 25MB",
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(error);
    }
    
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ErrorResponse> handleResourceAccessException(ResourceAccessException ex) {
        logger.error("Failed to access transcription service: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse(
            "SERVICE_UNAVAILABLE",
            "Transcription service is currently unavailable. Please try again later.",
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }
    
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleHttpClientErrorException(HttpClientErrorException ex) {
        logger.warn("Client error from transcription service ({}): {}", ex.getStatusCode(), ex.getMessage());
        ErrorResponse error = new ErrorResponse(
            "TRANSCRIPTION_SERVICE_ERROR",
            "Transcription service returned an error: " + ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(ex.getStatusCode()).body(error);
    }
    
    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleHttpServerErrorException(HttpServerErrorException ex) {
        logger.error("Server error from transcription service ({}): {}", ex.getStatusCode(), ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse(
            "TRANSCRIPTION_SERVICE_ERROR",
            "Transcription service encountered an error. Please try again later.",
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error);
    }
    
    @ExceptionHandler(ConcurrentModificationException.class)
    public ResponseEntity<ErrorResponse> handleConcurrentModificationException(ConcurrentModificationException ex) {
        logger.warn("Concurrent modification: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
            "CONCURRENT_MODIFICATION",
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logger.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse(
            "INTERNAL_SERVER_ERROR",
            "An unexpected error occurred",
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
