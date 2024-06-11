package energy.estimator;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

import org.junit.jupiter.api.Test;

class ValidationUtilsTest {

    @Test
    void testParseValidTimestamp() {
        String timestampStr = "1544213763";
        long timestamp = ValidationUtils.parseTimestamp(timestampStr);

        assertEquals(1544213763L, timestamp);
    }

    @Test
    void testParseInvalidTimestampFormat() {
        String timestampStr = "invalid";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ValidationUtils.parseTimestamp(timestampStr);
        });

        assertEquals("Invalid timestamp: invalid", exception.getMessage());
    }

    @Test
    void testParseInvalidTimestampRange() {
        String timestampStr = String.valueOf(Long.MAX_VALUE);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ValidationUtils.parseTimestamp(timestampStr);
        });

        assertTrue(exception.getMessage().startsWith("Invalid epoch timestamp:"));
    }

    @Test
    void testParseValidMessageType() {
        String typeStr = "Delta";
        MessageType messageType = ValidationUtils.parseMessageType(typeStr);

        assertEquals(MessageType.DELTA, messageType);
    }

    @Test
    void testParseInvalidMessageType() {
        String typeStr = "InvalidType";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ValidationUtils.parseMessageType(typeStr);
        });

        assertEquals("Invalid message type: InvalidType", exception.getMessage());
    }

    @Test
    void testValidateValidEpochTime() {
        long timestamp = Instant.now().getEpochSecond();
        assertDoesNotThrow(() -> ValidationUtils.validateEpochTime(timestamp));
    }

    @Test
    void testValidateNegativeEpochTime() {
        long timestamp = -1;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ValidationUtils.validateEpochTime(timestamp);
        });

        assertEquals("Invalid epoch timestamp: -1", exception.getMessage());
    }

    @Test
    void testValidateFutureEpochTime() {
        long timestamp = Instant.now().getEpochSecond() + 10000;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ValidationUtils.validateEpochTime(timestamp);
        });

        assertTrue(exception.getMessage().startsWith("Invalid epoch timestamp:"));
    }
}
