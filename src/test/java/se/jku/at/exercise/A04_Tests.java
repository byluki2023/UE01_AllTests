package se.jku.at.exercise;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import se.jku.at.inout.OutTestHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class A04_Tests {

    static String outStr;

    @BeforeAll
    public static void setup() {
        // Redirect output
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(output);
        OutTestHelper.redirectOutTo(ps);

        // Simulate input: distance, fuel/100km, cost/liter
        String simulatedInput = String.join(System.lineSeparator(),
                "1000",     // distance
                "7.5",      // fuel per 100 km
                "1.995"     // fuel cost per liter
        );
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8)));

        // Run main
        A04_Betriebskostenrechner.main(new String[0]);

        // Save output
        outStr = output.toString();
        System.out.println("DEBUG OUTPUT:\n" + outStr);
    }

    @Test
    public void testFuelCost() {
        // 1000 km → 75 L * 1.995 = 149.625 → 149.63
        assertTrue(outStr.contains("149.63") || outStr.contains("149,63"),
                "Treibstoffkosten sollten 149.63 Euro sein");
    }

    @Test
    public void testMaintenanceCost() {
        // 1000 km → 10 * 4.2 = 42.00
        assertTrue(outStr.contains("42.00") || outStr.contains("42,00"),
                "Wartungskosten sollten 42.00 Euro sein");
    }

    @Test
    public void testTotalCost() {
        // 149.63 + 42.00 = 191.63
        assertTrue(outStr.contains("191.63") || outStr.contains("191,63"),
                "Gesamtkosten sollten 191.63 Euro sein");
    }

    @Test
    public void testInputPromptsPresent() {
        assertTrue(outStr.contains("Gefahrene Kilometer") && outStr.contains("Verbrauch") && outStr.contains("Benzinkosten"),
                "Eingabeaufforderungen fehlen oder wurden nicht erkannt");
    }
}
