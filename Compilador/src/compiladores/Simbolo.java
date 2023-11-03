package compiladores;

public class Simbolo {
    private int token;
    private String lexema;
    private String tipo;
    private String uso;
    private int cantRep;

    public Simbolo(int tk, String lex) {
        token = tk;
        lexema = lex;
        tipo = "";
        uso = "";
        cantRep = 1;
    }

    public Simbolo(int tk, String lex, String t) {
        token = tk;
        lexema = lex;
        tipo = t;
        uso = "";
        cantRep = 1;
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

    public void disminuirCant() {
        cantRep = cantRep - 1;
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

    public String imprimir() {
        String contenido = "Token: " + token + ", Lexema: " + lexema + ", Tipo: " + tipo + ", Uso: " + uso
                + " Repeticiones: " + cantRep;
        return contenido;
    }
}
