package energy.estimator;

public class MessageFactory {

    // Private constructor to prevent instantiation
    private MessageFactory() {
        throw new UnsupportedOperationException("MessageFactory class. Cannot be instantiated");
    }

    public static Message createMessage(long timestamp, MessageType type, String[] messageDetails) {
        switch (type) {
            case TURNOFF:
                return TurnOffMessage.createMessage(timestamp);
            case DELTA:
                return DeltaMessage.createMessage(timestamp, messageDetails);
            case USAGE:
                return UsageMessage.createMessage(timestamp, messageDetails);
            default:
                throw new IllegalArgumentException("Unhandled message type: " + type);
        }
    }
}
