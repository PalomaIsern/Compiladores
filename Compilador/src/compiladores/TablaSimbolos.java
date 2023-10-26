package compiladores;

import java.util.HashMap;

public class TablaSimbolos {

    private static HashMap<Integer, Simbolo> TS;
    private static Integer puntero = 0;

    public TablaSimbolos() {
        TS = new HashMap<Integer, Simbolo>();
    }

    public static void agregar(String valor, Integer id) {
        Simbolo s = new Simbolo(id, valor);
        if (pertenece(s.get_Lex()) == -1) {
            TS.put(puntero, s);
            puntero = puntero + 1;
        }
    }

    public static void agregar(String valor, Integer id, String tipo) {
        Simbolo s = new Simbolo(id, valor, tipo);
        if (pertenece(s.get_Lex()) == -1) {
            TS.put(puntero, s);
            puntero = puntero + 1;
        }
    }

    public static int pertenece(String lexema) {
        for (HashMap.Entry<Integer, Simbolo> entry : TS.entrySet()) {
            Simbolo s = entry.getValue();
            if (s.get_Lex().equals(lexema)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public static void eliminar(String valor) {
        int clave = pertenece(valor);
        if (clave != -1)
            TS.remove(clave);
    }

    public Simbolo get_Simbolo(int clave) {
        return TS.get(clave);
    }

    public void imprimirContenido() {
        System.out.println("   TABLA DE SIMBOLOS\n");
        for (HashMap.Entry<Integer, Simbolo> i : TS.entrySet()) {
            System.out.println("Referencia: " + i.getKey() + ", Simbolo: " + i.getValue().imprimir() + "\n");
        }
    }

}
