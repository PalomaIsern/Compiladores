package compiladores;

import java.util.HashMap;

public class TablaSimbolos {

    private HashMap<String, Integer> TS;
    private static Integer cont;

    public TablaSimbolos() {
        this.TS = new HashMap<String, Integer>();
        this.cont = 0;
    }

    public Integer agregar(String valor, Integer ref) {
        if (!pertenece(valor)) {
            TS.put(valor, cont);
        }
        cont++;
        return cont;// Devuelve la referencia a la tabla de simbolos, esto se hace al momento de
                    // crear el objeto token al cual nos referimos
    }

    public boolean pertenece(String nombre) {
        if (TS.get(nombre) == null)
            return false;
        else
            return true;
    }

}
