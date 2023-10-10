package compiladores;

import java.util.HashMap;

public class TablaToken {

    private static HashMap<String, Integer> tokens;

    public TablaToken() {
        tokens = new HashMap<String, Integer>();
        tokens.put("+", 43);
        tokens.put(".", 46);
        tokens.put(">", 62);
        tokens.put("<", 60);
        tokens.put("(", 40);
        tokens.put(")", 41);
        tokens.put("{", 123);
        tokens.put("}", 125);
        tokens.put("-", 45);
        tokens.put("/", 47);
        tokens.put("*", 42);
        tokens.put(";", 59);
        tokens.put(",", 44);
        tokens.put(":", 58);
        tokens.put("=", 61);
        tokens.put("ID", 257);
        tokens.put("CTE", 258);
        tokens.put("CTEPOS", 259);
        tokens.put("IF", 260);
        tokens.put("END_IF", 261);
        tokens.put("ELSE", 262);
        tokens.put("PRINT", 263);
        tokens.put("CLASS", 264);
        tokens.put("VOID", 265);
        tokens.put("LONG", 266);// Especifico del grupo
        tokens.put("USHORT", 267);// Especifico del grupo
        tokens.put("DOUBLE", 268);// Especifico del grupo
        tokens.put("DO", 269);// Especifico del grupo
        tokens.put("UNTIL", 270);// Especifico del grupo
        tokens.put("IMPL", 271);// Especifico del grupo
        tokens.put("FOR", 272);// Especifico del grupo
        tokens.put("CADENA", 273);// Especifico del grupo
        tokens.put("RETURN", 274);
        tokens.put(">=", 275);
        tokens.put("<=", 276);
        tokens.put("==", 277);
        tokens.put("+=", 278);// Especifico del grupo
        tokens.put("!!", 279);
    }

    public static Integer getId(String valor) {
        return tokens.get(valor);
    }

}
