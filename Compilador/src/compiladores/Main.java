package compiladores;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) throws Exception {
    Scanner sc = new Scanner(System.in);
    System.out.println("Ingrese el nombre del archivo que desea leer");
    String archivo = sc.nextLine();
    
    sc.close();
    }
}