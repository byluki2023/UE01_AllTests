package se.jku.at.exercise;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import se.jku.at.inout.OutTestHelper;

public class A02_Tests {

    private static String outStr;

    @BeforeAll
    public static void setup() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(output);
        OutTestHelper.redirectOutTo(ps);

        String simulatedInput = String.join(System.lineSeparator(),
                "5", "7",
                "2",
                "3",
                "5",
                "4"
        );
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8)));

        A02_EinfacheRechnungen.main(new String[0]);

        outStr = output.toString();
        System.out.println("DEBUG OUTPUT:\n" + outStr);
    }

    @Test
    public void testAddition() {
        assertTrue(outStr.contains("Addition: 5 + 7 = 12"),
                "Addition stimmt nicht");
    }

    @Test
    public void testSubtraction() {
        assertTrue(outStr.contains("Subtraktion: 12 - 2 = 10"),
                "Subtraktion stimmt nicht");
    }

    @Test
    public void testMultiplication() {
        assertTrue(outStr.contains("Multiplikation: 10 * 3 = 30"),
                "Multiplikation stimmt nicht (Bezeichner falsch?)");
    }

    @Test
    public void testDivision() {
        assertTrue(outStr.contains("Division: 30 / 5 = 6"),
                "Division stimmt nicht (Bezeichner falsch?)");
    }

    @Test
    public void testModulo() {
        assertTrue(outStr.contains("Divisionsrest: 6 % 4 = 2"),
                "Modulo stimmt nicht");
    }
}
