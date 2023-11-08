//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";

//#line 2 "gramatica.y"
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import compiladores.Lexico;
import compiladores.Linea;
import compiladores.Simbolo;
import compiladores.TablaSimbolos;
import compiladores.Terceto;
import compiladores.Token;

//#line 30 "Parser.java"

public class Parser {

  boolean yydebug; // do I want debug output?
  int yynerrs; // number of errors so far
  int yyerrflag; // was there an error?
  int yychar; // the current working character

  // ########## MESSAGES ##########
  // ###############################################################
  // method: debug
  // ###############################################################
  void debug(String msg) {
    if (yydebug)
      System.out.println(msg);
  }

  // ########## STATE STACK ##########
  final static int YYSTACKSIZE = 500; // maximum stack size
  int statestk[] = new int[YYSTACKSIZE]; // state stack
  int stateptr;
  int stateptrmax; // highest index of stackptr
  int statemax; // state when highest index reached
  // ###############################################################
  // methods: state stack push,pop,drop,peek
  // ###############################################################

  final void state_push(int state) {
    try {
      stateptr++;
      statestk[stateptr] = state;
    } catch (ArrayIndexOutOfBoundsException e) {
      int oldsize = statestk.length;
      int newsize = oldsize * 2;
      int[] newstack = new int[newsize];
      System.arraycopy(statestk, 0, newstack, 0, oldsize);
      statestk = newstack;
      statestk[stateptr] = state;
    }
  }

  final int state_pop() {
    return statestk[stateptr--];
  }

  final void state_drop(int cnt) {
    stateptr -= cnt;
  }

  final int state_peek(int relative) {
    return statestk[stateptr - relative];
  }

  // ###############################################################
  // method: init_stacks : allocate and prepare stacks
  // ###############################################################
  final boolean init_stacks() {
    stateptr = -1;
    val_init();
    return true;
  }

  // ###############################################################
  // method: dump_stacks : show n levels of the stacks
  // ###############################################################
  void dump_stacks(int count) {
    int i;
    System.out.println("=index==state====value=     s:" + stateptr + "  v:" + valptr);
    for (i = 0; i < count; i++)
      System.out.println(" " + i + "    " + statestk[i] + "      " + valstk[i]);
    System.out.println("======================");
  }

  // ########## SEMANTIC VALUES ##########
  // public class ParserVal is defined in ParserVal.java

  String yytext;// user variable to return contextual strings
  ParserVal yyval; // used to return semantic vals from action routines
  ParserVal yylval;// the 'lval' (result) I got from yylex()
  ParserVal valstk[];
  int valptr;

  // ###############################################################
  // methods: value stack push,pop,drop,peek.
  // ###############################################################
  void val_init() {
    valstk = new ParserVal[YYSTACKSIZE];
    yyval = new ParserVal();
    yylval = new ParserVal();
    valptr = -1;
  }

  void val_push(ParserVal val) {
    if (valptr >= YYSTACKSIZE)
      return;
    valstk[++valptr] = val;
  }

  ParserVal val_pop() {
    if (valptr < 0)
      return new ParserVal();
    return valstk[valptr--];
  }

  void val_drop(int cnt) {
    int ptr;
    ptr = valptr - cnt;
    if (ptr < 0)
      return;
    valptr = ptr;
  }

  ParserVal val_peek(int relative) {
    int ptr;
    ptr = valptr - relative;
    if (ptr < 0)
      return new ParserVal();
    return valstk[ptr];
  }

  final ParserVal dup_yyval(ParserVal val) {
    ParserVal dup = new ParserVal();
    dup.ival = val.ival;
    dup.dval = val.dval;
    dup.sval = val.sval;
    dup.obj = val.obj;
    return dup;
  }

