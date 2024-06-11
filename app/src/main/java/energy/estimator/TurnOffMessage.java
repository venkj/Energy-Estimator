package energy.estimator;

public class TurnOffMessage extends Message {

    private TurnOffMessage(long timestamp) {
        super(timestamp, MessageType.TURNOFF);
    }

    public static TurnOffMessage createMessage(long timestamp) {
        return new TurnOffMessage(timestamp);
    }
}
