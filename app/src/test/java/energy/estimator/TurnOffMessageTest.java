package energy.estimator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TurnOffMessageTest {

    @Test
    void testCreateValidTurnOffMessage() {
        TurnOffMessage turnOffMessage = TurnOffMessage.createMessage(1544210163L);

        assertNotNull(turnOffMessage);
        assertEquals(MessageType.TURNOFF, turnOffMessage.getType());
    }
}
