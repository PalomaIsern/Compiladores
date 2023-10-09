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

import compiladores.Lexico;
import compiladores.Main;
import compiladores.TablaSimbolos;
import compiladores.Token;

//#line 24 "Parser.java"

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
      0, 2, 2, 4, 1, 1, 1, 1, 3, 3,
      3, 3, 7, 7, 7, 8, 8, 9, 9, 13,
      13, 14, 14, 11, 11, 11, 11, 12, 12, 15,
      15, 15, 16, 18, 18, 19, 20, 22, 6, 6,
      6, 6, 6, 6, 6, 5, 5, 5, 5, 26,
      10, 24, 24, 24, 24, 24, 24, 24, 24, 24,
      28, 28, 28, 29, 29, 29, 29, 29, 29, 29,
      30, 30, 30, 27, 17, 17, 23, 32, 32, 32,
      32, 32, 32, 21, 21, 21, 21, 21, 21, 31,
      31, 31, 31, 25,
  };
  final static short yylen[] = { 2,
      1, 2, 1, 1, 3, 3, 3, 1, 2, 2,
      2, 2, 3, 3, 3, 1, 1, 3, 1, 3,
      1, 1, 1, 1, 1, 2, 1, 1, 1, 3,
      7, 2, 2, 1, 1, 6, 3, 7, 1, 1,
      1, 1, 1, 1, 1, 1, 1, 1, 1, 3,
      3, 6, 4, 4, 6, 6, 6, 6, 3, 3,
      5, 5, 5, 1, 1, 1, 1, 1, 1, 1,
      4, 4, 4, 2, 3, 1, 2, 3, 2, 3,
      3, 2, 2, 4, 2, 4, 4, 2, 2, 1,
      1, 1, 1, 2,
  };
  final static short yydefred[] = { 0,
      0, 0, 0, 0, 0, 0, 92, 91, 90, 45,
      0, 0, 1, 0, 3, 0, 0, 39, 0, 49,
      48, 47, 34, 35, 40, 41, 42, 43, 46, 0,
      0, 25, 27, 16, 17, 0, 0, 0, 0, 0,
      0, 21, 0, 0, 77, 0, 0, 0, 0, 0,
      0, 94, 0, 0, 93, 0, 0, 2, 11, 9,
      12, 10, 0, 76, 0, 26, 0, 50, 82, 24,
      79, 0, 83, 0, 29, 28, 0, 0, 22, 23,
      0, 0, 0, 59, 60, 70, 64, 65, 66, 67,
      68, 69, 0, 0, 0, 0, 30, 0, 0, 0,
      0, 0, 5, 7, 0, 14, 15, 80, 78, 81,
      0, 20, 75, 0, 0, 54, 53, 0, 0, 0,
      0, 85, 0, 89, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 51, 62,
      61, 63, 55, 52, 57, 56, 58, 0, 86, 84,
      36, 87, 31,
  };
  final static short yydgoto[] = { 12,
      13, 14, 15, 137, 16, 17, 18, 40, 41, 19,
      42, 78, 43, 81, 20, 21, 44, 22, 23, 24,
      100, 0, 25, 26, 27, 28, 29, 51, 93, 0,
      30, 45,
  };
  final static short yysindex[] = { -109,
      0, 16, 29, -254, -232, -230, 0, 0, 0, 0,
      -151, 0, 0, -62, 0, -20, -15, 0, -27, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, -206,
      0, 0, 0, 0, 0, -193, -205, 43, -184, -29,
      87, 0, 39, 14, 0, 16, -29, -177, -171, -25,
      -109, 0, -84, 42, 0, 61, -159, 0, 0, 0,
      0, 0, 22, 0, 14, 0, 19, 0, 0, 0,
      0, 48, 0, 65, 0, 0, -156, -29, 0, 0,
      -29, -146, -25, 0, 0, 0, 0, 0, 0, 0,
      0, 0, -29, -136, -144, -151, 0, -33, -137, 15,
      -123, 0, 0, 0, 91, 0, 0, 0, 0, 0,
      39, 0, 0, -29, 90, 0, 0, -109, -109, 74,
      0, 0, -118, 0, -151, 99, -116, 49, -113, -134,
      -117, -115, -112, 9, -41, -151, 20, -106, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 35, 0, 0,
      0, 0, 0,
  };
  final static short yyrindex[] = { 0,
      1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, -6, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      -1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, -39, 27, 0, -30, 0, 0, 102, 0,
      0, 0, 109, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 57, 0, 34, 0, 0, 0,
      0, 0, 0, 58, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 89, 0, 0, 0, 0, 59,
      0, 3, 0, 0, 60, 0, 0, 0, 0, 0,
      -34, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      55, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      89, 0, 89, 0, 0, 36, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0,
  };
  final static short yygindex[] = { 0,
      45, 11, 73, 0, 0, 0, 0, 0, 28, 103,
      -48, 0, 84, 0, 0, 0, 137, 0, 0, 0,
      0, 0, 131, 0, 0, 0, 0, 0, 86, 0,
      -18, 0,
  };
  final static int YYTABLESIZE = 376;
  static short yytable[];
  static {
    yytable();
  }

  static void yytable() {
    yytable = new short[] { 150,
        8, 19, 6, 19, 19, 19, 18, 122, 18, 18,
        18, 24, 24, 11, 107, 36, 24, 76, 52, 75,
        19, 56, 19, 60, 53, 18, 54, 18, 62, 24,
        50, 24, 112, 63, 88, 101, 87, 44, 96, 24,
        24, 24, 76, 24, 8, 24, 6, 48, 38, 39,
        64, 67, 148, 36, 37, 38, 39, 76, 38, 39,
        36, 37, 57, 36, 66, 72, 36, 74, 47, 34,
        33, 73, 82, 36, 83, 49, 34, 51, 84, 123,
        79, 98, 99, 71, 85, 80, 58, 36, 109, 141,
        76, 76, 75, 75, 51, 94, 104, 97, 88, 110,
        74, 13, 37, 24, 55, 2, 120, 76, 3, 75,
        113, 4, 5, 6, 7, 8, 9, 119, 124, 116,
        115, 143, 10, 95, 117, 118, 144, 77, 58, 76,
        129, 75, 76, 126, 75, 136, 127, 125, 135, 138,
        139, 128, 142, 145, 151, 146, 1, 2, 147, 152,
        3, 11, 32, 4, 5, 6, 7, 8, 9, 153,
        4, 111, 130, 132, 10, 106, 65, 68, 114, 0,
        0, 1, 2, 0, 0, 3, 0, 88, 4, 5,
        6, 7, 8, 9, 0, 103, 0, 0, 0, 10,
        131, 133, 58, 55, 2, 0, 0, 3, 103, 0,
        4, 5, 6, 7, 8, 9, 0, 0, 58, 0,
        0, 10, 0, 3, 149, 0, 19, 0, 0, 0,
        0, 18, 121, 0, 0, 24, 3, 70, 32, 33,
        86, 0, 7, 8, 9, 59, 19, 19, 19, 19,
        61, 18, 18, 18, 18, 24, 24, 24, 24, 44,
        89, 90, 91, 92, 76, 0, 8, 93, 6, 93,
        0, 8, 8, 6, 6, 31, 32, 33, 0, 0,
        0, 0, 31, 32, 33, 70, 32, 33, 105, 32,
        33, 0, 33, 35, 1, 46, 32, 33, 3, 51,
        35, 4, 5, 6, 7, 8, 9, 55, 69, 70,
        32, 33, 10, 108, 140, 0, 0, 7, 8, 9,
        88, 93, 74, 13, 37, 24, 102, 2, 0, 0,
        3, 0, 0, 4, 5, 6, 7, 8, 9, 102,
        134, 0, 0, 3, 10, 0, 4, 5, 6, 7,
        8, 9, 0, 0, 3, 3, 0, 10, 3, 0,
        0, 3, 3, 3, 3, 3, 3, 0, 3, 0,
        0, 3, 3, 0, 3, 3, 3, 3, 3, 3,
        0, 0, 0, 0, 0, 3,
    };
  }

  static short yycheck[];
  static {
    yycheck();
  }

  static void yycheck() {
    yycheck = new short[] { 41,
        0, 41, 0, 43, 44, 45, 41, 41, 43, 44,
        45, 42, 43, 123, 63, 45, 47, 43, 273, 45,
        60, 11, 62, 44, 257, 60, 257, 62, 44, 60,
        3, 62, 81, 61, 60, 54, 62, 44, 123, 41,
        42, 43, 44, 45, 44, 47, 44, 3, 40, 41,
        257, 257, 44, 45, 46, 40, 41, 59, 40, 41,
        45, 46, 125, 45, 258, 38, 45, 40, 40, 61,
        44, 256, 59, 45, 47, 3, 61, 44, 256, 98,
        42, 40, 41, 41, 256, 47, 14, 45, 41, 41,
        43, 43, 45, 45, 61, 51, 256, 53, 44, 256,
        44, 44, 44, 44, 256, 257, 96, 43, 260, 45,
        257, 263, 264, 265, 266, 267, 268, 262, 256, 256,
        93, 256, 274, 51, 261, 262, 261, 41, 56, 43,
        41, 45, 43, 257, 45, 125, 46, 123, 257, 41,
        257, 114, 256, 261, 125, 261, 256, 257, 261, 256,
        260, 123, 44, 263, 264, 265, 266, 267, 268, 125,
        125, 78, 118, 119, 274, 63, 30, 37, 83, -1,
        -1, 256, 257, -1, -1, 260, -1, 123, 263, 264,
        265, 266, 267, 268, -1, 125, -1, -1, -1, 274,
        118, 119, 120, 256, 257, -1, -1, 260, 125, -1,
        263, 264, 265, 266, 267, 268, -1, -1, 136, -1,
        -1, 274, -1, 125, 256, -1, 256, -1, -1, -1,
        -1, 256, 256, -1, -1, 256, 125, 257, 258, 259,
        256, -1, 266, 267, 268, 256, 276, 277, 278, 279,
        256, 276, 277, 278, 279, 276, 277, 278, 279, 256,
        276, 277, 278, 279, 256, -1, 256, 257, 256, 257,
        -1, 261, 262, 261, 262, 257, 258, 259, -1, -1,
        -1, -1, 257, 258, 259, 257, 258, 259, 257, 258,
        259, -1, 256, 275, 256, 257, 258, 259, 260, 256,
        275, 263, 264, 265, 266, 267, 268, 256, 256, 257,
        258, 259, 274, 256, 256, -1, -1, 266, 267, 268,
        256, 257, 256, 256, 256, 256, 256, 257, -1, -1,
        260, -1, -1, 263, 264, 265, 266, 267, 268, 256,
        257, -1, -1, 260, 274, -1, 263, 264, 265, 266,
        267, 268, -1, -1, 256, 257, -1, 274, 260, -1,
        -1, 263, 264, 265, 266, 267, 268, -1, 257, -1,
        -1, 260, 274, -1, 263, 264, 265, 266, 267, 268,
        -1, -1, -1, -1, -1, 274,
    };
  }

  final static short YYFINAL = 12;
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
      "CADENA", "RETURN", "\"+=\"", "\">=\"", "\"<=\"", "\"!!\"", "\"==\"",
  };
  final static String yyrule[] = {
      "$accept : program",
      "program : bloque_de_Sentencias",
      "conjuntoSentencias : conjuntoSentencias sentencia",
      "conjuntoSentencias : sentencia",
      "cuerpo_funcion : conjuntoSentencias",
      "bloque_de_Sentencias : '{' conjuntoSentencias '}'",
      "bloque_de_Sentencias : '{' conjuntoSentencias error",
      "bloque_de_Sentencias : conjuntoSentencias '}' error",
      "bloque_de_Sentencias : error",
      "sentencia : sentenciaDeclarativa ','",
      "sentencia : sentenciaEjecutable ','",
      "sentencia : sentenciaDeclarativa error",
      "sentencia : sentenciaEjecutable error",
      "asignacion : ID simboloAsignacion expresion",
      "asignacion : atributo_objeto '=' atributo_objeto",
      "asignacion : atributo_objeto '=' factor",
      "simboloAsignacion : '='",
      "simboloAsignacion : \"+=\"",
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
      "declaracionClase : CLASS ID bloque_de_Sentencias",
      "declaracionClase : CLASS ID '{' conjuntoSentencias ID ',' '}'",
      "declaracionClase : CLASS ID",
      "declaracionObjeto : ID lista_Variables",
      "declaracionFuncion : funcion_VOID",
      "declaracionFuncion : funcion_VOID_vacia",
      "funcion_VOID : VOID ID parametro_formal '{' cuerpo_funcion '}'",
      "funcion_VOID_vacia : VOID ID parametro_formal",
      "clausula_IMPL : IMPL FOR ID ':' '{' funcion_VOID '}'",
      "sentenciaEjecutable : asignacion",
      "sentenciaEjecutable : invocacionFuncion",
      "sentenciaEjecutable : clausula_seleccion",
      "sentenciaEjecutable : print",
      "sentenciaEjecutable : metodo_objeto",
      "sentenciaEjecutable : atributo_objeto",
      "sentenciaEjecutable : RETURN",
      "sentenciaDeclarativa : declaracion",
      "sentenciaDeclarativa : declaracionFuncion",
      "sentenciaDeclarativa : declaracionObjeto",
      "sentenciaDeclarativa : declaracionClase",
      "metodo_objeto : ID '.' invocacionFuncion",
      "atributo_objeto : ID '.' ID",
      "clausula_seleccion : IF condicion bloque_de_Sentencias ELSE bloque_de_Sentencias END_IF",
      "clausula_seleccion : IF condicion bloque_de_Sentencias END_IF",
      "clausula_seleccion : IF condicion bloque_de_Sentencias error",
      "clausula_seleccion : IF condicion bloque_de_Sentencias ELSE bloque_de_Sentencias error",
      "clausula_seleccion : IF condicion sentencia ELSE bloque_de_Sentencias END_IF",
      "clausula_seleccion : IF condicion bloque_de_Sentencias ELSE sentencia END_IF",
      "clausula_seleccion : IF condicion sentencia ELSE sentencia END_IF",
      "clausula_seleccion : IF bloque_de_Sentencias error",
      "clausula_seleccion : IF sentencia error",
      "condicion : '(' expresion comparador expresion ')'",
      "condicion : '(' expresion comparador expresion error",
      "condicion : expresion comparador expresion ')' error",
      "comparador : '>'",
      "comparador : '<'",
      "comparador : \">=\"",
      "comparador : \"<=\"",
      "comparador : \"!!\"",
      "comparador : \"==\"",
      "comparador : error",
      "sentencia_de_Control : DO bloque_de_Sentencias UNTIL condicion",
      "sentencia_de_Control : DO sentencia UNTIL condicion",
      "sentencia_de_Control : DO sentencia UNTIL error",
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

  // #line 176 "gramatica.y"

  Lexico lex;
  TablaSimbolos TS;

  private int yylex() {
    Token token = new Token();
    try {
      token = lex.getToken();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (token != null) {
      yylval = new ParserVal(token.getLexema());
      return token.getIdToken();
    } else
      TS.imprimirContenido();
    return 0;
  }

  public void yyerror(String error) {
    System.out.println(error);
  }

  // #line 437 "Parser.java"
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
        case 6:
        // #line 26 "gramatica.y"
        {
          System.out.print("Falta llave que cierra en linea " + Main.getLinea());
        }
          break;
        case 7:
        // #line 27 "gramatica.y"
        {
          System.out.print("Falta llave que abre en linea" + Main.getLinea());
        }
          break;
        case 8:
        // #line 28 "gramatica.y"
        {
          System.out.print("error en linea " + Main.getLinea());
        }
          break;
        case 11:
        // #line 33 "gramatica.y"
        {
          System.out.print("Falta , en linea " + Main.getLinea());
        }
          break;
        case 12:
        // #line 34 "gramatica.y"
        {
          System.out.print("Falta , en linea " + Main.getLinea());
        }
          break;
        case 26:
        // #line 60 "gramatica.y"
        {
          System.out.print("Se reconocio constante negativa en linea " + Main.getLinea());
        }
          break;
        case 30:
        // #line 68 "gramatica.y"
        {
          System.out.print("Se reconocio una clase");
        }
          break;
        case 31:
        // #line 69 "gramatica.y"
        {
          System.out.print("Se reconocio una clase con herencia por composicion");
        }
          break;
        case 32:
        // #line 70 "gramatica.y"
        {
          System.out.print("Se reconocio una clase");
        }
          break;
        case 36:
        // #line 79 "gramatica.y"
        {
          System.out.print("Se reconocio una funcion void");
        }
          break;
        case 37:
        // #line 82 "gramatica.y"
        {
          System.out.print("Se reconocio una funcion void vacia");
        }
          break;
        case 38:
        // #line 85 "gramatica.y"
        {
          System.out.print("Se reconocio sentencia IMPL FOR");
        }
          break;
        case 50:
        // #line 103 "gramatica.y"
        {
          System.out.print("se reconocio la invocacion de un metodo de un objeto en linea " + Main.getLinea());
        }
          break;
        case 52:
        // #line 109 "gramatica.y"
        {
          System.out.print("Se reconocio un IF");
        }
          break;
        case 53:
        // #line 110 "gramatica.y"
        {
          System.out.print("Se reconocio un IF");
        }
          break;
        case 54:
        // #line 111 "gramatica.y"
        {
          System.out.print("Falta el END_IF");
        }
          break;
        case 55:
        // #line 112 "gramatica.y"
        {
          System.out.print("Falta el END_IF");
        }
          break;
        case 56:
        // #line 113 "gramatica.y"
        {
          System.out.print("Se reconocio un IF");
        }
          break;
        case 57:
        // #line 114 "gramatica.y"
        {
          System.out.print("Se reconocio un IF");
        }
          break;
        case 58:
        // #line 115 "gramatica.y"
        {
          System.out.print("Se reconocio un IF");
        }
          break;
        case 59:
        // #line 116 "gramatica.y"
        {
          System.out.print("Falto la condicion del IF");
        }
          break;
        case 60:
        // #line 117 "gramatica.y"
        {
          System.out.print("Falto la condicion del IF");
        }
          break;
        case 62:
        // #line 121 "gramatica.y"
        {
          System.out.print("Falta el parentesis que cierra en linea: " + Main.getLinea());
        }
          break;
        case 63:
        // #line 122 "gramatica.y"
        {
          System.out.print("Falta el parentesis que abre en linea: " + Main.getLinea());
        }
          break;
        case 70:
        // #line 131 "gramatica.y"
        {
          System.out.print("No se reconoce como comparador");
        }
          break;
        case 71:
        // #line 134 "gramatica.y"
        {
          System.out.print("Se reconocio sentencia DO UNTIL");
        }
          break;
        case 72:
        // #line 135 "gramatica.y"
        {
          System.out.print("Se reconocio sentencia DO UNTIL");
        }
          break;
        case 73:
        // #line 136 "gramatica.y"
        {
          System.out.print("Falta la condicion en linea: " + Main.getLinea());
        }
          break;
        case 77:
        // #line 146 "gramatica.y"
        {
          System.out.print("Se reconocio una invocacion de una funcion");
        }
          break;
        case 80:
        // #line 151 "gramatica.y"
        {
          System.out.print("Falta el parentesis que cierra en linea: " + Main.getLinea());
        }
          break;
        case 81:
        // #line 152 "gramatica.y"
        {
          System.out.print("Falta el parentesis que abre en linea: " + Main.getLinea());
        }
          break;
        case 82:
        // #line 153 "gramatica.y"
        {
          System.out.print("Falta el parentesis que cierra en linea: " + Main.getLinea());
        }
          break;
        case 83:
        // #line 154 "gramatica.y"
        {
          System.out.print("Falta el parentesis que abre en linea: " + Main.getLinea());
        }
          break;
        case 86:
        // #line 159 "gramatica.y"
        {
          System.out.print("Falta el parentesis que cierra en linea: " + Main.getLinea());
        }
          break;
        case 87:
        // #line 160 "gramatica.y"
        {
          System.out.print("Falta el parentesis que abre en linea: " + Main.getLinea());
        }
          break;
        case 88:
        // #line 161 "gramatica.y"
        {
          System.out.print("Falta el parentesis que cierra en linea: " + Main.getLinea());
        }
          break;
        case 89:
        // #line 162 "gramatica.y"
        {
          System.out.print("Falta el parentesis que abre en linea: " + Main.getLinea());
        }
          break;
        case 93:
        // #line 168 "gramatica.y"
        {
          System.out.print("No es un tipo definido");
        }
          break;
        case 94:
        // #line 171 "gramatica.y"
        {
          System.out.print("Se reconocio una impresion por pantalla");
        }
          break;
        // #line 742 "Parser.java"
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
