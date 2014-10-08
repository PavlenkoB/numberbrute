package ua.ho.godex;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner console=new Scanner(System.in);
        Integer var=1;
        while(true) {
            System.out.print("1)standart\n2)super(under work)\n0)exit\n");
            var =2;
            //var = console.nextInt();

            //console.nextLine();
            if (var == 1) {
                while (true) {
                    console.reset();
                    System.out.println("Input vuraz");
                    //String vuraz=   "2+3*2";     //console.nextLine();
                    String vuraz = console.nextLine();
                    if (vuraz.equals("e"))
                        break;
                    parsedString res = new parsedString(vuraz, parsedString.Type.NUMBER);

                    System.out.println("math:" + vuraz + "=" + res.mathResult());
                }
            }
            if (var == 2) {
                System.out.println("\nInput vuraz a+b=c");
                String vuraz = "sin+sin+cos=tang";//console.nextLine();

                parsedString res = new parsedString(vuraz, parsedString.Type.CHAR);
                res.mathResultstr();
                System.out.println("math:" + vuraz + "=" + "???");
            }
            if (var == 0) {
                break;
            }
        }
	// write your code here
    }
}
