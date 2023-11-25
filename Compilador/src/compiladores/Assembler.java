package compiladores;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

public class Assembler {

    private HashMap<Integer, Terceto> CodIntermedio;
    private static StringBuilder codigo = new StringBuilder();
    private TablaSimbolos datos;
    private HashMap<String, Boolean> registros = new HashMap<String, Boolean>();
    private HashMap<String, Boolean> registrosPtoFlot = new HashMap<String, Boolean>();
    private boolean seguir = true;
    private static String ultimoComparador;

    public Assembler(HashMap<Integer, Terceto> ci, TablaSimbolos d) {
        CodIntermedio = new HashMap<Integer, Terceto>(ci);
        datos = d;
        registros.put("EAX", false);
        registros.put("EBX", false);
        registros.put("ECX", false);
        registros.put("EDX", false);
        registrosPtoFlot.put("ST(0)", false);
        registrosPtoFlot.put("ST(1)", false);
        registrosPtoFlot.put("ST(2)", false);
        registrosPtoFlot.put("ST(3)", false);
        registrosPtoFlot.put("ST(4)", false);
        registrosPtoFlot.put("ST(5)", false);
        registrosPtoFlot.put("ST(6)", false);
        registrosPtoFlot.put("ST(7)", false);
    }

    /*
     * public String get_RegistroEnteros() {
     * if (registros.get("EAX"))
     * return "EAX";
     * else if (registros.get("EBX"))
     * return "EBX";
     * else if (registros.get("ECX"))
     * return "ECX";
     * else if (registros.get("EDX"))
     * return "EDX";
     * else {
     * System.out.println("Los registros de enteros están ocupados");
     * return "-";
     * }
     * }
     */

