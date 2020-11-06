package ua.ho.godex.mblogic.classes;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MathBruteTest {

    @ParameterizedTest
    @CsvSource(value = {
            "КНИГА+КНИГА+КНИГА=НАУКА:28375+28375+28375=85125",
            "A+FAT=ASS:9+891=900",
            "AB+BC+CA=ABC:19+98+81=198",
            "ABCDE*4=EDCBA:21978*4=87912",
            "один+один=много:6823+6823=13646",
            "книга+книга+книга=наука:28375+28375+28375=85125",
            "ВАГОН+ВАГОН=СОСТАВ:85679+85679=171358",
            "НИТКА+НИТКА=ТКАНЬ:15306+15306=30612",
    },
            delimiter = ':')
    void getResultOfCalculation(String input, String expected) {
        MathBrute mathBrute = new MathBrute(input, false);
        assertEquals(expected, mathBrute.getResultOfCalculation());
    }
}