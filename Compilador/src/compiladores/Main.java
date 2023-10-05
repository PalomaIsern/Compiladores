package compiladores;

public class Main {
    public static int numero_linea = 1;

    public static void main(String[] args) throws Exception {
        Lexico lex = new Lexico();
        while (!Lexico.finArchivo()) {
            lex.getToken();
        }
        lex.cerrarArchivo();
    }

    public static int getLinea() {
        return numero_linea;
    }

    public static void setLinea() {
        numero_linea = numero_linea + 1;
    }
}
