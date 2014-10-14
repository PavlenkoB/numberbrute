package ua.ho.godex;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by tf101 on 05.10.14.
 */
public class parsedString {
    public class Spec {
        private Character character;

        public boolean isNotZero() {
            return notZero;
        }

        public void setNotZero(boolean notZero) {
            this.notZero = notZero;
        }

        private boolean notZero;
        private Integer value;

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

    public ArrayList<Double> numbers = new ArrayList<>(); // array of numbers
    public ArrayList<String> numbersStr = new ArrayList<>(); // array of symbol numbers
    public ArrayList<Character> actions = new ArrayList<>();       // array of do
    public ArrayList<Spec> simbols = new ArrayList<>();
    public Double intResult;//result string
    public ArrayList<Integer> lastNumbers = new ArrayList<>();// last number of numeric
    public String intString;// result string after replace

    public boolean allAnswers = true;

    public Double progress = new Double(0);

    public ArrayList<String> ressultarray = new ArrayList<>();
    public String strResult = new String();
    public String inputString;

    public Integer iteration = 0;

    public Type type;
    public mathOper actionsType = mathOper.MIXED;

    public boolean debug = false;
    public boolean debugMath = false;

    public static enum Type {CHAR, NUMBER}

    ;

    /**
     * 0=mixed
     * 1=+
     * 2=-
     * 3=*
     * 4=/
     */
    public static enum mathOper {
        MIXED, PLUS, MINUS, MULTIPLY, DIVIDE
    }

    ;


    public parsedString() {
    }

    public void charinc(Integer index) {
        simbols.get(index).setValue(simbols.get(index).getValue() + 1);
        if (simbols.get(index).getValue() > 9 && (index < simbols.size() - 1)) {
            if (simbols.get(index).isNotZero())
                simbols.get(index).setValue(1);
            else
                simbols.get(index).setValue(0);
            charinc(index + 1);
        }

    }

    public Integer fact(Integer num) {
        return (num == 0) ? 1 : num * fact(num - 1);
    }

    public void mathResultstr() {
        StringBuilder stringBuilder;
        Double full = Math.pow(10, simbols.size());
        while (simbols.get(simbols.size() - 1).getValue() < 10) {
            progress++;
            if (ressultarray.size() != 0 && !allAnswers) {
                break;
            }
            charinc(0);
            boolean colision = false;
            for (Spec spec : simbols) {
                for (Spec spec1 : simbols) {
                    if (spec1.getValue() == spec.getValue() && spec1.getCharacter() != spec.getCharacter()) {
                        colision = true;
                        break;
                    }
                }
                if (colision)
                    break;
            }
            if (colision)
                continue;
            copyStrToInt();
/*
            if (lastNumbers.size() == 3) {
                if (actionsType == mathOper.PLUS) {
                    if ((lastNumbers.get(0) + lastNumbers.get(1)) % 10 != lastNumbers.get(2))
                        continue;
                } else if (actionsType == mathOper.MULTIPLY) {
                    if ((lastNumbers.get(0) * lastNumbers.get(1)) % 10 != lastNumbers.get(2))
                        continue;
                }
            }*/
            //iteration++;
            if (this.mathResult().equals(intResult)) {stringBuilder=new StringBuilder();
                for (Spec spec : simbols) {
                    stringBuilder.append(spec.getCharacter()+":"+spec.getValue()+"|");
                }

                ressultarray.add(stringBuilder.toString());
                ressultarray.add(inputString + "->" + intString);
            }/**/
        }
        //ressultarray.add("iteration=" + iteration);
    }

    /**
     * convert string with characters to string with numbers and slice ut
     */
    public void copyStrToInt() {//copy array of char numbers to integer with replace
        intString = inputString;
        for (int sim = 0; sim < simbols.size(); sim++) {
            intString = intString.replace(simbols.get(sim).getCharacter(), simbols.get(sim).getValue().toString().charAt(0));
        }
        sliceIntString(intString);
        //todo попробовать склеивать по точке заменять символы и розделять на числа
    }

