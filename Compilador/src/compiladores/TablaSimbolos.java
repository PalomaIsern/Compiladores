package compiladores;

import java.util.HashMap;

public class TablaSimbolos {

    private static HashMap<String, Integer> TS;
    private static Integer cont;

    public TablaSimbolos() {
        TS = new HashMap<String, Integer>();
        cont = 0;
    }

    public static void agregar(String valor) {
        if (!pertenece(valor)) {
            TS.put(valor, cont);
        }
        cont++;
    }

    public static boolean pertenece(String nombre) {
        if (TS.get(nombre) == null)
            return false;
        else
            return true;
    }

}
