package energy.estimator;

import java.util.Scanner;

public class EnergyEstimatorDemo {
    public static void main(String[] args) {
        EnergyEstimator estimator = new EnergyEstimator();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter messages (type 'EOF' to end input):");
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equals("EOF")) {
                break;
            }
            estimator.addMessage(input);
        }

        // Print energy usage for Usage messages
        for (UsageMessage usageMessage : estimator.getUsageMessages()) {
            System.out.printf("Energy usage from %d to %d: ", usageMessage.getStartTimestamp(), usageMessage.getEndTimestamp());
            estimator.printEstimatedEnergyUsage(usageMessage.getStartTimestamp(), usageMessage.getEndTimestamp());
        }

        scanner.close();
    }
}
