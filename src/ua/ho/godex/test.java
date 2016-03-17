package ua.ho.godex;

import ua.ho.godex.numberbrute.MathBrute;

/**
 * Created by
 * User: godex_000
 * Date: 11.10.2014.
 * Time: 0:53
 */
public class test {
    public static void main(String[] args) {
        MathBrute res = new MathBrute("ВЕТКА+ВЕТКА=ДЕРЕВО", MathBrute.Type.CHAR,false);
        long timer=System.currentTimeMillis();
        res.mathResultStr();
        System.out.println("time="+(System.currentTimeMillis()-timer));
        for (String string:res.ressultArray){
            System.out.println(string);
        }
    }
}
