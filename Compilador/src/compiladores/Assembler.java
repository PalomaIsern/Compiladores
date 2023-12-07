package compiladores;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Assembler {

    private HashMap<Integer, Terceto> CodIntermedio;
    private static StringBuilder codigo = new StringBuilder();
    private static StringBuilder instrucciones = new StringBuilder();
    private TablaSimbolos datos;
    private HashMap<String, Boolean> registros = new HashMap<String, Boolean>();
    private boolean seguir = true;
    private static String ultimoComparador;
    private Stack<String> funciones = new Stack<String>();

    public Assembler(HashMap<Integer, Terceto> ci, TablaSimbolos d) {
        CodIntermedio = new HashMap<Integer, Terceto>(ci);
        datos = d;
        registros.put("EDX", false);
        registros.put("EBX", false);
        registros.put("ECX", false);
        registros.put("EAX", false);
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
        codigo.append("include \\masm32\\include\\masm32.inc\n");
        codigo.append("include \\masm32\\include\\user32.inc\n");
        codigo.append("includelib \\masm32\\lib\\kernel32.lib\n");
        codigo.append("includelib \\masm32\\lib\\masm32.lib\n");
        codigo.append("includelib \\masm32\\lib\\user32.lib\n");
        codigo.append(".data\n");
        codigo.append("MaxNumUSHORT db 255\n");
        codigo.append("MinNumLong dd -2147483648\n");
        codigo.append("MaxNumLong dd 2147483647\n");
        codigo.append("MinNumDouble dq 2.2250738585072014e-308\n");
        codigo.append("MaxNumDouble dq 1.7976931348623157e+308\n");
        codigo.append("OverflowMultiplicacion db \"Overflow en multiplicacion de enteros\"" + "\n");
        codigo.append("OverflowResta db \"Resultado negativo en resta de enteros sin signo\"" + "\n");
        codigo.append("OverflowSuma db \"Overflow en suma de punto flotante\"" + "\n");

        generarInstrucciones();

        codigo.append(datos.getDatosAssembler());
        codigo.append(".code\n");
        HashMap<String, Integer> func = new HashMap<>(datos.getFuncionesAssembler());
        generarCodigoFunciones(func);
        // codigo
        codigo.append("START:\n");
        codigo.append(instrucciones);
        instrucciones.append("OverFlowMul: \n");
        instrucciones
                .append("invoke  MessageBox, NULL, ADDR OverFlowMultiplicacion, ADDR OverFlowMultiplicacion, MB_OK \n");
        instrucciones.append("invoke ExitProcess, 0" + "\n");
        instrucciones.append("OverFlowResta: \n");
        instrucciones.append("invoke  MessageBox, NULL, ADDR OverFlowResta, ADDR OverFlowResta, MB_OK \n");
        instrucciones.append("invoke ExitProcess, 0" + "\n");
        instrucciones.append("OverFlowSuma: \n");
        instrucciones.append("invoke  MessageBox, NULL, ADDR OverFlowSuma, ADDR OverFlowSuma, MB_OK \n");
        instrucciones.append("invoke ExitProcess, 0" + "\n");
        codigo.append("END START");
        generarArchivo();
        imprimirCodigoIntermedio();
    }

    public void agregarFunciones(String op, int ref1, int ref2) {

    }

    public void generarCodigoFunciones(HashMap<String, Integer> funciones) {
        String operador;
        for (HashMap.Entry<Integer, Terceto> i : CodIntermedio.entrySet()) {
            operador = i.getValue().get_Operador();
            if (funciones.get(operador) != null && funciones.get(operador) == -1)
                funciones.put(operador, i.getKey());
            else if (funciones.get(operador) != null && funciones.get(operador) != -1)
                agregarFunciones(operador, funciones.get(operador), i.getKey());
        }
    }

    private void imprimirCodigoIntermedio() {
        System.out.println("CODIGO INTERMEDIO");
        for (HashMap.Entry<Integer, Terceto> i : CodIntermedio.entrySet()) {
            String j = i.getValue().get_Op1();
            String s = i.getValue().get_Op2();
            if (!s.contains("[") && s != "-") {
                int k = Integer.parseInt(s);
                s = datos.get_Simbolo(k).get_Lex();
            }
            if (!j.contains("[") && j != "-") {
                int l = Integer.parseInt(j);
                j = datos.get_Simbolo(l).get_Lex();
            }
            System.out.println("Referencia: " + i.getKey() + ", Terceto: (" + i.getValue().get_Operador() + " , " + j
                    + " , " + s + ")" + " Tipo: " + i.getValue().get_Tipo() + " VA: " + i.getValue().get_VA());
        }
    }

    public String borrarCorchetes(String p) {
        if (p.startsWith(("[")))
            return p.substring(1, p.length() - 1);
        return p;
    }

    public void controlar_OverFlowMul(String tipo) {
        if (tipo == "LONG")
            instrucciones.append("JO OverFlowMul \n");
        else
            instrucciones.append("JC OverFlowMul \n");
    }

    public void controlar_OverFlowResta() {
        instrucciones.append("JC OverFlowResta \n");
    }

    public void controlar_OverFlowSum() {
        String registro = getRegistroDisponible();
        char segundo = registro.charAt(1);
        instrucciones.append("FSTSW aux_sumaDouble \n");
        instrucciones.append("MOV " + segundo + " X, aux_sumaDouble" + "\n");
        instrucciones.append("SAHF " + "\n");
        instrucciones.append("JO OverFlowSuma \n");
        setRegistroDisponible(registro);
    }

    private String obtenerOperando(String op, String instruccion) {
        if (op.startsWith("[")) {
            if (instruccion != "JMP")
                return CodIntermedio.get(Integer.parseInt(borrarCorchetes(op))).get_VA();
        } else if (op != "-") {
            Simbolo s = datos.get_Simbolo(Integer.parseInt(op));
            if (op != "-" && s.get_Uso() != "Constante" && s.get_Uso() != "ConstantePositiva"
                    && s.get_Uso() != "Constante negativa")
                return "_" + datos.reemplazarPuntos(s.get_Ambito());
            else if (op != "-" && s.get_Uso() == "Constante" || op != "-" && s.get_Uso() == "ConstantePositiva"
                    || op != "-" && s.get_Uso() == "Constante negativa")
                return s.get_Lex();
        }
        return op;
    }

    public void generarInstrucciones() {
        for (HashMap.Entry<Integer, Terceto> i : CodIntermedio.entrySet()) {
            if (seguir) {
                Terceto t = i.getValue();
                String registro = " ";
                String instruccion = devolverOperacion(t);
                String operador = t.get_Operador();
                if (operador.startsWith("Label")) {
                    instrucciones.append(operador + ":" + "\n");
                }
                if (instruccion != "ERROR") {
                    String op1 = obtenerOperando(t.get_Op1(), instruccion);
                    String op2 = obtenerOperando(t.get_Op2(), instruccion);

                    if ((operador == "+") || (operador == "-") || (operador == "*") || (operador == "/")) {
                        registro = getRegistroDisponible();
                        String tipo = t.get_Tipo();
                        if (tipo == "DOUBLE") {
                            instrucciones.append("FLD " + op1 + "\n");
                            instrucciones.append("FLD " + op2 + "\n");
                            instrucciones.append(instruccion + "\n");
                            String vAux = t.set_VA();
                            Simbolo sim = new Simbolo(280, vAux);
                            TablaSimbolos.agregar_sin_chequear(sim);
                            instrucciones.append("FSTP " + vAux + "\n");
                            if (operador == "+")
                                controlar_OverFlowSum();
                            setRegistroDisponible(registro);
                        } else {
                            instrucciones.append("MOV " + registro + ", " + op1 + "\n");
                            instrucciones.append(instruccion + " " + registro + ", " + op2 + "\n");
                            String vAux = t.set_VA();
                            Simbolo sim = new Simbolo(280, vAux);
                            TablaSimbolos.agregar_sin_chequear(sim);
                            if (operador == "-" && tipo == "USHORT")
                                controlar_OverFlowResta();
                            else if (operador == "*")
                                controlar_OverFlowMul(tipo);
                            instrucciones.append("MOV " + vAux + ", " + registro + "\n");
                            setRegistroDisponible(registro);
                        }
                    } else if (operador == "=") {
                        registro = getRegistroDisponible();
                        if (t.get_Tipo() == "DOUBLE") {
                            instrucciones.append("FLD " + op2 + "\n");
                            instrucciones.append("FSTP " + op1 + "\n");
                        } else {
                            instrucciones.append("MOV " + registro + ", " + op2 + "\n");
                            instrucciones.append("MOV " + op1 + ", " + registro + "\n");
                        }
                        setRegistroDisponible(registro);
                    } else if (operador == ">" || operador == ">=" || operador == "<" || operador == "<=") {
                        registro = getRegistroDisponible();
                        instrucciones.append("MOV " + registro + ", " + op1 + "\n");
                        instrucciones.append("CMP " + registro + ", " + op2 + "\n");
                        setRegistroDisponible(registro);
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
                System.out.println(e.getKey());
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
                if (tipo == "DOUBLE")
                    return "FADD";
                else
                    return "ADD";
            case "-":
                if (tipo == "DOUBLE")
                    return "FSUB";
                return "SUB";
            case "*":
                if (tipo == "DOUBLE")
                    return "FMUL";
                else if (tipo == "LONG")
                    return "IMUL";
                else if (tipo == "USHORT")
                    return "MUL";
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
            case "PRINT":
                instrucciones
                        .append("invoke MessageBox, NULL, addr @cadena" + op1 + ", addr @cadena" + op1 + ", MB_OK"
                                + "\n");
                return " ";
            case "UStoL":
                String registro = getRegistroDisponible();
                char segundo = registro.charAt(1);
                if (registro != " ") {
                    instrucciones.append(
                            "MOV " + segundo + "L, " + datos.get_Simbolo(Integer.parseInt(op1)).get_Lex() + "\n");
                    instrucciones.append("MOVSX " + registro + ", " + segundo + "L" + "\n");
                }
                t.set_VA();
                setRegistroDisponible(registro);
                return " ";
            case "UStoD":
                String registro_aux = getRegistroDisponible();
                char segundo_aux = registro_aux.charAt(1);
                if (op1.startsWith("["))
                    op1 = CodIntermedio.get(Integer.parseInt(borrarCorchetes(op1))).get_VA();
                else
                    op1 = "_" + datos.reemplazarPuntos(datos.get_Simbolo(Integer.parseInt(op1)).get_Ambito());
                if (registro_aux != " ") {
                    instrucciones.append(
                            "MOV " + segundo_aux + "L, " + op1 + "\n");
                    instrucciones.append("MOVSX " + registro_aux + ", " + segundo_aux + "L" + "\n");
                }
                instrucciones.append("FLD " + op1 + "\n");
                instrucciones.append("FST " + op1 + "\n");
                t.set_VA();
                setRegistroDisponible(registro_aux);
                return " ";
            case "LtoD":
                if (op1.startsWith("["))
                    op1 = CodIntermedio.get(Integer.parseInt(borrarCorchetes(op1))).get_VA();
                else
                    op1 = "_" + datos.reemplazarPuntos(datos.get_Simbolo(Integer.parseInt(op1)).get_Ambito());
                instrucciones.append("FLD " + op1 + "\n");
                t.set_VA();
                return " ";
            default:
                return " ";
        }
    }
}
