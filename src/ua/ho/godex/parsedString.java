package ua.ho.godex;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Created by tf101 on 05.10.14.
 */
public class parsedString {
    class Spec {
        private Character character;
        private Integer value;

        Spec(Character character, Integer value) {
            this.character = character;
            this.value = value;
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
    public ArrayList<Character> dii = new ArrayList<>();       // array of do
    public ArrayList<Spec> simbols = new ArrayList<>();
    public ArrayList<String> ressultarray =new ArrayList<>();
    public Integer intResult = new Integer(0);
    public String strResult = new String();//result string
    public String inputString;
    String intString;
    public Type type;

    public boolean debug = false;

    public static enum Type {CHAR, NUMBER}

    ;


    public parsedString() {
    }

    private void charinc(Integer index) {
        simbols.get(index).setValue(simbols.get(index).getValue()+1);
        if (simbols.get(index).getValue() > 9 && (index<simbols.size()-1)) {
            simbols.get(index).setValue(0);
            charinc(index+1);
        }
        for (int pos=0;pos<simbols.size();pos++){
            if (simbols.get(index).getValue()==simbols.get(pos).getValue() && index!=pos){
                charinc(index);
            }
        }
    }

    public void mathResultstr() {
        while (simbols.get(simbols.size()-1).getValue()<10){
            charinc(0);
            copyStrToInt();
            if(this.mathResult().intValue()==intResult) {
                /*simbols.forEach(new Consumer<Spec>() {
                    @Override
                    public void accept(Spec spec) {
                        System.out.print(spec.getCharacter() + "=" + spec.getValue() + "|");
                    }
                });*/

                System.out.print("\n");
                System.out.println(inputString + "->" + intString + "==" + this.mathResult());
                ressultarray.add(inputString + "->" + intString + "==" + this.mathResult());
                System.out.print("----------------------\n");/**/
            }/**/
        }
        //System.exit(1);
    }

    private void copyStrToInt(){//copy array of char numbers to integer with replace
        String tmpstr =new String(this.inputString);
                for(int sim=0;sim<simbols.size();sim++){
                    tmpstr=tmpstr.replace(simbols.get(sim).getCharacter(),simbols.get(sim).getValue().toString().charAt(0));
                }
        intString=tmpstr;
        sliseintstring(tmpstr);

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
                    System.out.println(diya + " *>" + tmpdouble + "*" + numbers.get(diya + 1) + "=" + (tmpdouble * numbers.get(diya + 1)));
                tmpdouble = tmpdouble * numbers.get(diya + 1);
            }
            if (dii.get(diya) == '/') {
                if (spec == false) {
                    pos = diya;
                    tmpdouble = numbers.get(diya);
                }
                spec = true;
                if (debug)
                    System.out.println(diya + " />" + tmpdouble + "/" + numbers.get(diya + 1) + "=" + (tmpdouble / numbers.get(diya + 1)));
                tmpdouble = tmpdouble / numbers.get(diya + 1);
            }
            if ((dii.get(diya) == '+' || dii.get(diya) == '-') && spec == true) {
                numbers.set(pos, tmpdouble);
                spec = false;
            }
            if (diya + 1 == dii.size() && spec == true) {
                numbers.set(pos, tmpdouble);
                spec = false;
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
    }

    private void sliseintstring(String string){
        StringBuilder tmpstr = new StringBuilder("");
        numbers.clear();
        dii.clear();
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
            //numbers.add(Double.parseDouble(tmpstr.toString()));
            intResult=Integer.parseInt(tmpstr.toString());
    }

    public parsedString(String string, Type type) {
        this.inputString=string;
        this.type = type;
        if (type == Type.NUMBER) {
            sliseintstring(string);
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
            if (tmpstr.length() > 0)
                strResult = tmpstr.toString();
            numbersStr.add(tmpstr.toString());
            //sobraty simvolu
             ArrayList<Character> allchar = new ArrayList<>();       // array of do
            for (int che = 0; che < numbersStr.size(); che++) {
                for (int pos = 0; pos < numbersStr.get(che).length(); pos++) {
                    if (!allchar.contains(numbersStr.get(che).charAt(pos))) {
                        allchar.add(numbersStr.get(che).charAt(pos));
                        if(pos==0) {
                            simbols.add(new Spec(numbersStr.get(che).charAt(pos), 1));
                        }else{
                            simbols.add(new Spec(numbersStr.get(che).charAt(pos), 0));
                        }
                    }
                    //todo problems withe zeros
                }
            }

            //delete result from numbers
            numbersStr.remove(numbersStr.size() - 1);
        }
    }
}
