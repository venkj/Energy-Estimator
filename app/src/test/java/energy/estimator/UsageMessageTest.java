package energy.estimator;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

import org.junit.jupiter.api.Test;

class UsageMessageTest {

    @Test
    void testCreateValidUsageMessage() {
        String[] messageDetails = {"1544206562", "1544210163"};
        UsageMessage usageMessage = UsageMessage.createMessage(1544210163L, messageDetails);

        assertNotNull(usageMessage);
        assertEquals(1544206562L, usageMessage.getStartTimestamp());
        assertEquals(1544210163L, usageMessage.getEndTimestamp());
        assertEquals(MessageType.USAGE, usageMessage.getType());
    }

    @Test
    void testCreateUsageMessageInvalidFormat() {
        String[] messageDetails = {"1544206562"};
        Exception exception = assertThrows(IllegalArgumentException.class, ()
                -> UsageMessage.createMessage(1544210163L, messageDetails)
        );
        assertEquals("Invalid Usage message format", exception.getMessage());
    }

    @Test
    void testCreateUsageMessageInvalidTimestamps() {
        String[] messageDetails = {"abc", "1544210163"};
        Exception exception = assertThrows(IllegalArgumentException.class, ()
                -> UsageMessage.createMessage(1544210163L, messageDetails)
        );
        assertTrue(exception.getMessage().contains("Invalid timestamp"));
    }

    @Test
    void testCreateUsageMessageEndBeforeStart() {
        String[] messageDetails = {"1544210163", "1544206562"};
        Exception exception = assertThrows(IllegalArgumentException.class, ()
                -> UsageMessage.createMessage(1544210163L, messageDetails)
        );
        assertEquals("Invalid Usage timestamps: startTimestamp(1544210163) less than endTimestamp(1544206562)", exception.getMessage());
    }

    @Test
    void testCreateUsageMessageOutOfEpochRange() {
        String[] messageDetails = {"-1", "1544210163"};
        Exception exception = assertThrows(IllegalArgumentException.class, ()
                -> UsageMessage.createMessage(1544210163L, messageDetails)
        );
        assertEquals("Invalid epoch timestamp: -1", exception.getMessage());
    }

    @Test
    void testCreateUsageMessageFutureTimestamp() {
        long futureTimestamp = Instant.now().getEpochSecond() + 10000;
        String[] messageDetails = {String.valueOf(futureTimestamp), "1544210163"};
        Exception exception = assertThrows(IllegalArgumentException.class, ()
                -> UsageMessage.createMessage(1544210163L, messageDetails)
        );
        assertTrue(exception.getMessage().startsWith("Invalid epoch timestamp"));
    }
}
