package compiladores;

public class Lexico {

    public int estado = 0;
    public int pos = 0;
    public String cod;
    public int linea = 1;

    private static int[][] transiciones = new int[][] {
            // Suponemos el estado 100 como el estado final y -1 estado de error (el error
            // se informa no corta la ejecucion)
            { 0, 0, 1, 1, 9, 100, 100, 100, 100, 100, 100, 10, 100, 2, 5, 4, 4, 6, 7, 100, 1, 7, 14, 16, 1, 1, 1, 0 },
            { 100, 100, 1, 1, 1, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                    100, 100, 1, 1, 1, 100 },
            { 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                    100, 100, 100, 100, 100, 100, 100 },
            { 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                    100, 100, 100, 100, 100, 100, 100 },
            { 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                    100, 100, 100, 100, 100, 100, 100 },
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 100, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1 },
            { 100, 100, 100, 6, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 6, 100, 100, 6,
                    100, 100, 100, 100, 100, 100 },
            { -1, -1, -1, 15, 7, -1, -1, -1, -1, -1, -1, 9, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    100 },
            { 100, 100, 100, 100, 9, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                    100, 100, 100, 100, 100, 100, 100 },
            { 100, 100, 100, 100, 9, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 10, 10,
                    100, 100, 100, 100, 100, 100 },
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, -1, -1, -1, -1, -1, 11, -1, -1, -1, -1, -1, -1,
                    -1, -1 },
            { 100, 100, 100, 100, 11, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                    100, 100, 100, 100, 100, 100, 100 },
            { 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                    100, 13, 100, 100, 100, 100, 100 },
            { 13, 0, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13,
                    13 },
            { 14, -1, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 100, 14, 14,
                    14, 14 },
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 100, 16,
                    -1, -1 },
            { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    100, -1 }
    };

    private static AccionSemantica1 AS1 = new AccionSemantica1();
    private static AccionSemantica2 AS2 = new AccionSemantica2();
    private static AccionSemantica3 AS3 = new AccionSemantica3();
    private static AccionSemantica4 AS4 = new AccionSemantica4();
    private static AccionSemantica5 AS5 = new AccionSemantica5();
    private static AccionSemantica6 AS6 = new AccionSemantica6();
    private static AccionSemantica7 AS7 = new AccionSemantica7();
    private static AccionSemantica8 AS8 = new AccionSemantica8();

    // La accion semántica 8 es error
    private static AccionSemantica[][] Matriz_Acciones = new AccionSemantica[][] {
            { AS7, AS7, AS1, AS1, AS1, AS4, AS4, AS4, AS4, AS4, AS4, AS1, AS4, AS1, AS1, AS8, AS1, AS1, AS1, AS1, AS4,
                    AS1, AS1, AS1, AS1, AS1, AS1, AS1, AS1 },
            { AS3, AS3, AS2, AS2, AS2, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3,
                    AS2, AS3, AS3, AS2, AS2, AS2, AS3, AS3 },
            { AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS5, AS6, AS6, AS6, AS6, AS6, AS6,
                    AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6 },
            { AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS5, AS6, AS6, AS6, AS6, AS6, AS6,
                    AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6 },
            { AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS5, AS6, AS6, AS6, AS6, AS6, AS6,
                    AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6 },
            { AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS5, AS8,
                    AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8 },
            { AS3, AS3, AS3, AS2, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS2, AS3,
                    AS3, AS2, AS3, AS3, AS3, AS3, AS3, AS3 },
            { AS8, AS8, AS8, AS2, AS2, AS8, AS8, AS8, AS8, AS8, AS8, AS2, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8,
                    AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8 },
            { AS6, AS6, AS6, AS6, AS2, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6,
                    AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6 },
            { AS8, AS8, AS8, AS8, AS2, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8,
                    AS2, AS2, AS8, AS8, AS8, AS8, AS8, AS8 },
            { AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS2, AS8, AS8, AS8, AS8, AS8, AS8, AS2,
                    AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8 },
            { AS6, AS6, AS6, AS6, AS2, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6,
                    AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6 },
            { AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6,
                    AS6, AS6, AS2, AS6, AS6, AS6, AS6, AS6 },
            { AS2, AS7, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2,
                    AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2 },
            { AS2, AS8, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2,
                    AS2, AS2, AS2, AS2, AS2, AS2, AS5, AS2 },
            { AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8,
                    AS8, AS8, AS8, AS2, AS8, AS5, AS8, AS8 },
            { AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8,
                    AS8, AS8, AS8, AS8, AS5, AS8, AS8, AS8 }
    };

    public String Leer() {// lee del archivo

    }

    public void ejecutarAS() {// Ejecuta la accion semántica correspondiente

    }

    public Token generarToken() {// Devuelve el token

    }

    // Hay que ver como vamos contando los saltos de linea, mas que nada para ver si
    // hacemos una clase linea o la vamos contando aca mismo

}
