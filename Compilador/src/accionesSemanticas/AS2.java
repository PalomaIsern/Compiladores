package accionesSemanticas;

import java.io.IOException;

import compiladores.Token;

public class AS2 extends AccionSemantica {
    // agregar letra o digito o caracter al stringBuilder
    @Override
    public Token ejecutarAS(char c) throws IOException {
        lexema.append(c);
        return null; // no retorna nada porque aun no forme completamente el token
    }

}
