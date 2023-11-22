package compiladores;

public class Terceto {

    private String operador;
    private String op1;
    private String op2;
    private String tipo;
    private String variableAuxiliar;
    private static int contadorVA = 1;

    public Terceto(String op, String o1, String o2) {
        operador = op;
        op1 = o1;
        op2 = o2;
        tipo = "-";
        variableAuxiliar = "@aux";
    }

    public Terceto(String op, String o1) {
        operador = op;
        op1 = o1;
        op2 = " ";
        tipo = "-";
        variableAuxiliar = "@aux";
    }

    public void set_VA() {
        contadorVA += 1;
        variableAuxiliar += Integer.toString(contadorVA);
    }

    public void set_Tipo(String t) {
        tipo = t;
    }

    public String get_VA() {
        return variableAuxiliar;
    }

    public String get_Tipo() {
        return tipo;
    }

    public String get_Operador() {
        return operador;
    }

    public String get_Op1() {
        return op1;
    }

    public String get_Op2() {
        return op2;
    }

    public void set_Op(String o) {
        op2 = o;
    }
}