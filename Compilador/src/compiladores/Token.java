package compiladores;

public class Token {
    private static int idToken;
    private static String lexema; // con este string puedo acceder a la TS

    public Token() {
        idToken = -1;
        lexema = "";
    }

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

    public String toString() {
        String retorno = "";
        int n = getIdToken();
        switch (n) {
            case 257:
                retorno = "Identificador " + getLexema();
                break;
            case 258:
                retorno = "Constante " + getLexema();
                break;
            case 259:
                retorno = "Constante positiva " + getLexema();
                break;
            case 260:
                retorno = "Palabra reservada IF";
                break;
            case 261:
                retorno = "Palabra reservada END_IF";
                break;
            case 262:
                retorno = "Palabra reservada ELSE";
                break;
            case 263:
                retorno = "Palabra reservada PRINT";
                break;
            case 264:
                retorno = "Palabra reservada CLASS";
                break;
            case 265:
                retorno = "Palabra reservada VOID";
                break;
            case 266:
                retorno = "Palabra reservada LONG";
                break;
            case 267:
                retorno = "Palabra reservada USHORT";
                break;
            case 268:
                retorno = "Palabra reservada DOUBLE";
                break;
            case 269:
                retorno = "Palabra reservada DO";
                break;
            case 270:
                retorno = "Palabra reservada UNTIL";
                break;
            case 271:
                retorno = "Palabra reservada IMPL";
                break;
            case 272:
                retorno = "Palabra reservada FOR";
                break;
            case 273:
                retorno = "Cadena de caracteres: " + getLexema();
                break;
            case 274:
                retorno = "Palabra reservada RETURN";
                break;
            case 275:
                retorno = ">=";
                break;
            case 276:
                retorno = "<=";
                break;
            case 277:
                retorno = "==";
                break;
            case 278:
                retorno = "+=";
                break;
            case 279:
                retorno = "!!";
                break;
            default:
                retorno = Character.toString((char) n);
        }
        return retorno;
    }
}
