package compiladores;

import java.io.IOException;

public abstract class AccionSemantica {
    public static StringBuilder lexema = new StringBuilder();

    public static String getStringBuilder(){
        return lexema.toString();
    }
    public abstract Token ejecutarAS(char c) throws IOException;
}
