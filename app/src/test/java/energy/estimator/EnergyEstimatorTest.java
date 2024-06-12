package energy.estimator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EnergyEstimatorTest {

    private EnergyEstimator estimator;

    @BeforeEach
    void setUp() {
        estimator = new EnergyEstimator();
    }

    @Test
    void testAddMessageAndGetUsageMessages() {
        // GIVEN
        estimator.addMessage("1544206562 TurnOff");
        estimator.addMessage("1544206563 Delta +0.5");
        estimator.addMessage("1544210163 TurnOff");
        estimator.addMessage("1544210163 Usage 1544206562 1544210163");

        // WHEN
        List<UsageMessage> usageMessages = estimator.getUsageMessages();
        UsageMessage usageMessage = usageMessages.get(0);

        // THEN
        assertEquals(1, usageMessages.size());
        assertEquals(1544206562L, usageMessage.getStartTimestamp());
        assertEquals(1544210163L, usageMessage.getEndTimestamp());
    }

    @Test
    void testEstimateEnergyUsage() {
        // GIVEN
        estimator.addMessage("1544206562 TurnOff");
        estimator.addMessage("1544206563 Delta +0.5");
        estimator.addMessage("1544210163 TurnOff");
        estimator.addMessage("1544210163 Usage 1544206562 1544210163");

        // Calculate expected energy usage
        double expectedEnergy = 0.5 * EnergyEstimator.MAX_POWER_WATTS * ((1544210163 - 1544206563) / 3600.0);

        // WHEN
        double energyUsage = estimator.estimateEnergyUsage(1544206562L, 1544210163L); // 0.5

        // THEN
        assertEquals(expectedEnergy, energyUsage, 0.001);
    }

    @Test
    void testEstimateEnergyUsageWithIntermediateDeltas() {
        // GIVEN
        estimator.addMessage("1544206562 TurnOff");
        estimator.addMessage("1544206563 Delta +0.5");
        estimator.addMessage("1544210163 Delta -0.25");
        estimator.addMessage("1544211963 Delta +0.75");
        estimator.addMessage("1544211963 Delta +0.75");
        estimator.addMessage("1544213763 TurnOff");
        estimator.addMessage("1544213763 Usage 1544206562 1544213763");

        // Calculate expected energy usage
        double expectedEnergy = 0.5 * EnergyEstimator.MAX_POWER_WATTS * ((1544210163 - 1544206563) / 3600.0)
                + 0.25 * EnergyEstimator.MAX_POWER_WATTS * ((1544211963 - 1544210163) / 3600.0)
                + 1.0 * EnergyEstimator.MAX_POWER_WATTS * ((1544213763 - 1544211963) / 3600.0); // 5.625

        // WHEN
        double energyUsage = estimator.estimateEnergyUsage(1544206562L, 1544213763L);

        // THEN
        assertEquals(expectedEnergy, energyUsage, 0.001);
    }

    @Test
    void testEstimateEnergyUsageWithOutOfOrderMessages() {
        // GIVEN
        estimator.addMessage("1544206562 TurnOff");
        estimator.addMessage("1544211963 Delta +0.75");
        estimator.addMessage("1544206563 Delta +0.5");
        estimator.addMessage("1544210163 Delta -0.25");
        estimator.addMessage("1544213763 TurnOff");
        estimator.addMessage("1544213763 Usage 1544206562 1544213763");

        // Calculate the expected energy usage
        double expectedEnergy = 0.5 * EnergyEstimator.MAX_POWER_WATTS * ((1544210163 - 1544206563) / 3600.0)
                + 0.25 * EnergyEstimator.MAX_POWER_WATTS * ((1544211963 - 1544210163) / 3600.0)
                + 1.0 * EnergyEstimator.MAX_POWER_WATTS * ((1544213763 - 1544211963) / 3600.0); // 5.625

        // WHEN
        double energyUsage = estimator.estimateEnergyUsage(1544206562L, 1544213763L);

        // THEN
        assertEquals(expectedEnergy, energyUsage, 0.001);
    }

    @Test
    void testGetStartingBrightness() {
        // GIVEN
        estimator.addMessage("1544206562 TurnOff");
        estimator.addMessage("1544206563 Delta +0.5");
        estimator.addMessage("1544210163 Delta -0.25");
        estimator.addMessage("1544211963 Delta +0.75");

        // WHEN
        double startingBrightness = estimator.getStartingBrightness(1544213763L);

        // THEN
        assertEquals(1.0, startingBrightness, 0.001);
    }

    @Test
    void testDuplicateMessages() {
        estimator.addMessage("1544206562 TurnOff");
        estimator.addMessage("1544206562 TurnOff");  // Duplicate message
        estimator.addMessage("1544206563 Delta +0.5");
        estimator.addMessage("1544206563 Delta +0.5");  // Duplicate message

        List<UsageMessage> usageMessages = estimator.getUsageMessages();
        assertTrue(usageMessages.isEmpty());
    }

    @Test
    void testUsageRequestWithOutExactBoundsTimestamps() {
        // GIVEN
        estimator.addMessage("1544206562 TurnOff");
        estimator.addMessage("1544206563 Delta +0.5");
        estimator.addMessage("1544206564 Delta +0.3");
        estimator.addMessage("1544210162 TurnOff");
        estimator.addMessage("1544213763 Usage 1544200000 1544210000"); // time range out of start and end timestamps

        // Calculate expected energy usage
        double expectedEnergy = 0.5 * EnergyEstimator.MAX_POWER_WATTS * ((1544206564 - 1544206563) / 3600.0)
                + 0.8 * EnergyEstimator.MAX_POWER_WATTS * ((1544210000 - 1544206564) / 3600.0); //3.818472

        // WHEN
        List<UsageMessage> usageMessages = estimator.getUsageMessages();
        UsageMessage usageMessage = usageMessages.get(0);
        double totalEnergy = estimator.estimateEnergyUsage(usageMessage.getStartTimestamp(), usageMessage.getEndTimestamp());

        // THEN
        assertEquals(1, usageMessages.size());
        assertEquals(expectedEnergy, totalEnergy, 0.001);
    }
}
