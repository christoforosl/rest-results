# RestResult

## Overview

`RestResult` is a Java class designed to encapsulate the result of a REST API operation. It includes the status, result data, and error information. The class provides static factory methods for creating instances representing successful or error results.

## Features

- Encapsulates the result of a REST API call.
- Provides factory methods for creating success and error results.
- Masks error messages in production environments for security.
- Uses Lombok for boilerplate code reduction.

## Usage

### Creating a Success Result

To create a success result, use the `success` static method and pass the result data:

```java
RestResult successResult = RestResult.success(results);
```

### Creating an Error Result

To create an error result with a custom error message:

```java
RestResult errorResult = RestResult.error("Error message");
```

To create an error result with an exception:

```java
RestResult errorResult = RestResult.error(new Exception("Exception message"));
```

## Environment Configuration

The class checks if the current environment is production by inspecting system properties `spring.profiles.active` or `rest-results.env`. If either property is set to `prod` or `production`, the environment is considered production.

## Dependencies

- Jackson for JSON inclusion annotations.
- Lombok for reducing boilerplate code.
- Java Logging API for logging errors.

## Author

- christoforosl

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Java File Details

- **Package**: `io.github.christoforosl`
- **Imports**:
  - `com.fasterxml.jackson.annotation.JsonInclude`
  - `java.util.logging.Level`
  - `java.util.logging.Logger`
  - `lombok.AccessLevel`
  - `lombok.Builder`
  - `lombok.Data`

- **Class**: `RestResult`
  - **Annotations**:
    - `@JsonInclude(JsonInclude.Include.NON_NULL)`
    - `@Builder(access = AccessLevel.PRIVATE, setterPrefix = "set")`
    - `@Data`

### Methods

- **Static Methods**:
  - `success(Object results)`: Creates a successful REST API result.
  - `error(String errorMessage)`: Creates an error REST API result with a custom message.
  - `error(Throwable thr)`: Creates an error REST API result with an exception.

### Fields

- `private static final boolean IS_PROD`
- `private String error`
- `private Object results`
- `private EnumRestResultStatus status`
- `private final long timestamp`
- `private static final Logger LOGGER`

## Building and Running

### Prerequisites

- Java Development Kit (JDK)
- Lombok library
- Jackson library

### Compilation

Use your preferred Java build tool (e.g., Maven, Gradle) to compile the project, ensuring Lombok and Jackson are included as dependencies.

### Example

```java
public class Main {
    public static void main(String[] args) {
        RestResult successResult = RestResult.success("Success data");
        RestResult errorResult = RestResult.error("Error occurred");
        System.out.println(successResult);
        System.out.println(errorResult);
    }
}
```

This example demonstrates how to create and print success and error results using the `RestResult` class.
