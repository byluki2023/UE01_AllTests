package se.jku.at.exercise;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class A03_LohnberechnungenTest {

    private static String output;

    @BeforeAll
    static void captureOutput() {
        // Stream zum Abfangen der Konsolenausgabe
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        // System.out auf unseren Stream umbiegen
        System.setOut(ps);

        // Programm ausführen
        A03_Lohnberechnungen.main(new String[]{});

        // Alles schreiben
        System.out.flush();

        // Inhalt als String speichern
        output = baos.toString();
    }

    @Test
    void testOutputContainsTotalIncome() {
        assertTrue(output.contains("Gemeinsames Einkommen"),
                "Die Ausgabe soll den Text 'Gemeinsames Einkommen' enthalten.");
    }

    @Test
    void testTotalIncomeValue() {
        assertTrue(output.contains("87.550"),
                "Das gemeinsame Einkommen sollte 87.550 enthalten.");
    }

    @Test
    void testOutputMatchesSample() {
        String expected = """
            Gehaltsberechnung
            Frau Moser: 
             Grundgehalt:          42.000 Euro
             Bonus:                1.500 Euro
             Montage:              1.400 Euro (28*50)
             Gesamt:               44.900 Euro
            Herr Moser: 
             Grundgehalt:          40.000 Euro
             Bonus:                1.000 Euro
             Montage:              1.650 Euro (33*50)
             Gesamt:               42.650 Euro
            Gemeinsames Einkommen:     87.550 Euro
            """;

        // Normalize Zeilenumbrüche, falls Plattform unterschiedlich
        String normalizedActual = output.replace("\r\n", "\n").trim();
        String normalizedExpected = expected.replace("\r\n", "\n").trim();

        assertEquals(normalizedExpected, normalizedActual);
    }
}
