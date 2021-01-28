package ua.ho.godex.mblogic.classes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Spec {
    private char character;
    private int intValue;
    private boolean canBeZero = true;

    public Spec(char character, int intValue, boolean canBeZero) {
        this.character = character;
        this.intValue = intValue;
        this.canBeZero = canBeZero;
    }

    public Spec(char character, int intValue) {
        this.character = character;
        this.intValue = intValue;
    }

    public char getValAsChar() {
        return Integer.toString(intValue).charAt(0);
    }
}