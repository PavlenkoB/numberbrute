package ua.ho.godex.mblogic.classes;


import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ua.ho.godex.mblogic.classes.EnumMathOperation.DIVIDE;
import static ua.ho.godex.mblogic.classes.EnumMathOperation.EQUALS;
import static ua.ho.godex.mblogic.classes.EnumMathOperation.MINUS;
import static ua.ho.godex.mblogic.classes.EnumMathOperation.MIXED;
import static ua.ho.godex.mblogic.classes.EnumMathOperation.MULTIPLY;
import static ua.ho.godex.mblogic.classes.EnumMathOperation.PLUS;

/**
 * Created by Bohdan Povlenko on 05.10.14.
 */
@Log4j2
public class MathBrute {
    private static final int COUNT_LIMIT = 10000000;
    private static boolean debugMode = true;
    private static boolean filterLastNumber = false;                // filter by last digits

    private List<Double> numbers = new ArrayList<>();          // array of numbers
    private List<String> numbersStr = new ArrayList<>();       // array of symbol numbers
    private List<EnumMathOperation> actions = new ArrayList<>();       // array of do
    private List<Spec> simbols = new ArrayList<>();
    private Double intResult;                                       //result string
    private String intString;                                       // result string after replace
    private List<Integer> lastNumbers = new ArrayList<>();     // last number of numeric
    private boolean findAllAnswers;
    private List<MathBruteResult> resultArray = new ArrayList<>();
    private String inputString;
    private Integer iterationCounter = 0;
    private EnumMathOperation actionsType = EnumMathOperation.MIXED;

    public MathBrute(String string) {
        this(string, false);
    }

    public MathBrute(String string, Boolean findAllAnswers) {
        this.inputString = string;
        this.findAllAnswers = findAllAnswers;
    }

    public String getResultOfCalculation() {
        this.reset();
        this.mathResultStr();
        if (resultArray.isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("Result array is empty");
        }
        return resultArray.get(0).getResultString();
    }

    public void charInc(Integer index) {
        simbols.get(index).setIntValue(simbols.get(index).getIntValue() + 1);
        if (simbols.get(index).getIntValue() > 9 && (index < simbols.size() - 1)) {
            if (simbols.get(index).isCanBeZero())
                simbols.get(index).setIntValue(0);
            else
                simbols.get(index).setIntValue(1);
            charInc(index + 1);
        }

    }

    /**
     * Reset calculations
     */
    private void reset() {
        for (Spec simbol : this.simbols) {
            simbol.setIntValue(0);
        }
        this.resultArray.clear();
        this.numbersStr.clear();
        this.iterationCounter = 0;
    }

    private void prepare() {
        inputString = inputString.replace(" ", "");
        inputString = inputString.toLowerCase();

        numbersStr.addAll(Arrays.asList(inputString.split("[^0-9a-zA-Zа-яА-Я]+")));

        for (String one : inputString.split("[0-9a-zA-Zа-яА-Я]+")) {
            if (one.length() > 0)
                actions.add(EnumMathOperation.fromChar(one.charAt(0)));
        }
        actionsType = getActionType();

        //sobraty simvolu
        ArrayList<Character> allchar = new ArrayList<>();       // array of do
        for (String numeric : numbersStr) { //for every numeric
            for (Character character : numeric.toCharArray()) {//for every character
                if (!allchar.contains(character)) {
                    allchar.add(character);
                    if (character.equals(numeric.charAt(0)))
                        simbols.add(new Spec(character, 1, false));// if char firs in numeric
                    else
                        simbols.add(new Spec(character, 0));// if char not firs in numeric

                } else {
                    if (allchar.contains(character) && character.equals(numeric.charAt(0))) {
                        for (Spec spec : simbols) {
                            if (spec.getCharacter() == character) {
                                spec.setIntValue(1);
                            }
                        }
                    }
                }
            }
        }
        //delete result from numbers
        numbersStr.remove(numbersStr.size() - 1);

    }