  // #### end semantic value section ####
  public final static short ID = 257;
  public final static short CTE = 258;
  public final static short CTEPOS = 259;
  public final static short IF = 260;
  public final static short END_IF = 261;
  public final static short ELSE = 262;
  public final static short PRINT = 263;
  public final static short CLASS = 264;
  public final static short VOID = 265;
  public final static short LONG = 266;
  public final static short USHORT = 267;
  public final static short DOUBLE = 268;
  public final static short DO = 269;
  public final static short UNTIL = 270;
  public final static short IMPL = 271;
  public final static short FOR = 272;
  public final static short CADENA = 273;
  public final static short RETURN = 274;
  public final static short YYERRCODE = 256;
  final static short yylhs[] = { -1,
      0, 2, 2, 4, 4, 7, 1, 8, 3, 3,
      6, 6, 10, 10, 10, 11, 11, 11, 12, 12,
      16, 16, 17, 17, 14, 14, 14, 14, 15, 15,
      18, 18, 18, 19, 20, 22, 13, 24, 24, 25,
      26, 28, 5, 5, 5, 5, 5, 5, 5, 5,
      9, 9, 9, 9, 33, 33, 33, 33, 33, 33,
      33, 34, 34, 34, 29, 29, 29, 29, 29, 29,
      29, 29, 29, 35, 37, 36, 36, 31, 31, 31,
      38, 32, 21, 21, 23, 40, 40, 40, 40, 40,
      40, 27, 27, 27, 27, 27, 27, 39, 39, 39,
      39, 30,
  };
  final static short yylen[] = { 2,
      1, 2, 1, 3, 2, 1, 3, 3, 2, 2,
      1, 1, 3, 3, 3, 1, 1, 1, 3, 1,
      3, 1, 1, 1, 1, 1, 2, 1, 1, 1,
      3, 7, 2, 1, 2, 3, 3, 1, 1, 6,
      3, 8, 1, 1, 1, 1, 1, 1, 1, 1,
      1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
      1, 5, 5, 5, 6, 6, 4, 4, 4, 4,
      6, 6, 3, 1, 1, 1, 1, 4, 4, 4,
      1, 2, 3, 1, 2, 3, 2, 3, 3, 2,
      2, 4, 2, 4, 4, 2, 2, 1, 1, 1,
      1, 2,
  };
  final static short yydefred[] = { 0,
      0, 0, 1, 101, 0, 0, 0, 0, 0, 100,
      99, 98, 81, 0, 50, 0, 3, 0, 0, 43,
      0, 54, 53, 47, 44, 52, 38, 39, 48, 45,
      46, 49, 51, 0, 0, 18, 0, 26, 28, 17,
      16, 0, 0, 0, 0, 0, 0, 22, 0, 0,
      85, 25, 0, 74, 0, 0, 0, 102, 34, 0,
      0, 0, 7, 2, 12, 11, 10, 9, 0, 0,
      0, 0, 0, 84, 0, 27, 0, 36, 90, 87,
      0, 91, 0, 30, 29, 0, 0, 23, 24, 0,
      0, 0, 61, 58, 57, 59, 60, 55, 56, 0,
      75, 0, 0, 73, 0, 31, 0, 0, 0, 0,
      0, 0, 14, 15, 0, 0, 0, 0, 88, 86,
      89, 0, 21, 83, 0, 0, 69, 67, 0, 70,
      68, 0, 0, 0, 93, 0, 97, 0, 0, 0,
      0, 8, 0, 5, 79, 80, 78, 0, 0, 76,
      77, 0, 0, 0, 0, 0, 0, 0, 0, 37,
      4, 63, 62, 64, 71, 65, 72, 66, 0, 94,
      92, 40, 95, 0, 0, 32, 0, 0, 0, 42,
  };
  final static short yydgoto[] = { 2,
      54, 16, 17, 115, 18, 67, 157, 73, 19, 20,
      46, 47, 21, 48, 87, 49, 90, 22, 60, 23,
      50, 24, 25, 26, 27, 28, 109, 29, 30, 31,
      32, 33, 100, 56, 57, 152, 103, 34, 35, 51,
  };
  final static short yysindex[] = { -81,
      68, 0, 0, 0, -2, 16, -239, -200, -197, 0,
      0, 0, 0, -191, 0, -78, 0, -26, -26, 0,
      23, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, -98, -160, 0, 0, 0, 0, 0,
      0, -154, -150, 42, -134, 13, 60, 0, 53, 64,
      0, 0, 13, 0, 10, -114, -128, 0, 0, 7,
      -35, -125, 0, 0, 0, 0, 0, 0, 48, 5,
      -161, -122, -112, 0, 64, 0, 22, 0, 0, 0,
      -15, 0, 35, 0, 0, -120, 13, 0, 0, 13,
      -123, 10, 0, 0, 0, 0, 0, 0, 0, 13,
      0, -147, -144, 0, 68, 0, -40, -111, 33, -96,
      105, 120, 0, 0, 52, -26, 37, 34, 0, 0,
      0, 53, 0, 0, 13, 86, 0, 0, -114, 0,
      0, -114, -57, 0, 0, -90, 0, 68, 127, 46,
      -85, 0, -26, 0, 0, 0, 0, 28, -82, 0,
      0, -140, -136, -9, -34, 68, 50, -73, -71, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 59, 0,
      0, 0, 0, -65, -26, 0, -35, 70, 33, 0,
  };
  final static short yyrindex[] = { 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 47, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, -41, 4,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 21,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 32, 0, 140, 0, 0, 0,
      0, 0, -31, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 41, 0,
      0, -11, 0, 0, 0, 0, 0, 0, 0, 0,
      0, -33, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, -4, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 77, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
  };
  final static short yygindex[] = { 0,
      15, -89, 8, 0, -17, -5, 0, 0, 0, 0,
      0, 80, 135, -49, 0, 126, 0, 0, 0, 0,
      183, 0, 176, 0, 61, 0, 63, 0, 0, 0,
      0, 0, 132, -95, 202, 133, 0, 0, 74, 0,
  };
  final static int YYTABLESIZE = 342;
  static short yytable[];
  static {
    yytable();
  }

  static void yytable() {
    yytable = new short[] { 20,
        135, 20, 20, 20, 107, 108, 171, 19, 1, 19,
        19, 19, 13, 68, 3, 133, 72, 66, 20, 114,
        20, 145, 147, 64, 71, 120, 19, 85, 19, 84,
        44, 45, 25, 58, 169, 42, 43, 44, 45, 96,
        123, 1, 42, 43, 44, 45, 63, 35, 156, 42,
        43, 41, 85, 116, 84, 53, 59, 42, 41, 61,
        42, 44, 45, 101, 33, 41, 42, 63, 163, 99,
        85, 98, 84, 53, 106, 82, 53, 85, 42, 84,
        62, 42, 80, 69, 41, 55, 42, 25, 25, 25,
        84, 25, 42, 25, 88, 70, 74, 143, 6, 89,
        86, 7, 85, 76, 84, 84, 77, 13, 127, 14,
        144, 130, 15, 128, 129, 165, 131, 132, 96, 167,
        166, 82, 91, 81, 168, 83, 149, 104, 85, 105,
        84, 111, 92, 124, 110, 121, 151, 161, 1, 151,
        64, 4, 5, 150, 137, 6, 150, 117, 7, 8,
        9, 10, 11, 12, 13, 138, 14, 118, 70, 15,
        139, 6, 140, 64, 7, 141, 155, 158, 159, 178,
        13, 160, 14, 164, 172, 15, 142, 4, 5, 126,
        136, 6, 173, 176, 7, 8, 9, 10, 11, 12,
        13, 177, 14, 174, 180, 15, 55, 55, 4, 154,
        37, 6, 6, 113, 148, 7, 8, 9, 10, 11,
        12, 13, 122, 14, 20, 134, 15, 75, 78, 175,
        4, 170, 19, 125, 13, 10, 11, 12, 20, 65,
        10, 11, 12, 20, 20, 20, 19, 20, 13, 179,
        119, 19, 19, 19, 25, 19, 36, 37, 38, 39,
        110, 96, 101, 36, 37, 38, 39, 102, 25, 35,
        36, 52, 38, 39, 153, 93, 0, 0, 40, 52,
        38, 39, 52, 38, 39, 40, 33, 0, 52, 38,
        39, 0, 40, 162, 94, 95, 96, 82, 97, 146,
        52, 38, 39, 52, 38, 39, 41, 79, 52, 38,
        39, 0, 84, 0, 112, 38, 39, 0, 70, 0,
        0, 6, 0, 0, 7, 0, 0, 0, 0, 0,
        13, 0, 14, 4, 5, 15, 0, 6, 0, 0,
        7, 8, 9, 10, 11, 12, 13, 0, 14, 0,
        0, 15,
    };
  }

  static short yycheck[];
  static {
    yycheck();
  }

