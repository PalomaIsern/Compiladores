package accionesSemanticas;

import java.io.IOException;

import compiladores.Lexico;
import compiladores.Linea;
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
                System.out.println("Linea " + Linea.getLinea() +
                        ": WARNING - el entero largo está fuera del rango permitido");
                entero = limite;
            }
            TablaSimbolos.agregarConstante(Long.toString(entero), TablaToken.getId("CTE"), "LONG");
            return new Token(TablaToken.getId("CTE"), Long.toString(entero));
        } else if (last == 's') { // entero corto _us
            int entero = Integer.parseInt(lexema.substring(0, (lexema.length() - 3)));
            if (entero > 255) {
                System.out.println("Linea " + Linea.getLinea() +
                        ": WARNING - el entero corto se encuentra fuera del rango permitido");
                entero = 255;
            }
            TablaSimbolos.agregarConstante(Integer.toString(entero), TablaToken.getId("CTEPOS"), "USHORT");
            return new Token(TablaToken.getId("CTEPOS"), Integer.toString(entero));
        }
        return null;
    }
}
