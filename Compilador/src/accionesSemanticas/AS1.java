package accionesSemanticas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import compiladores.Token;

public class AS1 extends AccionSemantica {
    // agregar caracter , verificar caracter valido

    ArrayList<Character> caracteres = new ArrayList<>(Arrays.asList('+', '<', '>', '!', '.', '*', '#', '='));

    @Override
    public Token ejecutarAS(char c) throws IOException {
        if (caracteres.contains(c) || Character.isLetter(c) || Character.isDigit(c)) {
            lexema.setLength(0);
            lexema.append(c);
        } else {
            System.out.println("El caracter no es valido");
        }
        return null; // no retorno nada porque aun no forme completamente el token
    }

}
