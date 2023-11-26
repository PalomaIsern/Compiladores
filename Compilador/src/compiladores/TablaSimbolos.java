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

    public static void agregarConstante(String valor, Integer id, String tipo) {
        Simbolo nuevo = new Simbolo(id, valor, tipo);
        boolean existe = false;
        for (HashMap.Entry<Integer, Simbolo> entry : TS.entrySet()) {
            Simbolo s = entry.getValue();
            if ((s.get_Lex().equals(valor)) && (s.get_Tipo().equals(tipo))) {
                existe = true;
            }
        }
        if (!existe) {
            TS.put(puntero, nuevo);
            puntero = puntero + 1;
        }
    }

    public static int buscarConstante(String valor, String tipo) {
        for (HashMap.Entry<Integer, Simbolo> entry : TS.entrySet()) {
            Simbolo s = entry.getValue();
            if ((s.get_Lex().equals(valor)) && (s.get_Tipo().equals(tipo))) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public static void eliminarConstante(String valor, String tipo) {
        for (HashMap.Entry<Integer, Simbolo> entry : TS.entrySet()) {
            Simbolo s = entry.getValue();
            if ((s.get_Lex().equals(valor)) && (s.get_Tipo().equals(tipo))) {
                TS.remove(entry.getKey());
            }
        }
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

    public String reemplazarPuntos(String palabra) {
        return palabra.replace(":", "$");
    }

    public StringBuilder getDatosAssembler() {
        StringBuilder sb = new StringBuilder();
        int contadorLong = 1;
        int contadorDouble = 1;
        int contadorUshort = 1;
        int funciones = 1;
        int cadenas = 1;
        for (HashMap.Entry<Integer, Simbolo> entry : TS.entrySet()) {
            Simbolo s = entry.getValue();
            String tipo = s.get_Tipo();
            int token = s.get_Token();
            if (tipo == "LONG")
                if (token == 257)
                    sb.append(reemplazarPuntos(s.get_Ambito()) + " db " + " ?\n");
                else {
                    sb.append("@cteLong" + contadorLong + " dd " + s.get_Lex() + " \n");
                    contadorLong += 1;
                }
            else if (tipo == "USHORT")
                if (token == 257)
                    sb.append(reemplazarPuntos(s.get_Ambito()) + " db " + " ?\n");
                else {
                    sb.append("@cteUS" + contadorUshort + " db " + s.get_Lex() + " \n");
                    contadorUshort += 1;
                }
            else if (tipo == "DOUBLE")
                if (token == 257)
                    sb.append(reemplazarPuntos(s.get_Ambito()) + " db " + " ?\n");
                else {
                    sb.append("@cteDouble" + contadorDouble + " dq " + s.get_Lex() + " \n");
                    contadorDouble += 1;
                }
            /*
             * else if (tipo == "VOID") {
             * sb.append("@funcion" + funciones + " db " + reemplazarPuntos(s.get_Ambito())
             * + " \n");
             * funciones += 1;}
             */
            else if (token == 273) {
                sb.append("@cadena" + cadenas + " db " + s.get_Lex() + " \n");
                cadenas += 1;
            } else {
                sb.append(s.get_Lex() + " db " + " ?\n");
            }
        }
        return sb;
    }

    public StringBuilder getFuncionesAssembler() {
        StringBuilder sb = new StringBuilder();
        for (HashMap.Entry<Integer, Simbolo> entry : TS.entrySet()) {
            Simbolo s = get_Simbolo(entry.getKey());
            String uso = s.get_Uso();
            if (uso == "Metodo") {
                sb.append(reemplazarPuntos(s.get_Ambito()) + ":" + "\n");
                sb.append("ret" + "\n");
            }
        }
        return sb;
    }

    public void imprimirContenido() {
        System.out.println("   TABLA DE SIMBOLOS\n");
        for (HashMap.Entry<Integer, Simbolo> i : TS.entrySet()) {
            System.out.println("ref: " + i.getKey() + ", Simbolo: " + i.getValue().imprimir() + "\n");
        }
    }

}
