package ua.ho.godex;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by tf101 on 05.10.14.
 */
public class parsedString {
    class Spec {
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
    public ArrayList<Character> dii = new ArrayList<>();       // array of do
    public ArrayList<Spec> simbols = new ArrayList<>();
    public ArrayList<String> ressultarray = new ArrayList<>();
    public Double intResult;
    public String strResult = new String();//result string
    public String inputString;
    String intString;
    public Type type;

    public boolean debug = false;
    public boolean debugMath = false;

    public static enum Type {CHAR, NUMBER}

    ;


    public parsedString() {
    }

    private void charinc(Integer index) {
        simbols.get(index).setValue(simbols.get(index).getValue() + 1);
        if (simbols.get(index).getValue() > 9 && (index < simbols.size() - 1)) {
            if (simbols.get(index).isNotZero())
                simbols.get(index).setValue(1);
            else
                simbols.get(index).setValue(0);
            charinc(index + 1);
        }

    }

    public int fact(int num) {
        return (num == 0) ? 1 : num * fact(num - 1);
    }

    public void mathResultstr() {
        int progress = 0;
        int full = (int) Math.pow(10, simbols.size());
        while (simbols.get(simbols.size() - 1).getValue() < 10) {
            //progress++;
            //System.out.print("\r"+progress+'/'+full);
            charinc(0);
            copyStrToInt();
            boolean colision = false;
            for (Spec spec : simbols) {
                for (Spec spec1 : simbols) {
                    if (spec1.getValue() == spec.getValue() && spec1.getCharacter() != spec.getCharacter()) {
                        colision = true;
                        break;
                    }
                    if (colision)
                        break;
                }
            }
            if (colision)
                continue;
            if (this.mathResult().intValue() == intResult && colision == false) {
                copyStrToInt();
                System.out.println(inputString + "->" + intString);
                ressultarray.add(inputString + "->" + intString);
                System.out.print("----------------------\n");/**/
            }/**/
        }
        //System.exit(1);
    }

    private void copyStrToInt() {//copy array of char numbers to integer with replace
        String tmpstr = new String(this.inputString);
        for (int sim = 0; sim < simbols.size(); sim++) {
            tmpstr = tmpstr.replace(simbols.get(sim).getCharacter(), simbols.get(sim).getValue().toString().charAt(0));
        }
        intString = tmpstr;
        sliseintstring(tmpstr);
    }


    public Double mathResult() {
        ArrayList<Double> numberstmp = numbers;
        int pos = 0;
        boolean spec = false;
        Double tmpdouble = null;
        for (int diya = 0; diya < dii.size(); diya++) {
            if (dii.get(diya) == '*') {
                if (spec == false) {
                    pos = diya;
                    tmpdouble = numberstmp.get(diya);
                }
                spec = true;
                if (debugMath)
                    System.out.println(diya + " *>" + tmpdouble + "*" + numberstmp.get(diya + 1) + "=" + (tmpdouble * numberstmp.get(diya + 1)));
                tmpdouble = tmpdouble * numberstmp.get(diya + 1);
            }
            if (dii.get(diya) == '/') {
                if (spec == false) {
                    pos = diya;
                    tmpdouble = numberstmp.get(diya);
                }
                spec = true;
                if (debugMath)
                    System.out.println(diya + " />" + tmpdouble + "/" + numberstmp.get(diya + 1) + "=" + (tmpdouble / numberstmp.get(diya + 1)));
                tmpdouble = tmpdouble / numberstmp.get(diya + 1);
            }
            if ((dii.get(diya) == '+' || dii.get(diya) == '-') && spec == true) {
                numberstmp.set(pos, tmpdouble);
                spec = false;
            }
            if (diya + 1 == dii.size() && spec == true) {
                numberstmp.set(pos, tmpdouble);
                spec = false;
            }
        }
        Double ret = new Double(numberstmp.get(0));
        for (int diya = 0; diya < dii.size(); diya++) {
            if (dii.get(diya) == '+') {
                if (debugMath)
                    System.out.println(diya + " +>" + (ret + numberstmp.get(diya + 1)) + "=" + ret + "+" + numberstmp.get(diya + 1));
                ret += numberstmp.get(diya + 1);
            }
            if (dii.get(diya) == '-') {
                if (debugMath)
                    System.out.println(diya + " ->" + (ret - numberstmp.get(diya + 1)) + "=" + ret + "-" + numberstmp.get(diya + 1));
                ret -= numberstmp.get(diya + 1);
            }
        }
        return ret;
    }

    private void sliseintstring(String string){
        numbers.clear();
        for(String one:string.split("[^0-9a-zA-Zа-яА-Я]+")) {
            numbers.add(Double.parseDouble(one));
        }
        intResult=numbers.get(numbers.size()-1);
        numbers.remove(numbers.size()-1);
    }

    public parsedString(String string, Type type) {
        string=string.toLowerCase();
        this.inputString=string;
        this.type = type;
        if (type == Type.NUMBER) {
            sliseintstring(string);
        }
        if (type == Type.CHAR) {
            for(String one:string.split("[^0-9a-zA-Zа-яА-Я]+")) {
                numbersStr.add(one);
            }
            for(String one:string.toString().split("[0-9a-zA-Zа-яА-Я]+")) {
                if(one.length()>0)
                dii.add(one.charAt(0));
            }
            //dii.remove(dii.indexOf(string.charAt(0)));
            //sobraty simvolu
             ArrayList<Character> allchar = new ArrayList<>();       // array of do
            for (int che = 0; che < numbersStr.size(); che++) {
                for (int pos = 0; pos < numbersStr.get(che).length(); pos++) {
                    if (!allchar.contains(numbersStr.get(che).charAt(pos))) {
                        allchar.add(numbersStr.get(che).charAt(pos));
                        if(pos==0) {
                            simbols.add(new Spec(numbersStr.get(che).charAt(pos), 1,true));
                        }else{
                            simbols.add(new Spec(numbersStr.get(che).charAt(pos), 0,false));
                        }
                    }else{
                        if(allchar.contains(numbersStr.get(che).charAt(pos))&&pos==0){
                            for(Spec spec: simbols){
                                if(spec.getCharacter().equals(numbersStr.get(che).charAt(pos))) {
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
}
