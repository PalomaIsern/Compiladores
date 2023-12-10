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
    private static StringBuilder sbFunciones = new StringBuilder();
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
        codigo.append("OverFlowMultiplicacion db \"Overflow en multiplicacion de enteros\"" + "\n");
        codigo.append("OverFlowResta db \"Resultado negativo en resta de enteros sin signo\"" + "\n");
        codigo.append("OverFlowSuma db \"Overflow en suma de punto flotante\"" + "\n");

        HashMap<String, ArrayList<Integer>> func = new HashMap<>(datos.getFuncionesAssembler());
        determinarLimites(func);

        generarInstrucciones(instrucciones, func);

        generarCodigoFunciones(sbFunciones, func);

        // imprimirLimites(func);

        codigo.append(datos.getDatosAssembler());
        codigo.append("@aux_sumaDouble dw ? \n  \n");
        codigo.append(".code\n \n");
        codigo.append(sbFunciones);
        // codigo
        codigo.append("START:\n\n");
        codigo.append(instrucciones);
        codigo.append("\n");
        codigo.append("JMP fin \n");
        codigo.append("OFM: \n");
        codigo
                .append("invoke  MessageBox, NULL, ADDR OverFlowMultiplicacion, ADDR OverFlowMultiplicacion, MB_OK \n");
        codigo.append("invoke ExitProcess, 0" + "\n\n");
        codigo.append("OFR: \n");
        codigo.append("invoke  MessageBox, NULL, ADDR OverFlowResta, ADDR OverFlowResta, MB_OK \n");
        codigo.append("invoke ExitProcess, 0" + "\n\n");
        codigo.append("OFS: \n");
        codigo.append("invoke  MessageBox, NULL, ADDR OverFlowSuma, ADDR OverFlowSuma, MB_OK \n");
        codigo.append("invoke ExitProcess, 0" + "\n\n");
        codigo.append("fin: \n");
        codigo.append("END START \n");
        codigo.append("invoke ExitProcess, 0" + "\n\n");
        generarArchivo();
        // imprimirCodigoIntermedio();
    }

    public void agregarFuncion(StringBuilder codigo, String op, int ref1, int ref2) {
        if ((ref2 - ref1) > 1) {
            Terceto t = CodIntermedio.get(ref1);
            String nombreFunc = reemplazarPuntos(t.get_Operador());
            codigo.append(nombreFunc + ": \n");
            for (int i = ref1 + 1; i < ref2; i++) {
                t = CodIntermedio.get(i);
                String aux = reemplazarPuntos(t.get_Operador());
                if (!aux.equals(nombreFunc))
                    generarInstruccion(codigo, t);
            }
            codigo.append("ret \n \n");
        }
    }

    private String reemplazarPuntos(String palabra) {
        return palabra.replace(":", "$");
    }

    private void imprimirLimites(HashMap<String, ArrayList<Integer>> func) {
        for (HashMap.Entry<String, ArrayList<Integer>> entry : func.entrySet()) {
            String clave = entry.getKey();
            ArrayList<Integer> lista = entry.getValue();

            System.out.println("Clave: " + clave);
            System.out.println("Lista de valores:");

            for (Integer valor : lista) {
                System.out.println("  " + valor);
            }

            System.out.println();
        }
    }

    private void determinarLimites(HashMap<String, ArrayList<Integer>> func) {
        String op, tope;
        for (HashMap.Entry<Integer, Terceto> i : CodIntermedio.entrySet()) {
            op = i.getValue().get_Operador();
            if (!operadorValido(op)) {
                op = reemplazarPuntos(op);
                if (!pilaFunc.isEmpty()) {
                    tope = pilaFunc.peek();
                    if (tope.equals(op)) {
                        ArrayList<Integer> aux = func.get(op);
                        aux.add(i.getKey());
                        func.put(op, aux);
                        pilaFunc.pop();
                    } else {
                        ArrayList<Integer> aux = func.get(op);
                        if (aux.size() > 0) {
                            aux.remove(0);
                            aux.remove(1);
                            aux.add(i.getKey());
                        } else {
                            aux.add(i.getKey());
                        }
                        func.put(op, aux);
                        pilaFunc.push(op);
                    }
                } else {
                    ArrayList<Integer> aux = func.get(op);
                    if (aux.size() > 0) {
                        aux.remove(0);
                        aux.remove(0);
                        aux.add(i.getKey());
                    } else {
                        aux.add(i.getKey());
                    }
                    func.put(op, aux);
                    pilaFunc.push(op);
                }
            }
        }
    }

    public void generarCodigoFunciones(StringBuilder codigo, HashMap<String, ArrayList<Integer>> funciones) {
        for (HashMap.Entry<String, ArrayList<Integer>> i : funciones.entrySet()) {
            String funcion = i.getKey();
            codigo.append(funcion + ": \n");
            ArrayList<Integer> tercetos = i.getValue();
            int index = tercetos.get(0);
            while (index < tercetos.get(1)) {
                Terceto t = CodIntermedio.get(index);
                String op = reemplazarPuntos(t.get_Operador());
                if (!operadorValido(op) && !op.equals(funcion)) {
                    index = funciones.get(op).get(1) + 1;
                    codigo.append("CALL " + op + "\n");
                } else {
                    generarInstruccion(codigo, t);
                    index++;
                }
            }
            codigo.append("ret \n \n");
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
            System.out.print("Referencia: " + i.getKey() + ", Terceto: (" + i.getValue().get_Operador() + " , " + j
                    + " , " + s + ")" + " Tipo: " + i.getValue().get_Tipo() + " VA: " + i.getValue().get_VA());
            if (i.getValue().get_VA() != " ")
                System.out.println(" tipo VA " + datos.get_Simbolo(datos.pertenece(i.getValue().get_VA())).get_Tipo());
            else
                System.out.println("");
        }
    }

    public String borrarCorchetes(String p) {
        if (p.startsWith(("[")))
            return p.substring(1, p.length() - 1);
        return p;
    }

    public void controlar_OverFlowMul(StringBuilder cod, String tipo) {
        if (tipo == "LONG")
            cod.append("JO OFM \n");
        else
            cod.append("JC OFM \n");
    }

    public void controlar_OverFlowResta(StringBuilder cod) {
        cod.append("JC OFR \n");
    }

    public void controlar_OverFlowSum(StringBuilder cod) {
        String registro = getRegistroDisponible();
        char segundo = registro.charAt(1);
        cod.append("FSTSW @aux_sumaDouble \n");
        cod.append("MOV " + segundo + "x, @aux_sumaDouble" + "\n");
        cod.append("SAHF " + "\n");
        cod.append("JO OFS \n");
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
            else if (op != "-" && s.get_Tipo() == "DOUBLE" && s.get_Uso() == "Constante")
                op = "@cte" + Integer.parseInt(op);
            else if (op != "-" && s.get_Tipo() == "DOUBLE" && s.get_Uso() == "Constante negativa")
                op = "@cte" + Integer.parseInt(op);
            else if (op != "-" && s.get_Uso() == "Constante" || op != "-" && s.get_Uso() == "ConstantePositiva"
                    || op != "-" && s.get_Uso() == "Constante negativa")
                return s.get_Lex();
        }
        return op;
    }

    private boolean operadorValido(String op) {
        return operadores.contains(op) || op.contains("Label");
    }

    public void generarInstrucciones(StringBuilder instrucc, HashMap<String, ArrayList<Integer>> funciones) {
        int i = 0;
        while (i < CodIntermedio.size()) {
            Terceto t = CodIntermedio.get(i);
            String op = t.get_Operador();
            if (seguir) {
                if (operadorValido(op)) {
                    generarInstruccion(instrucc, t);
                    i++;
                } else {
                    op = reemplazarPuntos(op);
                    ArrayList<Integer> aux = funciones.get(op);
                    if (aux.get(0) == i)
                        i = aux.get(1) + 1;
                    else
                        i = i + 2;
                }
            }
        }
    }

    public void generarInstruccion(StringBuilder cod, Terceto t) {
        String registro = " ";
        String instruccion = devolverOperacion(cod, t);
        String operador = t.get_Operador();
        String tipo = t.get_Tipo();
        String tipoOp1;
        String reg;
        if (operador.startsWith("Label")) {
            cod.append(operador + ":" + "\n");
        }
        if (instruccion != "ERROR") {
            String op1 = obtenerOperando(t.get_Op1(), instruccion);
            String op2 = obtenerOperando(t.get_Op2(), instruccion);

            if (tipo == "-") {
                if (op1.startsWith("_") || op1.startsWith("@"))
                    tipo = datos.get_Simbolo(Integer.parseInt(t.get_Op1())).get_Tipo();
            }
            if ((operador == "+") || (operador == "-") || (operador == "*") || (operador == "/")) {
                registro = getRegistroDisponible();
                char segundo = registro.charAt(1);
                if (tipo == "DOUBLE") {
                    cod.append("FLD " + op1 + "\n");
                    cod.append("FLD " + op2 + "\n");
                    cod.append(instruccion + "\n");
                    String vAux = setear_VA(t, "DOUBLE");
                    cod.append("FSTP " + vAux + "\n");
                    if (operador == "+")
                        controlar_OverFlowSum(cod);
                    setRegistroDisponible(registro);
                } else {
                    if (tipo == "USHORT")
                        reg = String.valueOf(segundo) + "l";
                    else
                        reg = registro;
                    cod.append("MOV " + reg + ", " + op1 + "\n");
                    cod.append(instruccion + " " + reg + ", " + op2 + "\n");
                    String vAux;
                    vAux = setear_VA(t, tipo);
                    if (operador == "-" && tipo == "USHORT")
                        controlar_OverFlowResta(cod);
                    else if (operador == "*")
                        controlar_OverFlowMul(cod, tipo);
                    cod.append("MOV " + vAux + ", " + reg + "\n");
                    setRegistroDisponible(registro);
                }
            } else if (operador == "=") {
                registro = getRegistroDisponible();
                char segundo = registro.charAt(1);
                if (tipo == "DOUBLE") {
                    cod.append("FLD " + op2 + "\n");
                    cod.append("FSTP " + op1 + "\n");
                } else {
                    if (tipo == "USHORT")
                        reg = String.valueOf(segundo) + "l";
                    else
                        reg = registro;
                    cod.append("MOV " + reg + ", " + op2 + "\n");
                    cod.append("MOV " + op1 + ", " + reg + "\n");
                }
                setRegistroDisponible(registro);
            } else if (operador == ">" || operador == ">=" || operador == "<" || operador == "<=" || operador == "=="
                    || operador == "!!") {
                registro = getRegistroDisponible();
                if (tipo == "DOUBLE") {
                    cod.append("FILD " + op1 + "\n");
                    cod.append("FILD " + op2 + "\n");
                    cod.append("FCOM \n");
                } else {
                    String regAux = registro;
                    if (tipo.equals("USHORT"))
                        regAux = registro.charAt(1) + "l";
                    cod.append("MOV " + regAux + ", " + op1 + "\n");
                    cod.append("CMP " + regAux + ", " + op2 + "\n");
                }
                setRegistroDisponible(registro);
            } else if (instruccion == "JMP") {
                String destino = borrarCorchetes(op2);
                cod.append("JMP " + "Label" + destino + "\n");
            } else if (instruccion == "BF") {
                generarSaltoCondicional(cod, t);
            } else if (instruccion == "CALL") {
                cod.append("CALL " + reemplazarPuntos(op1.substring(1)) + "\n");
            } else if (instruccion == "CALLMetodoClase") {
                String metodo = datos.get_Simbolo(Integer.parseInt(t.get_Op1())).get_Ambito();
                cod.append("CALL " + reemplazarPuntos(metodo) + "\n");
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
        else if (ultimoComparador == "==")
            cod.append("JNE Label" + nro + "\n");
        else if (ultimoComparador == "!!")
            cod.append("JE Label" + nro + "\n");

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

    public String setear_VA(Terceto t, String tipo) {
        String vAux = t.set_VA();
        Simbolo sim = new Simbolo(280, vAux);
        sim.set_Tipo(tipo);
        TablaSimbolos.agregar_sin_chequear(sim);
        return vAux;
    }

    public String devolverOperacion(StringBuilder cod, Terceto t) {
        String operador = t.get_Operador();
        String tipo = t.get_Tipo();
        String op1 = t.get_Op1();
        String op2 = t.get_Op2();
        String uso1, uso2 = "-";
        if (tipo == "-") {
            if (op1 != "-" && !op1.contains("["))
                tipo = datos.get_Simbolo(Integer.parseInt(t.get_Op1())).get_Tipo();
        }
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
                ultimoComparador = "==";
                return "JNE";
            case "!!":
                ultimoComparador = "!!";
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
                op1 = "_" + reemplazarPuntos(datos.get_Simbolo(Integer.parseInt(op1)).get_Ambito());
                if (registro != " ") {
                    cod.append(
                            "MOV " + segundo + "l, " + op1 + "\n");
                    cod.append("MOVSX " + registro + ", " + segundo + "l" + "\n");
                }
                setear_VA(t, "LONG");
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
                cod.append("FILD " + "dword ptr [" + op1 + "] \n");
                cod.append("FSTP " + "qword ptr [" + op1 + "] \n");
                setear_VA(t, "DOUBLE");
                setRegistroDisponible(registro_aux);
                return " ";
            case "LtoD":
                if (op1.startsWith("["))
                    op1 = CodIntermedio.get(Integer.parseInt(borrarCorchetes(op1))).get_VA();
                else {
                    String uso = datos.get_Simbolo(Integer.parseInt(op1)).get_Uso();
                    if (uso != "Constante" && uso != "ConstantePositiva" && uso != "Constante negativa")
                        op1 = "_" + reemplazarPuntos(datos.get_Simbolo(Integer.parseInt(op1)).get_Ambito());
                    else if (uso == "Constante negativa")
                        op1 = "@cteneg" + Integer.parseInt(op1);
                    else if (uso == "Constante")
                        op1 = "@cte" + Integer.parseInt(op1);
                    else
                        op1 = datos.get_Simbolo(Integer.parseInt(op1)).get_Lex();
                }
                cod.append("FILD " + "dword ptr [" + op1 + "] \n");
                setear_VA(t, "DOUBLE");
                return " ";
            default:
                return " ";
        }
    }
}
