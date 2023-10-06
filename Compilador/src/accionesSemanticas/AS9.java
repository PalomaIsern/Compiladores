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
        Lexico.VolverAtras();
        long limite = 2147483648L;
        if (last == 'l') { // entero largo _l
            long entero = Long.parseUnsignedLong(lexema.substring(0, (lexema.length() - 2)));
            if (entero > limite) {
                System.out.println(
                        "WARNING - Linea " + Main.getLinea() + ": el entero largo está fuera del rango permitido");
                entero = limite;
            } // Integer.MIN_VALUE se establece en -2,147,483,648.
              // Integer.MAX_VALUE se establece en 2,147,483,647.
            if (!TablaSimbolos.pertenece(Long.toString(entero))) {
                TablaSimbolos.agregar(Long.toString(entero), TablaToken.getId("LONG"));
            }
            return new Token(TablaToken.getId("LONG"), Long.toString(entero));
        } else if (last == 's') { // entero corto _us
            int entero = Integer.parseInt(lexema.substring(0, (lexema.length() - 3)));
            if (entero > 255) {
                System.out.println(
                        "WARNING - Linea " + Main.getLinea()
                                + ": el entero corto se encuentra fuera del rango permitido");
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
