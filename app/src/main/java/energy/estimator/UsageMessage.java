package energy.estimator;

public class UsageMessage extends Message {

    private final long startTimestamp;
    private final long endTimestamp;

    public UsageMessage(long timestamp, long startTimestamp, long endTimestamp) {
        super(timestamp, MessageType.USAGE);
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }
}
