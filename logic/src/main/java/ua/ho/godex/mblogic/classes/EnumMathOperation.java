package ua.ho.godex.mblogic.classes;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 0=mixed
 * 1=+
 * 2=-
 * 3=*
 * 4=/
 */
public enum EnumMathOperation {
    MIXED('m'), PLUS('+'), MINUS('-'), MULTIPLY('*'), DIVIDE('/'),
    EQUALS('=');

    @Getter
    private final Character character;

    public static EnumMathOperation fromChar(char character) throws RuntimeException {
        List<EnumMathOperation> operationList = Arrays.stream(values()).filter(enumMathOperation -> enumMathOperation.getCharacter().equals(character)).collect(Collectors.toList());
        if (operationList.size() != 1) {
            throw new RuntimeException("Unsupported character " + character);
        }
        return operationList.get(0);
    }

    public Character asChar() {
        return character;
    }

    EnumMathOperation(char asChar) {
        this.character = asChar;
    }
}