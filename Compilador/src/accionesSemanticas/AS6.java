package accionesSemanticas;

import java.io.IOException;

import compiladores.TablaSimbolos;
import compiladores.TablaToken;
import compiladores.Token;

public class AS6 extends AccionSemantica {
    // reconocer tokens '+=', '<=', '>=', '!!' y cadenas de una linea
    @Override
    public Token ejecutarAS(char c) throws IOException {
        lexema.append(c);
        char c1 = lexema.charAt(0); // primer caracter del StringBuilder
        if (c1 != '#') {
            return new Token(TablaToken.getId(lexema.toString()));
        } else {
            String lexemaCadena;
            if (lexema.charAt(lexema.length() - 1) == '#') {
                lexemaCadena = lexema.substring(1, lexema.charAt(lexema.length() - 1));
            } else {
                lexemaCadena = lexema.substring(1, lexema.charAt(lexema.length()));
            }
            TablaSimbolos.agregar(lexemaCadena);
            return new Token(TablaToken.getId("CADENA"));
        }
    }
}