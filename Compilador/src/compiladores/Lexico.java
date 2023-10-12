package compiladores;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import accionesSemanticas.AccionSemantica;

public class Lexico {

        private static int estado;
        private int columna;
        private TablaSimbolos TS;
        private TablaToken TT;
        private TablaPR TPR;
        private static boolean volverALeer;
        private static BufferedReader lector;
        private static boolean finArchivo;
        private static boolean saltoDeLinea;

        public Lexico() {
                estado = 0;
                this.columna = -1;
                this.TS = new TablaSimbolos();
                this.TPR = new TablaPR();
                this.TT = new TablaToken();
                volverALeer = false;
                try {
                        lector = abrirArchivo();
                } catch (FileNotFoundException e) {
                        System.err.println("El archivo no se encontró o no se puede acceder.");
                }
                finArchivo = false;
                saltoDeLinea = false;
        }

        private static int[][] transiciones = new int[][] {
                        // Suponemos el estado 100 como el estado final y -1 estado de error (el error
                        // se informa no corta la ejecucion)
                        // bl,tab nl l _ d / { } ( ) , . ; + = : < > ! L - "d" "D" * # "u" "s" "l" otro
                        { 0, 0, 1, 1, 7, 100, 100, 100, 100, 100, 100, 8, 100, 2, 4, 100, 3, 3, 5, 6, 100, 1, 6, 12,
                                        14, 1,
                                        1, 1, 0 },
                        { 100, 100, 1, 1, 1, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                                        100,
                                        1, 100, 100, 100, 1, 1, 1, 100 },
                        { 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                                        100,
                                        100, 100, 100, 100, 100, 100, 100, 100, 100 },
                        { 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                                        100,
                                        100, 100, 100, 100, 100, 100, 100, 100, 100 },
                        { 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                                        100,
                                        100, 100, 100, 100, 100, 100, 100, 100, 100 },
                        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 100, -1, -1, -1, -1,
                                        -1,
                                        -1, -1, -1, -1, -1 },
                        { 100, 100, 100, 6, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                                        6,
                                        100, 100, 6, 100, 100, 100, 100, 100, 100 },
                        { -1, -1, -1, 15, 7, -1, -1, -1, -1, -1, -1, 9, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                                        -1,
                                        -1, -1, -1, 100 },
                        { 100, 100, 100, 100, 9, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                                        100,
                                        100, 100, 100, 100, 100, 100, 100, 100, 100 },
                        { 100, 100, 100, 100, 9, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                                        100,
                                        100, 10, 10, 100, 100, 100, 100, 100, 100 },
                        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, -1, -1, -1, -1, -1, -1, 11, -1, -1,
                                        -1,
                                        -1, -1, -1, -1, -1 },
                        { 100, 100, 100, 100, 11, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                                        100,
                                        100, 100, 100, 100, 100, 100, 100, 100, 100 },
                        { 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
                                        100,
                                        100, 100, 100, 13, 100, 100, 100, 100, 100 },
                        { 13, 0, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13,
                                        13,
                                        13, 13, 13, 13 },
                        { 14, -1, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14,
                                        14,
                                        100, 14, 14, 14, 14 },
                        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                                        -1,
                                        -1, 16, -1, 100, -1 },
                        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                                        -1,
                                        -1, -1, 100, -1, -1 }
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
                        { AS8, AS8, AS1, AS1, AS1, AS5, AS5, AS5, AS5, AS5, AS5, AS1, AS5, AS1, AS1, AS5, AS1, AS1, AS1,
                                        AS1, AS5, AS1, AS1, AS1, AS1, AS1, AS1, AS1, AS8 }, // Estado 0
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
                columna = 28;
                if (Character.isDigit(caracter)) {
                        columna = 4;
                } else if (Character.isLowerCase(caracter)) {
                        if (caracter == 'u')
                                columna = 25;
                        else if (caracter == 's')
                                columna = 26;
                        else if (caracter == 'l')
                                columna = 27;
                        else if (caracter == 'd')
                                columna = 21;
                        else
                                columna = 2;
                } else if (Character.isUpperCase(caracter)) {
                        if (caracter == 'D')
                                columna = 22;
                        else
                                columna = 19;
                } else
                        switch (caracter) {
                                case ' ':
                                        columna = 0;
                                        break;
                                case '\t':
                                        columna = 0;
                                        break;
                                case '\n':
                                        columna = 1;
                                        break;
                                case '_':
                                        columna = 3;
                                        break;
                                case '/':
                                        columna = 5;
                                        break;
                                case '{':
                                        columna = 6;
                                        break;
                                case '}':
                                        columna = 7;
                                        break;
                                case '(':
                                        columna = 8;
                                        break;
                                case ')':
                                        columna = 9;
                                        break;
                                case ',':
                                        columna = 10;
                                        break;
                                case '.':
                                        columna = 11;
                                        break;
                                case ';':
                                        columna = 12;
                                        break;
                                case '+':
                                        columna = 13;
                                        break;
                                case '=':
                                        columna = 14;
                                        break;
                                case ':':
                                        columna = 15;
                                        break;
                                case '<':
                                        columna = 16;
                                        break;
                                case '>':
                                        columna = 17;
                                        break;
                                case '!':
                                        columna = 18;
                                        break;
                                case '-':
                                        columna = 20;
                                        break;
                                case '*':
                                        columna = 23;
                                        break;
                                case '#':
                                        columna = 24;
                                        break;
                        }
                return transiciones[estado][columna];
        }

        public BufferedReader abrirArchivo() throws FileNotFoundException {
                Scanner sc = new Scanner(System.in);
                System.out.println("Ingrese el nombre del archivo que desea leer");
                String nombreArchivo = sc.nextLine();
                FileReader archivo = new FileReader(nombreArchivo);
                sc.close();
                return new BufferedReader(archivo);
        }

        public static void VolverAtras() throws IOException {
                lector.reset();
        }

        public void cerrarArchivo() throws IOException {
                lector.close();
        }

        public static boolean finArchivo() {
                return finArchivo;
        }

        public Token getToken() throws IOException {// lee del archivo
                lector.mark(1);
                int num_caracter = lector.read();
                Token aux = null;
                saltoDeLinea = false;
                while (estado != -1 && estado != 100 && num_caracter != -1) {
                        char caracter = Character.toChars(num_caracter)[0];
                        if (num_caracter == 10)// Indica el salto de linea
                        {
                                Linea.setLinea();
                                saltoDeLinea = true;
                        }
                        int siguienteEstado = 0;
                        siguienteEstado = nuevoEstado(estado, caracter);
                        aux = ejecutarAS(estado, caracter);
                        estado = siguienteEstado;
                        lector.mark(1);
                        num_caracter = lector.read();
                        if (volverALeer) {
                                lector.reset();
                                volverALeer = false;
                        }
                }
                estado = 0;
                if (num_caracter == -1)
                        finArchivo = true;
                return aux;
        }

        public Token ejecutarAS(int estado, char caracter) throws IOException {
                AccionSemantica AS = Matriz_Acciones[estado][columna];
                return generarToken(AS, caracter);
        }

        public Token generarToken(AccionSemantica AS, char caracter) throws IOException {// Devuelve el token
                Token tk = new Token();
                tk = AS.ejecutarAS(caracter);
                return tk;
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

        public boolean salto() {
                return saltoDeLinea;
        }

}
