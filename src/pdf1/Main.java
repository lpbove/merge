package pdf1;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println((char)27 +"[34;1mIntrodueix el nom del primer arxiu a comparar");
        String path1 = sc.next();
        System.out.println((char)27 +"[34;1mIntrodueix el nom del segon arxiu a comparar");
        String path2 = sc.next();
        ComparadorText comparadorText = new ComparadorText(path1,path2);
    }
}
