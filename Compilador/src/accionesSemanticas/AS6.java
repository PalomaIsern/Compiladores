package accionesSemanticas;

import java.io.IOException;

import compiladores.Token;

public class AS6 extends AccionSemantica {
    // reconocer tokens '+=', '<=', '>=', '!!' y cadenas de una linea

    @Override
    public Token ejecutarAS(char c) throws IOException {
        lexema.append(c);
        char c1 = lexema.charAt(0);
        /*
         * if (c1 == '+') {
         * return new Token(getToken("+="));
         * } else if (c1 == '<') {
         * return new Token(getToken("<="));
         * } else if (c1 == '>') {
         * return new Token(getToken(">="));
         * } else if (c1 == '=') {
         * return new Token(getToken("=="));
         * } else if (c1 == '!') {
         * return new Token(getToken("!!"));
         * } else if (c1 == '#') {
         * return new Token(getToken("cadena")); //como devuelvo la cadena?
         * } //la funcion getToken deberia ir a la tabla de tokens y devolver
         * // el numero asociado a la entrada pasada por parametro
         */
        return null;
    }

}