    public static void generarArchivo() {
        try {
            String nombre = "Assembler.asm";
            File archivo = new File(nombre);
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
            FileWriter fw = new FileWriter(archivo);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(codigo.toString());
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void GenerarAssembler() {
        codigo.append(".386\n");
        codigo.append(".model flat, stdcall\n");
        codigo.append("option casemap :none\n");
        codigo.append("include \\masm32\\include\\windows.inc\n");
        codigo.append("include \\masm32\\include\\kernel32.inc\n");
        // codigo.append("include \\masm32\\include\\masm32.inc\n");
        codigo.append("include \\masm32\\include\\user32.inc\n");
        codigo.append("includelib \\masm32\\lib\\kernel32.lib\n");
        // codigo.append("includelib \\masm32\\lib\\masm32.lib\n");
        codigo.append("includelib \\masm32\\lib\\user32.lib\n");
        codigo.append(".data\n");
        codigo.append("MaxNumUSHORT db 255");
        codigo.append("MinNumLong dd -2147483648");
        codigo.append("MaxNumLong dd 2147483647");
        codigo.append("MinNumDouble dq 2.2250738585072014e-308");
        codigo.append("MaxNumDouble dq 1.7976931348623157e+308");
        codigo.append(datos.getDatosAssembler());
        codigo.append(".code\n");
        // codigo
        codigo.append("START:\n");
        generarInstrucciones();
        codigo.append("END START");
        generarArchivo();
    }

    public String borrarCorchetes(String p) {
        if (p.startsWith(("[")))
            return p.substring(1, p.length() - 1);
        return p;
    }

    public void generarInstrucciones() {
        for (HashMap.Entry<Integer, Terceto> i : CodIntermedio.entrySet()) {
            if (seguir) {
                Terceto t = i.getValue();
                String instruccion = devolverOperacion(t);
                String operador = t.get_Operador();
                System.out.println("Vuelta");
                if (instruccion != "ERROR") {
                    String registro = " ";
                    if (t.get_Tipo() == "DOUBLE") {
                        registro = "ST(O)";
                    } else {
                        registro = getRegistroDisponible();
                    }
                    String op1 = t.get_Op1();
                    String op2 = t.get_Op2();
                    if (op1.startsWith("[")) {
                        op1 = CodIntermedio.get(Integer.parseInt(borrarCorchetes(op1))).get_VA();
                    } else if (op1 != "-" && datos.get_Simbolo(Integer.parseInt(t.get_Op1())).get_Uso() != "Constante")
                        op1 = "_" + datos.get_Simbolo(Integer.parseInt(t.get_Op1())).get_Ambito();
                    else if (op1 != "-" && datos.get_Simbolo(Integer.parseInt(t.get_Op1())).get_Uso() == "Constante")
                        op1 = datos.get_Simbolo(Integer.parseInt(t.get_Op1())).get_Lex();
                    if (op2.startsWith("[")) {
                        op2 = CodIntermedio.get(Integer.parseInt(borrarCorchetes(op2))).get_VA();
                    } else if (op2 != "-" && datos.get_Simbolo(Integer.parseInt(t.get_Op2())).get_Uso() != "Constante")
                        op2 = "_" + datos.get_Simbolo(Integer.parseInt(t.get_Op2())).get_Ambito();
                    else if (op2 != "-"
                            && datos.get_Simbolo(Integer.parseInt(t.get_Op2())).get_Uso().equals("Constante"))
                        op2 = datos.get_Simbolo(Integer.parseInt(t.get_Op2())).get_Lex();
                    if ((operador == "+") || (operador == "-") || (operador == "*") || (operador == "/")
                            || (operador == "=")) {
                        codigo.append("MOV " + registro + ", " + op1 + "\n");
                        if (operador != "=") {
                            codigo.append(instruccion + " " + registro + ", " + op2 + "\n");
                            String vAux = t.set_VA();
                            codigo.append("MOV " + vAux + ", " + registro + "\n");
                        } else {
                            codigo.append("MOV " + op1 + ", " + registro + "\n"); // chequear si tamb es asi para double
                        }
                    } else if (operador == ">" || operador == ">=" || operador == "<" || operador == "<=") {
                        codigo.append("MOV " + registro + ", " + op1 + "\n");
                        codigo.append("CMP " + registro + ", " + op2 + "\n");
                    }
                } else {
                    System.out.println("La ejecución ha sido interrumpida porque se ha detectado un error");
                    seguir = false;
                }
            }
        }
    }

    public void generarSaltoCondicional(Terceto t) {
        String nro = borrarCorchetes(t.get_Op2());
        if (ultimoComparador == ">")
            codigo.append("JLE Label " + nro + "\n");
        else if (ultimoComparador == ">=")
            codigo.append("JL Label " + nro + "\n");
        else if (ultimoComparador == "<")
            codigo.append("JGE Label " + nro + "\n");
        else if (ultimoComparador == "<=")
            codigo.append("JG Label " + nro + "\n");
    }

    public String getRegistroDisponible() {
        for (HashMap.Entry<String, Boolean> e : registros.entrySet()) {
            if (!e.getValue()) {
                registros.put(e.getKey(), true);
                return e.getKey();
            }
        }
        return " ";
    }

    public void setRegistroDisponible(String reg) {
        registros.put(reg, false);
    }

    public String devolverOperacion(Terceto t) {
        String operador = t.get_Operador();
        String tipo = t.get_Tipo();
        String op1 = t.get_Op1();
        String op2 = t.get_Op2();
        System.out.println("Op1: " + op1);
        String uso1, uso2 = "-";
        if (op1.contains("["))
            uso1 = "terceto";
        else if (op1 != "-")
            uso1 = datos.get_Simbolo(Integer.parseInt(op1)).get_Uso();
        else
            uso1 = "-";
        if (op2.contains("["))
            uso2 = "terceto";
        else if (op2 != "-")
            uso2 = datos.get_Simbolo(Integer.parseInt(op2)).get_Uso();
        else
            uso2 = "-";
        switch (operador) {
            case "+":
                if (tipo == "DOUBLE") {
                    /*
                     * if (uso1 == "Constante" && uso2 == "Constante") {
                     * if ((Integer.parseInt(datos.get_Simbolo(Integer.parseInt(op1)).get_Lex()) +
                     * Integer
                     * .parseInt(datos.get_Simbolo(Integer.parseInt(op2)).get_Lex())) <
                     * 2.2250738585072014e-308
                     * || (Integer.parseInt(datos.get_Simbolo(Integer.parseInt(op1)).get_Lex())
                     * + Integer.parseInt(datos.get_Simbolo(Integer.parseInt(op2))
                     * .get_Lex())) > 1.7976931348623157e+308) {
                     * System.out.println("ERROR: linea " + Linea.getLinea()
                     * + " El resultado de la suma esta fuera del rango permitido");
                     * return "ERROR";
                     * }
                     * }
                     */
                    return "fadd";
                } else
                    return "add";
            case "-":
                if (tipo == "DOUBLE")
                    return "fsub";
                else if (tipo == "USHORT") {
                    /*
                     * if (uso1 == "Constante" && uso2 == "Constante") {
                     * if ((Integer.parseInt(op1) - Integer.parseInt(op2)) < 0) {
                     * System.out.println("ERROR: linea " + Linea.getLinea()
                     * +
                     * " El resultado de la resta en enteros sin signos no puede dar un resultado negativo"
                     * );
                     * return "ERROR";
                     * }
                     * }
                     */
                }
                return "sub";
            case "*":
                if (tipo == "DOUBLE")
                    return "fmul";
                else if (tipo == "LONG") {
                    /*
                     * if (uso1 == "Constante" && uso2 == "Constante") {
                     * if (((Integer.parseInt(datos.get_Simbolo(Integer.parseInt(op1)).get_Lex())
                     * + Integer.parseInt(datos.get_Simbolo(Integer.parseInt(op2)).get_Lex())) <
                     * -2147483648)
                     * || ((Integer.parseInt(datos.get_Simbolo(Integer.parseInt(op1)).get_Lex()) +
                     * Integer
                     * .parseInt(datos.get_Simbolo(Integer.parseInt(op2)).get_Lex())) > 2147483647))
                     * {
                     * System.out.println("ERROR: linea " + Linea.getLinea()
                     * + " El resultado del producto esta fuera del rango permitido");
                     * return "ERROR";
                     * }
                     * }
                     */
                    return "imul";
                } else if (tipo == "USHORT") {

                    /*
                     * if (uso1 == "Constante" && uso2 == "Constante") {
                     * if ((Integer.parseInt(datos.get_Simbolo(Integer.parseInt(op1)).get_Lex())
                     * + Integer.parseInt(datos.get_Simbolo(Integer.parseInt(op2)).get_Lex())) >
                     * 255) {
                     * System.out.println("ERROR: linea " + Linea.getLinea()
                     * + " El resultado del producto esta fuera del rango permitido");
                     * return "ERROR";
                     * }
                     * }
                     */
                    return "mul";
                }
            case "/":
                if (tipo == "DOUBLE")
                    return "fdiv";
                else if (tipo == "LONG")
                    return "idiv";
                else if (tipo == "USHORT")
                    return "div";
            case "=":
                return "=";
            case "<":
                ultimoComparador = "<";
                return "jge";
            case "<=":
                ultimoComparador = "<=";
                return "jg";
            case ">":
                ultimoComparador = ">";
                return "jle";
            case ">=":
                ultimoComparador = ">=";
                return "jl";
            case "==":
                return "jne";
            case "!!":
                return "je";
            case "CALL":
                // completar
                return " ";
            case "BI":
                String destino = borrarCorchetes(op2);
                codigo.append("JMP " + "Label " + destino + "\n");
                return "JMP";
            case "BF":
                generarSaltoCondicional(t);
                return "BF";
            case "UStoL":
                // ocupar los registros y chequear esto
                codigo.append("MOV BL, _" + datos.get_Simbolo(Integer.parseInt(op1)).get_Lex() + "\n");
                codigo.append("MOV BH, 0" + "\n");
                codigo.append("MOV BX, _" + datos.get_Simbolo(Integer.parseInt(op1)).get_Lex() + "\n");
                codigo.append("MOV ECX, 0" + "\n");
                codigo.append("MOV CX, BX" + "\n");
                codigo.append("MOV EBX, ECX" + "\n");
                return " ";
            case "UStoD":
                // ocupar los registros y chequear
                codigo.append("MOV BL, _" + datos.get_Simbolo(Integer.parseInt(op1)).get_Lex() + "\n");
                codigo.append("MOV BH, 0" + "\n");
                codigo.append("MOV BX, _" + datos.get_Simbolo(Integer.parseInt(op1)).get_Lex() + "\n");
                codigo.append("MOV ECX, 0" + "\n");
                codigo.append("MOV CX, BX" + "\n");
                codigo.append("MOV EBX, ECX" + "\n");
                codigo.append("MOV EAX, _" + datos.get_Simbolo(Integer.parseInt(op1)).get_Lex() + "\n");
                codigo.append("MOV EDX, 0" + "\n");
                return " ";
            case "LtoD":
                // ocupar los registros y chequear
                // codigo.append("FILD _" + datos.get_Simbolo(Integer.parseInt(op1)).get_Lex());
                // // en st(0)
                return " ";
            default:
                return " ";
        }
    }
}
