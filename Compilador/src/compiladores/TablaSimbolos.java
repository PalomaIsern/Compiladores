package compiladores;

import java.util.HashMap;

public class TablaSimbolos {

    private static HashMap<String, Integer> TS;

    public TablaSimbolos() {
        TS = new HashMap<String, Integer>();
    }

    public static void agregar(String valor, Integer id) {
        if (!pertenece(valor))
            TS.put(valor, id);
    }

    public static boolean pertenece(String nombre) {
        if (TS.get(nombre) == null)
            return false;
        else
            return true;
    }

    public void imprimirContenido() {
        System.out.println("   TABLA DE SIMBOLOS\n");
        for (HashMap.Entry<String, Integer> i : TS.entrySet()) {
            System.out.println("Lexema: " + i.getKey() + ", id: " + i.getValue() + "\n");
        }
    }

}
