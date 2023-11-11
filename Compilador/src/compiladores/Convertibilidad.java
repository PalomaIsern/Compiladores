package compiladores;

public class Convertibilidad {
    private int[][] matrixConvertibilidad;

   //0: DOUBLE
   //1: LONG
   //2: USHORT
    public Convertibilidad() {
        matrixConvertibilidad =new int[3][3];
        matrixConvertibilidad[0][0] = 0; //Conversión DOUBLE A DOUBLE = DOUBLE
        matrixConvertibilidad[0][1] = 0; //Conversión DOUBLE A LONG = DOUBLE
        matrixConvertibilidad[0][2] = 0; //Conversión DOUBLE A USHORT = DOUBLE
        matrixConvertibilidad[1][0] = 0; //Conversión LONG A DOUBLE = DOUBLE
        matrixConvertibilidad[1][1] = 1; //Conversión LONG A LONG = LONG
        matrixConvertibilidad[1][2] = 1; //Conversión LONG A USHORT = LONG
        matrixConvertibilidad[2][0] = 0; //Conversión USHORT A DOUBLE = DOUBLE
        matrixConvertibilidad[2][1] = 1; //Conversión USHORT A LONG = LONG
        matrixConvertibilidad[2][2] = 2; //Conversión USHORT A USHORT = USHORT
    }

    public int sonTiposCompatibles(String type1, String type2) {
        // Mapea los tipos de cadena a índices en la matriz de compatibilidad
        int index1 = mapTypeToIndex(type1);
        int index2 = mapTypeToIndex(type2);

        if (index1 != -1 && index2 != -1){
            return matrixConvertibilidad[index1][index2];
        } else {
            // Si alguno de los tipos no se encuentra en la matriz, no hay información de compatibilidad.
            return -1;
        }
    }

    public String tipoConversion(String type1, String type2) {
        int tipoAlConvertir = this.sonTiposCompatibles(type1, type2);
        if (tipoAlConvertir != -1){
            if (tipoAlConvertir == 0)
                return "DOUBLE";
            if (tipoAlConvertir == 1)
                return "LONG";
            return "USHORT";
        }
        return "-1";
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