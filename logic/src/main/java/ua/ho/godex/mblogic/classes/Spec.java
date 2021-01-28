package ua.ho.godex.mblogic.classes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Spec {
    private static final char[] charArray = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private char character;// cher
    private int value;// integer value
    private boolean canBeZero = true;//не может быть 0

    public Spec(char character, int value, boolean canBeZero) {
        this.character = character;
        this.value = value;
        this.canBeZero = canBeZero;
    }

    public Spec(char character, int value) {
        this.character = character;
        this.value = value;
    }

    public char getValChar() {
        return Integer.toString(value).charAt(0);
    }
}