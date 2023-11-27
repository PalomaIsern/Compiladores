package compiladores;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

public class Assembler {

    private HashMap<Integer, Terceto> CodIntermedio;
    private static StringBuilder codigo = new StringBuilder();
    private static StringBuilder instrucciones = new StringBuilder();
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
        codigo.append("MaxNumUSHORT db 255\n");
        codigo.append("MinNumLong dd -2147483648\n");
        codigo.append("MaxNumLong dd 2147483647\n");
        codigo.append("MinNumDouble dq 2.2250738585072014e-308\n");
        codigo.append("MaxNumDouble dq 1.7976931348623157e+308\n");

        generarInstrucciones();

        codigo.append(datos.getDatosAssembler());
        codigo.append(".code\n");
        codigo.append(datos.getFuncionesAssembler());
        // codigo
        codigo.append("START:\n");
        codigo.append(instrucciones);
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
                if (operador.startsWith("Label")) {
                    instrucciones.append(operador + ":" + "\n");
                }
                if (instruccion != "ERROR") {
                    String registro = " ";
                    if (t.get_Tipo() == "DOUBLE") {
                        registro = "ST(0)";
                    } else {
                        registro = getRegistroDisponible();
                    }
                    String op1 = t.get_Op1();
                    String op2 = t.get_Op2();

                    if (op1.startsWith("[")) {
                        if (instruccion != "JMP")
                            op1 = CodIntermedio.get(Integer.parseInt(borrarCorchetes(op1))).get_VA();
                    } else if (op1 != "-") {
                        Simbolo s1 = datos.get_Simbolo(Integer.parseInt(t.get_Op1()));
                        if (op1 != "-" && s1.get_Uso() != "Constante" && s1.get_Uso() != "ConstantePositiva")
                            op1 = "_" + datos.reemplazarPuntos(s1.get_Ambito());
                        else if (op1 != "-" && s1.get_Uso() == "Constante"
                                || op1 != "-" && s1.get_Uso() == "ConstantePositiva")
                            op1 = s1.get_Lex();
                    }

                    if (op2.startsWith("[")) {
                        if (instruccion != "JMP")
                            op2 = CodIntermedio.get(Integer.parseInt(borrarCorchetes(op2))).get_VA();
                    } else if (op2 != "-") {
                        Simbolo s2 = datos.get_Simbolo(Integer.parseInt(t.get_Op2()));
                        if (op2 != "-" && s2.get_Uso() != "Constante" && s2.get_Uso() != "ConstantePositiva")
                            op2 = "_" + datos.reemplazarPuntos(s2.get_Ambito());
                        else if (op2 != "-" && s2.get_Uso() == "Constante"
                                || op2 != "-" && s2.get_Uso() == "ConstantePositiva")
                            op2 = s2.get_Lex();
                    }
                    if ((operador == "+") || (operador == "-") || (operador == "*") || (operador == "/")
                            || (operador == "=")) {
                        instrucciones.append("MOV " + registro + ", " + op1 + "\n");
                        if (operador != "=") {
                            instrucciones.append(instruccion + " " + registro + ", " + op2 + "\n");
                            String vAux = t.set_VA();
                            Simbolo sim = new Simbolo(280, vAux);
                            TablaSimbolos.agregar_sin_chequear(sim);
                            instrucciones.append("MOV " + vAux + ", " + registro + "\n");
                        } else {
                            instrucciones.append("MOV " + op1 + ", " + registro + "\n");
                        }
                    } else if (operador == ">" || operador == ">=" || operador == "<" || operador == "<=") {
                        instrucciones.append("MOV " + registro + ", " + op1 + "\n");
                        instrucciones.append("CMP " + registro + ", " + op2 + "\n");
                    } else if (instruccion == "JMP") {
                        String destino = borrarCorchetes(op2);
                        instrucciones.append("JMP " + "Label" + destino + "\n");
                    } else if (instruccion == "BF") {
                        generarSaltoCondicional(t);
                    } else if (instruccion == "CALL") {
                        instrucciones.append("CALL " + op1.substring(1) + "\n");
                    } else if (instruccion == "CALLMetodoClase") {
                        String metodo = datos
                                .reemplazarPuntos(datos.get_Simbolo(Integer.parseInt(t.get_Op1())).get_Ambito());
                        instrucciones.append("CALL " + metodo + "\n");
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
            instrucciones.append("JLE Label" + nro + "\n");
        else if (ultimoComparador == ">=")
            instrucciones.append("JL Label" + nro + "\n");
        else if (ultimoComparador == "<")
            instrucciones.append("JGE Label" + nro + "\n");
        else if (ultimoComparador == "<=")
            instrucciones.append("JG Label" + nro + "\n");
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
                    return "FADD";
                } else
                    return "ADD";
            case "-":
                if (tipo == "DOUBLE")
                    return "FSUB";
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
                return "SUB";
            case "*":
                if (tipo == "DOUBLE")
                    return "FMUL";
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
                    return "IMUL";
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
                    return "MUL";
                }
            case "/":
                if (tipo == "DOUBLE")
                    return "FDIV";
                else if (tipo == "LONG")
                    return "IDIV";
                else if (tipo == "USHORT")
                    return "DIV";
            case "=":
                return "=";
            case "<":
                ultimoComparador = "<";
                return "JGE";
            case "<=":
                ultimoComparador = "<=";
                return "JG";
            case ">":
                ultimoComparador = ">";
                return "JLE";
            case ">=":
                ultimoComparador = ">=";
                return "JL";
            case "==":
                return "JNE";
            case "!!":
                return "JE";
            case "CALL":
                return "CALL";
            case "CALLMetodoClase":
                return "CALLMetodoClase";
            case "BI":
                return "JMP";
            case "BF":
                return "BF";
            case "UStoL":
                // ocupar los registros y chequear esto
                instrucciones.append("MOV BL, " + datos.get_Simbolo(Integer.parseInt(op1)).get_Lex() + "\n");
                instrucciones.append("MOV BH, 0" + "\n");
                instrucciones.append("MOV BX, " + datos.get_Simbolo(Integer.parseInt(op1)).get_Lex() + "\n");
                instrucciones.append("MOV ECX, 0" + "\n");
                instrucciones.append("MOV CX, BX" + "\n");
                instrucciones.append("MOV EBX, ECX" + "\n");
                return " ";
            case "UStoD":
                // ocupar los registros y chequear
                instrucciones.append("MOV BL, " + datos.get_Simbolo(Integer.parseInt(op1)).get_Lex() + "\n");
                instrucciones.append("MOV BH, 0" + "\n");
                instrucciones.append("MOV BX, " + datos.get_Simbolo(Integer.parseInt(op1)).get_Lex() + "\n");
                instrucciones.append("MOV ECX, 0" + "\n");
                instrucciones.append("MOV CX, BX" + "\n");
                instrucciones.append("MOV EBX, ECX" + "\n");
                instrucciones.append("MOV EAX, " + datos.get_Simbolo(Integer.parseInt(op1)).get_Lex() + "\n");
                instrucciones.append("MOV EDX, 0" + "\n");
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
