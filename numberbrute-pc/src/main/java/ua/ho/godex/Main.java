package ua.ho.godex;


import ua.ho.godex.mblogic.classes.MathBrute;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        Integer userInput = 0;
        while (true) {
            System.out.println("Created by G0DE][\nbrute logic exicaysis");
            System.out.print("1)standard\n 0)exit\n");
            userInput = console.nextInt();
            console.nextLine();
            if (userInput == 1) {
                System.out.println("\nInput expression (something like this 'a+b=c\n" +
                        "книга+книга+книга=наука\n" +
                        "two*two=three')");
                String expression;
                expression = console.nextLine();
                long timer = System.currentTimeMillis();
                MathBrute res = new MathBrute(expression);

                System.out.println(expression + " -> " + res.getResultOfCalculation());
                System.out.println("time=" + (System.currentTimeMillis() - timer));
            }
            if (userInput == 0) {
                break;
            }
        }
    }
}
