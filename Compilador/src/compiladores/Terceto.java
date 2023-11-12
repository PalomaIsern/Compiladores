package compiladores;

public class Terceto {

    private String operador;
    private String op1;
    private String op2;
    private String tipo;

    public Terceto(String op, String o1, String o2) {
        operador = op;
        op1 = o1;
        op2 = o2;
        tipo = "-";
    }

    public Terceto(String op, String o1) {
        operador = op;
        op1 = o1;
        op2 = " ";
        tipo = "-";
    }

    public void set_Tipo(String t) {
        tipo = t;
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