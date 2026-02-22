# Spring Web Exception Response

[![Maven Central](https://img.shields.io/maven-central/v/io.github.httpsvarad/spring-web-exception-response.svg)](https://search.maven.org/artifact/io.github.httpsvarad/spring-web-exception-response)

A lightweight Spring Boot support library that provides standardized global exception handling and structured API error responses.

---

## Overview

Spring Web Exception Response simplifies error handling in Spring Boot applications by:

- Providing a global exception handler
- Returning consistent JSON error responses
- Supporting custom business exceptions
- Allowing configurable error message visibility
- Being production-safe by default

This library helps you maintain clean, consistent, and secure API responses across your services.

---

## Installation

Add the dependency:

```xml
<dependency>
    <groupId>io.github.httpsvarad</groupId>
    <artifactId>spring-web-exception-response</artifactId>
    <version>1.0.0</version>
</dependency>
````

---

## Optional Configuration

By default, detailed exception messages are hidden for security reasons.

To expose actual exception messages (recommended only in development):

### application.properties

```properties
support.include-exception-message=true
```

### application.yml

```yaml
support:
  include-exception-message: true
```

Default value:

```
false
```

---

## Usage

### 1️⃣ Throw Custom Exception

```java
import io.github.httpsvarad.support.exception.ApiException;
import io.github.httpsvarad.support.exception.ErrorCode;

throw new ApiException(
        ErrorCode.RESOURCE_NOT_FOUND,
        "User not found"
);
```

---

### 2️⃣ Example Controller

```java
@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/{id}")
    public String getUser(@PathVariable Long id) {

        if (id == 0) {
            throw new ApiException(
                    ErrorCode.RESOURCE_NOT_FOUND,
                    "User not found"
            );
        }

        return "User found";
    }
}
```

---

## Sample Error Response

```json
{
  "errorCode": "RESOURCE_NOT_FOUND",
  "message": "User not found",
  "path": "/users/0",
  "timestamp": "2026-02-21T12:00:00"
}
```

---

## Production Safety

If configured as:

```properties
support.include-exception-message=false
```

Internal exception details are hidden automatically:

```json
{
  "errorCode": "INTERNAL_ERROR",
  "message": "Unexpected error occurred",
  "path": "/users/0",
  "timestamp": "..."
}
```

This prevents leaking internal stack traces or sensitive information.

---

## Supported Error Codes

* `RESOURCE_NOT_FOUND`
* `RESOURCE_ALREADY_EXISTS`
* `BAD_REQUEST`
* `INTERNAL_ERROR`

You can extend the `ErrorCode` enum to add custom domain-specific error codes.

---

## Requirements

* Java 17+
* Spring Boot 3+

---

## How It Works

* Registers a global `@RestControllerAdvice`
* Handles `ApiException`
* Handles generic `Exception`
* Returns standardized `ApiError` response model
* Supports configuration via `@ConfigurationProperties`

No additional setup required. Just add the dependency and use.

---

## License

This project is licensed under the Apache License 2.0.

---

## Author

**Varad Manegopale**

[GitHub](https://github.com/httpsvarad) | [LinkedIn](https://www.linkedin.com/in/varadmanegopale/)
