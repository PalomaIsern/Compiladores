package compiladores;

import java.util.HashMap;

public class TablaToken {

    private static HashMap<String, Integer> tokens;

    public TablaToken() {
        tokens = new HashMap<String, Integer>();
        tokens.put("+", 1);
        tokens.put(".", 2);
        tokens.put(">", 3);
        tokens.put("<", 4);
        tokens.put(">=", 5);
        tokens.put("<=", 6);
        tokens.put("(", 7);
        tokens.put(")", 8);
        tokens.put("{", 9);
        tokens.put("}", 10);
        tokens.put("-", 11);
        tokens.put("/", 12);
        tokens.put("*", 13);
        tokens.put(";", 14);
        tokens.put(",", 15);
        tokens.put(":", 16);
        tokens.put("=", 17);
        tokens.put("==", 18);
        tokens.put("+=", 19);// Especifico del grupo
        tokens.put("!!", 20);
        tokens.put("ID", 21);
        tokens.put("CTE", 22);
        tokens.put("IF", 23);
        tokens.put("END_IF", 24);
        tokens.put("ELSE", 25);
        tokens.put("PRINT", 26);
        tokens.put("CLASS", 27);
        tokens.put("VOID", 28);
        tokens.put("LONG", 29);// Especifico del grupo
        tokens.put("USHORT", 30);// Especifico del grupo
        tokens.put("DOUBLE", 31);// Especifico del grupo
        tokens.put("DO", 32);// Especifico del grupo
        tokens.put("UNTIL", 36);// Especifico del grupo
        tokens.put("IMPL", 33);// Especifico del grupo
        tokens.put("FOR", 34);// Especifico del grupo
        tokens.put("CADENA", 35);// Especifico del grupo
    }

    public static Integer getId(String valor) {
        return tokens.get(valor);
    }

}
