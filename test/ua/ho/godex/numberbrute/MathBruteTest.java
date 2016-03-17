package ua.ho.godex.numberbrute;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by godex_000
 * on  24.04.2015.13:17
 * for numberbrute
 */
public class MathBruteTest {


    @Test
    public void testMathResultstr() throws Exception {
        long timer;
        long sum = 0;
        ArrayList<MathBrute> mathBruteList = new ArrayList<>();
        int coll = 5;
//        mathBruteList.add(new MathBrute("книга+книга+книга=наука", MathBrute.Type.CHAR, false));
        mathBruteList.add(new MathBrute("один+один=много", MathBrute.Type.CHAR, false));
        for (MathBrute mathBrute : mathBruteList) {
            for (int iter = 0; iter < coll; iter++) {
                timer = System.currentTimeMillis();
                mathBrute.reset();
                mathBrute.mathResultStr();
                timer = System.currentTimeMillis() - timer;
                sum += timer;
                //System.out.println(timer);
            }
            for (String string : mathBrute.ressultArray) {
                System.out.println(string);
            }
            System.out.println("sum=" + sum);
            System.out.println("mid=" + sum / coll);
        }
    }
}