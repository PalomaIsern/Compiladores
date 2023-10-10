import compiladores.Lexico;

public class Ejecutar {

    public static void main(String[] args) throws Exception {
        Lexico lexico = new Lexico();
        Parser p = new Parser(lexico);
        p.run();
    }

}
