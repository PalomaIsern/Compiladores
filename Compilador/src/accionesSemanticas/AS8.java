package accionesSemanticas;

import java.io.IOException;

import compiladores.Token;
import compiladores.Lexico;

public class AS8 extends AccionSemantica {
    @Override
    public Token ejecutarAS(char c) throws IOException {
        // esta accion descarta los caracteres leidos ya que pertenecen a comentarios
        // no devuelve ningun token
        if (c == '*')
            System.out.println("Se reconoció un comentario en la linea " + Lexico.getLinea());
        return null;
    }
}