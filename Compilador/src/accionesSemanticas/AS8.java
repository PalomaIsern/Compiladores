package accionesSemanticas;

import java.io.IOException;

import compiladores.Linea;
import compiladores.Token;

public class AS8 extends AccionSemantica {
    @Override
    public Token ejecutarAS(char c) throws IOException {
        // esta accion descarta los caracteres leidos ya que pertenecen a comentarios o
        // son blancos/saltos de linea
        // no devuelve ningun token
        if (c == '*')
            System.out.println("Se reconoció un comentario en la linea " + Linea.getLinea());
        return null;
    }
}