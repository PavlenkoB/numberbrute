package ua.ho.godex;


import ua.ho.godex.mblogic.classes.MathBrute;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner console=new Scanner(System.in);
        Integer var=0;
        while(true) {
            System.out.println("Created by G0DE][\nbrute logic exicaysis");
            System.out.print("1)standart\n0)exit\n");
            //var = console.nextInt();
            var =1;

           //console.nextLine();
            if (var == -1) {//hidden don't work
                    System.out.println("Input vuraz (something like this '2+4+6+7')");
                    //String vuraz=   "2+3*2";     //console.nextLine();
                    String vuraz = console.nextLine();
                    if (vuraz.equals("e"))
                        break;
                    MathBrute res = new MathBrute(vuraz, MathBrute.Type.NUMBER,true);

                    System.out.println("math:" + vuraz + "=" + res.mathResult());

            }
            if (var == 1) {
                System.out.println("\nInput vuraz (something like this 'a+b=c" +
                        "книга+книга+книга=наука" +
                        "two*two=three')");
                String vuraz = "книга+книга+книга=наука";
                //vuraz=console.nextLine();
                long timer=System.currentTimeMillis();
                MathBrute res = new MathBrute(vuraz, MathBrute.Type.CHAR,true);
                res.mathResultStr();
                System.out.println("time="+(System.currentTimeMillis()-timer));
            }
            if (var == 0) {
                break;
            }
        }
	// write your code here
    }
}