  static void yycheck() {
    yycheck = new short[] { 41,
        41, 43, 44, 45, 40, 41, 41, 41, 123, 43,
        44, 45, 44, 19, 0, 105, 34, 44, 60, 69,
        62, 117, 118, 16, 123, 41, 60, 43, 62, 45,
        40, 41, 44, 273, 44, 45, 46, 40, 41, 44,
        90, 123, 45, 46, 40, 41, 125, 44, 138, 45,
        46, 61, 43, 71, 45, 40, 257, 45, 61, 257,
        45, 40, 41, 56, 44, 61, 45, 125, 41, 60,
        43, 62, 45, 40, 60, 44, 40, 43, 45, 45,
        272, 45, 41, 61, 44, 6, 45, 41, 42, 43,
        44, 45, 45, 47, 42, 257, 257, 115, 260, 47,
        41, 263, 43, 258, 45, 59, 257, 269, 256, 271,
        116, 256, 274, 261, 262, 256, 261, 262, 123, 256,
        261, 256, 59, 44, 261, 46, 41, 256, 43, 123,
        45, 257, 53, 257, 61, 256, 129, 143, 123, 132,
        133, 256, 257, 129, 256, 260, 132, 270, 263, 264,
        265, 266, 267, 268, 269, 123, 271, 270, 257, 274,
        257, 260, 58, 156, 263, 46, 257, 41, 123, 175,
        269, 257, 271, 256, 125, 274, 125, 256, 257, 100,
        107, 260, 256, 125, 263, 264, 265, 266, 267, 268,
        269, 257, 271, 265, 125, 274, 117, 118, 256, 257,
        61, 125, 260, 69, 125, 263, 264, 265, 266, 267,
        268, 269, 87, 271, 256, 256, 274, 35, 43, 159,
        256, 256, 256, 92, 256, 266, 267, 268, 270, 256,
        266, 267, 268, 275, 276, 277, 270, 279, 270, 177,
        256, 275, 276, 277, 256, 279, 256, 257, 258, 259,
        177, 256, 257, 256, 257, 258, 259, 56, 270, 256,
        256, 257, 258, 259, 132, 256, -1, -1, 278, 257,
        258, 259, 257, 258, 259, 278, 256, -1, 257, 258,
        259, -1, 278, 256, 275, 276, 277, 256, 279, 256,
        257, 258, 259, 257, 258, 259, 256, 256, 257, 258,
        259, -1, 256, -1, 257, 258, 259, -1, 257, -1,
        -1, 260, -1, -1, 263, -1, -1, -1, -1, -1,
        269, -1, 271, 256, 257, 274, -1, 260, -1, -1,
        263, 264, 265, 266, 267, 268, 269, -1, 271, -1,
        -1, 274,
    };
  }

  final static short YYFINAL = 2;
  final static short YYMAXTOKEN = 279;
  final static String yyname[] = {
      "end-of-file", null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, "'('", "')'", "'*'", "'+'", "','",
      "'-'", "'.'", "'/'", null, null, null, null, null, null, null, null, null, null, "':'", "';'",
      "'<'", "'='", "'>'", null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      "'{'", null, "'}'", null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, "ID", "CTE", "CTEPOS", "IF", "END_IF", "ELSE",
      "PRINT", "CLASS", "VOID", "LONG", "USHORT", "DOUBLE", "DO", "UNTIL", "IMPL", "FOR",
      "CADENA", "RETURN", "\"<=\"", "\">=\"", "\"!!\"", "\"+=\"", "\"==\"",
  };
  final static String yyrule[] = {
      "$accept : programa",
      "programa : bloque_de_Sentencias",
      "conjuntoSentencias : conjuntoSentencias sentencia",
      "conjuntoSentencias : sentencia",
      "conjuntoSentenciasEjecutables : conjuntoSentenciasEjecutables sentenciaEjecutable fin_sentencia",
      "conjuntoSentenciasEjecutables : sentenciaEjecutable fin_sentencia",
      "cuerpo_funcion : conjuntoSentencias",
      "bloque_de_Sentencias : '{' conjuntoSentencias '}'",
      "bloque_de_SentenciasEjecutables : '{' conjuntoSentenciasEjecutables '}'",
      "sentencia : sentenciaDeclarativa fin_sentencia",
      "sentencia : sentenciaEjecutable fin_sentencia",
      "fin_sentencia : ','",
      "fin_sentencia : error",
      "asignacion : ID simboloAsignacion expresion",
      "asignacion : atributo_objeto '=' atributo_objeto",
      "asignacion : atributo_objeto '=' factor",
      "simboloAsignacion : '='",
      "simboloAsignacion : \"+=\"",
      "simboloAsignacion : error",
      "expresion : expresion operadorMasMenos termino",
      "expresion : termino",
      "termino : termino simboloTermino factor",
      "termino : factor",
      "simboloTermino : '*'",
      "simboloTermino : '/'",
      "factor : ID",
      "factor : CTE",
      "factor : '-' CTE",
      "factor : CTEPOS",
      "operadorMasMenos : '+'",
      "operadorMasMenos : '-'",
      "declaracionClase : CLASS nombreClase bloque_de_Sentencias",
      "declaracionClase : CLASS nombreClase '{' conjuntoSentencias ID ',' '}'",
      "declaracionClase : CLASS nombreClase",
      "nombreClase : ID",
      "declaracionObjeto : ID lista_Variables",
      "metodo_objeto : ID '.' invocacionFuncion",
      "atributo_objeto : ID '.' ID",
      "declaracionFuncion : funcion_VOID",
      "declaracionFuncion : funcion_VOID_vacia",
      "funcion_VOID : VOID ID parametro_formal '{' cuerpo_funcion '}'",
      "funcion_VOID_vacia : VOID ID parametro_formal",
      "clausula_IMPL : IMPL FOR ID ':' '{' funcion_VOID fin_sentencia '}'",
      "sentenciaEjecutable : asignacion",
      "sentenciaEjecutable : invocacionFuncion",
      "sentenciaEjecutable : clausula_seleccion",
      "sentenciaEjecutable : print",
      "sentenciaEjecutable : metodo_objeto",
      "sentenciaEjecutable : clausula_IMPL",
      "sentenciaEjecutable : sentencia_de_Control",
      "sentenciaEjecutable : RETURN",
      "sentenciaDeclarativa : declaracion",
      "sentenciaDeclarativa : declaracionFuncion",
      "sentenciaDeclarativa : declaracionObjeto",
      "sentenciaDeclarativa : declaracionClase",
      "comparador : '>'",
      "comparador : '<'",
      "comparador : \">=\"",
      "comparador : \"<=\"",
      "comparador : \"!!\"",
      "comparador : \"==\"",
      "comparador : error",
      "condicion : '(' expresion comparador expresion ')'",
      "condicion : '(' expresion comparador expresion error",
      "condicion : expresion comparador expresion ')' error",
      "clausula_seleccion : IF condicion bloque_IF ELSE bloque_ELSE END_IF",
      "clausula_seleccion : IF condicion sentencia_IF ELSE bloque_ELSE END_IF",
      "clausula_seleccion : IF condicion bloque_IF END_IF",
      "clausula_seleccion : IF condicion sentencia_IF END_IF",
      "clausula_seleccion : IF condicion bloque_IF error",
      "clausula_seleccion : IF condicion sentencia_IF error",
      "clausula_seleccion : IF condicion bloque_IF ELSE bloque_ELSE error",
      "clausula_seleccion : IF condicion sentencia_IF ELSE bloque_ELSE error",
      "clausula_seleccion : IF bloque_IF error",
      "bloque_IF : bloque_de_Sentencias",
      "sentencia_IF : sentencia",
      "bloque_ELSE : bloque_de_Sentencias",
      "bloque_ELSE : sentencia",
      "sentencia_de_Control : inicio_DO bloque_de_SentenciasEjecutables UNTIL condicion",
      "sentencia_de_Control : inicio_DO sentenciaEjecutable UNTIL condicion",
      "sentencia_de_Control : inicio_DO bloque_de_SentenciasEjecutables UNTIL error",
      "inicio_DO : DO",
      "declaracion : tipo lista_Variables",
      "lista_Variables : lista_Variables ';' ID",
      "lista_Variables : ID",
      "invocacionFuncion : ID parametro_real",
      "parametro_real : '(' expresion ')'",
      "parametro_real : '(' ')'",
      "parametro_real : '(' expresion error",
      "parametro_real : expresion ')' error",
      "parametro_real : '(' error",
      "parametro_real : ')' error",
      "parametro_formal : '(' tipo ID ')'",
      "parametro_formal : '(' ')'",
      "parametro_formal : '(' tipo ID error",
      "parametro_formal : tipo ID ')' error",
      "parametro_formal : '(' error",
      "parametro_formal : ')' error",
      "tipo : DOUBLE",
      "tipo : USHORT",
      "tipo : LONG",
      "tipo : error",
      "print : PRINT CADENA",
  };

