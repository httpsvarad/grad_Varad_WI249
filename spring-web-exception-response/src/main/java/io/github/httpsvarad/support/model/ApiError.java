package io.github.httpsvarad.support.model;

import java.time.LocalDateTime;

public class ApiError {

    private final String timestamp = LocalDateTime.now().toString();
    private final String errorCode;
    private final String message;
    private final String path;

    public ApiError(String errorCode,
                    String message,
                    String path) {

        this.errorCode = errorCode;
        this.message = message;
        this.path = path;
    }

    public String getTimestamp() { return timestamp; }
    public String getErrorCode() { return errorCode; }
    public String getMessage() { return message; }
    public String getPath() { return path; }
}