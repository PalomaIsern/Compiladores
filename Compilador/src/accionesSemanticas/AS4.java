package accionesSemanticas;

import java.io.IOException;

import compiladores.Token;

public class AS4 extends AccionSemantica {
    // devolver a la entrada el ultimo caracter leido, verificar si es PR, devolver
    // PR
    @Override
    public Token ejecutarAS(char c) throws IOException {
        // devolver lo ultimo leido
        String pr = lexema.toString();
        /*
         * if (pr existe en TPR){
         * return new Token
         * }
         * return new Token(getToken(from TT))
         */

        return null;
    }

}
