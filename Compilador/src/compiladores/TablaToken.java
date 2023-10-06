package compiladores;

import java.util.HashMap;

public class TablaToken {

    private static HashMap<String, Integer> tokens;

    public TablaToken() {
        tokens = new HashMap<String, Integer>();
        tokens.put("+", 260);
        tokens.put(".", 261);
        tokens.put(">", 262);
        tokens.put("<", 263);
        tokens.put(">=", 264);
        tokens.put("<=", 265);
        tokens.put("(", 266);
        tokens.put(")", 267);
        tokens.put("{", 268);
        tokens.put("}", 269);
        tokens.put("-", 270);
        tokens.put("/", 271);
        tokens.put("*", 272);
        tokens.put(";", 273);
        tokens.put(",", 274);
        tokens.put(":", 275);
        tokens.put("=", 276);
        tokens.put("==", 277);
        tokens.put("+=", 278);// Especifico del grupo
        tokens.put("!!", 279);
        tokens.put("ID", 280);
        tokens.put("IF", 281);
        tokens.put("END_IF", 282);
        tokens.put("ELSE", 283);
        tokens.put("PRINT", 284);
        tokens.put("CLASS", 285);
        tokens.put("VOID", 286);
        tokens.put("LONG", 287);// Especifico del grupo
        tokens.put("USHORT", 288);// Especifico del grupo
        tokens.put("DOUBLE", 289);// Especifico del grupo
        tokens.put("DO", 290);// Especifico del grupo
        tokens.put("UNTIL", 291);// Especifico del grupo
        tokens.put("IMPL", 292);// Especifico del grupo
        tokens.put("FOR", 293);// Especifico del grupo
        tokens.put("CADENA", 294);// Especifico del grupo
        tokens.put("RETURN", 295);
    }

    public static Integer getId(String valor) {
        return tokens.get(valor);
    }

}
