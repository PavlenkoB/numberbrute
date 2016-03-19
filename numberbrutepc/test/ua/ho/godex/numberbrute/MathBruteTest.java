package ua.ho.godex.numberbrute;

import org.junit.Test;
import ua.ho.godex.mblogic.classes.MathBrute;

import java.util.LinkedList;

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
        LinkedList<MathBrute> mathBruteList = new LinkedList<>();
        int testInerator = 1;
        mathBruteList.add(new MathBrute("A+FAT=ASS", MathBrute.Type.CHAR, false));
        mathBruteList.add(new MathBrute("AB+BC+CA=ABC", MathBrute.Type.CHAR, false));
        mathBruteList.add(new MathBrute("ABCDE*4=EDCBA", MathBrute.Type.CHAR, false));
        mathBruteList.add(new MathBrute("AS+A=MOM", MathBrute.Type.CHAR, false));
        mathBruteList.add(new MathBrute("один+один=много", MathBrute.Type.CHAR, false));
        mathBruteList.add(new MathBrute("Я*С=СЕМЬЯ", MathBrute.Type.CHAR, false));
        mathBruteList.add(new MathBrute("книга+книга+книга=наука", MathBrute.Type.CHAR, false));
        mathBruteList.add(new MathBrute("ВАГОН+ВАГОН=СОСТАВ", MathBrute.Type.CHAR, false));
        mathBruteList.add(new MathBrute("НИТКА+НИТКА=ТКАНЬ", MathBrute.Type.CHAR, false));
        mathBruteList.add(new MathBrute("БИТ+БАЙТ=СЛОВО", MathBrute.Type.CHAR, false));
        for (MathBrute mathBrute : mathBruteList) {
            System.out.println("*****************");
            for (int iter = 0; iter < testInerator; iter++) {
                timer = System.currentTimeMillis();
                mathBrute.reset();
                mathBrute.mathResultStr();
                timer = System.currentTimeMillis() - timer;
                sum += timer;
                //System.out.println(timer);
            }
            if(mathBrute.ressultArray.size()>0) {
                for (String string : mathBrute.ressultArray) {
                    System.out.println(string);
                }
            }
            //System.out.println("sum=" + sum);
            System.out.println("mid=" + sum / testInerator);
        }
    }
}