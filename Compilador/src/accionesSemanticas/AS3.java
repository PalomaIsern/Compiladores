package accionesSemanticas;

import java.io.IOException;

import compiladores.Lexico;
import compiladores.Linea;
import compiladores.TablaSimbolos;
import compiladores.TablaToken;
import compiladores.Token;

public class AS3 extends AccionSemantica {
    // devolver a la entrada el ultimo caracter leido, verificar rango del string,
    // alta en la TS, devolver ID y puntero a la TS
    @Override
    public Token ejecutarAS(char c) throws IOException {
        Lexico.setVolverALeer(true); // devolver lo ultimo leido
        Lexico.VolverAtras();
        String id = lexema.toString();
        if (id.length() > 20) {
            System.out.println("Linea " + Linea.getLinea() +
                    ": WARNING - el identificador ha superado la longitud maxima permitida de 20 caracteres");
            id = id.substring(0, 20);
        }
        TablaSimbolos.agregar(id, TablaToken.getId("ID"));
        return new Token(TablaToken.getId("ID"), id);
    }

}
