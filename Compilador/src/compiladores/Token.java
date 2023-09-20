package compiladores;

public class Token {
    private int idToken;
    private String lexema; // con este string puedo acceder a la TS

    public Token(int idToken, String valor) {
        this.idToken = idToken;
        this.lexema = valor;
    }

    public String getLexema(TablaSimbolos TS, TablaToken TT) {
        return lexema;
    }

    public void setIdToken(int idToken) {
        this.idToken = idToken;
    }

    public int getIdToken() {
        return idToken;
    }
}
