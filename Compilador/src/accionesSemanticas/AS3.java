package accionesSemanticas;

import java.io.IOException;

import compiladores.Token;

public class AS3 extends AccionSemantica {
    // devolver a la entrada el ultimo caracter leido, verificar rango del string,
    // alta en la TS, devolver ID y puntero a la TS
    @Override
    public Token ejecutarAS(char c) throws IOException {
        // devolver lo ultimo leido
        String id = lexema.toString();
        if (id.length() > 20) {
            System.out.println("El identificador ha superado la longitud maxima permitida de 20 caracteres");
            id = id.substring(0, 20);
        }
        if (! TS.pertenece(id)) {
            TS.agregar(id);
        } 
        return new Token(tokens.getId("ID"), id);
    }

}
