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

        outStr = output.toString().replace("\r\n", "\n");
        System.out.println("DEBUG OUTPUT:\n" + outStr);
    }

    @Test
    public void testAmountLine() {
        assertMatches("Amount:\\s+\\$?983\\.93");
    }

    @Test
    public void testBillsAndCoins() {
        assertMatches("Franklins:\\s+9\\s+x\\s+\\$100");
        assertMatches("Grants:\\s+1\\s+x\\s+\\$50");
        assertMatches("Jacksons:\\s+1\\s+x\\s+\\$20");
        assertMatches("Hamiltons:\\s+1\\s+x\\s+\\$10");
        assertMatches("Lincolns:\\s+0\\s+x\\s+\\$5");
        assertMatches("Bucks:\\s+3\\s+x\\s+\\$1");
        assertMatches("Quarters:\\s+3\\s+x\\s+¢25");
        assertMatches("Dimes:\\s+1\\s+x\\s+¢10");
        assertMatches("Nickels:\\s+1\\s+x\\s+¢05");
        assertMatches("Pennies:\\s+3\\s+x\\s+¢01");
    }

    @Test
    public void testFinalEquation() {
        assertMatches("9\\s+x\\s+100\\s+\\+\\s+1\\s+x\\s+50\\s+\\+\\s+1\\s+x\\s+20\\s+\\+\\s+1\\s+x\\s+10\\s+\\+\\s+0\\s+x\\s+5\\s+\\+\\s+3\\s+x\\s+1\\s+\\+\\s+3\\s+x\\s+0\\.25\\s+\\+\\s+1\\s+x\\s+0\\.10\\s+\\+\\s+1\\s+x\\s+0\\.05\\s+\\+\\s+3\\s+x\\s+0\\.01\\s+=\\s+983\\.93");
    }

    private void assertMatches(String regex) {
        assertTrue(Pattern.compile(regex).matcher(outStr).find(),
                "Expected pattern not found:\n" + regex);
    }
}
