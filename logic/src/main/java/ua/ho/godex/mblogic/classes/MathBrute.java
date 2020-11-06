package ua.ho.godex.mblogic.classes;

import java.util.ArrayList;

/**
 * Created by tf101 on 05.10.14.
 */
public class MathBrute {
    private static final int COUNT_LIMIT = 10000000;
    private static boolean debug = false;
    private static boolean filterLastNumber = false;// фильтр по последним цыфрам

    private ArrayList<Double> numbers = new ArrayList<>(); // array of numbers
    private ArrayList<String> numbersStr = new ArrayList<>(); // array of symbol numbers
    private ArrayList<Character> actions = new ArrayList<>();       // array of do
    private ArrayList<Spec> simbols = new ArrayList<>();
    private Double intResult;//result string
    private String intString;// result string after replace
    private ArrayList<Integer> lastNumbers = new ArrayList<>();// last number of numeric
    private boolean findAllAnswers = false;
    private ArrayList<MathBruteResult> resultArray = new ArrayList<>();
    private String inputString;
    private Integer iterationCounter = 0;
    private Type typeOfTask;
    private mathOper actionsType = mathOper.MIXED;

    public MathBrute(String string) {
        this.inputString = string;
        this.findAllAnswers = false;
        this.typeOfTask = Type.CHAR;
    }

    public String getResultOfCalculation() {
        this.reset();
        this.mathResultStr();
        if (resultArray.isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("Result array is empty");
        }
        return resultArray.get(0).getResult();
    }

    public void charInc(Integer index) {
        simbols.get(index).setValue(simbols.get(index).getValue() + 1);
        if (simbols.get(index).getValue() > 9 && (index < simbols.size() - 1)) {
            if (simbols.get(index).getCantBeZero())
                simbols.get(index).setValue(1);
            else
                simbols.get(index).setValue(0);
            charInc(index + 1);
        }

    }

    /**
     * Сброс расчетов
     */
    private void reset() {
        for (Spec simbol : this.simbols) {
            simbol.setValue(0);
        }
        this.resultArray.clear();
        this.numbersStr.clear();
        this.iterationCounter = 0;
    }

    private void prepare() {
        inputString = inputString.replace(" ", "");
        inputString = inputString.toLowerCase();
        if (typeOfTask == Type.NUMBER) {
            sliceIntString(inputString);
        }
        if (typeOfTask == Type.CHAR) {
            for (String one : inputString.split("[^0-9a-zA-Zа-яА-Я]+")) {
                numbersStr.add(one);
            }
            for (String one : inputString.split("[0-9a-zA-Zа-яА-Я]+")) {
                if (one.length() > 0)
                    actions.add(one.charAt(0));
            }
            actionsType = getActionType();

            //sobraty simvolu
            ArrayList<Character> allchar = new ArrayList<>();       // array of do
            for (String numeric : numbersStr) { //for every numeric
                for (Character character : numeric.toCharArray()) {//for every character
                    if (!allchar.contains(character)) {
                        allchar.add(character);
                        if (character.equals(numeric.charAt(0)))
                            simbols.add(new Spec(character, 1, true));// if char firs in numeric
                        else
                            simbols.add(new Spec(character, 0));// if char not firs in numeric

                    } else {
                        if (allchar.contains(character) && character.equals(numeric.charAt(0))) {
                            for (Spec spec : simbols) {
                                if (spec.getCharacter() == character) {
                                    spec.setValue(1);
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

    private void mathResultStr() {
        this.prepare();
        boolean colision;
        while (simbols.get(simbols.size() - 1).getValue() < 10) {
            if (!resultArray.isEmpty() && !findAllAnswers) {
                break;
            }
            this.iterationCounter++;
            if (this.iterationCounter > COUNT_LIMIT) {
                System.out.println("!!!!!!!!!this.iteration>" + COUNT_LIMIT);
                break;
            }
            charInc(0);
            colision = false;
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
            //todo а фильтр то не готов
            if (filterLastNumber && actionsType != mathOper.MIXED) {
                for (Double last : this.numbers) {
                    lastNumbers.add(last.intValue() % 10);
                }
                if (actionsType == mathOper.PLUS) {
                    int sum = 0;
                    //todo вынисти счетчик сколько цыфр в другое место
                    int numbers = lastNumbers.size();
                    for (int count = 0; count < numbers - 1; count++) {
                        sum += lastNumbers.get(count);
                    }
                    if (sum % 10 != lastNumbers.get(numbers - 1)) {
                        continue;
                    }
                } else if (actionsType == mathOper.MULTIPLY) {
                    if ((lastNumbers.get(0) * lastNumbers.get(1)) % 10 != lastNumbers.get(2))
                        continue;
                }
            }
            if(debug){
                System.out.println(showValues());
            }
            if (this.mathResult().equals(intResult)) {
                /*stringBuilder = new StringBuilder();
                for (Spec spec : simbols) {
                    stringBuilder.append(spec.getCharacter() + ":" + spec.getValue() + "|");
                }
                ressultArray.add(stringBuilder.toString());*/
                resultArray.add(new MathBruteResult(inputString, intString));
            }
        }
    }

    private String showValues() {
        StringBuilder builder = new StringBuilder();
        for (Spec spec : simbols) {
            builder.append(spec.getCharacter() + ":" + spec.getValue() + "|");
        }
        return builder.toString();
    }

    /**
     * convert string with characters to string with numbers and slice ut
     */
    private void copyStrToInt() {//copy array of char numbers to integer with replace
        intString = inputString;
        for (int sim = 0; sim < simbols.size(); sim++) {
            intString = intString.replace(simbols.get(sim).getCharacter(), simbols.get(sim).getValChar());
        }/**/

        sliceIntString(intString);
        /**/
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
            for (int poz = 1; poz < numberstmp.size(); poz++) {
                if (actionsType == mathOper.PLUS) {
                    ret += numberstmp.get(poz);
                } else if (actionsType == mathOper.MINUS) {
                    ret -= numberstmp.get(poz);
                } else if (actionsType == mathOper.MULTIPLY) {
                    ret *= numberstmp.get(poz);
                } else if (actionsType == mathOper.DIVIDE) {
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

    private mathOper getActionType() {
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

    public enum Type {CHAR, NUMBER}

    /**
     * 0=mixed
     * 1=+
     * 2=-
     * 3=*
     * 4=/
     */
    public enum mathOper {
        MIXED, PLUS, MINUS, MULTIPLY, DIVIDE
    }
}
