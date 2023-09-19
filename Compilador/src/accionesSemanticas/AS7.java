package accionesSemanticas;

import java.io.IOException;

import compiladores.Token;

public class AS7 extends AccionSemantica {
    @Override
    public Token ejecutarAS(char c) throws IOException {
        // devuelve el ultimo caracter leido
        // reconoce '+', '<', '>', '=','*', '.'
        // DEVOLVER EL ULTIMO CARACTER
        char c1 = lexema.charAt(0);
        /*
         * if (c1 == '+') {
         * return new Token(getToken('+'));
         * } else if (c1 == '<') {
         * return new Token(getToken('<'));
         * } else if (c1 == '>') {
         * return new Token(getToken('>'));
         * } else if (c1 == '=') {
         * return new Token(getToken('='));
         * } else if (c1 == '*') {
         * return new Token(getToken('*'));
         * } else if (c1 == '.') {
         * return new Token(getToken('.'));
         * }
         */
        return null;
    }
}
