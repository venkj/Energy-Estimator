package energy.estimator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DeltaMessageTest {

    @Test
    void testValidDeltaMessage() {
        long timestamp = 1544213763L;
        String[] messageDetails = {"+0.5"};
        DeltaMessage message = DeltaMessage.createMessage(timestamp, messageDetails);

        assertNotNull(message);
        assertEquals(timestamp, message.getTimestamp());
        assertEquals(0.5, message.getDelta(), 0.001);
    }

    @Test
    void testValidNegativeDeltaMessage() {
        long timestamp = 1544213763L;
        String[] messageDetails = {"-0.5"};
        DeltaMessage message = DeltaMessage.createMessage(timestamp, messageDetails);

        assertNotNull(message);
        assertEquals(timestamp, message.getTimestamp());
        assertEquals(-0.5, message.getDelta(), 0.001);
    }

    @Test
    void testInvalidDeltaMessageFormat() {
        long timestamp = 1544213763L;
        String[] messageDetails = {"0.5", "extra"};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            DeltaMessage.createMessage(timestamp, messageDetails);
        });

        assertEquals("Invalid Delta message format", exception.getMessage());
    }

    @Test
    void testInvalidDeltaMessageValue() {
        long timestamp = 1544213763L;
        String[] messageDetails = {"invalid"};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            DeltaMessage.createMessage(timestamp, messageDetails);
        });

        assertEquals("Invalid delta value: invalid", exception.getMessage());
    }

    @Test
    void testOutOfRangeDeltaValue() {
        long timestamp = 1544213763L;
        String[] messageDetails = {"1.5"};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            DeltaMessage.createMessage(timestamp, messageDetails);
        });

        assertEquals("Delta value must be between -1.0 and 1.0", exception.getMessage());
    }

    @Test
    void testEdgeCaseDeltaValueMin() {
        long timestamp = 1544213763L;
        String[] messageDetails = {"-1.0"};
        DeltaMessage message = DeltaMessage.createMessage(timestamp, messageDetails);

        assertNotNull(message);
        assertEquals(timestamp, message.getTimestamp());
        assertEquals(-1.0, message.getDelta(), 0.001);
    }

    @Test
    void testEdgeCaseDeltaValueMax() {
        long timestamp = 1544213763L;
        String[] messageDetails = {"1.0"};
        DeltaMessage message = DeltaMessage.createMessage(timestamp, messageDetails);

        assertNotNull(message);
        assertEquals(timestamp, message.getTimestamp());
        assertEquals(1.0, message.getDelta(), 0.001);
    }
}
