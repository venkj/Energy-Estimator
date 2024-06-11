package energy.estimator;

import java.time.Instant;

public class ValidationUtils {

    // Private constructor to prevent instantiation
    private ValidationUtils() {
        throw new UnsupportedOperationException("ValidationUtils class. Cannot be instantiated");
    }

    public static long parseTimestamp(String timestampStr) {
        try {
            long timestamp = Long.parseLong(timestampStr);
            validateEpochTime(timestamp);
            return timestamp;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid timestamp: " + timestampStr);
        }
    }

    public static MessageType parseMessageType(String typeStr) {
        try {
            return MessageType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid message type: " + typeStr);
        }
    }

    public static void validateEpochTime(long timestamp) {
        long now = Instant.now().getEpochSecond();
        if (timestamp < 0 || timestamp > now) {
            throw new IllegalArgumentException("Invalid epoch timestamp: " + timestamp);
        }
    }
}
