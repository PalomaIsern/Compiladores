package compiladores;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
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
    private ArrayList<String> operadores = new ArrayList<>(Arrays.asList("+", "-", "*", "/", "=", "<", "<=", ">", ">=",
            "==", "!!", "CALL", "CALLMetodoClase", "BI", "BF", "PRINT", "UStoL", "UStoD", "LtoD", "RETURN"));
    private Stack<String> pilaFunc = new Stack<>();

    public Assembler(HashMap<Integer, Terceto> ci, TablaSimbolos d) {
        CodIntermedio = new HashMap<Integer, Terceto>(ci);
        datos = d;
        registros.put("edx", false);
        registros.put("ebx", false);
        registros.put("ecx", false);
        registros.put("eax", false);
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

        HashMap<String, Integer> func = new HashMap<>(datos.getFuncionesAssembler());
        generarInstrucciones(instrucciones, func);

        codigo.append(datos.getDatosAssembler());
        codigo.append(".code\n \n");
        generarCodigoFunciones(codigo, func);
        // codigo
        codigo.append("START:\n\n");
        codigo.append(instrucciones);
        codigo.append("\n");
        codigo.append("OverFlowMul: \n");
        codigo
                .append("invoke  MessageBox, NULL, ADDR OverFlowMultiplicacion, ADDR OverFlowMultiplicacion, MB_OK \n");
        codigo.append("invoke ExitProcess, 0" + "\n\n");
        codigo.append("OverFlowResta: \n");
        codigo.append("invoke  MessageBox, NULL, ADDR OverFlowResta, ADDR OverFlowResta, MB_OK \n");
        codigo.append("invoke ExitProcess, 0" + "\n\n");
        codigo.append("OverFlowSuma: \n");
        codigo.append("invoke  MessageBox, NULL, ADDR OverFlowSuma, ADDR OverFlowSuma, MB_OK \n");
        codigo.append("invoke ExitProcess, 0" + "\n\n");
        codigo.append("END START");
        generarArchivo();
    }

    public void agregarFuncion(StringBuilder codigo, String op, int ref1, int ref2) {
        if ((ref2 - ref1) > 1) {
            Terceto t = CodIntermedio.get(ref1);
            codigo.append(reemplazarPuntos(t.get_Operador()) + ": \n");
            for (int i = ref1 + 1; i < ref2; i++) {
                t = CodIntermedio.get(i);
                generarInstruccion(codigo, t);
            }
            codigo.append("ret \n \n");
        }
    }

    private String reemplazarPuntos(String palabra) {
        return palabra.replace(":", "$");
    }

    public void generarCodigoFunciones(StringBuilder codigo, HashMap<String, Integer> funciones) {
        String operador, tope;
        for (HashMap.Entry<Integer, Terceto> i : CodIntermedio.entrySet()) {
            operador = reemplazarPuntos(i.getValue().get_Operador());
            if (funciones.get(operador) != null) {
                if (!pilaFunc.isEmpty()) {
                    tope = pilaFunc.peek();
                    if (tope.equals(operador)) {
                        agregarFuncion(codigo, operador, funciones.get(operador), i.getKey());
                        pilaFunc.pop();
                    } else {
                        funciones.put(operador, i.getKey());
                        pilaFunc.push(operador);
                    }
                } else {
                    funciones.put(operador, i.getKey());
                    pilaFunc.push(operador);
                }
            }
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

    public void controlar_OverFlowMul(StringBuilder cod, String tipo) {
        if (tipo == "LONG")
            cod.append("JO OverFlowMul \n");
        else
            cod.append("JC OverFlowMul \n");
    }

    public void controlar_OverFlowResta(StringBuilder cod) {
        cod.append("JC OverFlowResta \n");
    }

    public void controlar_OverFlowSum(StringBuilder cod) {
        String registro = getRegistroDisponible();
        char segundo = registro.charAt(1);
        cod.append("FSTSW aux_sumaDouble \n");
        cod.append("MOV " + segundo + "x, aux_sumaDouble" + "\n");
        cod.append("SAHF " + "\n");
        cod.append("JO OverFlowSuma \n");
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
                return "_" + reemplazarPuntos(s.get_Ambito());
            else if (op != "-" && s.get_Uso() == "Constante" || op != "-" && s.get_Uso() == "ConstantePositiva"
                    || op != "-" && s.get_Uso() == "Constante negativa")
                return s.get_Lex();
        }
        return op;
    }

    private boolean operadorValido(String op) {
        return operadores.contains(op) || op.contains("Label");
    }

    public void generarInstrucciones(StringBuilder instrucc, HashMap<String, Integer> funciones) {
        Boolean dentroFuncion = false;
        String tope;
        for (HashMap.Entry<Integer, Terceto> i : CodIntermedio.entrySet()) {
            Terceto t = i.getValue();
            String op = t.get_Operador();
            if (seguir) {
                if (operadorValido(op) && !dentroFuncion) {
                    generarInstruccion(instrucc, i.getValue());
                } else {
                    op = reemplazarPuntos(op);
                    if (funciones.get(op) != null) {
                        if (!pilaFunc.isEmpty()) {
                            tope = pilaFunc.peek();
                            if (tope.equals(op)) {
                                pilaFunc.pop();
                                if (pilaFunc.isEmpty()) {
                                    dentroFuncion = false;
                                }
                            } else {
                                pilaFunc.push(op);
                                dentroFuncion = true;
                            }
                        } else {
                            pilaFunc.push(op);
                            dentroFuncion = true;
                        }
                    }
                }
            }
        }
    }

    public void generarInstruccion(StringBuilder cod, Terceto t) {
        String registro = " ";
        String instruccion = devolverOperacion(cod, t);
        String operador = t.get_Operador();
        if (operador.startsWith("Label")) {
            cod.append(operador + ":" + "\n");
        }
        if (instruccion != "ERROR") {
            String op1 = obtenerOperando(t.get_Op1(), instruccion);
            String op2 = obtenerOperando(t.get_Op2(), instruccion);

            if ((operador == "+") || (operador == "-") || (operador == "*") || (operador == "/")) {
                registro = getRegistroDisponible();
                String tipo = t.get_Tipo();
                if (tipo == "DOUBLE") {
                    cod.append("FLD " + op1 + "\n");
                    cod.append("FLD " + op2 + "\n");
                    cod.append(instruccion + "\n");
                    String vAux = setear_VA(t);
                    cod.append("FSTP " + vAux + "\n");
                    if (operador == "+")
                        controlar_OverFlowSum(cod);
                    setRegistroDisponible(registro);
                } else {
                    cod.append("MOV " + registro + ", " + op1 + "\n");
                    cod.append(instruccion + " " + registro + ", " + op2 + "\n");
                    String vAux = setear_VA(t);
                    if (operador == "-" && tipo == "USHORT")
                        controlar_OverFlowResta(cod);
                    else if (operador == "*")
                        controlar_OverFlowMul(cod, tipo);
                    cod.append("MOV " + vAux + ", " + registro + "\n");
                    setRegistroDisponible(registro);
                }
            } else if (operador == "=") {
                registro = getRegistroDisponible();
                if (t.get_Tipo() == "DOUBLE") {
                    cod.append("FLD " + op2 + "\n");
                    cod.append("FSTP " + op1 + "\n");
                } else {
                    cod.append("MOV " + registro + ", " + op2 + "\n");
                    cod.append("MOV " + op1 + ", " + registro + "\n");
                }
                setRegistroDisponible(registro);
            } else if (operador == ">" || operador == ">=" || operador == "<" || operador == "<=") {
                registro = getRegistroDisponible();
                cod.append("MOV " + registro + ", " + op1 + "\n");
                cod.append("CMP " + registro + ", " + op2 + "\n");
                setRegistroDisponible(registro);
            } else if (instruccion == "JMP") {
                String destino = borrarCorchetes(op2);
                cod.append("JMP " + "Label" + destino + "\n");
            } else if (instruccion == "BF") {
                generarSaltoCondicional(cod, t);
            } else if (instruccion == "CALL") {
                cod.append("CALL " + op1.substring(1) + "\n");
            } else if (instruccion == "CALLMetodoClase") {
                String metodo = datos.get_Simbolo(Integer.parseInt(t.get_Op1())).get_Ambito();
                cod.append("CALL " + metodo + "\n");
            }
        } else {
            System.out.println("La ejecución ha sido interrumpida porque se ha detectado un error");
            seguir = false;
        }
    }

    public void generarSaltoCondicional(StringBuilder cod, Terceto t) {
        String nro = borrarCorchetes(t.get_Op2());
        if (ultimoComparador == ">")
            cod.append("JLE Label" + nro + "\n");
        else if (ultimoComparador == ">=")
            cod.append("JL Label" + nro + "\n");
        else if (ultimoComparador == "<")
            cod.append("JGE Label" + nro + "\n");
        else if (ultimoComparador == "<=")
            cod.append("JG Label" + nro + "\n");
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

    public String setear_VA(Terceto t) {
        String vAux = t.set_VA();
        Simbolo sim = new Simbolo(280, vAux);
        TablaSimbolos.agregar_sin_chequear(sim);
        return vAux;
    }

    public String devolverOperacion(StringBuilder cod, Terceto t) {
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
                cod
                        .append("invoke MessageBox, NULL, addr @cadena" + op1 + ", addr @cadena" + op1 + ", MB_OK"
                                + "\n");
                return " ";
            case "UStoL":
                String registro = getRegistroDisponible();
                char segundo = registro.charAt(1);
                if (registro != " ") {
                    cod.append(
                            "MOV " + segundo + "l, " + datos.get_Simbolo(Integer.parseInt(op1)).get_Lex() + "\n");
                    cod.append("MOVSX " + registro + ", " + segundo + "l" + "\n");
                }
                setear_VA(t);
                setRegistroDisponible(registro);
                return " ";
            case "UStoD":
                String registro_aux = getRegistroDisponible();
                char segundo_aux = registro_aux.charAt(1);
                if (op1.startsWith("["))
                    op1 = CodIntermedio.get(Integer.parseInt(borrarCorchetes(op1))).get_VA();
                else
                    op1 = "_" + reemplazarPuntos(datos.get_Simbolo(Integer.parseInt(op1)).get_Ambito());
                if (registro_aux != " ") {
                    cod.append(
                            "MOV " + segundo_aux + "l, " + op1 + "\n");
                    cod.append("MOVSX " + registro_aux + ", " + segundo_aux + "l" + "\n");
                }
                cod.append("FLD " + op1 + "\n");
                cod.append("FST " + op1 + "\n");
                setear_VA(t);
                setRegistroDisponible(registro_aux);
                return " ";
            case "LtoD":
                if (op1.startsWith("["))
                    op1 = CodIntermedio.get(Integer.parseInt(borrarCorchetes(op1))).get_VA();
                else
                    op1 = "_" + reemplazarPuntos(datos.get_Simbolo(Integer.parseInt(op1)).get_Ambito());
                cod.append("FLD " + op1 + "\n");
                setear_VA(t);
                return " ";
            default:
                return " ";
        }
    }
}
