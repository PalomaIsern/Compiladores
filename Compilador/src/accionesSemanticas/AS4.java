package accionesSemanticas;

import java.io.IOException;

import compiladores.Token;

public class AS4 extends AccionSemantica {
    // devolver a la entrada el ultimo caracter leido, verificar si es PR, 
    //devolver PR
    @Override
    public Token ejecutarAS(char c) throws IOException {
        // devolver lo ultimo leido
        String pRes = lexema.toString();
        if (TPR.pertenece(pRes)) {
            return new Token(getId(pRes));
        } else {
            System.out.println(pRes+"no es una palabra reservada valida");
        }

        return null;
    }

}