    /**
     * @return sum of all numbers
     */
    public Double mathResult() {
        ArrayList<Double> numberstmp = numbers;
        int pos = 0;
        boolean spec = false;
        Double tmpdouble = null;
        Double ret = numberstmp.get(0);
        if (actionsType == mathOper.MIXED) {
            for (int diya = 0; diya < actions.size(); diya++) {
                if (actions.get(diya) == '*') {
                    if (spec == false) {
                        pos = diya;
                        tmpdouble = numberstmp.get(diya);
                    }
                    spec = true;
                    tmpdouble *= numberstmp.get(diya + 1);
                }
                if (actions.get(diya) == '/') {
                    if (spec == false) {
                        pos = diya;
                        tmpdouble /= numberstmp.get(diya);
                    }
                    spec = true;
                    tmpdouble /= numberstmp.get(diya + 1);
                }
                if ((actions.get(diya) == '+' || actions.get(diya) == '-') && spec == true) {
                    numberstmp.set(pos, tmpdouble);
                    spec = false;
                }
                if (diya + 1 == actions.size() && spec == true) {
                    numberstmp.set(pos, tmpdouble);
                    spec = false;
                }
            }
            ret = numberstmp.get(0);
            for (int diya = 0; diya < actions.size(); diya++) {
                if (actions.get(diya) == '+') {
                    ret += numberstmp.get(diya + 1);
                }
                if (actions.get(diya) == '-') {
                    ret -= numberstmp.get(diya + 1);
                }
            }
        } else {
            if (actionsType == mathOper.PLUS) {
                for (Double number : numberstmp)
                    if (!number.equals(numberstmp.get(0)))
                        ret += number;
            } else if (actionsType == mathOper.MINUS) {
                for (Double number : numberstmp)
                    if (!number.equals(numberstmp.get(0)))
                        ret -= number;
            } else if (actionsType == mathOper.MULTIPLY) {
                for (Double number : numberstmp)
                    if (!number.equals(numberstmp.get(0)))
                        ret *= number;
            } else if (actionsType == mathOper.DIVIDE) {
                for (Double number : numberstmp)
                    if (!number.equals(numberstmp.get(0)))
                        ret /= number;
            }
        }
        return ret;
    }

    /**
     * slice string for numbers
     * and add last number of numeric to array of last numbers
     *
     * @param string string to slice for numbers
     */
    private void sliceIntString(String string) {
        numbers.clear();
        //lastNumbers.clear();
        for (String one : string.split("[^0-9a-zA-Zа-яА-Я]+")) {
            numbers.add(Double.parseDouble(one));
            //lastNumbers.add(Integer.parseInt(one.substring(one.length() - 1)));
        }
        intResult = numbers.get(numbers.size() - 1);
        numbers.remove(numbers.size() - 1);
    }

    /**
     * @param string input string
     * @param type   type fo string 'NUMBER' ot 'CHAR'
     * @param all    display all(true) results or only first(false)
     */
    public parsedString(String string, Type type, boolean all) {
        this.allAnswers = all;
        //string=string.replace(" ","");
        string = string.toLowerCase();
        this.inputString = string;
        this.type = type;
        if (type == Type.NUMBER) {
            sliceIntString(string);
        }
        if (type == Type.CHAR) {
            for (String one : string.split("[^0-9a-zA-Zа-яА-Я]+")) {
                numbersStr.add(one);
            }
            for (String one : string.toString().split("[0-9a-zA-Zа-яА-Я]+")) {
                if (one.length() > 0)
                    actions.add(one.charAt(0));
            }
            actionsType = getactionType();

            //sobraty simvolu
            ArrayList<Character> allchar = new ArrayList<>();       // array of do
            for (String numeric : numbersStr) { //for every numeric
                for (Character character : numeric.toCharArray()) {//for every character
                    if (!allchar.contains(character)) {
                        allchar.add(character);
                        if (character.equals(numeric.charAt(0)))
                            simbols.add(new Spec(character, 1, true));// if char firs in numeric
                        else
                            simbols.add(new Spec(character, 0, false));// if char not firs in numeric

                    } else {
                        if (allchar.contains(character) && character.equals(numeric.charAt(0))) {
                            for (Spec spec : simbols) {
                                if (spec.getCharacter().equals(character)) {
                                    spec.setValue(1);
                                    spec.setNotZero(true);
                                }
                            }
                        }
                    }
                }
            }

            //delete result from numbers
            numbersStr.remove(numbersStr.size() - 1);
        }
    }

    private mathOper getactionType() {

        Character firstAction = actions.get(0);
        for (Character character : actions) {
            if (firstAction.equals(character) || character.equals('='))
                continue;
            else
                return mathOper.MIXED;
        }
        if (firstAction.equals('+')) {
            return mathOper.PLUS;
        } else if (firstAction.equals('-')) {
            return mathOper.MINUS;
        } else if (firstAction.equals('*')) {
            return mathOper.MULTIPLY;
        } else if (firstAction.equals('/')) {
            return mathOper.DIVIDE;
        }
        return mathOper.MIXED;
    }
}
