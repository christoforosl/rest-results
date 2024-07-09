package io.github.christoforosl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class APIResultsJsonSerializationTest {

	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
	}

	@Test
	void testSuccessResultSerialization() throws Exception {
		String resultData = "Test Data";
		APIResults result = APIResults.success(resultData);

		String json = objectMapper.writeValueAsString(result);

		assertTrue(json.contains("\"status\":\"SUCCESS\""));
		assertTrue(json.contains("\"results\":\"Test Data\""));
		assertFalse(json.contains("\"error\":"));
	}

	@Test
	void testErrorResultSerialization() throws Exception {
		String errorMessage = "Test Error";
		APIResults result = APIResults.error(errorMessage);

		String json = objectMapper.writeValueAsString(result);

		assertTrue(json.contains("\"status\":\"ERROR\""));
		assertTrue(json.contains("\"error\":\"Test Error\""));
		assertFalse(json.contains("\"results\":null"));
	}

	@Test
	void testErrorResultWithThrowableSerialization() throws Exception {
		try {
			new Exception("Test Exception");
		} catch (Exception testException) {
			APIResults result = APIResults.error(testException);
			String json = objectMapper.writeValueAsString(result);

			assertTrue(json.contains("\"status\":\"ERROR\""));
			assertTrue(json.contains("\"error\":"));
			assertTrue(json.contains("Exception Number"));
			assertFalse(json.contains("\"results\":null"));
		}

	}

	@Test
	void testCustomResultSerialization() throws Exception {
		CustomObject customObject = new CustomObject("Test Name", 42);
		APIResults result = APIResults.success(customObject);

		String json = objectMapper.writeValueAsString(result);

		assertTrue(json.contains("\"status\":\"SUCCESS\""));
		assertTrue(json.contains("\"results\":{"));
		assertTrue(json.contains("\"name\":\"Test Name\""));
		assertTrue(json.contains("\"value\":42"));
		assertFalse(json.contains("\"error\":"));
	}

	// Helper class for testing custom object serialization
	private static class CustomObject {

		private String name;
		private int value;

		public CustomObject(String name, int value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public int getValue() {
			return value;
		}
	}
}
