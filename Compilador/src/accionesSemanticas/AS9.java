package accionesSemanticas;

import java.io.IOException;

import compiladores.Lexico;
import compiladores.Main;
import compiladores.TablaSimbolos;
import compiladores.TablaToken;
import compiladores.Token;

public class AS9 extends AccionSemantica {
    // Verificar rango del entero y obtener el número
    @Override
    public Token ejecutarAS(char c) throws IOException {
        lexema.append(c);
        char last = lexema.charAt(lexema.length() - 1);
        int entero;
        Lexico.VolverAtras();
        if (last == 'l') { // entero largo _l
            entero = Integer.parseInt(lexema.substring(0, (lexema.length() - 2)));
            if (entero < Integer.MIN_VALUE) {
                System.out.println(
                        "WARNING - Linea " + Main.getLinea() + ": el entero es menor al valor mínimo permitido");
                entero = Integer.MIN_VALUE;
            }
            if (entero > Integer.MAX_VALUE) {
                System.out.println(
                        "WARNING - Linea " + Main.getLinea() + ": el entero es mayor al valor máximo permitido");
                entero = Integer.MAX_VALUE;
            }
            if (!TablaSimbolos.pertenece(Integer.toString(entero))) {
                TablaSimbolos.agregar(Integer.toString(entero), TablaToken.getId("LONG"));
            }
            return new Token(TablaToken.getId("LONG"), Integer.toString(entero));
        } else if (last == 's') { // entero corto _us
            entero = Integer.parseInt(lexema.substring(0, (lexema.length() - 3)));
            if (entero > 255) {
                System.out.println(
                        "WARNING - Linea " + Main.getLinea() + ": el entero se encuentra fuera del rango permitido");
                entero = 255;
            }
            if (!TablaSimbolos.pertenece(Integer.toString(entero))) {
                TablaSimbolos.agregar(Integer.toString(entero), TablaToken.getId("USHORT"));
            }
            return new Token(TablaToken.getId("USHORT"), Integer.toString(entero));
        }
        return null;
    }
}
