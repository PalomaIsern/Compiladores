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
        int clave = pertenece(s.get_Lex());
        if (clave == -1) {
            TS.put(puntero, s);
            puntero = puntero + 1;
        } else
            TS.get(clave).set_Rep();
    }

    public static void agregar(String valor, Integer id, String tipo) {
        Simbolo s = new Simbolo(id, valor, tipo);
        int clave = pertenece(s.get_Lex());
        if (clave == -1) {
            TS.put(puntero, s);
            puntero = puntero + 1;
        } else
            TS.get(clave).set_Rep();
    }

    public static void agregar_sin_chequear(Simbolo s) {
        TS.put(puntero, s);
        puntero = puntero + 1;
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
        if (clave != -1) {
            Simbolo s = TS.get(clave);
            if ((s.get_Rep()) > 1)
                s.disminuirCant();
            else
                TS.remove(clave);
        }
    }

    public static int buscar_por_ambito(String a) {
        for (HashMap.Entry<Integer, Simbolo> entry : TS.entrySet()) {
            Simbolo s = entry.getValue();
            if (s.get_Ambito().equals(a)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public static void remove_Simbolo(int clave) {
        TS.remove(clave);
    }

    public Simbolo get_Simbolo(int clave) {
        return TS.get(clave);
    }

    public void imprimirContenido() {
        System.out.println("   TABLA DE SIMBOLOS\n");
        for (HashMap.Entry<Integer, Simbolo> i : TS.entrySet()) {
            System.out.println("ref: " + i.getKey() + ", Simbolo: " + i.getValue().imprimir() + "\n");
        }
    }

}
