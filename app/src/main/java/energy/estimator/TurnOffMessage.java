package energy.estimator;

public class TurnOffMessage extends Message {

    public TurnOffMessage(long timestamp) {
        super(timestamp, MessageType.TURNOFF);
    }
}
