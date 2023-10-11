package accionesSemanticas;

import java.io.IOException;

import compiladores.Lexico;
import compiladores.Linea;
import compiladores.TablaPR;
import compiladores.TablaToken;
import compiladores.Token;
import compiladores.Linea;

public class AS4 extends AccionSemantica {
    // devolver a la entrada el ultimo caracter leido, verificar si es PR,
    // devolver PR
    @Override
    public Token ejecutarAS(char c) throws IOException {
        Lexico.setVolverALeer(true);// devolver lo ultimo leido
        Lexico.VolverAtras();
        String pRes = lexema.toString();
        if (TablaPR.pertenece(pRes)) {
            return new Token(TablaToken.getId(pRes));
        } else {
            System.out.println("Linea " + Linea.getLinea() + ": WARNING -" +
                    pRes + "no es una palabra reservada valida");
        }
        return null;
    }

}
