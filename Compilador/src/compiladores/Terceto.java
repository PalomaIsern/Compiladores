package compiladores;

public class Terceto {

    private String operador;
    private int op1;
    private int op2;

    public Terceto(String op, int o1, int o2) {
        operador = op;
        op1 = o1;
        op2 = o2;
    }

    public Terceto(String op, int o1) {
        operador = op;
        op1 = o1;
        op2 = -1;
    }

    public String get_Operador() {
        return operador;
    }

    public int get_Op1() {
        return op1;
    }

    public int get_Op2() {
        return op2;
    }
}