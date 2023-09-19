package compiladores;

import java.util.HashMap;

public class Tabla {
    // Hay que ver cómo implementarlo ya que puede ser un hashmap de por ejemplo
    // nombre y tipo o nose
    private HashMap<String, String> TS = new HashMap<>();// REVISAR
    private HashMap<> TPR = new HashMap<>();

    // A continuacion pesudocodigo para agregar a la tabla de simbolos
    public void agregar(String nombre, String tipo) {
        if (!pertenece(nombre, tipo))
            TS.put(nombre, tipo);
    }

    public boolean pertenece(String nombre, String tipo) {
        if (TS.get(nombre) == null)
            return false;
        else
            return true;
    }

    public void eliminar() {

    }
}
