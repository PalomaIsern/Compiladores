package accionesSemanticas;

import java.io.IOException;

import compiladores.Token;

public class AS8 extends AccionSemantica {
    @Override
    public Token ejecutarAS(char c) throws IOException {
        // esta accion descarta los caracteres leidos ya que pertenecen a comentarios o
        // son blancos/saltos de linea
        // no devuelve ningun token
        // if (c == '*')
        // System.out.println("Linea " + (Linea.getLinea() - 1) + ": Se reconocio un
        // comentario");
        // else if (c != '\n' && c != '\t' && c != ' ')
        // System.out.println("Caracter inválido");
        return null;
    }
}