package compiladores;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import accionesSemanticas.AccionSemantica;

public class Lexico {

        private static int estado = 0;
        private int pos = 0;
        private String cod;
        private static int linea = 1;
        private TablaSimbolos TS = new TablaSimbolos();
        private TablaToken TT = new TablaToken();
        private TablaPR TPR = new TablaPR();
        private static boolean volverALeer = false;

        private static int[][] transiciones = new int[][] {
                        // Suponemos el estado 100 como el estado final y -1 estado de error (el error
                        // se informa no corta la ejecucion)
                        { 0, 0, 1, 1, 9, 100, 100, 100, 100, 100, 100, 10, 100, 2, 5, 4, 4, 6, 7, 100, 1, 7, 14, 16, 1,
                                        1, 1, 0 },
                        { 100, 100, 1, 1, 1, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                                        100, 100, 100, 100, 1, 1, 1, 100 },
                        { 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                                        100, 100, 100, 100, 100, 100, 100, 100, 100 },
                        { 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                                        100, 100, 100, 100, 100, 100, 100, 100, 100 },
                        { 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                                        100, 100, 100, 100, 100, 100, 100, 100, 100 },
                        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 100, -1, -1, -1, -1, -1,
                                        -1, -1, -1, -1, -1 },
                        { 100, 100, 100, 6, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 6,
                                        100, 100, 6, 100, 100, 100, 100, 100, 100 },
                        { -1, -1, -1, 15, 7, -1, -1, -1, -1, -1, -1, 9, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                                        -1, -1, -1, 100 },
                        { 100, 100, 100, 100, 9, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                                        100, 100, 100, 100, 100, 100, 100, 100, 100 },
                        { 100, 100, 100, 100, 9, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                                        100, 10, 10, 100, 100, 100, 100, 100, 100 },
                        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, -1, -1, -1, -1, -1, 11, -1, -1, -1,
                                        -1, -1, -1, -1, -1 },
                        { 100, 100, 100, 100, 11, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                                        100, 100, 100, 100, 100, 100, 100, 100, 100 },
                        { 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                                        100, 100, 100, 13, 100, 100, 100, 100, 100 },
                        { 13, 0, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13,
                                        13, 13, 13, 13 },
                        { 14, -1, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14,
                                        100, 14, 14, 14, 14 },
                        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                                        -1, 100, 16, -1, -1 },
                        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                                        -1, -1, -1, 100, -1 }
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
        private static accionesSemanticas.AS11 AS11 = new accionesSemanticas.AS11();

        // La accion semántica 8 es error

        private static AccionSemantica[][] Matriz_Acciones = new AccionSemantica[][] {
                        // bl,tab nl l _ d / { } ( ) , . ; + = : < > ! L - "d" "D" * # "u" "s" "l" otro
                        { AS8, AS8, AS1, AS1, AS1, AS5, AS5, AS5, AS5, AS5, AS5, AS1, AS5, AS1, AS1, AS8, AS1, AS1, AS1,
                                        AS1, AS5, AS1, AS1, AS1, AS1, AS1, AS1, AS1, AS1 }, // Estado 0
                        { AS3, AS3, AS2, AS2, AS2, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3, AS3,
                                        AS3, AS3, AS2, AS3, AS3, AS3, AS2, AS2, AS2, AS3 }, // Estado 1
                        { AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS6, AS7, AS7, AS7, AS7,
                                        AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7 }, // Estado 2
                        { AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS6, AS7, AS7, AS7, AS7,
                                        AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7 }, // Estado 3
                        { AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS6, AS7, AS7, AS7, AS7,
                                        AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7 }, // Estado 4
                        { AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11,
                                        AS11, AS11, AS11, AS6,
                                        AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11 }, // Estado 5
                        { AS4, AS4, AS4, AS2, AS4, AS4, AS4, AS4, AS4, AS4, AS4, AS4, AS4, AS4, AS4, AS4, AS4, AS4, AS4,
                                        AS2, AS4, AS4, AS2, AS4, AS4, AS4, AS4, AS4, AS4 }, // Estado 6
                        { AS11, AS11, AS11, AS2, AS2, AS11, AS11, AS11, AS11, AS11, AS11, AS2, AS11, AS11, AS11, AS11,
                                        AS11, AS11, AS11,
                                        AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11 }, // Estado 7
                        { AS7, AS7, AS7, AS7, AS2, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7,
                                        AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7 }, // Estado 8
                        { AS10, AS10, AS10, AS10, AS2, AS10, AS10, AS10, AS10, AS10, AS10, AS10, AS10, AS10, AS10, AS10,
                                        AS10, AS10, AS10, AS10, AS10, AS2, AS2, AS10, AS10, AS10, AS10, AS10, AS10 }, // Estado
                                                                                                                      // 9
                        { AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS2, AS11, AS11,
                                        AS11, AS11, AS11,
                                        AS11, AS2, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11 }, // Estado 10
                        { AS10, AS10, AS10, AS10, AS2, AS10, AS10, AS10, AS10, AS10, AS10, AS10, AS10, AS10, AS10, AS10,
                                        AS10, AS10, AS10, AS10, AS10, AS10, AS10, AS10, AS10, AS10, AS10, AS10, AS10 }, // Estado
                                                                                                                        // 11
                        { AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7, AS7,
                                        AS7, AS7, AS7, AS7, AS8, AS7, AS7, AS7, AS7, AS7 }, // Estado 12
                        { AS2, AS8, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2,
                                        AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2 }, // Estado 13
                        { AS2, AS6, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2, AS2,
                                        AS2, AS2, AS2, AS2, AS2, AS6, AS2, AS2, AS2, AS2 }, // Estado 14
                        { AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11,
                                        AS11, AS11, AS11, AS11,
                                        AS11, AS11, AS11, AS11, AS11, AS11, AS2, AS11, AS9, AS11 }, // Estado 15
                        { AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS11,
                                        AS11, AS11, AS11, AS11,
                                        AS11, AS11, AS11, AS11, AS11, AS11, AS11, AS9, AS11, AS11 }// Estado 16
        };

        public int nuevoEstado(int estado, char caracter) {
                int columna = 0;
                switch (caracter) {
                        case ' ':
                        case '\t':
                                columna = 0;
                                break;
                        case '\n':
                                columna = 1;
                                break;
                        case '_':
                                columna = 3;
                        case '/':
                                columna = 5;
                        case '{':
                                columna = 6;
                        case '}':
                                columna = 7;
                        case '(':
                                columna = 8;
                        case ')':
                                columna = 9;
                        case ',':
                                columna = 10;
                        case '.':
                                columna = 11;
                        case ';':
                                columna = 12;
                        case '+':
                                columna = 13;
                        case '=':
                                columna = 14;
                        case ':':
                                columna = 15;
                        case '<':
                                columna = 16;
                        case '>':
                                columna = 17;
                        case '!':
                                columna = 18;
                        case '-':
                                columna = 20;
                        case 'd':
                                columna = 21;
                        case 'D':
                                columna = 22;
                        case '*':
                                columna = 23;
                        case '#':
                                columna = 24;
                        case 'u':
                                columna = 25;
                        case 's':
                                columna = 26;
                        case 'l':
                                columna = 27;
                }
                if (Character.isLowerCase(caracter)) {
                        columna = 2;
                } else if (Character.isDigit(caracter)) {
                        columna = 4;
                } else if (Character.isUpperCase(caracter)) {
                        columna = 19;
                } else {
                        columna = 28;
                }
                return transiciones[estado][columna];
        }

        public void Leer(int numero_linea) throws IOException {// lee del archivo
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
                        String[] caracteres = linea.split("");
                        int i = 0;
                        int siguienteEstado = 0;
                        while (i < caracteres.length) {
                                System.out.println("Caracter: " + caracteres[i]);
                                siguienteEstado = nuevoEstado(estado, caracteres[i]);

                                // cambiar de estado
                                // ejecutar AS
                                if (volverALeer) {
                                        i--;
                                        volverALeer = false;
                                }
                                i++;
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

        public static void setVolverALeer(boolean value) {
                volverALeer = value;
        }

        public boolean getVolverALeer() {
                return volverALeer;
        }

        public static int getEstadoActual() {
                return estado;
        }

}
