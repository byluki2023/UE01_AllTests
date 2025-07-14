package se.jku.at.exercise;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se.jku.at.inout.OutTestHelper;

public class A01_TicTacToeTest {

    private ByteArrayOutputStream output;

    @BeforeEach
    public void setup() {
        output = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(output);
        OutTestHelper.redirectOutTo(ps);

        // Dummy Input für alle In.readChar()-Aufrufe:
        System.setIn(new ByteArrayInputStream((
                String.join(System.lineSeparator(),
                        "X", "O", "X",
                        "O", "X", "X",
                        "O", "O", "X"
                )
        ).getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void testTicTacToeOutput() {
        // Programm ausführen
        A01_TicTacToe.main(new String[0]);

        String outStr = output.toString();
        System.out.println("DEBUG OUTPUT:\n" + outStr);

        // FIRST PART prüfen
        assertTrue(outStr.contains("+---+---+---+"), "Rahmen fehlt im ersten Teil");
        assertTrue(outStr.contains("|   |   |   |"), "Leere Zeile fehlt im ersten Teil");

        // SECOND PART prüfen (Variablen-Board)
        int delimiterCount = countOccurrences(outStr, "+---+---+---+");
        int rowCount = countOccurrences(outStr, "|   |   |   |");
        assertTrue(delimiterCount >= 4, "Zu wenige Begrenzungszeilen mit Variablen im zweiten Teil");
        assertTrue(rowCount >= 3, "Zu wenige Inhaltszeilen mit Variablen im zweiten Teil");

        // THIRD PART prüfen (gefülltes Board)
        String normalized = outStr.replaceAll("\\s+", " ").trim();

        assertTrue(normalized.matches(".*\\|\\s*X\\s*\\|\\s*O\\s*\\|\\s*X\\s*\\|.*"),
                "Zeile 1 falsch oder fehlt im dritten Teil");
        assertTrue(normalized.matches(".*\\|\\s*O\\s*\\|\\s*X\\s*\\|\\s*X\\s*\\|.*"),
                "Zeile 2 falsch oder fehlt im dritten Teil");
        assertTrue(normalized.matches(".*\\|\\s*O\\s*\\|\\s*O\\s*\\|\\s*X\\s*\\|.*"),
                "Zeile 3 falsch oder fehlt im dritten Teil");
    }

    private int countOccurrences(String haystack, String needle) {
        return haystack.split(java.util.regex.Pattern.quote(needle), -1).length - 1;
    }
}
