package energy.estimator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class EnergyEstimator {

    public static final double MAX_POWER_WATTS = 5.0; // Maximum power in watts
    private final SortedMap<Long, List<Message>> messageMap;
    private final Set<String> processedMessages;

    public EnergyEstimator() {
        messageMap = new TreeMap<>();
        processedMessages = new HashSet<>();
    }

    public void addMessage(String input) {
        // Skip duplicate message
        if (processedMessages.contains(input)) {
            return;
        }
        processedMessages.add(input);

        String[] parts = input.split(" ");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid message format");
        }

        long timestamp = ValidationUtils.parseTimestamp(parts[0]);
        MessageType type = ValidationUtils.parseMessageType(parts[1]);

        String[] messageDetails = Arrays.copyOfRange(parts, 2, parts.length);
        Message message = MessageFactory.createMessage(timestamp, type, messageDetails);

        messageMap.computeIfAbsent(timestamp, k -> new ArrayList<>()).add(message);
    }

    public List<UsageMessage> getUsageMessages() {
        List<UsageMessage> usageMessages = new ArrayList<>();
        for (List<Message> messages : messageMap.values()) {
            for (Message message : messages) {
                if (message.getType() == MessageType.USAGE) {
                    usageMessages.add((UsageMessage) message);
                }
            }
        }
        return usageMessages;
    }

    public double estimateEnergyUsage(long startTimestamp, long endTimestamp) {
        double totalEnergy = 0.0;
        double currentBrightness = getStartingBrightness(startTimestamp);
        long previousTimestamp = startTimestamp;

        for (Map.Entry<Long, List<Message>> entry : messageMap.subMap(startTimestamp, endTimestamp + 1).entrySet()) {
            long currentTimestamp = entry.getKey();
            double timeInterval = (currentTimestamp - previousTimestamp) / 3600.0; // convert seconds to hours
            totalEnergy += currentBrightness * MAX_POWER_WATTS * timeInterval;
            previousTimestamp = currentTimestamp;

            currentBrightness = calculateBrightness(entry.getValue(), currentBrightness);
        }

        // Ensure we handle the end timestamp correctly
        if (previousTimestamp < endTimestamp) {
            double finalTimeInterval = (endTimestamp - previousTimestamp) / 3600.0;
            totalEnergy += currentBrightness * MAX_POWER_WATTS * finalTimeInterval;
        }
        return totalEnergy;
    }

    public void printEstimatedEnergyUsage(long startTimestamp, long endTimestamp) {
        double totalEnergy = estimateEnergyUsage(startTimestamp, endTimestamp);
        System.out.printf("%.3f Wh%n", totalEnergy);
    }

    public SortedMap<Long, List<Message>> getMessageMap() {
        return messageMap;
    }

    //Ensure the initial brightness calculated based on messages prior to the startTimestamp
    public double getStartingBrightness(long startTimestamp) {
        double brightness = 0.0;
        for (Map.Entry<Long, List<Message>> entry : messageMap.headMap(startTimestamp).entrySet()) {
            brightness = calculateBrightness(entry.getValue(), brightness);
        }
        return brightness;
    }

    private double calculateBrightness(List<Message> messages, double brightness) {
        for (Message message : messages) {
            if (message instanceof DeltaMessage) {
                brightness = normalizeBrightness(brightness + ((DeltaMessage) message).getDelta());
            } else if (message instanceof TurnOffMessage) {
                brightness = 0.0;
            }
        }
        return brightness;
    }

    // Ensures that the brightness value stays within the valid range of 0.0 to 1.0
    private double normalizeBrightness(double brightness) {
        return Math.max(0.0, Math.min(1.0, brightness));
    }
}
