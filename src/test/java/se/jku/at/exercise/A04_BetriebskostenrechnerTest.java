package se.jku.at.exercise;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class A04_BetriebskostenrechnerTest {

    static String distance;
    static String fuelCostPerLiter;
    static String consumptionPer100km;
    static String maintenancePer100km;
    static String fuelCost;
    static String maintenanceCost;
    static String totalCost;

    @BeforeAll
    static void captureOutput() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        A04_Betriebskostenrechner.main(new String[]{});

        System.setOut(originalOut);
        String actual = baos.toString().replace("\r\n", "\n");

        String regex =
                "(?s).*" +
                        "Distanz:\\s+([\\d\\.,]+) km\\n" +
                        ".*Treibstoffkosten/l:\\s+([\\d\\.,]+) Euro\\n" +
                        ".*Verbrauch.*:\\s+([\\d\\.,]+) l\\n" +
                        ".*Wartung.*:\\s+([\\d\\.,]+) Euro\\n" +
                        ".*Treibstoffkosten:\\s+([\\d\\.,]+) Euro\\n" +
                        ".*Wartungskosten:\\s+([\\d\\.,]+) Euro\\n" +
                        ".*Gesamtkosten:\\s+([\\d\\.,]+) Euro.*";

        Pattern p = Pattern.compile(regex);
        var m = p.matcher(actual);

        if (m.matches()) {
            distance = m.group(1);
            fuelCostPerLiter = m.group(2);
            consumptionPer100km = m.group(3);
            maintenancePer100km = m.group(4);
            fuelCost = m.group(5);
            maintenanceCost = m.group(6);
            totalCost = m.group(7);
        } else {
            throw new AssertionError("Output did not match expected pattern.\nActual output:\n" + actual);
        }
    }

    @Test
    void testDistance() {
        assertTrue(matchesNumber(distance, "1,000", "1.000"),
                "Distance does not match. Was: " + distance);
    }

    @Test
    void testFuelCostPerLiter() {
        assertTrue(matchesNumber(fuelCostPerLiter, "2.00", "2,00"),
                "Fuel cost per liter does not match. Was: " + fuelCostPerLiter);
    }

    @Test
    void testConsumptionPer100km() {
        assertTrue(matchesNumber(consumptionPer100km, "7.50", "7,50"),
                "Consumption per 100 km does not match. Was: " + consumptionPer100km);
    }

    @Test
    void testMaintenancePer100km() {
        assertTrue(matchesNumber(maintenancePer100km, "4.20", "4,20"),
                "Maintenance per 100 km does not match. Was: " + maintenancePer100km);
    }

    @Test
    void testFuelCost() {
        assertTrue(matchesNumber(fuelCost, "149.63", "149,63"),
                "Fuel cost does not match. Was: " + fuelCost);
    }

    @Test
    void testMaintenanceCost() {
        assertTrue(matchesNumber(maintenanceCost, "42.00", "42,00"),
                "Maintenance cost does not match. Was: " + maintenanceCost);
    }

    @Test
    void testTotalCost() {
        assertTrue(matchesNumber(totalCost, "191.63", "191,63"),
                "Total cost does not match. Was: " + totalCost);
    }

    private static boolean matchesNumber(String actual, String english, String german) {
        return actual.equals(english) || actual.equals(german);
    }
}
