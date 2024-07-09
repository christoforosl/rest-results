# APIResults

## Overview
This library includes only one file: `APIResults` 
This is a Java class designed to encapsulate the result of a REST API operation. It includes the status, result data, and error information. The class provides static factory methods for creating instances representing successful or error results.

## Features

- Encapsulates the result of a REST API call.
- Provides factory methods for creating success and error results.
- Masks error messages in production environments for security.
- Uses Lombok for boilerplate code reduction.

## Usage
To include, use this in your maven dependencies:  
```xml
<!-- https://mvnrepository.com/artifact/io.github.christoforosl/rest-results -->
<dependency>
    <groupId>io.github.christoforosl</groupId>
    <artifactId>rest-results</artifactId>
    <version>0.2</version>
</dependency>
```

### Typical usage

To create a success result, use the `success` static method and pass the result data, where data can be anything.
You can create an error result with a custom error message, and finally you can create an error result with an exception within a try-catch block:


```java
public APIResults doSomething() {
    try {
        ...
        if (someCondition) {
            return APIResults.error("Some Custom Error message"); 
        }
        Object results = someCall();

        return APIResults.success(results);

    } catch (Exception e) {
        return APIResults.error(e);
    }
}
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

