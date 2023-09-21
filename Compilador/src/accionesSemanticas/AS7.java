package accionesSemanticas;

import java.io.IOException;

import compiladores.Token;

public class AS7 extends AccionSemantica {
    @Override
    public Token ejecutarAS(char c) throws IOException {
        // devuelve el ultimo caracter leido y
        // reconoce '+', '<', '>', '=','*', '.'
        // DEVOLVER EL ULTIMO CARACTER
        char c1 = lexema.charAt(0);
        return new Token(getId(c1));
    }
}
/*
 * Para esta AS, falta determinar cómo se genera el Token,
 * para saber como buscarlo en la tabla de tokens
 * 
 * 
 */