package ua.ho.godex;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by tf101 on 05.10.14.
 */
public class parsedString {
    public ArrayList<Double> numbers = new ArrayList<>(); // array of numbers
    public ArrayList<String> numbersStr = new ArrayList<>(); // array of symbol numbers
    public ArrayList<Character> dii = new ArrayList<>();       // array of do
    public TreeMap<Character,Integer> simbols= new TreeMap<>();
    public Integer intResult = new Integer(0);
    public String strResult = new String();
    public ArrayList<Character> charResult = new ArrayList<>(); //result string
    public boolean debug = true;

    public static enum Type {CHAR, NUMBER};


    public parsedString() {
    }

    public Double mathResult() {
        int pos = 0;
        boolean spec = false;
        Double tmpdouble = null;
        for (int diya = 0; diya < dii.size(); diya++) {
            if (dii.get(diya) == '*') {
                if (spec == false) {
                    pos = diya;
                    tmpdouble = numbers.get(diya);
                }
                spec = true;
                if (debug)
                    System.out.println(diya + " *>"  + tmpdouble + "*" + numbers.get(diya + 1)+"="+(tmpdouble * numbers.get(diya + 1)));
                tmpdouble=tmpdouble*numbers.get(diya + 1);
            }
            if (dii.get(diya) == '/') {
                if (spec == false) {
                    pos = diya;
                    tmpdouble = numbers.get(diya);
                }
                spec = true;
                if (debug)
                    System.out.println(diya + " />"  + tmpdouble + "/" + numbers.get(diya + 1)+"="+(tmpdouble / numbers.get(diya + 1)));
                tmpdouble =tmpdouble/ numbers.get(diya + 1);
            }
            if (dii.get(diya) == '+' || dii.get(diya) == '-') {
                numbers.set(pos, tmpdouble);
                spec=false;
            }
            if(diya+1 == dii.size()) {
                numbers.set(pos, tmpdouble);
                spec=false;
            }
        }
        Double ret = new Double(numbers.get(0));
        for (int diya = 0; diya < dii.size(); diya++) {
            if (dii.get(diya) == '+') {
                if (debug)
                    System.out.println(diya + " +>" + (ret + numbers.get(diya + 1)) + "=" + ret + "+" + numbers.get(diya + 1));
                ret += numbers.get(diya + 1);
            }
            if (dii.get(diya) == '-') {
                if (debug)
                    System.out.println(diya + " ->" + (ret - numbers.get(diya + 1)) + "=" + ret + "-" + numbers.get(diya + 1));
                ret -= numbers.get(diya + 1);
            }
        }
        return ret;
    };

    public parsedString(String string, Type type) {

        if (type == Type.NUMBER) {
            StringBuilder tmpstr = new StringBuilder("");
            for (int pos = 0; pos < string.length(); pos++) {
                if (string.charAt(pos) == '+' || string.charAt(pos) == '-' || string.charAt(pos) == '=' || string.charAt(pos) == '*' || string.charAt(pos) == '/') {
                    if (tmpstr.length() > 0) {
                        numbers.add(Double.parseDouble(tmpstr.toString()));
                        tmpstr = new StringBuilder("");
                    }
                    dii.add(string.charAt(pos));
                } else {
                    tmpstr.append(string.charAt(pos));// add to end of string
                }
            }
            if (tmpstr.length() > 0)
                numbers.add(Double.parseDouble(tmpstr.toString()));
//            intResult=Integer.parseInt(tmpstr.toString());
        }
        if (type == Type.CHAR) {
            StringBuilder tmpstr = new StringBuilder("");//number string
            for (int pos = 0; pos < string.length(); pos++) {
                if (string.charAt(pos) == '+' || string.charAt(pos) == '-' || string.charAt(pos) == '=' || string.charAt(pos) == '*' || string.charAt(pos) == '/') {
                    if (tmpstr.length() > 0) {
                        numbersStr.add(tmpstr.toString());
                        //numbers.add(Double.parseDouble(tmpstr.toString()));
                        tmpstr = new StringBuilder("");
                    }
                    dii.add(string.charAt(pos));
                } else {
                    tmpstr.append(string.charAt(pos));// add to end of string
                }
            }
            if(tmpstr.length()>0)
                strResult=tmpstr.toString();
                numbersStr.add(tmpstr.toString());
            //sobraty simvolu
            for(int che=0;che<numbersStr.size();che++){
                for(int pos=0;pos<numbersStr.get(che).length();pos++){
                    if(!simbols.containsKey(numbersStr.get(che).charAt(pos))){
                        simbols.put(numbersStr.get(che).charAt(pos), 0);
                    }
                }
            }
            //delete result from numbers
            numbersStr.remove(numbersStr.size()-1);
        }
    }
}
