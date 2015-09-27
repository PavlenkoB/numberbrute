package ua.ho.godex.numberbrute;

import org.junit.Test;

/**
 * Created by godex_000
 * on  24.04.2015.13:17
 * for numberbrute
 */
public class MathBruteTest {

    @Test
    public void testMathResultstr() throws Exception {
        long timer;
        long sum=0;
        MathBrute res;
        int coll=1;
        for(int iter=0;iter<1;iter++) {
            timer = System.currentTimeMillis();
            res = new MathBrute("книга+книга+книга=наука", MathBrute.Type.CHAR, false);
            res.mathResultstr();
            timer=System.currentTimeMillis()-timer;
            sum+=timer;
            System.out.println(timer);
            for (String string:res.ressultArray){
                System.out.println(string);
            }
        }
        System.out.println("mid="+sum / coll);
    }
}