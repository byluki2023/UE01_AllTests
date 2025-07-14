package se.jku.at.exercise;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class A06_Wechselgeld_BerechnungTest {

    @Test
    void testSampleOutput() {
        // Standardausgabe abfangen
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Programm ausführen
        A06_Wechselgeld_Berechnung.main(new String[0]);

        // Standardausgabe wiederherstellen
        System.setOut(originalOut);

        String output = outContent.toString();

        assertTrue(containsAny(output,
                "Amount: $983.93",
                "Amount: $983,93",
                "Amount: $983. 93",
                "Amount: $983, 93"
        ), "Amount-Zeile nicht gefunden!");

        assertTrue(output.contains("Franklins: 9 x $100"), "Franklins-Zeile nicht gefunden!");
        assertTrue(output.contains("Grants: 1 x $50"), "Grants-Zeile nicht gefunden!");
        assertTrue(output.contains("Jacksons: 1 x $20"), "Jacksons-Zeile nicht gefunden!");
        assertTrue(output.contains("Hamiltons: 1 x $10"), "Hamiltons-Zeile nicht gefunden!");
        assertTrue(output.contains("Lincolns: 0 x $5"), "Lincolns-Zeile nicht gefunden!");
        assertTrue(output.contains("Bucks: 3 x $1"), "Bucks-Zeile nicht gefunden!");
        assertTrue(output.contains("Quarters: 3 x ¢25"), "Quarters-Zeile nicht gefunden!");
        assertTrue(output.contains("Dimes: 1 x ¢10"), "Dimes-Zeile nicht gefunden!");
        assertTrue(output.contains("Nickels: 1 x ¢05"), "Nickels-Zeile nicht gefunden!");
        assertTrue(output.contains("Pennies: 3 x ¢01"), "Pennies-Zeile nicht gefunden!");

        // Summen-Zeile prüfen (mit Zeilenumbruch!)
        assertTrue(
                containsAny(output,
                        "9 x 100 + 1 x 50 + 1 x 20 + 1 x 10 + 0 x 5 + 3 x 1 + \n" +
                                "3 x 0.25 + 1 x 0.10 + 1 x 0.05 + 3 x 0.01 = 983.93",
                        "9 x 100 + 1 x 50 + 1 x 20 + 1 x 10 + 0 x 5 + 3 x 1 + \r\n" +
                                "3 x 0.25 + 1 x 0.10 + 1 x 0.05 + 3 x 0.01 = 983.93"
                ),
                "Summen-Zeile nicht gefunden!"
        );
    }

    private boolean containsAny(String output, String... patterns) {
        for (String p : patterns) {
            if (output.contains(p)) {
                return true;
            }
        }
        return false;
    }
}
