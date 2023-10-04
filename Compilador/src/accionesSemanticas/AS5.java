package accionesSemanticas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import compiladores.Lexico;
import compiladores.TablaToken;
import compiladores.Token;

public class AS5 extends AccionSemantica {
    private ArrayList<Character> caracteres = new ArrayList<>(Arrays.asList('/', '{', '}', '(', ')', ',', ';', '-'));

    // devuelve el caracter leido. Reconoce '/', '{', '}', '(', ')', ',', ';', '-'
    @Override
    public Token ejecutarAS(char c) throws IOException {
        if (caracteres.contains(c)) {
            Lexico.setVolverALeer(true);
            return new Token(TablaToken.getId(Character.toString(c)));
        } else {
            System.out.println("El caracter " + c + " ingresado no es valido");
        }
        return null;
    }

}
