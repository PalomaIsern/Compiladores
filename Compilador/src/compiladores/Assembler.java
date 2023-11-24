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
        codigo.append("MinNumLong dd -2147483648")
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
            if (seguir){
            Terceto t = i.getValue();
            String instruccion = devolverOperacion(t);
            String operador = t.get_Operador();
            if (instruccion !="ERROR") {
                if ((operador == "+") || (operador == "-") || (operador == "*") || (operador == "/") || (operador == "=")) {
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
                    } else {
                        op1 = "_" + op1;
                    }
                    if (op2.startsWith("[")) {
                        op1 = CodIntermedio.get(Integer.parseInt(borrarCorchetes(op2))).get_VA();
                    } else {
                        op2 = "_" + op2;
                    }
                    codigo.append("MOV " + registro + ", " + op1);
                    if (operador != "=") 
                        codigo.append(instruccion + " " + registro + ", " + op2);
                    String vAux = t.set_VA();
                    codigo.append("MOV " + vAux + ", " + registro);
                } 
            }
            else{
                System.out.println("La ejecución ha sido interrumpida porque se ha detectado un error");
                seguir = false;
            }
        }
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
        switch (operador) {
            case "+":
                if (tipo == "DOUBLE") {
                    if ((Integer.parseInt(op1) + Integer.parseInt(Op2)) < 2.2250738585072014e-308
                            || (Integer.parseInt(op1) + Integer.parseInt(Op2)) > 1.7976931348623157e+308) {
                        System.out.println("ERROR: linea " + Linea.getLinea()
                                + " El resultado de la suma esta fuera del rango permitido");
                        return "ERROR";
                    }
                    return "fadd";
                } else
                    return "add";
            case "-":
                if (tipo == "DOUBLE")
                    return "fsub";
                else if (tipo == "USHORT")
                    if ((Integer.parseInt(op1) - Integer.parseInt(Op2)) < 0) {
                        System.out.println("ERROR: linea " + Linea.getLinea()
                                + " El resultado de la resta en enteros sin signos no puede dar un resultado negativo");
                        return "ERROR";
                    }
                return "sub";
            case "*":
                if (tipo == "DOUBLE")
                    return "fmul";
                else if (tipo == "LONG") {
                    if ((Integer.parseInt(op1) + Integer.parseInt(Op2)) < -2147483648
                            || (Integer.parseInt(op1) + Integer.parseInt(Op2)) > 2147483647) {
                        System.out.println("ERROR: linea " + Linea.getLinea()
                                + " El resultado del producto esta fuera del rango permitido");
                        return "ERROR";
                    }
                    return "imul";
                } else if (tipo == "USHORT") {
                    if ((Integer.parseInt(op1) + Integer.parseInt(Op2)) > 255) {
                        System.out.println("ERROR: linea " + Linea.getLinea()
                                + " El resultado del producto esta fuera del rango permitido");
                        return "ERROR";
                    }
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
            case "<":
                return "jg";
            case "<=":
                return "jge";
            case ">":
                return "jl";
            case ">=":
                return "jle";
            case "==":
                return "jne";
            case "!!":
                return "je";
            case "CALL":
                // completar
                return " ";
            case "BI":
                // completar
                return " ";
            case "BF":
                // completar
                return " ";
            case "UStoL":
                // ocupar los registros y chequear esto
                codigo.append("MOV BL, _" + datos.get_Simbolo(Integer.toString(op1)).get_lex());
                codigo.append("MOV BH, 0");
                codigo.append("MOV BX, _" + datos.get_Simbolo(Integer.toString(op1)).get_lex());
                codigo.append("MOV ECX, 0");
                codigo.append("MOV CX, BX");
                codigo.append("MOV EBX, ECX");
                return " ";
            case "UStoD":
                // ocupar los registros y chequear
                codigo.append("MOV BL, _" + datos.get_Simbolo(Integer.toString(op1)).get_lex());
                codigo.append("MOV BH, 0");
                codigo.append("MOV BX, _" + datos.get_Simbolo(Integer.toString(op1)).get_lex());
                codigo.append("MOV ECX, 0");
                codigo.append("MOV CX, BX");
                codigo.append("MOV EBX, ECX");
                codigo.append("MOV EAX, _" + datos.get_Simbolo(Integer.toString(op1)).get_lex());
                codigo.append("MOV EDX, 0");
                return " ";
            case "LtoD":
                // ocupar los registros y chequear
                codigo.append("FILD _" + datos.get_Simbolo(Integer.toString(op1)).get_lex()); // en st(0)
                return " ";
            default:
                return " ";
        }
    }
}
