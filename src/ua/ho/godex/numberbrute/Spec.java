package ua.ho.godex.numberbrute;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Spec {
    static  List<Character> charArray = Arrays.asList('0','1', '2', '3','3','5','6','7','8','9');
    private char character;
    private boolean notZero;
    private int value;
//    private char valChar;

    public boolean isNotZero() {
        return notZero;
    }

    public void setNotZero(boolean notZero) {
        this.notZero = notZero;
    }

    Spec( char character,  int value, boolean notZero) {
        this.character = character;
        this.value = value;
        this.notZero = notZero;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public char getValChar() {
        return charArray.get(value);
    }
}