package accionesSemanticas;

import java.io.IOException;

import compiladores.Token;

public class AS9 extends AccionSemantica {
    // Verificar rango del entero y obtener el número
    @Override
    public Token ejecutarAS(char c) throws IOException {

        char last = lexema.charAt(lexema.length() - 1);
        int entero = Integer.parseInt(lexema.toString());
        if (last == 'l') { // entero largo

        } else if (last == 's') { // entero corto

        }
        // return new Token(getToken("CTE"), lexema);
        return null;
    }
}
