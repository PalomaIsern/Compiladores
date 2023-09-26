package compiladores;

import java.io.BufferedReader;
import java.io.FileReader;

import accionesSemanticas.AccionSemantica;

public class Lexico {

        private int estado = 0;
        private int pos = 0;
        private String cod;
        private int linea = 1;
        private TablaSimbolos TS = new TablaSimbolos();
        private TablaToken TT = new TablaToken();
        private TablaPR TPR = new TablaPR();

        private static int[][] transiciones = new int[][] {
                        // Suponemos el estado 100 como el estado final y -1 estado de error (el error
                        // se informa no corta la ejecucion)
                        { 0, 0, 1, 1, 9, 100, 100, 100, 100, 100, 100, 10, 100, 2, 5, 4, 4, 6, 7, 100, 1, 7, 14, 16, 1,
                                        1, 1, 0 },
                        { 100, 100, 1, 1, 1, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                                        100, 100,
                                        100, 100, 1, 1, 1, 100 },
                        { 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                                        100, 100,
                                        100, 100, 100, 100, 100, 100, 100 },
                        { 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                                        100, 100,
                                        100, 100, 100, 100, 100, 100, 100 },
                        { 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                                        100, 100,
                                        100, 100, 100, 100, 100, 100, 100 },
                        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 100, -1, -1, -1, -1, -1,
                                        -1, -1, -1,
                                        -1, -1 },
                        { 100, 100, 100, 6, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 6,
                                        100, 100, 6,
                                        100, 100, 100, 100, 100, 100 },
                        { -1, -1, -1, 15, 7, -1, -1, -1, -1, -1, -1, 9, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                                        -1, -1, -1,
                                        100 },
                        { 100, 100, 100, 100, 9, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                                        100, 100,
                                        100, 100, 100, 100, 100, 100, 100 },
                        { 100, 100, 100, 100, 9, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                                        100, 10, 10,
                                        100, 100, 100, 100, 100, 100 },
                        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, -1, -1, -1, -1, -1, 11, -1, -1, -1,
                                        -1, -1, -1,
                                        -1, -1 },
                        { 100, 100, 100, 100, 11, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                                        100, 100,
                                        100, 100, 100, 100, 100, 100, 100 },
                        { 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                                        100, 100,
                                        100, 13, 100, 100, 100, 100, 100 },
                        { 13, 0, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13,
                                        13, 13, 13,
                                        13 },
                        { 14, -1, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14,
                                        100, 14, 14,
                                        14, 14 },
                        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                                        -1, 100, 16,
                                        -1, -1 },
                        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                                        -1, -1, -1,
                                        100, -1 }
        };

        private static accionesSemanticas.AS1 AS1 = new accionesSemanticas.AS1();
        private static accionesSemanticas.AS2 AS2 = new accionesSemanticas.AS2();
        private static accionesSemanticas.AS3 AS3 = new accionesSemanticas.AS3();
        private static accionesSemanticas.AS4 AS4 = new accionesSemanticas.AS4();
        private static accionesSemanticas.AS5 AS5 = new accionesSemanticas.AS5();
        private static accionesSemanticas.AS6 AS6 = new accionesSemanticas.AS6();
        private static accionesSemanticas.AS7 AS7 = new accionesSemanticas.AS7();
        private static accionesSemanticas.AS8 AS8 = new accionesSemanticas.AS8();
        private static accionesSemanticas.AS9 AS9 = new accionesSemanticas.AS9();
        private static accionesSemanticas.AS10 AS10 = new accionesSemanticas.AS10();

        // La accion semántica 8 es error

        private static AccionSemantica[][] Matriz_Acciones = new AccionSemantica[][] {
                        { AS7, AS7, AS1, AS1, AS1, AS4, AS4, AS4, AS4, AS4, AS4, AS1, AS4, AS1, AS1, AS8, AS1, AS1, AS1,
                                        AS1, AS4,
                                        AS1, AS1, AS1, AS1, AS1, AS1, AS1, AS1 },
                        { AS3, AS3, AS2, AS2, AS2, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3,
                                        AS3, AS3,
                                        AS2, AS3, AS3, AS2, AS2, AS2, AS3, AS3 },
                        { AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS5, AS6, AS6, AS6, AS6,
                                        AS6, AS6,
                                        AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6 },
                        { AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS5, AS6, AS6, AS6, AS6,
                                        AS6, AS6,
                                        AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6 },
                        { AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS5, AS6, AS6, AS6, AS6,
                                        AS6, AS6,
                                        AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6 },
                        { AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8,
                                        AS5, AS8,
                                        AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8 },
                        { AS3, AS3, AS3, AS2, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3,
                                        AS2, AS3,
                                        AS3, AS2, AS3, AS3, AS3, AS3, AS3, AS3 },
                        { AS8, AS8, AS8, AS2, AS2, AS8, AS8, AS8, AS8, AS8, AS8, AS2, AS8, AS8, AS8, AS8, AS8, AS8, AS8,
                                        AS8, AS8,
                                        AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8 },
                        { AS6, AS6, AS6, AS6, AS2, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6,
                                        AS6, AS6,
                                        AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6 },
                        { AS8, AS8, AS8, AS8, AS2, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8,
                                        AS8, AS8,
                                        AS2, AS2, AS8, AS8, AS8, AS8, AS8, AS8 },
                        { AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS2, AS8, AS8, AS8, AS8, AS8,
                                        AS8, AS2,
                                        AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8 },
                        { AS6, AS6, AS6, AS6, AS2, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6,
                                        AS6, AS6,
                                        AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6 },
                        { AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6, AS6,
                                        AS6, AS6,
                                        AS6, AS6, AS2, AS6, AS6, AS6, AS6, AS6 },
                        { AS2, AS7, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2,
                                        AS2, AS2,
                                        AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2 },
                        { AS2, AS8, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2,
                                        AS2, AS2,
                                        AS2, AS2, AS2, AS2, AS2, AS2, AS5, AS2 },
                        { AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8,
                                        AS8, AS8,
                                        AS8, AS8, AS8, AS2, AS8, AS5, AS8, AS8 },
                        { AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8, AS8,
                                        AS8, AS8,
                                        AS8, AS8, AS8, AS8, AS5, AS8, AS8, AS8 }
        };

        public void Leer(int numero_linea) {// lee del archivo
                // Scanner sc = new Scanner(System.in);
                // System.out.println("Ingrese el nombre del archivo que desea leer");
                // String narchivo = sc.nextLine();
                String nombreArchivo = "C:\\Users\\Paloma\\Trabajo Compiladores\\Compiladores\\Compilador\\Prueba.txt";
                FileReader archivo = new FileReader(nombreArchivo);
                BufferedReader lector = new BufferedReader(archivo);
                String linea;
                while ((linea = lector.readLine()) != null) {
                        // Procesa cada línea del archivo
                        System.out.println("Línea " + numero_linea + ": " + linea);
                        // Divide la línea en palabras usando espacios en blanco como delimitador
                        String[] palabras = linea.split("");
                        for (String palabra : palabras) {
                                System.out.println("Palabra: " + palabra);
                        }
                        numero_linea = numero_linea + 1;
                }
                lector.close();
        }

        public void ejecutarAS() {// Ejecuta la accion semántica correspondiente

        }

        public Token generarToken() {// Devuelve el token

                return null;
        }

}
