package accionesSemanticas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import compiladores.Token;

public class AS5 extends AccionSemantica {
    ArrayList<Character> caracteres = new ArrayList<>(Arrays.asList('/', '{', '}', '(', ')', ',', ';', '-'));

    // devuelve el caracter leido
    @Override
    public Token ejecutarAS(char c) throws IOException {
        /*
         * if (caracteres.contains(c)){
         * return new Token()
         * } else {
         * System.out.println("El caracter "+ c +" ingresado no es valido");
         * }
         */
        return null;
    }

}