  // #line 273 "gramatica.y"

  Lexico lex;
  TablaSimbolos TS = new TablaSimbolos();
  HashMap<Integer, Terceto> CodigoIntermedio = new HashMap<Integer, Terceto>();
  HashMap<String, String> funciones = new HashMap<String, String>();
  int puntero_Terceto = 0;
  String tipo;
  ArrayList<String> variables = new ArrayList<String>();
  Stack<Integer> pila = new Stack<Integer>();
  HashMap<String, ArrayList<Integer>> clases = new HashMap<String, ArrayList<Integer>>();
  ArrayList<Integer> metodosTemp = new ArrayList<Integer>();

  public void agregarClase(String ID, ArrayList<Integer> metodos) {
    clases.put(ID, metodos);
  }

  public void imprimirClases() {
    System.out.println("CLASES");
    for (HashMap.Entry<String, ArrayList<Integer>> e : clases.entrySet()) {
      ArrayList<Integer> mets = e.getValue();
      String r = " nombre clase: " + e.getKey() + " | metodos => ";
      for (Integer i : mets) {
        r += "ref TS:" + Integer.toString(i) + ", nombre : " + TS.get_Simbolo(i).get_Lex() + " ; ";
      }
      System.out.println(r);
    }
  }

  public void imprimirFunciones() {
    System.out.println("FUNCIONES");
    for (HashMap.Entry<String, String> e : funciones.entrySet()) {
      System.out.println(" funcion: " + e.getKey() + " - parametro: " + e.getValue());
    }
  }

  public String buscar_Parametro(String id) {
    return funciones.get(id);
  }

  public int crear_terceto(String operador, String punt1, String punt2) {
    Terceto t = new Terceto(operador, punt1, punt2);
    CodigoIntermedio.put(puntero_Terceto, t);
    puntero_Terceto = puntero_Terceto + 1;
    return puntero_Terceto - 1;
  }

  public Parser(Lexico lexico) {
    this.lex = lexico;
  }

  private void imprimirCodigoIntermedio() {
    System.out.println("CODIGO INTERMEDIO");
    for (HashMap.Entry<Integer, Terceto> i : CodigoIntermedio.entrySet()) {
      String j = i.getValue().get_Op1();
      String s = i.getValue().get_Op2();
      if (!s.contains("[") && s != "-") {
        int k = Integer.parseInt(s);
        s = TS.get_Simbolo(k).get_Lex();
      }
      if (!j.contains("[") && j != "-") {
        int l = Integer.parseInt(j);
        j = TS.get_Simbolo(l).get_Lex();
      }
      System.out.println(
          "Referencia: " + i.getKey() + ", Terceto: (" + i.getValue().get_Operador() + " , " + j + " , " + s + ")");
    }
  }

