package accionesSemanticas;

import java.io.IOException;

import compiladores.Token;

public class AS9 extends AccionSemantica {
    // Verificar rango del entero y obtener el número
    @Override
    public Token ejecutarAS(char c) throws IOException {

        char last = lexema.charAt(lexema.length() - 1);
        int entero = Integer.parseInt(lexema.toString());
        if (last == 'l') { // entero largo
            if (entero < Integer.MIN_VALUE) {
                System.out.println("El entero es menor al valor mínimo permitido");
            }
            if (entero > Integer.MAX_VALUE) {
                System.out.println("El entero es mayor al valor máximo permitido");
            }
        } else if (last == 's') { // entero corto
            if (entero < 0 || entero > 255) {
                System.out.println("El entero se encuentra fuera del rango permitido");
            }
        }
        /*
         * if !(TS.pertenece(lexema)) {
         * TS.agregar(lexema);
         * }
         */
        // return new Token(getToken("CTE"), lexema);
        return null;
    }
}
