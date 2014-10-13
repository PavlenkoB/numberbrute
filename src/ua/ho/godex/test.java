package ua.ho.godex;

/**
 * Created by
 * User: godex_000
 * Date: 11.10.2014.
 * Time: 0:53
 */
public class test {
    public static void main(String[] args) {
        long timer=System.currentTimeMillis();
        parsedString res = new parsedString("цветок*в=букетик", parsedString.Type.CHAR,false);
        res.mathResultstr();
        System.out.println("time="+(System.currentTimeMillis()-timer));
        for (String string:res.ressultarray){
            System.out.println(string);
        }
    }
}
