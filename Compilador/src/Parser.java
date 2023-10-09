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
      0, 2, 2, 4, 1, 3, 3, 7, 7, 7,
      8, 8, 9, 9, 13, 13, 14, 14, 11, 11,
      11, 11, 12, 12, 15, 15, 15, 16, 18, 18,
      19, 20, 22, 6, 6, 6, 6, 6, 6, 6,
      6, 6, 5, 5, 5, 5, 26, 10, 24, 24,
      24, 24, 24, 24, 24, 24, 29, 29, 29, 30,
      30, 30, 30, 30, 30, 30, 27, 27, 27, 28,
      17, 17, 23, 32, 32, 32, 32, 32, 32, 21,
      21, 21, 21, 21, 21, 31, 31, 31, 31, 25,
  };
  final static short yylen[] = { 2,
      1, 2, 1, 1, 3, 2, 2, 3, 3, 3,
      1, 1, 3, 1, 3, 1, 1, 1, 1, 1,
      2, 1, 1, 1, 3, 7, 2, 2, 1, 1,
      6, 3, 7, 1, 1, 1, 1, 1, 1, 1,
      1, 1, 1, 1, 1, 1, 3, 3, 6, 4,
      4, 6, 6, 6, 6, 3, 5, 5, 5, 1,
      1, 1, 1, 1, 1, 1, 4, 4, 4, 2,
      3, 1, 2, 3, 2, 3, 3, 2, 2, 4,
      2, 4, 4, 2, 2, 1, 1, 1, 1, 2,
  };
  final static short yydefred[] = { 0,
      0, 0, 1, 89, 0, 0, 0, 0, 0, 88,
      87, 86, 0, 0, 42, 0, 3, 0, 0, 34,
      0, 46, 45, 44, 29, 30, 40, 35, 36, 37,
      38, 41, 43, 0, 0, 20, 22, 11, 12, 0,
      0, 0, 0, 0, 0, 16, 0, 0, 73, 19,
      0, 0, 0, 0, 90, 0, 0, 0, 0, 0,
      5, 2, 6, 7, 0, 72, 0, 21, 0, 47,
      78, 75, 0, 79, 0, 24, 23, 0, 0, 17,
      18, 0, 0, 0, 56, 66, 60, 61, 62, 63,
      64, 65, 0, 0, 0, 0, 25, 0, 0, 0,
      0, 0, 0, 0, 0, 9, 10, 76, 74, 77,
      0, 15, 71, 0, 0, 51, 50, 0, 0, 0,
      0, 81, 0, 85, 0, 0, 67, 69, 68, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 48, 58, 57, 59, 52, 49, 54,
      53, 55, 0, 82, 80, 31, 83, 0, 0, 26,
      0, 33, 0,
  };
  final static short yydgoto[] = { 2,
      3, 16, 17, 141, 18, 19, 20, 44, 45, 21,
      46, 79, 47, 82, 22, 23, 48, 24, 25, 26,
      100, 27, 28, 29, 30, 31, 32, 33, 54, 93,
      34, 49,
  };
  final static short yysindex[] = { -94,
      -157, 0, 0, 0, -2, -28, -240, -184, -183, 0,
      0, 0, -109, -188, 0, -84, 0, 52, 53, 0,
      37, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, -156, 0, 0, 0, 0, 0, -153,
      -144, 21, -141, -5, 75, 0, -24, 63, 0, 0,
      -5, -132, -32, -109, 0, 3, 8, -145, -140, -124,
      0, 0, 0, 0, 27, 0, 63, 0, -25, 0,
      0, 0, 15, 0, 42, 0, 0, -122, -5, 0,
      0, -5, -121, -32, 0, 0, 0, 0, 0, 0,
      0, 0, -5, -170, -127, -157, 0, -40, -119, 16,
      -117, 10, 2, 80, 97, 0, 0, 0, 0, 0,
      -24, 0, 0, -5, 78, 0, 0, -109, -109, -68,
      0, 0, -113, 0, -157, 104, 0, 0, 0, 23,
      -107, 26, -104, -168, -100, -98, -97, -9, -35, -157,
      43, -87, -95, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 46, 0, 0, 0, 0, -83, 50, 0,
      8, 0, 16,
  };
  final static short yyrindex[] = { 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      133, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 35, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, -41, 134, 0, 0,
      0, 0, 0, 0, 0, 142, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 147, 0, -10, 0,
      0, 0, 0, 0, 149, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 150,
      0, 0, 0, 0, 158, 0, 0, 0, 0, 0,
      -36, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      -34, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 79,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0,
  };
  final static short yygindex[] = { 0,
      48, -50, 9, 0, 0, 0, 0, 0, 39, 140,
      -12, 0, 128, 0, 0, 0, 174, 0, 66, 0,
      49, 0, 170, 0, 0, 0, 0, 0, -38, 129,
      -30, 0,
  };
  final static int YYTABLESIZE = 286;
  static short yytable[];
  static {
    yytable();
  }

  static void yytable() {
    yytable = new short[] { 14,
        122, 14, 14, 14, 13, 155, 13, 13, 13, 84,
        77, 51, 76, 1, 42, 43, 40, 80, 14, 40,
        14, 59, 81, 13, 62, 13, 101, 88, 1, 87,
        42, 43, 55, 48, 153, 40, 41, 42, 43, 40,
        61, 51, 40, 41, 53, 120, 40, 98, 99, 51,
        48, 38, 107, 52, 40, 109, 61, 77, 38, 76,
        58, 72, 95, 127, 129, 40, 146, 123, 77, 112,
        76, 40, 56, 57, 140, 19, 19, 19, 72, 19,
        73, 19, 75, 60, 77, 116, 76, 148, 84, 84,
        117, 118, 149, 72, 1, 63, 64, 65, 4, 5,
        66, 94, 6, 97, 68, 7, 8, 9, 10, 11,
        12, 13, 69, 14, 74, 78, 15, 77, 133, 76,
        77, 83, 76, 85, 102, 96, 135, 137, 62, 103,
        101, 115, 104, 110, 119, 113, 124, 130, 125, 126,
        53, 53, 131, 139, 142, 143, 4, 5, 62, 144,
        6, 147, 132, 7, 8, 9, 10, 11, 12, 13,
        150, 14, 151, 152, 15, 134, 136, 156, 157, 158,
        160, 4, 5, 161, 162, 6, 39, 28, 7, 8,
        9, 10, 11, 12, 13, 27, 14, 4, 138, 15,
        70, 6, 8, 32, 7, 8, 9, 10, 11, 12,
        13, 19, 14, 4, 106, 15, 111, 67, 159, 163,
        70, 0, 114, 0, 14, 121, 0, 0, 0, 13,
        154, 0, 89, 86, 0, 10, 11, 12, 50, 36,
        37, 50, 36, 37, 14, 14, 14, 14, 0, 13,
        13, 13, 13, 89, 90, 91, 92, 35, 36, 37,
        0, 50, 36, 37, 35, 36, 37, 128, 50, 36,
        37, 0, 0, 4, 0, 39, 50, 36, 37, 0,
        108, 0, 39, 10, 11, 12, 71, 50, 36, 37,
        0, 145, 0, 105, 36, 37,
    };
  }

  static short yycheck[];
  static {
    yycheck();
  }

  static void yycheck() {
    yycheck = new short[] { 41,
        41, 43, 44, 45, 41, 41, 43, 44, 45, 44,
        43, 40, 45, 123, 40, 41, 45, 42, 60, 45,
        62, 13, 47, 60, 16, 62, 57, 60, 123, 62,
        40, 41, 273, 44, 44, 45, 46, 40, 41, 45,
        125, 40, 45, 46, 6, 96, 45, 40, 41, 40,
        61, 61, 65, 6, 45, 41, 125, 43, 61, 45,
        13, 41, 54, 102, 103, 45, 41, 98, 43, 82,
        45, 45, 257, 257, 125, 41, 42, 43, 44, 45,
        42, 47, 44, 272, 43, 256, 45, 256, 123, 51,
        261, 262, 261, 59, 123, 44, 44, 61, 256, 257,
        257, 54, 260, 56, 258, 263, 264, 265, 266, 267,
        268, 269, 257, 271, 256, 41, 274, 43, 41, 45,
        43, 59, 45, 256, 270, 123, 118, 119, 120, 270,
        161, 93, 257, 256, 262, 257, 256, 58, 123, 257,
        102, 103, 46, 257, 41, 123, 256, 257, 140, 257,
        260, 256, 114, 263, 264, 265, 266, 267, 268, 269,
        261, 271, 261, 261, 274, 118, 119, 125, 256, 265,
        125, 256, 257, 257, 125, 260, 44, 44, 263, 264,
        265, 266, 267, 268, 269, 44, 271, 256, 257, 274,
        44, 260, 44, 44, 263, 264, 265, 266, 267, 268,
        269, 44, 271, 125, 65, 274, 79, 34, 143, 161,
        41, -1, 84, -1, 256, 256, -1, -1, -1, 256,
        256, -1, 257, 256, -1, 266, 267, 268, 257, 258,
        259, 257, 258, 259, 276, 277, 278, 279, -1, 276,
        277, 278, 279, 276, 277, 278, 279, 257, 258, 259,
        -1, 257, 258, 259, 257, 258, 259, 256, 257, 258,
        259, -1, -1, 256, -1, 275, 257, 258, 259, -1,
        256, -1, 275, 266, 267, 268, 256, 257, 258, 259,
        -1, 256, -1, 257, 258, 259,
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
      "CADENA", "RETURN", "\"+=\"", "\">=\"", "\"<=\"", "\"!!\"", "\"==\"",
  };
  final static String yyrule[] = {
      "$accept : program",
      "program : bloque_de_Sentencias",
      "conjuntoSentencias : conjuntoSentencias sentencia",
      "conjuntoSentencias : sentencia",
      "cuerpo_funcion : conjuntoSentencias",
      "bloque_de_Sentencias : '{' conjuntoSentencias '}'",
      "sentencia : sentenciaDeclarativa ','",
      "sentencia : sentenciaEjecutable ','",
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
      "sentenciaEjecutable : clausula_IMPL",
      "sentenciaEjecutable : sentencia_de_Control",
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

  // #line 173 "gramatica.y"

  Lexico lex;
  TablaSimbolos TS = new TablaSimbolos();

  public Parser(Lexico lexico) {
    this.lex = lexico;
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
      return token.getIdToken();
    } else
      TS.imprimirContenido();
    return 0;
  }

  public void yyerror(String error) {
    System.out.println(error);
  }

  // #line 420 "Parser.java"
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
        case 21:
        // #line 55 "gramatica.y"
        {
          System.out.print("Se reconocio constante negativa en linea " + Main.getLinea());
        }
          break;
        case 25:
        // #line 63 "gramatica.y"
        {
          System.out.print("Se reconocio una clase");
        }
          break;
        case 26:
        // #line 64 "gramatica.y"
        {
          System.out.print("Se reconocio una clase con herencia por composicion");
        }
          break;
        case 27:
        // #line 65 "gramatica.y"
        {
          System.out.print("Se reconocio una clase");
        }
          break;
        case 31:
        // #line 75 "gramatica.y"
        {
          System.out.print("Se reconocio una funcion void");
        }
          break;
        case 32:
        // #line 78 "gramatica.y"
        {
          System.out.print("Se reconocio una funcion void vacia");
        }
          break;
        case 33:
        // #line 81 "gramatica.y"
        {
          System.out.print("Se reconocio sentencia IMPL FOR");
        }
          break;
        case 47:
        // #line 101 "gramatica.y"
        {
          System.out.print("se reconocio la invocacion de un metodo de un objeto en linea " + Main.getLinea());
        }
          break;
        case 49:
        // #line 107 "gramatica.y"
        {
          System.out.print("Se reconocio un IF");
        }
          break;
        case 50:
        // #line 108 "gramatica.y"
        {
          System.out.print("Se reconocio un IF");
        }
          break;
        case 51:
        // #line 109 "gramatica.y"
        {
          System.out.print("Falta el END_IF");
        }
          break;
        case 52:
        // #line 110 "gramatica.y"
        {
          System.out.print("Falta el END_IF");
        }
          break;
        case 53:
        // #line 111 "gramatica.y"
        {
          System.out.print("Se reconocio un IF");
        }
          break;
        case 54:
        // #line 112 "gramatica.y"
        {
          System.out.print("Se reconocio un IF");
        }
          break;
        case 55:
        // #line 113 "gramatica.y"
        {
          System.out.print("Se reconocio un IF");
        }
          break;
        case 56:
        // #line 114 "gramatica.y"
        {
          System.out.print("Falto la condicion del IF");
        }
          break;
        case 58:
        // #line 118 "gramatica.y"
        {
          System.out.print("Falta el parentesis que cierra en linea: " + Main.getLinea());
        }
          break;
        case 59:
        // #line 119 "gramatica.y"
        {
          System.out.print("Falta el parentesis que abre en linea: " + Main.getLinea());
        }
          break;
        case 66:
        // #line 128 "gramatica.y"
        {
          System.out.print("No se reconoce como comparador");
        }
          break;
        case 67:
        // #line 131 "gramatica.y"
        {
          System.out.print("Se reconocio sentencia DO UNTIL");
        }
          break;
        case 68:
        // #line 132 "gramatica.y"
        {
          System.out.print("Se reconocio sentencia DO UNTIL");
        }
          break;
        case 69:
        // #line 133 "gramatica.y"
        {
          System.out.print("Falta la condicion en linea: " + Main.getLinea());
        }
          break;
        case 73:
        // #line 143 "gramatica.y"
        {
          System.out.print("Se reconocio una invocacion de una funcion");
        }
          break;
        case 76:
        // #line 148 "gramatica.y"
        {
          System.out.print("Falta el parentesis que cierra en linea: " + Main.getLinea());
        }
          break;
        case 77:
        // #line 149 "gramatica.y"
        {
          System.out.print("Falta el parentesis que abre en linea: " + Main.getLinea());
        }
          break;
        case 78:
        // #line 150 "gramatica.y"
        {
          System.out.print("Falta el parentesis que cierra en linea: " + Main.getLinea());
        }
          break;
        case 79:
        // #line 151 "gramatica.y"
        {
          System.out.print("Falta el parentesis que abre en linea: " + Main.getLinea());
        }
          break;
        case 82:
        // #line 156 "gramatica.y"
        {
          System.out.print("Falta el parentesis que cierra en linea: " + Main.getLinea());
        }
          break;
        case 83:
        // #line 157 "gramatica.y"
        {
          System.out.print("Falta el parentesis que abre en linea: " + Main.getLinea());
        }
          break;
        case 84:
        // #line 158 "gramatica.y"
        {
          System.out.print("Falta el parentesis que cierra en linea: " + Main.getLinea());
        }
          break;
        case 85:
        // #line 159 "gramatica.y"
        {
          System.out.print("Falta el parentesis que abre en linea: " + Main.getLinea());
        }
          break;
        case 89:
        // #line 165 "gramatica.y"
        {
          System.out.print("No es un tipo definido");
        }
          break;
        case 90:
        // #line 168 "gramatica.y"
        {
          System.out.print("Se reconocio una impresion por pantalla");
        }
          break;
        // #line 701 "Parser.java"
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
