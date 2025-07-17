package se.jku.at.exercise;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import se.jku.at.inout.OutTestHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class A06_Tests {

    static String outStr;

    @BeforeAll
    public static void setup() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(output);
        OutTestHelper.redirectOutTo(ps);

        String simulatedInput = String.join(System.lineSeparator(), "983", "93");
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8)));

        A06_Wechselgeld_Berechnung.main(new String[0]);

        outStr = output.toString()
                .replace("\r\n", "\n")
                .replace("¢", "c")  // ← ersetzt das problematische Zeichen
                .trim();

        System.out.println("DEBUG OUTPUT:\n" + outStr);
    }

    @Test
    public void testAmountLine() {
        assertMatches("Amount:\\s+\\$983\\.93");
    }

    @Test
    public void testBillsAndCoins() {
        assertContains("Franklins: 9 x $100");
        assertContains("Grants: 1 x $50");
        assertContains("Jacksons: 1 x $20");
        assertContains("Hamiltons: 1 x $10");
        assertContains("Lincolns: 0 x $5");
        assertContains("Bucks: 3 x $1");
        assertContains("Quarters: 3 x");
        assertContains("Dimes: 1 x");
        assertContains("Nickels: 1 x");
        assertContains("Pennies: 3 x");
    }

    @Test
    public void testSumLine() {
        assertMatches("9 x 100 \\+ 1 x 50 \\+ 1 x 20 \\+ 1 x 10 \\+ 0 x 5 \\+ 3 x 1 \\+");
        assertMatches("3 x 0\\.25 \\+ 1 x 0\\.10 \\+ 1 x 0\\.05 \\+ 3 x 0\\.01 = 983\\.93");
    }

    // Helper method
    private static void assertMatches(String regex) {
        assertTrue(Pattern.compile(regex).matcher(outStr).find(),
                "Expected pattern not found:\n" + regex);
    }

    private void assertContains(String expected) {
        assertTrue(outStr.contains(expected), "Expected to find: \"" + expected + "\" in output.");
    }
}
