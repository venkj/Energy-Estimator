package energy.estimator;

public class UsageMessage extends Message {

    private final long startTimestamp;
    private final long endTimestamp;

    private UsageMessage(long timestamp, long startTimestamp, long endTimestamp) {
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

    public static UsageMessage createMessage(long timestamp, String[] messageDetails) {
        if (messageDetails.length != 2) {
            throw new IllegalArgumentException("Invalid Usage message format");
        }

        long startTimestamp = ValidationUtils.parseTimestamp(messageDetails[0]);
        long endTimestamp = ValidationUtils.parseTimestamp(messageDetails[1]);
        validateTimestamps(startTimestamp, endTimestamp);
        return new UsageMessage(timestamp, startTimestamp, endTimestamp);
    }

    private static void validateTimestamps(long startTimestamp, long endTimestamp) {
        ValidationUtils.validateEpochTime(startTimestamp);
        ValidationUtils.validateEpochTime(endTimestamp);
        if (endTimestamp < startTimestamp) {
            throw new IllegalArgumentException("Invalid Usage timestamps: startTimestamp(" + startTimestamp + ") less than endTimestamp(" + endTimestamp + ")");
        }
    }
}
