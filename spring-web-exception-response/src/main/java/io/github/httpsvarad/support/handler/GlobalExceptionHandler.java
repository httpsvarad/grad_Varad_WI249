package io.github.httpsvarad.support.handler;

import io.github.httpsvarad.support.exception.ApiException;
import io.github.httpsvarad.support.exception.ErrorCode;
import io.github.httpsvarad.support.model.ApiError;
import io.github.httpsvarad.support.properties.SupportProperties;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final SupportProperties properties;

    public GlobalExceptionHandler(SupportProperties properties) {
        this.properties = properties;
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleApiException(
            ApiException ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
                ex.getErrorCode().name(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(ex.getErrorCode().getStatus())
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(
            Exception ex,
            HttpServletRequest request) {

        String message = properties.isIncludeExceptionMessage()
                ? ex.getMessage()
                : "Unexpected error occurred";

        ApiError error = new ApiError(
                ErrorCode.INTERNAL_ERROR.name(),
                message,
                request.getRequestURI()
        );

        return ResponseEntity.internalServerError().body(error);
    }
}