package compiladores;

import java.util.HashMap;

public class TablaSimbolos {

    private HashMap<String, Integer> TS;
    private static Integer cont;

    public TablaSimbolos() {
        this.TS = new HashMap<String, Integer>();
        this.cont = 0;
    }

    public void agregar(String valor) {
        if (!pertenece(valor)) {
            TS.put(valor, cont);
        }
        cont++;
    }

    public boolean pertenece(String nombre) {
        if (TS.get(nombre) == null)
            return false;
        else
            return true;
    }

}
