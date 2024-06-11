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

        // Print energy usage Usage messages
        for (UsageMessage usageMessage : estimator.getUsageMessages()) {
            estimator.printEstimatedEnergyUsage(usageMessage.getStartTimestamp(), usageMessage.getEndTimestamp());
        }

        scanner.close();
    }
}
