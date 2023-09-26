package accionesSemanticas;

import java.io.IOException;

import compiladores.Token;

public class AS8 extends AccionSemantica {
    @Override
    public Token ejecutarAS(char c) throws IOException {
        if (c == '*')
            System.out.println("Se reconoció un comentario en la linea ");
        return null;
    }
}