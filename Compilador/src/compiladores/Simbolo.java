package compiladores;

public class Simbolo {
    private int token;
    private String lexema;
    private String tipo;
    private String uso;
    private int cantRep;
    private String parametro;
    private String ambito;
    private String usada;

    public Simbolo(int tk, String lex) {
        token = tk;
        lexema = lex;
        tipo = "";
        uso = "";
        cantRep = 1;
        parametro = "-";
        ambito = "-";
        usada = "-";
    }

    public Simbolo(int tk, String lex, String t) {
        token = tk;
        lexema = lex;
        tipo = t;
        uso = "";
        cantRep = 1;
        parametro = "-";
        ambito = "-";
        usada = "-";
    }

    public void set_Usada(String u) {
        usada = u;
    }

    public void set_Tipo(String t) {
        tipo = t;
    }

    public void set_Uso(String u) {
        uso = u;
    }

    public void set_Rep() {
        cantRep = cantRep + 1;
    }

    public void set_Parametro(String p) {
        parametro = p;
    }

    public void set_Ambito(String a) {
        ambito = a;
    }

    public void disminuirCant() {
        cantRep = cantRep - 1;
    }

    public String parametro() {
        return parametro;
    }

    public String get_Usada() {
        return usada;
    }

    public int get_Token() {
        return token;
    }

    public String get_Lex() {
        return lexema;
    }

    public String get_Tipo() {
        return tipo;
    }

    public String get_Uso() {
        return uso;
    }

    public int get_Rep() {
        return cantRep;
    }

    public String get_Ambito() {
        return ambito;
    }

    public String get_Parametro() {
        return parametro;
    }

    public String imprimir() {
        String contenido = "Token: " + token + ", Lexema: " + lexema + ", Tipo: " + tipo + ", Uso: " + uso
                + ", Repeticiones: " + cantRep + ", Parametro: " + parametro + ", Ambito: " + ambito + ", Usada: "
                + usada;
        return contenido;
    }
}