  private int yylex() {
    Token token = new Token();
    try {
      token = lex.getToken();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (token != null) {
      yylval = new ParserVal(token.getLexema());
      System.out.println(token.toString());
      return token.getIdToken();
    } else
      TS.imprimirContenido();
    imprimirCodigoIntermedio();
    System.out.println(" ");
    imprimirClases();
    System.out.println(" ");
    imprimirFunciones();
    return 0;
  }

  public void yyerror(String error) {
    System.out.println(error);
  }

  public void chequearRangoPositivo(String numero, ParserVal factor) {
    if (numero.contains(".")) // DOUBLE
    {
      double num = Double.parseDouble(numero);
      if (num < 2.2250738585072014e-308) {
        if (num != 0.0) {
          System.out.println("El double positivo es menor al limite permitido. Tiene valor: " + num);
          TS.eliminar(Double.toString(num));
          String nuevo = "2.2250738585072014e-308";
          TS.agregar(nuevo, 258);
          System.out.println("El numero fue actualizado en la TS a un valor permitido");
          setear_Uso("Constante", nuevo);
          factor.sval = Integer.toString(TS.pertenece(nuevo));
        } else {
          setear_Uso("Constante", numero);
          factor.sval = Integer.toString(TS.pertenece(numero));
        }
      } else if (num > 1.7976931348623157e+308) {
        System.out.println("El double positivo es mayor al limite permitido. Tiene valor: " + num);
        TS.eliminar(Double.toString(num));
        String nuevo = "1.7976931348623157e+308";
        TS.agregar(nuevo, 258);
        setear_Uso("Constante", nuevo);
        factor.sval = Integer.toString(TS.pertenece(nuevo));
      } else {
        setear_Uso("Constante", numero);
        factor.sval = Integer.toString(TS.pertenece(numero));
      }
    } else {
      Long entero = Long.valueOf(numero);
      if (entero > 2147483647L) {
        System.out.println("El entero largo positivo es mayor al limite permitido. Tiene valor: " + entero);
        TS.eliminar(Long.toString(entero));
        String nuevo = "2147483647";
        TS.agregar(nuevo, 258);
        System.out.println("El numero fue actualizado en la TS a un valor permitido");
        setear_Uso("Constante", nuevo);
        factor.sval = Integer.toString(TS.pertenece(nuevo));
      } else {
        setear_Uso("Constante", numero);
        factor.sval = Integer.toString(TS.pertenece(numero));
      }
    }
  }

  public void chequearRangoNegativo(String numero, ParserVal factor) {
    if (numero.contains(".")) // DOUBLE
    {
      // Convertimos el double en negativo para chequear rango negativo
      double num = Double.parseDouble(numero) * (-1.0);
      if (num > -2.2250738585072014e-308) {
        if (num != 0.0) // Si esta fuera del rango todavia puede ser válido por el 0.0
        {
          System.out.println("El double negativo es mayor al limite permitido. Tiene valor: " + num);
          TS.eliminar(Double.toString(num * (-1.0)));
          String nuevo = "-2.2250738585072014e-308";
          TS.agregar(nuevo, 258);// Lo agrego a la tabla de simbolos con el signo
          setear_Uso("Constante negativa", nuevo);
          System.out.println("El numero fue actualizado en la TS a un valor permitido");
          factor.sval = Integer.toString(TS.pertenece(nuevo));
        }
      } else if (num < -1.7976931348623157e+308) {
        System.out.println("El double positivo es menor al limite permitido. Tiene valor: " + num);
        TS.eliminar(Double.toString(num * (-1.0)));
        String nuevo = "-1.7976931348623157e+308";
        TS.agregar(nuevo, 258);
        setear_Uso("Constante negativa", nuevo);
        factor.sval = Integer.toString(TS.pertenece(nuevo));
      } else {// En caso de respetar el rango solo le agrega el menos en la tabla de simbolos
        TS.eliminar(Double.toString(num * (-1.0)));
        String nuevo = Double.toString(num);
        TS.agregar(nuevo, 258);
        setear_Uso("Constante negativa", nuevo);
        System.out.println("Se actualizo el double dentro del rango a negativo");
        factor.sval = Integer.toString(TS.pertenece("-" + numero));
      }
    } else // LONG
    {
      // Convierto el numero a negativo y verifico el rango
      Long entero = Long.valueOf(numero);
      entero = entero * (-1);
      if (entero < -2147483648L) {
        System.out.println("El entero largo negativo es menor al limite permitido. Tiene valor: " + entero);
        TS.eliminar(Long.toString(entero * (-1)));
        String nuevo = "-2147483648";
        TS.agregar(nuevo, 258);// Lo agrego a la tabla de simbolos en negativo borrando el numero positivo
        setear_Uso("Constante negativa", nuevo);
        factor.sval = Integer.toString(TS.pertenece(nuevo));
      } else {
        // En caso de respetar el rango solo le agrega el menos en la tabla de simbolos
        TS.eliminar(Long.toString(entero * (-1)));
        String nuevo = Long.toString(entero);
        TS.agregar(nuevo, 258);
        setear_Uso("Constante negativa", nuevo);
        factor.sval = Integer.toString(TS.pertenece("-" + numero));
      }
    }
  }

  public void VerificarSalto() {
    if (lex.salto())
      System.out.println("Falto la coma en la linea " + (Linea.getLinea() - 1) + " al final de la sentencia");
    else
      System.out.println("Falto la coma en la linea " + Linea.getLinea() + " al final de la sentencia");
  }

  public void setear_Uso(String uso, String pos) {
    int clave = TS.pertenece(pos);
    Simbolo s = TS.get_Simbolo(clave);
    s.set_Uso(uso);
  }

  public void guardar_Tipo(String t) {
    System.out.println("tipo: " + t);
    tipo = t;
  }

  public void setear_Tipo() {
    for (String s : variables) {
      int clave = TS.pertenece(s);
      Simbolo sim = TS.get_Simbolo(clave);
      sim.set_Tipo(tipo);
    }
    variables.clear();
  }

  public void guardar_Var(String id) {
    variables.add(id);
  }

  public void completarTerceto(int pos, int aux) {
    Terceto t = CodigoIntermedio.get(pos);
    String operando = "[" + Integer.toString(aux) + "]";
    t.set_Op(operando);
  }

  // #line 668 "Parser.java"
  // ###############################################################
  // method: yylexdebug : check lexer state
  // ###############################################################
  void yylexdebug(int state, int ch) {
    String s = null;
    if (ch < 0)
      ch = 0;
    if (ch <= YYMAXTOKEN) // check index bounds
      s = yyname[ch]; // now get it
    if (s == null)
      s = "illegal-symbol";
    debug("state " + state + ", reading " + ch + " (" + s + ")");
  }

  // The following are now global, to aid in error reporting
  int yyn; // next next thing to do
  int yym; //
  int yystate; // current parsing state from state table
  String yys; // current token string

  // ###############################################################
  // method: yyparse : parse input and execute indicated items
  // ###############################################################
  int yyparse() {
    boolean doaction;
    init_stacks();
    yynerrs = 0;
    yyerrflag = 0;
    yychar = -1; // impossible char forces a read
    yystate = 0; // initial state
    state_push(yystate); // save it
    val_push(yylval); // save empty value
    while (true) // until parsing is done, either correctly, or w/error
    {
      doaction = true;
      if (yydebug)
        debug("loop");
      // #### NEXT ACTION (from reduction table)
      for (yyn = yydefred[yystate]; yyn == 0; yyn = yydefred[yystate]) {
        if (yydebug)
          debug("yyn:" + yyn + "  state:" + yystate + "  yychar:" + yychar);
        if (yychar < 0) // we want a char?
        {
          yychar = yylex(); // get next token
          if (yydebug)
            debug(" next yychar:" + yychar);
          // #### ERROR CHECK ####
          if (yychar < 0) // it it didn't work/error
          {
            yychar = 0; // change it to default string (no -1!)
            if (yydebug)
              yylexdebug(yystate, yychar);
          }
        } // yychar<0
        yyn = yysindex[yystate]; // get amount to shift by (shift index)
        if ((yyn != 0) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar) {
          if (yydebug)
            debug("state " + yystate + ", shifting to state " + yytable[yyn]);
          // #### NEXT STATE ####
          yystate = yytable[yyn];// we are in a new state
          state_push(yystate); // save it
          val_push(yylval); // push our lval as the input for next rule
          yychar = -1; // since we have 'eaten' a token, say we need another
          if (yyerrflag > 0) // have we recovered an error?
            --yyerrflag; // give ourselves credit
          doaction = false; // but don't process yet
          break; // quit the yyn=0 loop
        }

        yyn = yyrindex[yystate]; // reduce
        if ((yyn != 0) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar) { // we reduced!
          if (yydebug)
            debug("reduce");
          yyn = yytable[yyn];
          doaction = true; // get ready to execute
          break; // drop down to actions
        } else // ERROR RECOVERY
        {
          if (yyerrflag == 0) {
            yyerror("syntax error");
            yynerrs++;
          }
          if (yyerrflag < 3) // low error count?
          {
            yyerrflag = 3;
            while (true) // do until break
            {
              if (stateptr < 0) // check for under & overflow here
              {
                yyerror("stack underflow. aborting..."); // note lower case 's'
                return 1;
              }
              yyn = yysindex[state_peek(0)];
              if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                  yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE) {
                if (yydebug)
                  debug("state " + state_peek(0) + ", error recovery shifting to state " + yytable[yyn] + " ");
                yystate = yytable[yyn];
                state_push(yystate);
                val_push(yylval);
                doaction = false;
                break;
              } else {
                if (yydebug)
                  debug("error recovery discarding state " + state_peek(0) + " ");
                if (stateptr < 0) // check for under & overflow here
                {
                  yyerror("Stack underflow. aborting..."); // capital 'S'
                  return 1;
                }
                state_pop();
                val_pop();
              }
            }
          } else // discard this token
          {
            if (yychar == 0)
              return 1; // yyabort
            if (yydebug) {
              yys = null;
              if (yychar <= YYMAXTOKEN)
                yys = yyname[yychar];
              if (yys == null)
                yys = "illegal-symbol";
              debug("state " + yystate + ", error recovery discards token " + yychar + " (" + yys + ")");
            }
            yychar = -1; // read another
          }
        } // end error recovery
      } // yyn=0 loop
      if (!doaction) // any reason not to proceed?
        continue; // skip action
      yym = yylen[yyn]; // get count of terminals on rhs
      if (yydebug)
        debug("state " + yystate + ", reducing " + yym + " by rule " + yyn + " (" + yyrule[yyn] + ")");
      if (yym > 0) // if count of rhs not 'nil'
        yyval = val_peek(yym - 1); // get current semantic value
      yyval = dup_yyval(yyval); // duplicate yyval if ParserVal is used as semantic value
      switch (yyn) {
        // ########## USER-SUPPLIED ACTIONS ##########
        case 1:
        // #line 21 "gramatica.y"
        {
          System.out.println("Programa completamente reconocido");
        }
          break;
        case 7:
        // #line 35 "gramatica.y"
        {
          System.out.println("Bloque de Sentencias reconocido");
        }
          break;
        case 12:
        // #line 46 "gramatica.y"
        {
          VerificarSalto();
        }
          break;
        case 13:
        // #line 49 "gramatica.y"
        {
          if (val_peek(1).sval == "+=") {
            String aux = "[" + Integer
                .toString(crear_terceto("+", Integer.toString(TS.pertenece(val_peek(2).sval)), val_peek(0).sval)) + "]";
            yyval.sval = '['
                + Integer.toString(crear_terceto("=", Integer.toString(TS.pertenece(val_peek(2).sval)), aux)) + ']';
          } else
            yyval.sval = '['
                + Integer.toString(
                    crear_terceto(val_peek(1).sval, Integer.toString(TS.pertenece(val_peek(2).sval)), val_peek(0).sval))
                + ']';
        }
          break;
        case 14:
        // #line 54 "gramatica.y"
        {
          System.out.println("Se reconocio una asignacion a un atributo objeto en linea " + Linea.getLinea());
        }
          break;
        case 15:
        // #line 55 "gramatica.y"
        {
          System.out.println("Se reconocio una asignacion a un atributo objeto en linea " + Linea.getLinea());
        }
          break;
        case 16:
        // #line 58 "gramatica.y"
        {
          System.out.println("Se reconocio una asignacion en linea " + Linea.getLinea());
          yyval.sval = "=";
        }
          break;
        case 17:
        // #line 60 "gramatica.y"
        {
          System.out.println("Se reconocio una asignacion suma en linea " + Linea.getLinea());
          yyval.sval = "+=";
        }
          break;
        case 18:
        // #line 62 "gramatica.y"
        {
          System.out.println("No es valido el signo de asignacion");
        }
          break;
        case 19:
        // #line 65 "gramatica.y"
        {
          yyval.sval = '[' + Integer.toString(crear_terceto(val_peek(1).sval, val_peek(2).sval, val_peek(0).sval))
              + ']';
        }
          break;
        case 20:
        // #line 66 "gramatica.y"
        {
          yyval.sval = val_peek(0).sval;
        }
          break;
        case 21:
        // #line 69 "gramatica.y"
        {
          yyval.sval = '[' + Integer.toString(crear_terceto(val_peek(1).sval, val_peek(2).sval, val_peek(0).sval))
              + ']';
        }
          break;
        case 22:
        // #line 70 "gramatica.y"
        {
          yyval.sval = val_peek(0).sval;
        }
          break;
        case 23:
        // #line 73 "gramatica.y"
        {
          yyval.sval = "*";
        }
          break;
        case 24:
        // #line 74 "gramatica.y"
        {
          yyval.sval = "/";
        }
          break;
        case 25:
        // #line 77 "gramatica.y"
        {
          yyval.sval = Integer.toString(TS.pertenece(val_peek(0).sval));
          setear_Uso("identificador", val_peek(0).sval);
        }
          break;
        case 26:
        // #line 79 "gramatica.y"
        {
          System.out.println("Se reconocio una constante en linea " + Linea.getLinea());
          chequearRangoPositivo(val_peek(0).sval, yyval);
        }
          break;
        case 27:
        // #line 81 "gramatica.y"
        {
          System.out.println("Se reconocio constante negativa en linea " + Linea.getLinea());
          chequearRangoNegativo(val_peek(0).sval, yyval);
          ;
        }
          break;
        case 28:
        // #line 83 "gramatica.y"
        {
          setear_Uso("ConstantePositiva", val_peek(0).sval);
          yyval.sval = Integer.toString(TS.pertenece(val_peek(0).sval));
        }
          break;
        case 29:
        // #line 87 "gramatica.y"
        {
          yyval.sval = "+";
        }
          break;
        case 30:
        // #line 88 "gramatica.y"
        {
          yyval.sval = "-";
        }
          break;
        case 31:
        // #line 92 "gramatica.y"
        {
          setear_Uso("Clase", val_peek(1).sval);
          agregarClase(val_peek(1).sval, metodosTemp);
          metodosTemp = new ArrayList<Integer>();
        }
          break;
        case 32:
        // #line 96 "gramatica.y"
        {
          System.out.println("Clase con herencia por composicion en linea " + Linea.getLinea());
          setear_Uso("Clase", val_peek(5).sval);
          agregarClase(val_peek(5).sval, metodosTemp);
          metodosTemp = new ArrayList<Integer>();
        }
          break;
        case 33:
        // #line 100 "gramatica.y"
        {
          setear_Uso("Clase", val_peek(0).sval);
        }
          break;
        case 34:
        // #line 103 "gramatica.y"
        {
          metodosTemp = new ArrayList<Integer>();
        }
          break;
        case 35:
        // #line 106 "gramatica.y"
        {
          guardar_Tipo(val_peek(1).sval);
          setear_Tipo();
        }
          break;
        case 36:
        // #line 109 "gramatica.y"
        {
          int aux = crear_terceto("CALLMetodoClase", Integer.toString(TS.pertenece(val_peek(2).sval)),
              val_peek(0).sval);
        }
          break;
        case 40:
        // #line 119 "gramatica.y"
        {
          System.out.println("Se reconocio una invocacion de una funcion VOID en linea " + Linea.getLinea());
          setear_Uso("Metodo", val_peek(4).sval);
          guardar_Var(val_peek(4).sval);
          guardar_Tipo("VOID");
          setear_Tipo();
          funciones.put(val_peek(4).sval, val_peek(3).sval);
          metodosTemp.add(TS.pertenece(val_peek(4).sval));
        }
          break;
        case 41:
        // #line 128 "gramatica.y"
        {
          System.out.println("Se reconocio una invocacion de una funcion VOID vacia en linea " + Linea.getLinea());
          setear_Uso("Metodo", val_peek(1).sval);
          guardar_Var(val_peek(1).sval);
          guardar_Tipo("VOID");
          setear_Tipo();
          funciones.put(val_peek(1).sval, val_peek(0).sval);
          metodosTemp.add(TS.pertenece(val_peek(1).sval));
        }
          break;
        case 45:
        // #line 142 "gramatica.y"
        {
          System.out.println("Se reconocio una clausula de seleccion IF en linea " + Linea.getLinea());
        }
          break;
        case 46:
        // #line 143 "gramatica.y"
        {
          System.out.println("Se reconocio una impresion por pantalla en linea " + Linea.getLinea());
        }
          break;
        case 47:
        // #line 144 "gramatica.y"
        {
          System.out.println("Se reconocio la invocacion de un metodo de un objeto en linea " + Linea.getLinea());
        }
          break;
        case 48:
        // #line 145 "gramatica.y"
        {
          System.out.println("Se reconocio sentencia IMPL FOR en linea " + Linea.getLinea());
        }
          break;
        case 49:
        // #line 146 "gramatica.y"
        {
          System.out.println("Se reconocio sentencia de control DO UNTIL en linea " + Linea.getLinea());
        }
          break;
        case 50:
        // #line 147 "gramatica.y"
        {
          System.out.println("Se reconocio sentencia de retorno RETURN en linea " + Linea.getLinea());
          int aux = crear_terceto("RETURN", "-", "-");
        }
          break;
        case 51:
        // #line 151 "gramatica.y"
        {
          System.out.println("Se reconocio una declaracion simple en linea " + Linea.getLinea());
        }
          break;
        case 53:
        // #line 153 "gramatica.y"
        {
          System.out.println("Se reconocio una declaracion de un objeto de una clase en linea " + Linea.getLinea());
        }
          break;
        case 54:
        // #line 154 "gramatica.y"
        {
          System.out.println("Se reconocio una clase en linea " + Linea.getLinea());
        }
          break;
        case 55:
        // #line 157 "gramatica.y"
        {
          yyval.sval = ">";
        }
          break;
        case 56:
        // #line 158 "gramatica.y"
        {
          yyval.sval = "<";
        }
          break;
        case 57:
        // #line 159 "gramatica.y"
        {
          yyval.sval = ">=";
        }
          break;
        case 58:
        // #line 160 "gramatica.y"
        {
          yyval.sval = "<=";
        }
          break;
        case 59:
        // #line 161 "gramatica.y"
        {
          yyval.sval = "!!";
        }
          break;
        case 60:
        // #line 162 "gramatica.y"
        {
          yyval.sval = "==";
        }
          break;
        case 61:
        // #line 163 "gramatica.y"
        {
          System.out.println("Error: El caracter no se reconoce como comparador  en linea " + Linea.getLinea());
        }
          break;
        case 62:
        // #line 166 "gramatica.y"
        {
          System.out.println("Se reconoció una condicion  en linea " + Linea.getLinea());
          yyval.sval = '[' + Integer.toString(crear_terceto(val_peek(2).sval, val_peek(3).sval, val_peek(1).sval))
              + ']';
          int aux = crear_terceto("BF", yyval.sval, "-");
          pila.push(aux);
        }
          break;
        case 63:
        // #line 170 "gramatica.y"
        {
          System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());
          yyval.sval = '[' + Integer.toString(crear_terceto(val_peek(2).sval, val_peek(3).sval, val_peek(1).sval))
              + ']';
          int aux = crear_terceto("BF", yyval.sval, "-");
          pila.push(aux);
        }
          break;
        case 64:
        // #line 174 "gramatica.y"
        {
          System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());
          yyval.sval = '[' + Integer.toString(crear_terceto(val_peek(3).sval, val_peek(4).sval, val_peek(2).sval))
              + ']';
          int aux = crear_terceto("BF", yyval.sval, "-");
          pila.push(aux);
        }
          break;
        case 65:
        // #line 180 "gramatica.y"
        {
          int primero = pila.pop();
          completarTerceto(primero, puntero_Terceto);
        }
          break;
        case 66:
        // #line 182 "gramatica.y"
        {
          int primero = pila.pop();
          completarTerceto(primero, puntero_Terceto);
        }
          break;
        case 67:
        // #line 184 "gramatica.y"
        {
          int primero = pila.pop();
          completarTerceto(primero, puntero_Terceto);
        }
          break;
        case 68:
        // #line 186 "gramatica.y"
        {
          int primero = pila.pop();
          completarTerceto(primero, puntero_Terceto);
        }
          break;
        case 69:
        // #line 188 "gramatica.y"
        {
          System.out.println("Falta el END_IF");
          int primero = pila.pop();
          completarTerceto(primero, puntero_Terceto);
        }
          break;
        case 70:
        // #line 191 "gramatica.y"
        {
          System.out.println("Falta el END_IF");
          int primero = pila.pop();
          completarTerceto(primero, puntero_Terceto);
        }
          break;
        case 71:
        // #line 194 "gramatica.y"
        {
          System.out.println("Falta el END_IF");
          int primero = pila.pop();
          completarTerceto(primero, puntero_Terceto);
        }
          break;
        case 72:
        // #line 197 "gramatica.y"
        {
          System.out.println("Falta el END_IF");
          int primero = pila.pop();
          completarTerceto(primero, puntero_Terceto);
        }
          break;
        case 73:
        // #line 200 "gramatica.y"
        {
          System.out.println("Falto la condicion del IF");
        }
          break;
        case 74:
        // #line 203 "gramatica.y"
        {
          int primero = pila.pop();
          int aux = crear_terceto("BI", "-", "-");
          completarTerceto(primero, aux + 1);
          pila.push(aux);
        }
          break;
        case 75:
        // #line 209 "gramatica.y"
        {
          int primero = pila.pop();
          int aux = crear_terceto("BI", "-", "-");
          completarTerceto(primero, aux + 1);
          pila.push(aux);
        }
          break;
        case 78:
        // #line 219 "gramatica.y"
        {
          int primero = pila.pop();
          completarTerceto(primero, val_peek(3).ival);
        }
          break;
        case 79:
        // #line 221 "gramatica.y"
        {
          int primero = pila.pop();
          completarTerceto(primero, val_peek(3).ival);
        }
          break;
        case 80:
        // #line 223 "gramatica.y"
        {
          System.out.println("Falta la condicion de la sentencia de control");
        }
          break;
        case 81:
        // #line 226 "gramatica.y"
        {
          yyval.ival = puntero_Terceto;
        }
          break;
        case 82:
        // #line 229 "gramatica.y"
        {
          setear_Tipo();
        }
          break;
        case 83:
        // #line 232 "gramatica.y"
        {
          setear_Uso("Variable", val_peek(0).sval);
          guardar_Var(val_peek(0).sval);
        }
          break;
        case 84:
        // #line 233 "gramatica.y"
        {
          setear_Uso("Variable", val_peek(0).sval);
          guardar_Var(val_peek(0).sval);
        }
          break;
        case 85:
        // #line 236 "gramatica.y"
        {
          String aux = buscar_Parametro(val_peek(1).sval);
          if ((aux == null && val_peek(0).sval == null) || (aux != null && val_peek(0).sval != null)) {
            int axu1 = crear_terceto("=", Integer.toString(TS.pertenece(aux)), val_peek(0).sval);
            yyval.sval = "["
                + Integer.toString(crear_terceto("CALL", Integer.toString(TS.pertenece(val_peek(1).sval)), "-")) + "]";
          } else
            System.out.println("Los parámetros no coinciden");
        }
          break;
        case 86:
        // #line 245 "gramatica.y"
        {
          yyval.sval = val_peek(1).sval;
        }
          break;
        case 87:
        // #line 246 "gramatica.y"
        {
          yyval.sval = null;
        }
          break;
        case 88:
        // #line 247 "gramatica.y"
        {
          System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());
        }
          break;
        case 89:
        // #line 248 "gramatica.y"
        {
          System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());
        }
          break;
        case 90:
        // #line 249 "gramatica.y"
        {
          System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());
        }
          break;
        case 91:
        // #line 250 "gramatica.y"
        {
          System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());
        }
          break;
        case 92:
        // #line 253 "gramatica.y"
        {
          setear_Uso("Parametro formal", val_peek(1).sval);
          yyval.sval = val_peek(1).sval;
        }
          break;
        case 93:
        // #line 255 "gramatica.y"
        {
          yyval.sval = null;
        }
          break;
        case 94:
        // #line 256 "gramatica.y"
        {
          System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());
          setear_Uso("Parametro formal", val_peek(1).sval);
        }
          break;
        case 95:
        // #line 257 "gramatica.y"
        {
          System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());
          setear_Uso("Parametro formal", val_peek(2).sval);
        }
          break;
        case 96:
        // #line 258 "gramatica.y"
        {
          System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());
        }
          break;
        case 97:
        // #line 259 "gramatica.y"
        {
          System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());
        }
          break;
        case 98:
        // #line 262 "gramatica.y"
        {
          guardar_Tipo("DOUBLE");
        }
          break;
        case 99:
        // #line 263 "gramatica.y"
        {
          guardar_Tipo("USHORT");
        }
          break;
        case 100:
        // #line 264 "gramatica.y"
        {
          guardar_Tipo("LONG");
        }
          break;
        case 101:
        // #line 265 "gramatica.y"
        {
          System.out.println("Error: No es un tipo definido en linea " + Linea.getLinea());
        }
          break;
        case 102:
        // #line 268 "gramatica.y"
        {
          setear_Uso("Cadena", val_peek(0).sval);
          int aux = crear_terceto("PRINT", Integer.toString(TS.pertenece(val_peek(0).sval)), "-");
        }
          break;
        // #line 1219 "Parser.java"
        // ########## END OF USER-SUPPLIED ACTIONS ##########
      }// switch
       // #### Now let's reduce... ####
      if (yydebug)
        debug("reduce");
      state_drop(yym); // we just reduced yylen states
      yystate = state_peek(0); // get new state
      val_drop(yym); // corresponding value drop
      yym = yylhs[yyn]; // select next TERMINAL(on lhs)
      if (yystate == 0 && yym == 0)// done? 'rest' state and at first TERMINAL
      {
        if (yydebug)
          debug("After reduction, shifting from state 0 to state " + YYFINAL + "");
        yystate = YYFINAL; // explicitly say we're done
        state_push(YYFINAL); // and save it
        val_push(yyval); // also save the semantic value of parsing
        if (yychar < 0) // we want another character?
        {
          yychar = yylex(); // get next character
          if (yychar < 0)
            yychar = 0; // clean, if necessary
          if (yydebug)
            yylexdebug(yystate, yychar);
        }
        if (yychar == 0) // Good exit (if lex returns 0 ;-)
          break; // quit the loop--all DONE
      } // if yystate
      else // else not done yet
      { // get next state and push, for next yydefred[]
        yyn = yygindex[yym]; // find out where to go
        if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
          yystate = yytable[yyn]; // get new state
        else
          yystate = yydgoto[yym]; // else go to new defred
        if (yydebug)
          debug("after reduction, shifting from state " + state_peek(0) + " to state " + yystate + "");
        state_push(yystate); // going again, so push state & val...
        val_push(yyval); // for next action
      }
    } // main loop
    return 0;// yyaccept!!
  }
  // ## end of method parse() ######################################

  // ## run() --- for Thread #######################################
  /**
   * A default run method, used for operating this parser
   * object in the background. It is intended for extending Thread
   * or implementing Runnable. Turn off with -Jnorun .
   */
  public void run() {
    yyparse();
  }
  // ## end of method run() ########################################

  // ## Constructors ###############################################
  /**
   * Default constructor. Turn off with -Jnoconstruct .
   * 
   */
  public Parser() {
    // nothing to do
  }

  /**
   * Create a parser, setting the debug to true or false.
   * 
   * @param debugMe true for debugging, false for no debug.
   */
  public Parser(boolean debugMe) {
    yydebug = debugMe;
  }
  // ###############################################################

}
// ################### END OF CLASS ##############################
