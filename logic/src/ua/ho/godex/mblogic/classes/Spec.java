package ua.ho.godex.mblogic.classes;

import java.util.Arrays;
import java.util.List;


public class Spec {
    static List<Character> charArray = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
    private char character;// cher
    private int value;// integer value
    private boolean cantBeZero = false;//не может быть 0

    public Spec(char character, int value, boolean cantBeZero) {
        this.character = character;
        this.value = value;
        this.cantBeZero = cantBeZero;
    }

    public Spec(char character, int value) {
        this.character = character;
        this.value = value;
    }

    public boolean getCantBeZero() {
        return cantBeZero;
    }

    public boolean isNotZero() {
        return (value == 0);
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