package accionesSemanticas;

import java.io.IOException;

import compiladores.Lexico;
import compiladores.TablaToken;
import compiladores.Token;

public class AS7 extends AccionSemantica {
    @Override

    // devuelve el ultimo caracter leido y reconoce '+', '<', '>', '=','*', '.'

    public Token ejecutarAS(char c) throws IOException {
        Lexico.VolverAtras();
        // Lexico.setVolverALeer(true); // devolver lo ultimo leido
        char c1 = lexema.charAt(0);
        if (c1 == '=')
            return new Token(TablaToken.getId("="));
        else
            return new Token(TablaToken.getId(Character.toString(c1)));
    }
}
