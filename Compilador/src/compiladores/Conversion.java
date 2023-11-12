package compiladores;

public class Conversion {
    private String[][][] matrixConvertibilidad;
    private int elementoAConvertir = -1;

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
            System.out.println("No hay que hacer conversion");
            return "-";
        } else if (primero != "-") {
            elementoAConvertir = 1;
            return primero;
        } else {
            elementoAConvertir = 2;
            return segundo;
        }
    }

    public int devolverElementoAConvertir() {
        int aux = elementoAConvertir;
        elementoAConvertir = -1;
        return aux;
    }

    public String devolverTipoAConvertir(String op) {
        String tipo = "-";
        switch (op) {
            case "UStoD":
                tipo = "DOUBLE";
            case "DOUBLE":
                tipo = "DOUBLE";
            case "USHORT":
                tipo = "USHORT";
            case "LONG":
                tipo = "LONG";
            case "LtoD":
                tipo = "DOUBLE";
            case "UStoL":
                tipo = "LONG";
        }
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