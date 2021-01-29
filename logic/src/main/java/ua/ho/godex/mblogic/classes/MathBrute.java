package ua.ho.godex.mblogic.classes;


import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleBinaryOperator;
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
    private static final String REGEX_FOR_EXTRACT_NUMBERS = "[^0-9a-zA-Zа-яА-Я]+";
    private static final String REGEX_FOR_EXTRACT_ACTIONS = "[0-9a-zA-Zа-яА-Я]+";
    private static final int MAX_POSSIBLE_NUMBER = 10;

    private static final int ITERATION_LIMIT = 10000000;
    private static final boolean DEBUG_MODE = true;
    private static final boolean FILTER_BY_LAST_NUMBER = false;

    private final List<Integer> lastNumbers = new ArrayList<>();
    private final boolean findAllAnswers;
    private final List<MathBruteResult> resultArray = new ArrayList<>();
    private final String inputStringTrimmed;

    private List<EnumMathOperation> actionsArr;
    private Integer iterationCounter = 0;

    public MathBrute(String string) {
        this(string, false);
    }

    public MathBrute(String string, Boolean findAllAnswers) {
        this.inputStringTrimmed = string.replace(" ", "").toLowerCase();
        this.findAllAnswers = findAllAnswers;
        this.actionsArr = getActionArr(inputStringTrimmed);
    }

    public String getResultOfCalculation() {
        this.mathResultStr();
        if (!resultArray.isEmpty()) {
            return resultArray.get(0).getResultString();
        }
        throw new ArrayIndexOutOfBoundsException("Result array is empty");
    }

    private List<EnumMathOperation> getActionArr(String inputStringTrimmed) {
        return Arrays.stream(inputStringTrimmed.split(REGEX_FOR_EXTRACT_ACTIONS))
                .filter(one -> one.length() > 0)
                .map(one -> EnumMathOperation.fromChar(one.charAt(0)))
                .collect(Collectors.toList());
    }

    private List<Spec> extractSymbols(String inputStringTrimmed) {
        List<String> symbolNumbersArr = Arrays.asList(inputStringTrimmed.split(REGEX_FOR_EXTRACT_NUMBERS));
        //collect symbol
        List<Spec> localSymbols = new ArrayList<>();
        List<Character> allChar = new ArrayList<>();       // array of do
        //for every numeric
        symbolNumbersArr.forEach(numeric -> {
            for (Character character : numeric.toCharArray()) {//for every character
                if (!allChar.contains(character)) {
                    allChar.add(character);
                    if (character.equals(numeric.charAt(0)))
                        localSymbols.add(new Spec(character, 1, false));// if char first in numeric
                    else
                        localSymbols.add(new Spec(character, 0));// if char not first in numeric
                } else {
                    if (allChar.contains(character) && character.equals(numeric.charAt(0))) {
                        for (Spec spec : localSymbols) {
                            if (spec.getCharacter() == character) {
                                spec.setIntValue(1);
                            }
                        }
                    }
                }
            }
        });
        return localSymbols;
    }

    private void mathResultStr() {
        List<Spec> symbolsArr = extractSymbols(inputStringTrimmed);
        EnumMathOperation actionsType = identifyActionType(this.actionsArr);
        while (true) {
            if (!(symbolsArr.get(symbolsArr.size() - 1).getIntValue() < MAX_POSSIBLE_NUMBER)) break;
            if (!resultArray.isEmpty() && !findAllAnswers) break;
            checkIterrator();

            charInc(symbolsArr, 0);
            if (isSymbolsHasCollision(symbolsArr)) continue;

            String stringWithCharsToNumbers = transformStringWithCharsToNumbers(this.inputStringTrimmed, symbolsArr);
            List<Double> stringNumbers = sliceIntStringToNumbersArray(stringWithCharsToNumbers);
            Double calculationResult = stringNumbers.remove(stringNumbers.size() - 1);
            //todo filter not ready
            if (FILTER_BY_LAST_NUMBER && actionsType != EnumMathOperation.MIXED) {
                for (Double last : stringNumbers) {
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
            if (DEBUG_MODE) {
                log.debug(showValues(symbolsArr));
            }
            if (this.mathResult(stringNumbers, actionsType).equals(calculationResult)) {
                resultArray.add(new MathBruteResult(inputStringTrimmed, stringWithCharsToNumbers));
            }
        }
    }

    private void checkIterrator() {
        this.iterationCounter++;
        if (this.iterationCounter > ITERATION_LIMIT) {
            log.error("ITERATION_LIMIT reached {}", ITERATION_LIMIT);
            throw new RuntimeException("ITERATION_LIMIT reached " + ITERATION_LIMIT);
        }
    }

    private boolean isSymbolsHasCollision(List<Spec> symbolsArr) {
        boolean collision = false;
        for (Spec spec : symbolsArr) {
            for (Spec spec1 : symbolsArr) {
                if (spec1.getIntValue() == spec.getIntValue() && spec1.getCharacter() != spec.getCharacter()) {
                    collision = true;
                    break;
                }
            }
            if (collision)
                break;
        }
        return collision;
    }

    private void charInc(List<Spec> symbolsArr, Integer index) {
        symbolsArr.get(index).setIntValue(symbolsArr.get(index).getIntValue() + 1);
        if (symbolsArr.get(index).getIntValue() > 9 && (index < symbolsArr.size() - 1)) {
            if (symbolsArr.get(index).isCanBeZero())
                symbolsArr.get(index).setIntValue(0);
            else
                symbolsArr.get(index).setIntValue(1);
            charInc(symbolsArr, index + 1);
        }
    }

    private String showValues(List<Spec> symbolsArr) {
        return symbolsArr.stream().map(spec -> spec.getCharacter() + "->" + spec.getIntValue() + "|").collect(Collectors.joining());
    }

    /**
     * convert string with characters to string with numbers and slice it
     */
    private String transformStringWithCharsToNumbers(String inputString, List<Spec> symbolsArr) {
        String newString = inputString;
        for (Spec symbol : symbolsArr) {
            newString = newString.replace(symbol.getCharacter(), symbol.getValAsChar());
        }
        return newString;
    }

    /**
     * @return sum of all numbers
     */
    private Double mathResult(List<Double> numbers, EnumMathOperation actionsType) {
        List<Double> numbersTmp = numbers;
        int pos = 0;
        boolean spec = false;
        Double tmpDouble = null;
        Double ret = numbersTmp.get(0);
        if (actionsType == EnumMathOperation.MIXED) {
            for (int action = 0; action < actionsArr.size(); action++) {
                if (actionsArr.get(action).equals(MULTIPLY)) {
                    if (!spec) {
                        pos = action;
                    }
                    spec = true;
                    tmpDouble *= numbersTmp.get(action + 1);
                }
                if (actionsArr.get(action) == DIVIDE) {
                    if (!spec) {
                        pos = action;
                    }
                    spec = true;
                    tmpDouble /= numbersTmp.get(action + 1);
                }
                if ((actionsArr.get(action) == PLUS || actionsArr.get(action) == MINUS) && spec) {
                    numbersTmp.set(pos, tmpDouble);
                    spec = false;
                }
                if (action + 1 == actionsArr.size() && spec) {
                    numbersTmp.set(pos, tmpDouble);
                    spec = false;
                }
            }

            ret = numbersTmp.get(0);
            for (int diya = 0; diya < actionsArr.size(); diya++) {
                if (actionsArr.get(diya) == PLUS) {
                    ret += numbersTmp.get(diya + 1);
                }
                if (actionsArr.get(diya) == MINUS) {
                    ret -= numbersTmp.get(diya + 1);
                }
            }
        } else {
            ret = numbersTmp.stream()
                    .mapToDouble(value -> value)
                    .reduce(extractFunction(actionsType))
                    .getAsDouble();
        }
        return ret;
    }

    private DoubleBinaryOperator extractFunction(EnumMathOperation actionsType) throws RuntimeException {
        switch (actionsType) {
            case PLUS:
                return (a, b) -> a + b;
            case MINUS:
                return (a, b) -> a - b;
            case MULTIPLY:
                return (a, b) -> a * b;
            case DIVIDE:
                return (a, b) -> a / b;
        }
        throw new RuntimeException("Unsupported action type");
    }

    private List<Double> sliceIntStringToNumbersArray(String string) {

        return Arrays.stream(string.split(REGEX_FOR_EXTRACT_NUMBERS)).map(Double::parseDouble).collect(Collectors.toList());
    }

    private EnumMathOperation identifyActionType(final List<EnumMathOperation> actionsArr) {
        EnumMathOperation firstAction = actionsArr.get(0);
        for (EnumMathOperation character : actionsArr) {
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
