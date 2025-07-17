package se.jku.at.exercise;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;

public class A05_Tests {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        // Redirect System.out to capture Out.println() output
        System.setOut(new PrintStream(outContent));

        // Reset the Out class to use the new PrintStream
        try {
            Field outField = Class.forName("se.jku.at.inout.Out").getDeclaredField("out");
            outField.setAccessible(true);
            outField.set(null, System.out);
        } catch (Exception e) {
            fail("Could not redirect Out class output: " + e.getMessage());
        }
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);

        // Restore the Out class to use System.out
        try {
            Field outField = Class.forName("se.jku.at.inout.Out").getDeclaredField("out");
            outField.setAccessible(true);
            outField.set(null, System.out);
        } catch (Exception e) {
            // Ignore cleanup errors
        }
    }

    @Test
    public void testBasicCalculations() {
        // Test mit den vorgegebenen Werten
        A05_Angebotserstellung.main(new String[]{});

        String output = outContent.toString();

        // Grundpreis: 573.5 m² x 12.50 = 7168.75 Euro
        assertTrue(output.contains("7.168,75"), "Grundpreis sollte 7.168,75 Euro betragen");

        // Räume bis 40m²: 7 x 34.00 = 238.00 Euro
        assertTrue(output.contains("238,00"), "Kosten für Räume bis 40m² sollten 238,00 Euro betragen");

        // Räume bis 80m²: 3 x 123.00 = 369.00 Euro
        assertTrue(output.contains("369,00"), "Kosten für Räume bis 80m² sollten 369,00 Euro betragen");

        // Räume über 80m²: 2 x 210.50 = 421.00 Euro
        assertTrue(output.contains("421,00"), "Kosten für Räume über 80m² sollten 421,00 Euro betragen");
    }

    @Test
    public void testNettoCalculation() {
        A05_Angebotserstellung.main(new String[]{});
        String output = outContent.toString();

        // Netto: 7168.75 + 238.00 + 369.00 + 421.00 = 8196.75 Euro
        assertTrue(output.contains("8.196,75"), "Nettobetrag sollte 8.196,75 Euro betragen");
    }

    @Test
    public void testVATCalculation() {
        A05_Angebotserstellung.main(new String[]{});
        String output = outContent.toString();

        // MwSt: 8196.75 * 0.20 = 1639.35 Euro
        assertTrue(output.contains("1.639,35"), "MwSt sollte 1.639,35 Euro betragen");

        // Sollte 20% MwSt anzeigen
        assertTrue(output.contains("20.0% MwSt"), "Sollte 20% MwSt anzeigen");
    }

    @Test
    public void testGrossTotal() {
        A05_Angebotserstellung.main(new String[]{});
        String output = outContent.toString();

        // Brutto: 8196.75 + 1639.35 = 9836.10 Euro
        assertTrue(output.contains("9.836,10"), "Bruttobetrag sollte 9.836,10 Euro betragen");
    }

    @Test
    public void testCustomerInfo() {
        A05_Angebotserstellung.main(new String[]{});
        String output = outContent.toString();

        // Kundendaten testen
        assertTrue(output.contains("Maria Moser"), "Kundenname sollte in der Ausgabe enthalten sein");
        assertTrue(output.contains("JKU"), "Firmenname sollte in der Ausgabe enthalten sein");
        assertTrue(output.contains("Altenbergerstr. 69, 4040 Linz"), "Adresse sollte in der Ausgabe enthalten sein");
    }

    @Test
    public void testConstants() {
        A05_Angebotserstellung.main(new String[]{});
        String output = outContent.toString();

        // Konstanten in der Ausgabe prüfen
        assertTrue(output.contains("12,50"), "Kosten pro Quadratmeter sollten angezeigt werden");
        assertTrue(output.contains("34,00"), "Aufpreis für Räume bis 40m² sollte angezeigt werden");
        assertTrue(output.contains("123,00"), "Aufpreis für Räume bis 80m² sollte angezeigt werden");
        assertTrue(output.contains("210,50"), "Aufpreis für Räume über 80m² sollte angezeigt werden");
    }

    @Test
    public void testOutputFormat() {
        A05_Angebotserstellung.main(new String[]{});
        String output = outContent.toString();

        // Struktur der Ausgabe prüfen
        assertTrue(output.contains("Angebot"), "Ausgabe sollte 'Angebot' enthalten");
        assertTrue(output.contains("Kunde:"), "Ausgabe sollte Kundenfeld enthalten");
        assertTrue(output.contains("Firma:"), "Ausgabe sollte Firmenfeld enthalten");
        assertTrue(output.contains("Adresse:"), "Ausgabe sollte Adressfeld enthalten");
        assertTrue(output.contains("Grundpreis:"), "Ausgabe sollte Grundpreis enthalten");
        assertTrue(output.contains("Netto:"), "Ausgabe sollte Nettobetrag enthalten");
        assertTrue(output.contains("Brutto:"), "Ausgabe sollte Bruttobetrag enthalten");

        // Trennlinien prüfen
        assertTrue(output.contains("----------------------------------------------"),
                "Ausgabe sollte Trennlinien enthalten");
    }

    @Test
    public void testRoomCounting() {
        A05_Angebotserstellung.main(new String[]{});
        String output = outContent.toString();

        // Anzahl der Räume in der Ausgabe prüfen
        assertTrue(output.contains("7 Räume bis 40m²"), "Sollte 7 Räume bis 40m² anzeigen");
        assertTrue(output.contains("3 Räume bis 80m²"), "Sollte 3 Räume bis 80m² anzeigen");
        assertTrue(output.contains("2 Räume über 80m²"), "Sollte 2 Räume über 80m² anzeigen");
    }

    @Test
    public void testNumberFormatting() {
        A05_Angebotserstellung.main(new String[]{});
        String output = outContent.toString();

        // Deutsche Zahlenformatierung prüfen (mit Punkt als Tausendertrennzeichen)
        assertTrue(output.contains("573,50"), "Quadratmeter sollten mit Komma als Dezimaltrennzeichen formatiert sein");
        assertTrue(output.contains("7.168,75"), "Große Zahlen sollten mit Punkt als Tausendertrennzeichen formatiert sein");
    }

    @Test
    public void testConstantValues() {
        // Test der Konstanten durch Reflection
        try {
            Field costPerSqmField = A05_Angebotserstellung.class.getDeclaredField("COST_PER_SQM");
            costPerSqmField.setAccessible(true);
            double costPerSqm = (Double) costPerSqmField.get(null);
            assertEquals(12.50, costPerSqm, 0.01, "COST_PER_SQM sollte 12.50 sein");

            Field extraRoom40Field = A05_Angebotserstellung.class.getDeclaredField("EXTRA_ROOM_UP_TO_40");
            extraRoom40Field.setAccessible(true);
            double extraRoom40 = (Double) extraRoom40Field.get(null);
            assertEquals(34.00, extraRoom40, 0.01, "EXTRA_ROOM_UP_TO_40 sollte 34.00 sein");

            Field extraRoom80Field = A05_Angebotserstellung.class.getDeclaredField("EXTRA_ROOM_UP_TO_80");
            extraRoom80Field.setAccessible(true);
            double extraRoom80 = (Double) extraRoom80Field.get(null);
            assertEquals(123.00, extraRoom80, 0.01, "EXTRA_ROOM_UP_TO_80 sollte 123.00 sein");

            Field extraRoom80PlusField = A05_Angebotserstellung.class.getDeclaredField("EXTRA_ROOM_80_PLUS");
            extraRoom80PlusField.setAccessible(true);
            double extraRoom80Plus = (Double) extraRoom80PlusField.get(null);
            assertEquals(210.50, extraRoom80Plus, 0.01, "EXTRA_ROOM_80_PLUS sollte 210.50 sein");

            Field vatField = A05_Angebotserstellung.class.getDeclaredField("VAT");
            vatField.setAccessible(true);
            double vat = (Double) vatField.get(null);
            assertEquals(0.20, vat, 0.01, "VAT sollte 0.20 sein");

        } catch (Exception e) {
            fail("Fehler beim Testen der Konstanten: " + e.getMessage());
        }
    }

    @Test
    public void testMathematicalCorrectness() {
        A05_Angebotserstellung.main(new String[]{});
        String output = outContent.toString();

        // Manuelle Berechnung zur Verifikation
        double expectedBaseCost = 573.5 * 12.50; // 7168.75
        double expectedRooms40 = 7 * 34.00; // 238.00
        double expectedRooms80 = 3 * 123.00; // 369.00
        double expectedRooms80Plus = 2 * 210.50; // 421.00
        double expectedNetto = expectedBaseCost + expectedRooms40 + expectedRooms80 + expectedRooms80Plus; // 8196.75
        double expectedVat = expectedNetto * 0.20; // 1639.35
        double expectedBrutto = expectedNetto + expectedVat; // 9836.10

        // Deutsche Formatierung mit Tausendertrennzeichen berücksichtigen
        String formattedBaseCost = String.format("%,.2f", expectedBaseCost);
        String formattedNetto = String.format("%,.2f", expectedNetto);
        String formattedVat = String.format("%,.2f", expectedVat);
        String formattedBrutto = String.format("%,.2f", expectedBrutto);

        // Formatierte Werte prüfen
        assertTrue(output.contains(formattedBaseCost),
                "Grundpreis sollte mathematisch korrekt berechnet werden: " + formattedBaseCost);
        assertTrue(output.contains(formattedNetto),
                "Nettobetrag sollte mathematisch korrekt berechnet werden: " + formattedNetto);
        assertTrue(output.contains(formattedVat),
                "MwSt sollte mathematisch korrekt berechnet werden: " + formattedVat);
        assertTrue(output.contains(formattedBrutto),
                "Bruttobetrag sollte mathematisch korrekt berechnet werden: " + formattedBrutto);
    }

    @Test
    public void testOutputStructure() {
        A05_Angebotserstellung.main(new String[]{});
        String output = outContent.toString();

        // Überprüfung der Reihenfolge der Ausgabe
        int angebotPos = output.indexOf("Angebot");
        int kundePos = output.indexOf("Kunde:");
        int firmaPos = output.indexOf("Firma:");
        int adressePos = output.indexOf("Adresse:");
        int grundpreisPos = output.indexOf("Grundpreis:");
        int nettoPos = output.indexOf("Netto:");
        int bruttoPos = output.indexOf("Brutto:");

        assertTrue(angebotPos < kundePos, "Angebot sollte vor Kunde erscheinen");
        assertTrue(kundePos < firmaPos, "Kunde sollte vor Firma erscheinen");
        assertTrue(firmaPos < adressePos, "Firma sollte vor Adresse erscheinen");
        assertTrue(adressePos < grundpreisPos, "Adresse sollte vor Grundpreis erscheinen");
        assertTrue(grundpreisPos < nettoPos, "Grundpreis sollte vor Netto erscheinen");
        assertTrue(nettoPos < bruttoPos, "Netto sollte vor Brutto erscheinen");
    }

    @Test
    public void testUseInputFlag() {
        // Test ob useInput auf false gesetzt ist
        try {
            Field useInputField = A05_Angebotserstellung.class.getDeclaredField("useInput");
            useInputField.setAccessible(true);
            boolean useInput = (Boolean) useInputField.get(null);
            assertFalse(useInput, "useInput sollte für automatisierte Tests auf false gesetzt sein");
        } catch (Exception e) {
            fail("Fehler beim Überprüfen des useInput Flags: " + e.getMessage());
        }
    }
}