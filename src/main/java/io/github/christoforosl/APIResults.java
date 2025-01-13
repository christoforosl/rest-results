package io.github.christoforosl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Data;

/**
 * Represents the result of a REST API operation.
 * This class encapsulates the status, result data, and error information of a
 * REST API call.
 * It provides static factory methods for creating success and error results.
 *
 * <p>
 * In production environments, error messages are masked for security reasons.
 * </p>
 *
 * @author christoforosl
 *         io.github.christoforosl.APIResults
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class APIResults<T> {

    private static final Logger LOGGER = Logger.getLogger(APIResults.class.getName());
    private static final boolean IS_PROD = isProduction();

    private static final String PROD_PROFILE = "prod";
    private static final String PRODUCTION_PROFILE = "production";
    private static final String SPRING_PROFILE_PROPERTY = "spring.profiles.active";
    private static final String FALLBACK_ENV_PROPERTY = "rest-results.env";

    private final String error;
    private final T results;
    private final EnumAPIResultsStatus status;
    private final long timestamp = System.currentTimeMillis();

    /**
     * Determines if the current environment is production.
     * It checks the "spring.profiles.active" system property first, then falls back
     * to "rest-results.env".
     * Returns true if the active profile is "prod" or "production", false
     * otherwise.
     *
     * @see <a href=
     *      "https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-profiles">Spring
     *      Boot Profiles</a>
     * @return true if the environment is production, false otherwise
     */
    private static boolean isProduction() {
        String springProfile = System.getProperty(SPRING_PROFILE_PROPERTY, "");
        if (springProfile == null || springProfile.isEmpty()) {
            springProfile = System.getProperty(FALLBACK_ENV_PROPERTY, "");
        }

        return PROD_PROFILE.equalsIgnoreCase(springProfile)
            || PRODUCTION_PROFILE.equalsIgnoreCase(springProfile);
    }


    @JsonCreator
    public APIResults(
            @JsonProperty("status") EnumAPIResultsStatus status,
            @JsonProperty("error") String error,
            @JsonProperty("results") T results) {

        this.status = status;
        this.error = error;
        this.results = results;

    }

    /**
     * Creates a successful REST API result.
     *
     * @param results the result data
     * @return a successful REST API result
     */
    public static <T> APIResults<T> success(T results) {
        APIResults<T> response = new APIResults<>(EnumAPIResultsStatus.SUCCESS, null, results);
        return response;
    }

    /**
     * Creates an error REST API result.
     *
     * @param errorMessage the error message
     * @return an error REST API result
     */
    public static <T> APIResults<T> error(String errorMessage) {

        APIResults<T> response = new APIResults<>(EnumAPIResultsStatus.ERROR, errorMessage, null);
        return response;

     }


    /**
     * Creates an error REST API result.
     *
     * @param thr the exception that caused the error
     * @return an error REST API result
     */
    public static <T> APIResults<T> error(final Throwable thr) {

        long errornumber = System.currentTimeMillis();

        final String throwableMessage = (thr.getCause() != null)  ? thr.getCause().getMessage() : thr.getMessage();
        final String userErrorMessage = "Error  Number [" + errornumber + "] occurred. " + (IS_PROD ? " " : throwableMessage);

        // dont reveal the error message in production
        final String loggerMessage = "Error Number [" + errornumber + "]:" + throwableMessage;
        LOGGER.log(Level.SEVERE, loggerMessage, thr);

        APIResults<T> response = new APIResults<>(EnumAPIResultsStatus.ERROR, userErrorMessage, null);
        return response;

    }

}
