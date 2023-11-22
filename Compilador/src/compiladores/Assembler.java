package compiladores;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

public class Assembler {

    private HashMap<Integer, Terceto> CodIntermedio;
    private static StringBuilder codigo = new StringBuilder();
    private TablaSimbolos datos;

    public Assembler(HashMap<Integer, Terceto> ci, TablaSimbolos d) {
        CodIntermedio = new HashMap<Integer, Terceto>(ci);
        datos = d;
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
        codigo.append(datos.getDatosAssembler());
        codigo.append(".code\n");
        // codigo
        codigo.append("START:\n");
        codigo.append("END START");
        generarArchivo();
    }

    public String devolverOperacion(String operador, String tipo) {
        switch (operador) {
            case "+":
                return "add";
            case "-":
                return "sub";
            case "*":
                if (tipo == "DOUBLE" || tipo == "LONG")
                    return "imul";
                else
                    return "mul";
            case "/":
                if (tipo == "DOUBLE" || tipo == "LONG")
                    return "idiv";
                else
                    return "div";
            case "=":
            case "<":
            case "<=":
            case ">":
            case ">=":
            case "==":
            case "!!":
            case "CALL":
            case "BI":
            case "BF":
        }
    }
}
