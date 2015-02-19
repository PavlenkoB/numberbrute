package ua.ressalts;

import java.util.ArrayList;

/**
 * Created by Alx Shcherbak on 19.02.2015.
 */
public class tessc {
    public static void main(String[] args) {
        funk funk = new funk();

        long timer=System.currentTimeMillis();
        ArrayList<letter> res = funk.DoIt("ВЕТКА+ВЕТКА=ДЕРЕВО");
        System.out.println("time="+(System.currentTimeMillis()-timer));
        if (res.size() > 0) {
            for (int ii = 0; ii < res.get(0).getResnum().size(); ii++) {
                for (int i = 0; i < res.size(); i++) {
                    System.out.print(res.get(i).getLet()+"="+res.get(i).getResnum().get(ii) + "|");
                }
                System.out.println();
            }
        }
    }
}
