package compiladores;

public class Main {
    public static int numero_linea = 1;

    public static void main(String[] args) throws Exception {
        Lexico lex = new Lexico();
        lex.Leer(numero_linea);
    }

    public static int getLinea() {
        return numero_linea;
    }

    public static void setLinea() {
        numero_linea = numero_linea + 1;
    }
}