    private void mathResultStr() {
        this.prepare();
        boolean colision;
        while (simbols.get(simbols.size() - 1).getIntValue() < 10) {
            if (!resultArray.isEmpty() && !findAllAnswers) {
                break;
            }
            this.iterationCounter++;
            if (this.iterationCounter > COUNT_LIMIT) {
                log.warn("!!!!!!!!!this.iteration> {}", COUNT_LIMIT);
                break;
            }
            charInc(0);
            colision = false;
            for (Spec spec : simbols) {
                for (Spec spec1 : simbols) {
                    if (spec1.getIntValue() == spec.getIntValue() && spec1.getCharacter() != spec.getCharacter()) {
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
            //todo filter not ready
            if (filterLastNumber && actionsType != EnumMathOperation.MIXED) {
                for (Double last : this.numbers) {
                    lastNumbers.add(last.intValue() % 10);
                }
                if (actionsType == EnumMathOperation.PLUS) {
                    int sum = 0;
                    //todo extract counter how many numbers in better place
                    int numbers = lastNumbers.size();
                    for (int count = 0; count < numbers - 1; count++) {
                        sum += lastNumbers.get(count);
                    }
                    if (sum % 10 != lastNumbers.get(numbers - 1)) {
                        continue;
                    }
                } else if (actionsType == MULTIPLY) {
                    if ((lastNumbers.get(0) * lastNumbers.get(1)) % 10 != lastNumbers.get(2))
                        continue;
                }
            }
            if (debugMode) {
                log.debug(showValues());
            }
            if (this.mathResult().equals(intResult)) {
                resultArray.add(new MathBruteResult(inputString, intString));
            }
        }
    }

    private String showValues() {
        return simbols.stream().map(spec -> spec.getCharacter() + "->" + spec.getIntValue() + "|").collect(Collectors.joining());
    }

    /**
     * convert string with characters to string with numbers and slice ut
     */
    private void copyStrToInt() {//copy array of char numbers to integer with replace
        intString = inputString;
        simbols.forEach(simbol -> intString = intString.replace(simbol.getCharacter(), simbol.getValAsChar()));

        sliceIntString(intString);
        //todo try glue by dot, replace charters and split to numbers
    }

    /**
     * @return sum of all numbers
     */
    public Double mathResult() {
        List<Double> numberstmp = numbers;
        int pos = 0;
        boolean spec = false;
        Double tmpDouble = null;
        Double ret = numberstmp.get(0);
        if (actionsType == EnumMathOperation.MIXED) {
            for (int action = 0; action < actions.size(); action++) {
                if (actions.get(action).equals(MULTIPLY)) {
                    if (!spec) {
                        pos = action;
                    }
                    spec = true;
                    tmpDouble *= numberstmp.get(action + 1);
                }
                if (actions.get(action) == DIVIDE) {
                    if (!spec) {
                        pos = action;
                    }
                    spec = true;
                    tmpDouble /= numberstmp.get(action + 1);
                }
                if ((actions.get(action) == PLUS || actions.get(action) == MINUS) && spec) {
                    numberstmp.set(pos, tmpDouble);
                    spec = false;
                }
                if (action + 1 == actions.size() && spec) {
                    numberstmp.set(pos, tmpDouble);
                    spec = false;
                }
            }
            ret = numberstmp.get(0);
            for (int diya = 0; diya < actions.size(); diya++) {
                if (actions.get(diya) == PLUS) {
                    ret += numberstmp.get(diya + 1);
                }
                if (actions.get(diya) == MINUS) {
                    ret -= numberstmp.get(diya + 1);
                }
            }
        } else {
            for (int poz = 1; poz < numberstmp.size(); poz++) {
                if (actionsType == EnumMathOperation.PLUS) {
                    ret += numberstmp.get(poz);
                } else if (actionsType == EnumMathOperation.MINUS) {
                    ret -= numberstmp.get(poz);
                } else if (actionsType == MULTIPLY) {
                    ret *= numberstmp.get(poz);
                } else if (actionsType == EnumMathOperation.DIVIDE) {
                    ret /= numberstmp.get(poz);
                }
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
        for (String one : string.split("[^0-9a-zA-Zа-яА-Я]+")) {
            numbers.add(Double.parseDouble(one));
        }
        intResult = numbers.get(numbers.size() - 1);
        numbers.remove(numbers.size() - 1);
    }

    private EnumMathOperation getActionType() {
        EnumMathOperation firstAction = actions.get(0);
        for (EnumMathOperation character : actions) {
            if (!firstAction.equals(character) && !character.equals(EQUALS)) return EnumMathOperation.MIXED;
        }
        if (firstAction.equals(PLUS)) {
            return PLUS;
        } else if (firstAction.equals(MINUS)) {
            return MINUS;
        } else if (firstAction.equals(MULTIPLY)) {
            return MULTIPLY;
        } else if (firstAction.equals(DIVIDE)) {
            return DIVIDE;
        }
        return MIXED;
    }

}
