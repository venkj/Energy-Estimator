package energy.estimator;

public class DeltaMessage extends Message {

    private final double delta;

    public DeltaMessage(long timestamp, double delta) {
        super(timestamp, MessageType.DELTA);
        validateDelta(delta);
        this.delta = delta;
    }

    public double getDelta() {
        return delta;
    }

    private static void validateDelta(double delta) {
        if (delta < -1.0 || delta > 1.0) {
            throw new IllegalArgumentException("Delta value must be between -1.0 and 1.0");
        }
    }
}
