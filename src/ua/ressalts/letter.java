package ua.ressalts;

import java.util.ArrayList;

/**
 * Created by Alx Shcherbak on 10.10.2014.
 */

/**
 * Клас описуючий зв'язок букви з числом
 * let - буква
 * num - число від 0 до 9
 * resnum - масив правильних (результуючих) значень num.
 */
public class letter {
    private char let;
    private Integer num;
    private ArrayList<Integer> resnum;
    private Integer result;
    private boolean not_zero;


    public void setResnum(ArrayList<Integer> resnum) {
        this.resnum = resnum;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public letter() {
        this.resnum = new ArrayList<Integer>();
        this.num = new Integer(0);
        this.let = ' ';
        this.not_zero = false;

    }

    /**
     * @param let - буква
     * @param num - число від 0 до 9
     */
    public letter(char let, Integer num) {
        this.resnum = new ArrayList<Integer>();
        this.let = let;
        this.num = num;
    }

    public letter(Integer num) {
        this.resnum = new ArrayList<Integer>();
        this.num = num;
    }

    public void numIncrement () {
        this.num++;
    }

    public char getLet() {
        return let;
    }

    public void setLet(char let) {
        this.let = let;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public ArrayList<Integer> getResnum() {
        return resnum;
    }

    public void addResnum(Integer resnum) {
        this.resnum.add(resnum);
    }

    public boolean isNot_zero() {
        return not_zero;
    }

    public void setNot_zero(boolean not_zero) {
        this.not_zero = not_zero;
    }
}

