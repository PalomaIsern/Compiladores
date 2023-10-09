import compiladores.Lexico;

public class Ejecutar {
    public static int numero_linea = 1;

    public static void main(String[] args) throws Exception {
        Lexico lexico = new Lexico();
        Parser p = new Parser(lexico);
        p.yyparse();
    }

    public static int getLinea() {
        return numero_linea;
    }

    public static void setLinea() {
        numero_linea = numero_linea + 1;
    }
}
