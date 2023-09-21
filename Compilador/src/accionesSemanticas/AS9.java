package accionesSemanticas;

import java.io.IOException;

import compiladores.Token;

public class AS9 extends AccionSemantica {
    // Verificar rango del entero y obtener el número
    @Override
    public Token ejecutarAS(char c) throws IOException {

        char last = lexema.charAt(lexema.length() - 1);
        int entero;
        if (last == 'l') { // entero largo _l
            entero = Integer.parseInt(lexema.substring(0, (lexema.length()-3)));
            if (entero < Integer.MIN_VALUE) {
                System.out.println("El entero es menor al valor mínimo permitido");
            }
            if (entero > Integer.MAX_VALUE) {
                System.out.println("El entero es mayor al valor máximo permitido");
            }
            if (! TS.pertenece(entero)) {
                TS.agregar(entero);
            }
            return new Token(getId("LONG", Integer.toString(entero)));
        } else if (last == 's') { // entero corto _us
            entero = Integer.parseInt(lexema.substring(0, (lexema.length()-4))); 
            if (entero < 0 || entero > 255) {
                System.out.println("El entero se encuentra fuera del rango permitido");
            }
            if (!TS.pertenece(entero)) {
                TS.agregar(entero);
            }
            return new Token(getId("SHORT", Integer.toString(entero)));
        }
        return null;
    }
}
