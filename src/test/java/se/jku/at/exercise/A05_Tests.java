package se.jku.at.exercise;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import se.jku.at.inout.OutTestHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class A05_Tests {

    static String outStr;

    @BeforeAll
    public static void setup() {
        String simulatedInput = String.join(System.lineSeparator(),
                "JKU",
                "Maria Moser",
                "Altenbergerstr. 69, 4040 Linz",
                "573.5",
                "7",
                "3",
                "2"
        );
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8)));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(output);
        OutTestHelper.redirectOutTo(ps);

        A05_Angebotserstellung.main(new String[0]);

        outStr = output.toString();
        System.out.println("DEBUG OUTPUT:\n" + outStr);
    }

    @Test
    public void testCustomerInfo() {
        assertTrue(matchesRegex("Kunde:\\s+Maria Moser"), "Kunde-Zeile nicht gefunden");
        assertTrue(matchesRegex("Firma:\\s+JKU"), "Firma-Zeile nicht gefunden");
        assertTrue(matchesRegex("Adresse:\\s+Altenbergerstr\\. 69, 4040 Linz"), "Adresse-Zeile nicht gefunden");
    }

    private boolean matchesRegex(String regex) {
        return java.util.regex.Pattern.compile(regex).matcher(outStr).find();
    }

    @Test
    public void testBaseCostCalculation() {
        double sqm = 573.5;
        double costPerSqm = 12.50;
        double expected = sqm * costPerSqm;
        assertTrue(outStr.contains(String.format("=  %,.2f Euro", expected)),
                "Base cost calculation not found or incorrect.");
    }

    @Test
    public void testExtraRoomCharges() {
        double e40 = 7 * 34.00;
        double e80 = 3 * 123.00;
        double e80p = 2 * 210.50;

        assertTrue(outStr.contains(String.format("%,.2f", e40)), "40m² room charge missing");
        assertTrue(outStr.contains(String.format("%,.2f", e80)), "80m² room charge missing");
        assertTrue(outStr.contains(String.format("%,.2f", e80p)), ">80m² room charge missing");
    }

    @Test
    public void testNetVatGross() {
        double net = (573.5 * 12.50) + (7 * 34.00) + (3 * 123.00) + (2 * 210.50);
        double vat = net * 0.20;
        double gross = net + vat;

        assertTrue(outStr.contains(String.format("%,.2f Euro", net)), "Net total not found");
        assertTrue(outStr.contains(String.format("%,.2f Euro", vat)), "VAT not found");
        assertTrue(outStr.contains(String.format("%,.2f Euro", gross)), "Gross total not found");
    }
}
