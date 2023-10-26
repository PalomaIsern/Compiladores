package compiladores;

public class Simbolo {
    private int token;
    private String lexema;
    private String tipo;
    private String uso;

    public Simbolo(int tk, String lex) {
        token = tk;
        lexema = lex;
        tipo = "";
        uso = "";
    }

    public Simbolo(int tk, String lex, String t) {
        token = tk;
        lexema = lex;
        tipo = t;
        uso = "";
    }

    public void set_Tipo(String t) {
        tipo = t;
    }

    public void set_Uso(String u) {
        uso = u;
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

    public String imprimir() {
        String contenido = "Token: " + token + ", Lexema: " + lexema + ", Tipo: " + tipo + ", Uso: " + uso;
        return contenido;
    }
}
