package energy.estimator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid message format");
        }

        long timestamp = ValidationUtils.parseTimestamp(parts[0]);
        MessageType type = ValidationUtils.parseMessageType(parts[1]);

        if (processedMessages.contains(input)) {
            return; // Skip duplicate message
        }
        processedMessages.add(input);

        // TODO: Create message object and add to messageMap
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
        // TODO: Process messages to calculate energy estimate
        return 0.0;
    }

    public void printEstimatedEnergyUsage(long startTimestamp, long endTimestamp) {
        double totalEnergy = estimateEnergyUsage(startTimestamp, endTimestamp);
        System.out.printf("%.3f Wh%n", totalEnergy);
    }

    public SortedMap<Long, List<Message>> getMessageMap() {
        return messageMap;
    }
}
