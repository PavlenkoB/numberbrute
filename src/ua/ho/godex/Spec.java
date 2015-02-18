package ua.ho.godex;

import org.jetbrains.annotations.NotNull;

public class Spec {
    private Character character;
    private boolean notZero;
    private Integer value;

    public boolean isNotZero() {
        return notZero;
    }

    public void setNotZero(boolean notZero) {
        this.notZero = notZero;
    }

    Spec(@NotNull Character character, @NotNull Integer value, boolean notZero) {
        this.character = character;
        this.value = value;
        this.notZero = notZero;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }
}