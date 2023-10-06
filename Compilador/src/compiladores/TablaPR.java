package compiladores;

import java.util.ArrayList;

public class TablaPR {
    private static ArrayList<String> TPR = new ArrayList<String>();

    public TablaPR() {
        TPR.add("IF");
        TPR.add("END_IF");
        TPR.add("ELSE");
        TPR.add("PRINT");
        TPR.add("CLASS");
        TPR.add("VOID");
        TPR.add("LONG");
        TPR.add("USHORT");
        TPR.add("DOUBLE");
        TPR.add("DO");
        TPR.add("UNTIL");
        TPR.add("IMPL");
        TPR.add("FOR");
        TPR.add("RETURN");
    }

    public static boolean pertenece(String valor) {
        if (TPR.contains(valor))
            return true;
        else
            return false;
    }

}