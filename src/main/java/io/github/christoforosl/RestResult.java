package io.github.christoforosl;

import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

/**
 * Base class for REST API results.
 *
 * @author christoforosl
 */
@Builder(access = AccessLevel.PRIVATE, setterPrefix = "set")
@Data
public class RestResult {

	protected String error;
	protected Object results;
	protected EnumRestResultStatus status;

	private static final Logger LOGGER = Logger.getLogger(RestResult.class.getName());

	public static RestResult success(final Object results) {
		return RestResult.builder().setResults(results).setStatus(EnumRestResultStatus.SUCCESS).setError(null).build();

	}

	public static RestResult error(final String errorMessage) {
		return RestResult.builder().setResults(null).setStatus(EnumRestResultStatus.ERROR).setError(errorMessage).build();
	}

	public static RestResult error(final Throwable thr) {
		LOGGER.log(Level.SEVERE, thr.getMessage(), thr);
		return RestResult.builder().setResults(null).setStatus(EnumRestResultStatus.ERROR).setError(thr.getMessage()).build();
	}

}
