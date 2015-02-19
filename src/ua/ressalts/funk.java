package ua.ressalts;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Alx Shcherbak on 19.02.2015.
 */
public class funk {
    // try this
    ArrayList<Map<Character, Integer>> maps = new ArrayList<>();
    //***end***********

    //debug switcher
    private boolean debug = false;

    /**
     * @param letters      - асоціативний масив
     * @param task_word    - масив слів
     * @param task_symbols - масив символів
     */
    private ArrayList<letter> letters = new ArrayList<>();
    private String[] task_word;
    private String[] task_symbols;

    public ArrayList<letter> getLetters() {
        return letters;
    }

    public ArrayList<letter> DoIt(String input){
        letters.clear();
        //Розбір введеної строки на слова
        task_word = input.toString().split("[^0-9a-zA-Zа-яА-Я]+");
        //Розбір введеної строки на спецсимволи (+; -; = .. т.п.)
        task_symbols = input.toString().split("[0-9a-zA-Zа-яА-Я]+");

        //debug
        if (debug) {
            for (int i = 0; i < task_word.length; i++) {
                System.out.println("task_word" + i + " " + task_word[i]);
            }
            for (int i = 0; i < task_symbols.length; i++) {
                System.out.println("task_symbols" + i + " " + task_symbols[i]);
            }
        }


        letter letter_buff = new letter();
        for (int word = 0; word < task_word.length; word++) {
            for (int char_word = 0; char_word < task_word[word].length(); char_word++) {
                boolean exist = false;
                for (int i = 0; i < letters.size(); i++) {
                    if (letters.get(i).getLet() == task_word[word].charAt(char_word)) {
                        exist = true;
                    }
                }
                if (!exist) {
                    letters.add(new letter(task_word[word].charAt(char_word), 0));
                    if (char_word == 0) {
                        letters.get(letters.size() - 1).setNum(1);
                        letters.get(letters.size() - 1).setNot_zero(true);
                    }
                }
                if (exist & (char_word == 0)) {
                    letters.get(letters.size() - 1).setNum(0);
                }
            }
        }

        MyThread thread = new MyThread();
        thread.start();
            try {
                thread.join();
                return letters;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        return null;
    }

    /**
     * @return правильність виконання діїї
     */
    private boolean math_control() {
        int type;
        type = type(task_symbols[1]);

        Integer[] numbers = new Integer[task_word.length];
        for (int i = 0; i < task_word.length; i++) {
            numbers[i] = new Integer(0);
            for (int j = 0; j < task_word[i].length(); j++) {
                double v = num(task_word[i].charAt(j)) * Math.pow(10, (task_word[i].length() - 1 - j));
                numbers[i] = numbers[i] + (int) v;
            }
        }

        //debug
        if (debug) {
            for (int i = 0; i < numbers.length; i++) {
                System.out.print("numb" + i + " " + numbers[i] + "  ");
            }
        }

        if (type == 0) {
            for (int i = 0; i < numbers.length - 1; i++)
                numbers[numbers.length - 1] -= numbers[i];
            if (numbers[numbers.length - 1] == 0) return true;
        } else if (type == 1) {
            for (int i = numbers.length - 1; i > 0; i--)
                numbers[0] -= numbers[i];
            if (numbers[0] == 0) return true;
        } else if (type == 2) {
            double mult = 1;
            for (int i = 0; i < numbers.length - 1; i++)
                mult *= numbers[i];
            if (numbers[numbers.length - 1] == (int) mult) return true;
        } else if (type == 3) {
            double div = 1;
            for (int i = 1; i < numbers.length; i++)
                div *= numbers[i];
            if (numbers[0] == (int) div) return true;
        }
        ;
        return false;
    }

    /**
     * type = 0, symbol +
     * type = 1, symbol -
     * type = 2, symbol *
     * type = 3, symbol /
     * type = 4, symbol =
     * type = 5, action none - error
     *
     * @param symbol - знак математичної дії
     */
    private int type(String symbol) {
        int type;
        if (symbol.equals("+")) type = 0;
        else if (symbol.equals("-")) type = 1;
        else if (symbol.equals("*")) type = 2;
        else if (symbol.equals("/")) type = 3;
        else if (symbol.equals("=")) type = 4;
        else type = 5;
        return type;
    }

    /**
     * @param let - буква для асоціації
     * @return число яке асоціюється з буквою
     */
    private Integer num(char let) {
        for (int i = 0; i < letters.size(); i++) {
            if (letters.get(i).getLet() == let)
                return letters.get(i).getNum();
        }
        return null;
    }

    /**
     * генератор перебору значень асоціативних літер
     */
    private void generator() {
        for (int i = letters.size() - 1; i > 0; i--) {
            if (letters.get(i).getNum() > 9) {
                letters.get(i - 1).numIncrement();
                if (!letters.get(i).isNot_zero())
                    letters.get(i).setNum(0);
                else letters.get(i).setNum(1);
            }
        }
        if (math_control()) {
            boolean repeater = false;
            for (int i = 0; i < letters.size(); i++) {
                if (!repeater)
                    for (int j = 0; j < letters.size(); j++) {
                        if (j != i) if (letters.get(i).getNum() == letters.get(j).getNum()) {
                            repeater = true;
                            break;
                        }
                    }
            }
            if (!repeater) {
                for (int i = 0; i < letters.size(); i++) {
                    letters.get(i).addResnum(letters.get(i).getNum());
                }
            }
        }
        letters.get(letters.size() - 1).numIncrement();

        //debug
        if (debug) {
            for (int i = 0; i < letters.size(); i++) {
                System.out.println("letters" + i + " " + letters.get(i).getLet() + " = " + letters.get(i).getNum() + "  ");
            }
        }
    }

    class MyThread extends Thread {
        public MyThread() {
        }

        public void run() {
            while (letters.get(0).getNum() < 10) {
                //debug
                if (debug) {
                    if ((letters.get(0).getNum() == 9) & (letters.get(1).getNum() == 8) & (letters.get(2).getNum() == 0) & (letters.get(3).getNum() == 10)) {
                        System.out.println("lol");
                    }
                }
                generator();
            }
        }
    }
}

