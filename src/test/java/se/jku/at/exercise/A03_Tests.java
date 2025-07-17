package se.jku.at.exercise;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class A03_Tests {

    static String baseSalaryMs;
    static String bonusMs;
    static String montageMs;
    static String incomeMs;
    static String baseSalaryMr;
    static String bonusMr;
    static String montageMr;
    static String incomeMr;
    static String totalIncome;

    @BeforeAll
    static void setup() {
        int noAssemblyMs = 28;
        int noAssemblyMr = 33;

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        String simulatedInput = String.join(System.lineSeparator(),
                String.valueOf(noAssemblyMr),
                String.valueOf(noAssemblyMs)
        );
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8)));

        A03_Lohnberechnungen.main(new String[]{});

        System.setOut(System.out);
        String actual = output.toString().replace("\r\n", "\n");

        String regex = """
                (?s).*Frau Moser:.*Grundgehalt:\\s+([\\d.,]+) Euro\\n.*Bonus:\\s+([\\d.,]+) Euro\\n.*Montage:\\s+([\\d.,]+) Euro.*?\\n.*Gesamt:\\s+([\\d.,]+) Euro\\n.*Herr Moser:.*Grundgehalt:\\s+([\\d.,]+) Euro\\n.*Bonus:\\s+([\\d.,]+) Euro\\n.*Montage:\\s+([\\d.,]+) Euro.*?\\n.*Gesamt:\\s+([\\d.,]+) Euro\\n.*Gemeinsames Einkommen:\\s+([\\d.,]+) Euro.*""";

        Pattern p = Pattern.compile(regex);
        var m = p.matcher(actual);

        if (m.matches()) {
            baseSalaryMs = m.group(1);
            bonusMs = m.group(2);
            montageMs = m.group(3);
            incomeMs = m.group(4);
            baseSalaryMr = m.group(5);
            bonusMr = m.group(6);
            montageMr = m.group(7);
            incomeMr = m.group(8);
            totalIncome = m.group(9);
        } else {
            throw new AssertionError("Output did not match expected pattern.\nActual output:\n" + actual);
        }
    }

    @Test
    void testBaseSalaryMs() {
        assertTrue(matches(baseSalaryMs, "42,000", "42.000"));
    }

    @Test
    void testBonusMs() {
        assertTrue(matches(bonusMs, "1,500", "1.500"));
    }

    @Test
    void testMontageMs() {
        assertTrue(matches(montageMs, "1,400", "1.400"));
    }

    @Test
    void testIncomeMs() {
        assertTrue(matches(incomeMs, "44,900", "44.900"));
    }

    @Test
    void testBaseSalaryMr() {
        assertTrue(matches(baseSalaryMr, "40,000", "40.000"));
    }

    @Test
    void testBonusMr() {
        assertTrue(matches(bonusMr, "1,000", "1.000"));
    }

    @Test
    void testMontageMr() {
        assertTrue(matches(montageMr, "1,650", "1.650"));
    }

    @Test
    void testIncomeMr() {
        assertTrue(matches(incomeMr, "42,650", "42.650"));
    }

    @Test
    void testTotalIncome() {
        assertTrue(matches(totalIncome, "87,550", "87.550"));
    }

    private boolean matches(String actual, String... expectedVariants) {
        for (String expected : expectedVariants) {
            if (actual.equals(expected)) return true;
        }
        return false;
    }
}
