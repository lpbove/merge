package pdf1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ComparadorText {
    static final String LS = System.getProperty("file.separator");
    private int page_width;

    public ComparadorText(String path1, String path2) {
        String name1 = path1.isEmpty() ? path1.lastIndexOf(LS) == -1 ? ": la ruta es buida" : path1.substring(path1.lastIndexOf(LS)) : path1;
        String name2 = path1.isEmpty() ? path2.lastIndexOf(LS) == -1 ? ": la ruta es buida" : path2.substring(path2.lastIndexOf(LS)) : path2;

        BufferedReader file1 = leerArchivo(path1, name1);
        BufferedReader file2 = leerArchivo(path2, name2);
        try{
            comparar(file1,file2,name1, name2, path1, path2);
        }catch(IOException e){
            System.err.println("Fallo alhora de processar els arxius");
        }
    }

    private BufferedReader leerArchivo(String path, String name){
        try{
            return new BufferedReader(new FileReader(path));
        }catch(IOException e) {
            if (!new File(path).exists()) {
                System.err.println("No s'ha pogut llegir l'arxiu " + name);
            }
            return null;
        }
    }

    private void comparar(BufferedReader file1, BufferedReader file2, String name1, String name2, String path1, String path2) throws IOException {
        String line1 = file1.readLine();
        String line2 = file2.readLine();
        int numLine = 0;
        boolean identic = true;

        while(line1 != null && identic){
            if(!line1.equals(line2)){
                identic = false;
                System.out.println((char)27 + "[33;1mDiferencia linea: ("+numLine+")");

                System.out.println((char)27 + "[33;1mArxiu: "+name1);
                if(line1 == null){
                    System.out.println((char)27 + "[31;1mAquesta linea es buida!");
                }else{
                    for(int i=0; i<line1.length(); i++){
                       char c = line1.charAt(i);
                       if(line2 == null){
                           System.out.print((char)27 + "[31;1m"+c);
                       }
                       else if(i > line2.length()-1){
                           System.out.print((char)27 + "[31;1m"+c);
                       }
                       else if(c != line2.charAt(i)){
                           System.out.print((char)27 + "[31;1m"+c);
                        }else{
                           System.out.print((char)27 + "[30m"+c);
                       }
                    }
                }
                System.out.println();
                System.out.println((char)27 + "[33;1mArxiu: "+name2);
                if(line2 == null){
                    System.out.println((char)27 + "[31;1mAquesta linea es buida!");
                }else{
                    for(int i=0; i<line2.length(); i++){
                        char c = line2.charAt(i);
                        if(i >= line2.length()){
                            System.out.print((char)27 + "[31;1m"+c);
                        }
                        else if(c != line1.charAt(i)){
                            System.out.print((char)27 + "[31;1m"+c);
                        }else{
                            System.out.print((char)27 + "[30m"+c);
                        }
                    }
                }
            }else{
                line1 = file1.readLine();
                line2 = file2.readLine();
            }
            numLine++;
        }
        if(identic){
            System.out.println((char)27 + "[32;1mEls dos arxius son identics");
        }else{
            Scanner sc = new Scanner(System.in);
            System.out.println();
            System.out.println((char)27 +"[34;1mEscolleix una opcio:");
            System.out.println((char)27 +"[34m1 - sortir");
            System.out.println((char)27 +"[34m2 - merge");
            String opcio = sc.next();
            while(!opcio.equals("1") && !opcio.equals("2")){
                System.out.println((char)27 +"[34;1mOpcio incorrecta!");
                System.out.println((char)27 +"[34m1 - sortir");
                System.out.println((char)27 +"[34m2 - merge");
                opcio = sc.next();
            }
            if(opcio.equals("2")){
                try{
                    merge(path1, path2);
                }catch(IOException e){
                    System.err.println("Fallo alhora de processar els arxius per al merge");
                }finally{
                   file1.close();
                   file2.close();
                }
            }
        }
    }

    private int findLongestLine(String path1, String path2) throws IOException {
        BufferedReader file1 = leerArchivo(path1, null);
        BufferedReader file2 = leerArchivo(path2, null);
        String line = file1.readLine();
        String line2 = file2.readLine();

        int longestLine = line.length() > line2.length()? line.length() : line2.length();

        while(line != null || line2 != null){
            if(line != null){
                if(line.length() > longestLine){
                    longestLine = line.length();
                }
            }
            if(line2 != null){
                if(line2.length() > longestLine){
                    longestLine = line2.length();
                }
            }
            line = file1.readLine();
            line2 = file2.readLine();
        }
        return longestLine;
    }

    public void merge(String path1, String path2) throws IOException {
        BufferedReader file1= leerArchivo(path1, null);
        BufferedReader file2= leerArchivo(path2, null);

        String line1 = file1.readLine();
        String line2 = file2.readLine();

        page_width = findLongestLine(path1,path2);

        while(line1 != null || line2 != null){
            if(line1 != null){
                System.out.print(String.format("%-" + page_width + "s", (char)27 +"[30;43;1m"+line1));
                line1 = file1.readLine();
            }

            System.out.print(String.format("%-" + page_width + "s", (char)27 +"[0m"));

            if(line2 != null){
                System.out.print((char)27 +"[30;42;1m"+line2);
                line2 = file2.readLine();
            }
            System.out.println();
        }
    }
}
