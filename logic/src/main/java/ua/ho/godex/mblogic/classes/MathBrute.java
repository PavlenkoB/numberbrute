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

    private final boolean findAllAnswers;
    private final String inputStringTrimmed;

    public MathBrute(String string) {
        this(string, false);
    }

    public MathBrute(String string, Boolean findAllAnswers) {
        this.inputStringTrimmed = string.replace(" ", "").toLowerCase();
        this.findAllAnswers = findAllAnswers;
    }

    public List<MathBruteResult> startCalculation(){
         return this.mathResultStr(this.inputStringTrimmed);
    }

    public String getResultOfCalculation() {
        List<MathBruteResult> resultArray = this.startCalculation();
        if (!resultArray.isEmpty()) {
            return resultArray.get(0).getResultString();
        }
        throw new ArrayIndexOutOfBoundsException("Result array is empty");
    }

    private List<EnumMathOperation> getActionsArr(String inputStringTrimmed) {
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

    private List<MathBruteResult> mathResultStr(String inputStringTrimmed) {
        Long iterationCounter = 0L;
        List<Spec> symbolsArr = extractSymbols(inputStringTrimmed);
        List<EnumMathOperation> actionsArr = getActionsArr(inputStringTrimmed);
        EnumMathOperation actionsType = identifyActionType(actionsArr);

        final List<Integer> lastNumbers = new ArrayList<>();

        final List<MathBruteResult> resultArray = new ArrayList<>();

        while (true) {
            if (!(symbolsArr.get(symbolsArr.size() - 1).getIntValue() < MAX_POSSIBLE_NUMBER)) break;
            if (!resultArray.isEmpty() && !findAllAnswers) break;
            checkIterator(iterationCounter);

            charInc(symbolsArr, 0);
            if (isSymbolsHasCollision(symbolsArr)) continue;

            String stringWithCharsToNumbers = transformStringWithCharsToNumbers(inputStringTrimmed, symbolsArr);
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
            if (this.mathResult(stringNumbers, actionsType, actionsArr).equals(calculationResult)) {
                resultArray.add(new MathBruteResult(inputStringTrimmed, stringWithCharsToNumbers));
            }
        }
        return resultArray;
    }

    private void checkIterator(Long iterationCounter) {
        iterationCounter++;
        if (iterationCounter > ITERATION_LIMIT) {
            log.error("ITERATION_LIMIT reached {}", ITERATION_LIMIT);
            throw new UnsupportedOperationException("ITERATION_LIMIT reached " + ITERATION_LIMIT);
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
        Spec spec = symbolsArr.get(index);
        spec.setIntValue(spec.getIntValue() + 1);
        if (spec.getIntValue() > 9 && (index < symbolsArr.size() - 1)) {
            spec.setIntValue((spec.isCanBeZero()) ? 0 : 1);
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
    private Double mathResult(List<Double> numbers, EnumMathOperation actionsType, List<EnumMathOperation> actionsArr) {
        Double ret;
        if (actionsType == EnumMathOperation.MIXED) {
            double tmpDouble = 0;
            int pos = 0;
            boolean spec = false;
            for (int action = 0; action < actionsArr.size(); action++) {
                EnumMathOperation enumMathOperation = actionsArr.get(action);
                if (enumMathOperation == MULTIPLY || enumMathOperation == DIVIDE) {
                    if (!spec) {
                        pos = action;
                    }
                    spec = true;
                    tmpDouble = extractFunction(enumMathOperation).applyAsDouble(numbers.get(action), numbers.get(action + 1));
                } else if ((enumMathOperation == PLUS || enumMathOperation == MINUS) && spec) {
                    numbers.set(pos, tmpDouble);
                    spec = false;
                }
                if (action + 1 == actionsArr.size() && spec) {
                    numbers.set(pos, tmpDouble);
                    spec = false;
                }
            }
            ret = numbers.get(0);
            for (int diya = 0; diya < actionsArr.size(); diya++) {
                ret = extractFunction(actionsArr.get(diya))
                        .applyAsDouble(ret, numbers.get(diya + 1));
            }
        } else {
            ret = numbers.stream()
                    .mapToDouble(value -> value)
                    .reduce(extractFunction(actionsType))
                    .getAsDouble();
        }
        return ret;
    }

    private DoubleBinaryOperator extractFunction(EnumMathOperation actionsType) {
        if (actionsType == PLUS) return Double::sum;
        else if (actionsType == MINUS) return (a, b) -> a - b;
        else if (actionsType == MULTIPLY) return (a, b) -> a * b;
        else if (actionsType == DIVIDE) return (a, b) -> a / b;
        throw new UnsupportedOperationException("Unsupported action type");
    }

    private List<Double> sliceIntStringToNumbersArray(String string) {
        return Arrays.stream(string.split(REGEX_FOR_EXTRACT_NUMBERS)).map(Double::parseDouble).collect(Collectors.toList());
    }

    private EnumMathOperation identifyActionType(final List<EnumMathOperation> actionsArr) {
        EnumMathOperation firstAction = actionsArr.get(0);
        for (EnumMathOperation action : actionsArr) {
            if (!firstAction.equals(action) && !action.equals(EQUALS)) return EnumMathOperation.MIXED;
        }
        return firstAction;
    }
}