package energy.estimator;

public abstract class Message {

    protected long timestamp;
    protected MessageType type;

    protected Message(long timestamp, MessageType type) {
        this.timestamp = timestamp;
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public MessageType getType() {
        return type;
    }
}
