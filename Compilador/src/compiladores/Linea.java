package compiladores;

public class Linea {
    public static int numero_linea = 1;

    public Linea() {
    }

    public static int getLinea() {
        return numero_linea;
    }

    public static void setLinea() {
        numero_linea = numero_linea + 1;
    }
}
