package energy.estimator;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EnergyEstimatorDemoTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private ByteArrayInputStream inputStream;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(System.in);
    }

    @Test
    void testEnergyEstimatorDemo() {
        String input = "1544206562 TurnOff\n"
                + "1544206563 Delta +0.5\n"
                + "1544210163 TurnOff\n"
                + "1544210163 Usage 1544206562 1544210163\n"
                + "EOF\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        EnergyEstimatorDemo.main(new String[]{});

        String expectedOutput = "Enter messages (type 'EOF' to end input):\n"
                + "Energy usage from 1544206562 to 1544210163: 2.500 Wh";
        assertEquals(expectedOutput, outputStream.toString().trim());
    }

    @Test
    void testEnergyEstimatorWithIntermediateDeltas() {
        String input = "1544206562 TurnOff\n"
                + "1544206563 Delta +0.5\n"
                + "1544210163 Delta -0.25\n"
                + "1544211963 Delta +0.75\n"
                + "1544211963 Delta +0.75\n"
                + "1544213763 TurnOff\n"
                + "1544213763 Usage 1544206562 1544213763\n"
                + "EOF\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        EnergyEstimatorDemo.main(new String[]{});

        String expectedOutput = "Enter messages (type 'EOF' to end input):\n"
                + "Energy usage from 1544206562 to 1544213763: 5.625 Wh";
        assertEquals(expectedOutput, outputStream.toString().trim());
    }

    @Test
    void testEnergyEstimatorWithOutOfOrderMessages() {
        String input = "1544206562 TurnOff\n"
                + "1544211963 Delta +0.75\n"
                + "1544206563 Delta +0.5\n"
                + "1544210163 Delta -0.25\n"
                + "1544213763 TurnOff\n"
                + "1544213763 Usage 1544206562 1544213763\n"
                + "EOF\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        EnergyEstimatorDemo.main(new String[]{});

        String expectedOutput = "Enter messages (type 'EOF' to end input):\n"
                + "Energy usage from 1544206562 to 1544213763: 5.625 Wh";
        assertEquals(expectedOutput, outputStream.toString().trim());
    }
}
