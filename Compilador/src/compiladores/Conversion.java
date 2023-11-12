package compiladores;

public class Conversion {
    private String[][][] matrixConvertibilidad;

    // 0: DOUBLE
    // 1: LONG
    // 2: USHORT
    public Conversion() {
        matrixConvertibilidad = new String[3][3][3];
        matrixConvertibilidad[0][0] = new String[] { "-", "DOUBLE", "-" }; // Conversión DOUBLE A DOUBLE = DOUBLE
        matrixConvertibilidad[0][1] = new String[] { "-", "DOUBLE", "LtoD" }; // Conversión DOUBLE A LONG = DOUBLE
        matrixConvertibilidad[0][2] = new String[] { "-", "DOUBLE", "UStoD" }; // Conversión DOUBLE A USHORT = DOUBLE
        matrixConvertibilidad[1][0] = new String[] { "LtoD", "DOUBLE", "-" }; // Conversión LONG A DOUBLE = DOUBLE
        matrixConvertibilidad[1][1] = new String[] { "-", "LONG", "-" }; // Conversión LONG A LONG = LONG
        matrixConvertibilidad[1][2] = new String[] { "-", "LONG", "UStoL" }; // Conversión LONG A USHORT = LONG
        matrixConvertibilidad[2][0] = new String[] { "UStoD", "DOUBLE", "-" }; // Conversión USHORT A DOUBLE = DOUBLE
        matrixConvertibilidad[2][1] = new String[] { "UStoL", "LONG", "-" }; // Conversión USHORT A LONG = LONG
        matrixConvertibilidad[2][2] = new String[] { "-", "USHORT", "-" }; // Conversión USHORT A USHORT = USHORT
    }

    public String Convertir(String type1, String type2) {
        // Mapea los tipos de cadena a índices en la matriz de compatibilidad
        int index1 = mapTypeToIndex(type1);
        int index2 = mapTypeToIndex(type2);

        String[] elemento = matrixConvertibilidad[index1][index2];
        String primero = elemento[0];
        String segundo = elemento[2];
        if (primero == "-" && segundo == "-") {
            System.out.println("No hay que hacer conversion entre dos " + type1);
            return "-";
        } else if (primero != "-") {
            return primero;
        } else {
            return segundo;
        }
    }

    public String devolverElementoAConvertir(String elemento1, String tipo1, String elemento2, String tipo2) {
        int index1 = mapTypeToIndex(tipo1);
        int index2 = mapTypeToIndex(tipo2);
        String[] elemento = matrixConvertibilidad[index1][index2];
        if (elemento[1].equals(tipo1))
            return elemento2;
        else
            return elemento1;
    }

    public String devolverTipoAConvertir(String op) {
        String tipo = "-";
        if (op == "UStoD" || op == "DOUBLE" || op == "LtoD")
            tipo = "DOUBLE";
        else if (op == "USHORT")
            tipo = "USHORT";
        else if (op == "LONG" || op == "UStoL")
            tipo = "LONG";
        return tipo;
    }

    private int mapTypeToIndex(String type) {
        // Mapea una cadena de tipo a su índice correspondiente
        switch (type) {
            case "DOUBLE":
                return 0;
            case "LONG":
                return 1;
            case "USHORT":
                return 2;
            default:
                return -1; // Tipo desconocido
        }
    }
}