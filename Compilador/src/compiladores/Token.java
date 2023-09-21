package compiladores;

public class Token {
    private static int idToken;
    private static String lexema; // con este string puedo acceder a la TS

    public Token(int id) {
        idToken = id;
        lexema = "";
    }

    public Token(int id, String valor) {
        idToken = id;
        lexema = valor;
    }

    public static String getLexema() {
        return lexema;
    }

    public static void setIdToken(int id) {
        idToken = id;
    }

    public static int getIdToken() {
        return idToken;
    }
}
