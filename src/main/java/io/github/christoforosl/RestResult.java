package io.github.christoforosl;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;


/**
 * Represents the result of a REST API operation.
 * This class encapsulates the status, result data, and error information of a REST API call.
 * It provides static factory methods for creating success and error results.
 *
 *
 * <p>In production environments, error messages are masked for security reasons.</p>
 *
 * @author christoforosl
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(access = AccessLevel.PRIVATE, setterPrefix = "set")
@Data
public class RestResult {

    private static final boolean IS_PROD = isProduction();

    /**
     * Determines if the current environment is production.
     * It checks the "spring.profiles.active" system property first, then falls back to "rest-results.env".
     * Returns true if the active profile is "prod" or "production", false otherwise.
     * @see <a href="https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-profiles">Spring Boot Profiles</a>
     * @return true if the environment is production, false otherwise
     */
    private static boolean isProduction() {
        // Check for Spring Boot active profile first
        String springProfile = System.getProperty("spring.profiles.active", "");
        if (springProfile == null || springProfile.isEmpty()) {
            // Check for system property rest-results.env
            springProfile = System.getProperty("rest-results.env", "");
        }

        return springProfile.equalsIgnoreCase("prod") || springProfile.equalsIgnoreCase("production");
    }

    private String error;
    private Object results;
    private EnumRestResultStatus status;
    private final long timestamp = System.currentTimeMillis();

    private static final Logger LOGGER = Logger.getLogger(RestResult.class.getName());

    /**
     * Creates a successful REST API result.
     * @param results the result data
     * @return a successful REST API result
     */
    public static RestResult success(final Object results) {
        return RestResult.builder().setResults(results).setStatus(EnumRestResultStatus.SUCCESS).setError(null).build();

    }

    /**
     * Creates an error REST API result.
     * @param errorMessage the error message
     * @return an error REST API result
     */
    public static RestResult error(final String errorMessage) {
        return RestResult.builder().setResults(null).setStatus(EnumRestResultStatus.ERROR).setError(errorMessage)
                .build();
    }
    /**
     * Creates an error REST API result.
     * @param thr the exception that caused the error
     * @return an error REST API result
     */
    public static RestResult error(final Throwable thr) {
        long errornumber = System.currentTimeMillis();

        // dont reveal the error message in production
        final String errorMessage = "Exception Number [" + errornumber + "]" + (IS_PROD ? "" : thr.getMessage());
        LOGGER.log(Level.SEVERE, errorMessage, thr);

        return RestResult.builder().setResults(null).setStatus(EnumRestResultStatus.ERROR)
                .setError(errorMessage).build();

    }

}
