package energy.estimator;

public class DeltaMessage extends Message {

    private final double delta;

    private DeltaMessage(long timestamp, double delta) {
        super(timestamp, MessageType.DELTA);
        this.delta = delta;
    }

    public double getDelta() {
        return delta;
    }

    public static DeltaMessage createMessage(long timestamp, String[] messageDetails) {
        if (messageDetails.length != 1) {
            throw new IllegalArgumentException("Invalid Delta message format");
        }
        double delta = parseAndValidateDelta(messageDetails[0]);
        return new DeltaMessage(timestamp, delta);
    }

    private static double parseAndValidateDelta(String deltaStr) {
        double delta;
        try {
            delta = Double.parseDouble(deltaStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid delta value: " + deltaStr);
        }

        if (delta < -1.0 || delta > 1.0) {
            throw new IllegalArgumentException("Delta value must be between -1.0 and 1.0");
        }

        return delta;
    }
}
