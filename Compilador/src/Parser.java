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
import compiladores.Linea;
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
      0, 2, 2, 4, 4, 6, 1, 7, 3, 3,
      9, 9, 10, 10, 10, 11, 11, 11, 12, 12,
      16, 16, 17, 17, 14, 14, 14, 14, 15, 15,
      18, 18, 18, 19, 21, 21, 22, 23, 25, 5,
      5, 5, 5, 5, 5, 5, 5, 8, 8, 8,
      8, 29, 13, 32, 32, 32, 32, 32, 32, 32,
      33, 33, 33, 27, 27, 27, 27, 27, 27, 27,
      27, 27, 30, 30, 30, 31, 20, 20, 26, 35,
      35, 35, 35, 35, 35, 24, 24, 24, 24, 24,
      24, 34, 34, 34, 34, 28,
  };
  final static short yylen[] = { 2,
      1, 2, 1, 2, 1, 1, 3, 3, 2, 2,
      1, 1, 3, 3, 3, 1, 1, 1, 3, 1,
      3, 1, 1, 1, 1, 1, 2, 1, 1, 1,
      3, 7, 2, 2, 1, 1, 6, 3, 7, 1,
      1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
      1, 3, 3, 1, 1, 1, 1, 1, 1, 1,
      5, 5, 5, 6, 4, 4, 6, 6, 6, 6,
      3, 4, 4, 4, 4, 2, 3, 1, 2, 3,
      2, 3, 3, 2, 2, 4, 2, 4, 4, 2,
      2, 1, 1, 1, 1, 2,
  };
  final static short yydefred[] = { 0,
      0, 0, 1, 95, 0, 0, 0, 0, 0, 94,
      93, 92, 0, 0, 47, 0, 3, 0, 0, 40,
      0, 51, 50, 49, 35, 36, 45, 41, 42, 43,
      44, 46, 48, 0, 18, 0, 26, 28, 17, 16,
      0, 0, 0, 0, 0, 0, 22, 0, 0, 79,
      25, 0, 0, 0, 0, 96, 0, 0, 0, 0,
      0, 0, 0, 7, 2, 12, 11, 10, 9, 0,
      78, 0, 27, 0, 52, 84, 81, 0, 85, 0,
      30, 29, 0, 0, 23, 24, 0, 0, 0, 71,
      60, 57, 56, 58, 59, 54, 55, 0, 0, 0,
      0, 31, 0, 0, 0, 0, 0, 5, 0, 0,
      0, 0, 14, 15, 82, 80, 83, 0, 21, 77,
      0, 0, 66, 65, 0, 72, 0, 0, 0, 87,
      0, 91, 0, 0, 8, 4, 75, 74, 73, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 53, 62, 61, 63, 67, 64, 69,
      68, 70, 0, 88, 86, 37, 89, 0, 0, 32,
      0, 39, 0,
  };
  final static short yydgoto[] = { 2,
      3, 16, 17, 107, 18, 151, 62, 19, 68, 20,
      45, 46, 21, 47, 84, 48, 87, 22, 23, 49,
      24, 25, 26, 105, 27, 28, 29, 30, 31, 32,
      33, 98, 55, 34, 50,
  };
  final static short yysindex[] = { -113,
      137, 0, 0, 0, 8, -34, -239, -219, -199, 0,
      0, 0, -100, -204, 0, -78, 0, -19, -19, 0,
      10, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, -182, 0, 0, 0, 0, 0, 0,
      -172, -165, 86, -156, 89, 74, 0, 65, 39, 0,
      0, 89, -155, 19, -115, 0, -3, 69, 15, -158,
      -164, -152, -143, 0, 0, 0, 0, 0, 0, 92,
      0, 39, 0, 25, 0, 0, 0, -36, 0, -23,
      0, 0, -133, 89, 0, 0, 89, -132, 19, 0,
      0, 0, 0, 0, 0, 0, 0, 89, -189, -184,
      137, 0, 22, -127, 13, -124, 117, 0, 51, 81,
      66, 93, 0, 0, 0, 0, 0, 65, 0, 0,
      89, 87, 0, 0, -115, 0, -115, 96, 0, 0,
      -119, 0, 137, 99, 0, 0, 0, 0, 0, 20,
      -111, -1, -112, -171, -114, -106, -103, -9, -24, 137,
      36, -94, -101, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 41, 0, 0, 0, 0, -87, 48, 0,
      69, 0, 13,
  };
  final static short yyrindex[] = { 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, -29, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, -41, -15, 0,
      0, 0, 0, 0, 0, 0, -11, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, -5, 0, 114, 0, 0, 0, 0, 0, 43,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 7, 0, 0, 0, 0, 0,
      0, 59, 0, 0, 0, 0, 0, -17, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, -43, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 52,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0,
  };
  final static short yygindex[] = { 0,
      40, -51, 67, 0, 28, 0, 0, 0, 157, 0,
      0, 275, 110, -13, 0, 97, 0, 0, 0, 149,
      0, 44, 0, 27, 0, 158, 0, 0, 0, 0,
      0, 112, -16, 1, 0,
  };
  final static int YYTABLESIZE = 411;
  static short yytable[];
  static {
    yytable();
  }

  static void yytable() {
    yytable = new short[] { 20,
        90, 20, 20, 20, 116, 52, 82, 1, 81, 1,
        41, 25, 25, 25, 78, 25, 165, 25, 20, 82,
        20, 81, 60, 19, 67, 19, 19, 19, 34, 78,
        43, 44, 33, 56, 163, 41, 42, 57, 76, 156,
        61, 82, 19, 81, 19, 53, 64, 43, 44, 128,
        38, 40, 41, 42, 43, 44, 114, 58, 106, 41,
        42, 82, 130, 81, 43, 44, 123, 63, 40, 41,
        70, 124, 125, 119, 71, 40, 126, 127, 97, 90,
        96, 150, 65, 20, 158, 73, 13, 108, 1, 159,
        52, 74, 138, 139, 99, 41, 102, 88, 59, 79,
        90, 6, 25, 131, 7, 109, 85, 19, 103, 104,
        13, 86, 14, 111, 83, 15, 82, 110, 81, 101,
        52, 100, 117, 140, 120, 41, 77, 143, 132, 82,
        41, 81, 134, 41, 136, 133, 41, 149, 141, 152,
        4, 5, 153, 157, 6, 154, 160, 7, 8, 9,
        10, 11, 12, 13, 161, 14, 59, 162, 15, 6,
        166, 167, 7, 168, 144, 170, 146, 13, 13, 171,
        14, 106, 172, 15, 53, 69, 6, 4, 5, 113,
        118, 6, 72, 25, 7, 8, 9, 10, 11, 12,
        13, 145, 14, 147, 65, 15, 169, 173, 0, 75,
        121, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 90, 95, 20, 20, 65, 0, 20, 115,
        64, 20, 51, 37, 38, 0, 78, 20, 20, 20,
        0, 164, 20, 20, 20, 20, 66, 20, 19, 19,
        34, 135, 19, 0, 33, 19, 35, 36, 37, 38,
        76, 19, 19, 19, 155, 0, 19, 19, 19, 19,
        0, 19, 38, 35, 36, 37, 38, 0, 39, 0,
        35, 51, 37, 38, 91, 0, 0, 129, 0, 0,
        54, 51, 37, 38, 0, 39, 0, 10, 11, 12,
        0, 0, 39, 92, 93, 94, 0, 95, 13, 13,
        0, 0, 13, 0, 0, 13, 137, 51, 37, 38,
        0, 13, 13, 13, 25, 25, 13, 78, 25, 80,
        0, 25, 0, 0, 4, 0, 89, 25, 25, 25,
        0, 0, 25, 0, 10, 11, 12, 51, 37, 38,
        0, 76, 51, 37, 38, 51, 37, 38, 112, 37,
        38, 4, 148, 0, 0, 6, 0, 0, 7, 8,
        9, 10, 11, 12, 13, 0, 14, 0, 0, 15,
        0, 0, 122, 59, 0, 0, 6, 0, 0, 7,
        0, 0, 0, 54, 54, 13, 0, 14, 0, 0,
        15, 0, 4, 5, 0, 142, 6, 0, 0, 7,
        8, 9, 10, 11, 12, 13, 0, 14, 0, 0,
        15,
    };
  }

  static short yycheck[];
  static {
    yycheck();
  }

  static void yycheck() {
    yycheck = new short[] { 41,
        44, 43, 44, 45, 41, 40, 43, 123, 45, 123,
        45, 41, 42, 43, 44, 45, 41, 47, 60, 43,
        62, 45, 123, 41, 44, 43, 44, 45, 44, 59,
        40, 41, 44, 273, 44, 45, 46, 257, 44, 41,
        13, 43, 60, 45, 62, 6, 125, 40, 41, 101,
        44, 61, 45, 46, 40, 41, 70, 257, 58, 45,
        46, 43, 41, 45, 40, 41, 256, 272, 61, 45,
        61, 261, 262, 87, 257, 61, 261, 262, 60, 123,
        62, 133, 16, 125, 256, 258, 44, 60, 123, 261,
        40, 257, 109, 110, 55, 45, 57, 59, 257, 256,
        256, 260, 44, 103, 263, 270, 42, 125, 40, 41,
        269, 47, 271, 257, 41, 274, 43, 270, 45, 123,
        40, 55, 256, 58, 257, 45, 41, 41, 256, 43,
        45, 45, 257, 45, 107, 123, 45, 257, 46, 41,
        256, 257, 123, 256, 260, 257, 261, 263, 264, 265,
        266, 267, 268, 269, 261, 271, 257, 261, 274, 260,
        125, 256, 263, 265, 125, 125, 127, 125, 269, 257,
        271, 171, 125, 274, 61, 19, 125, 256, 257, 70,
        84, 260, 34, 125, 263, 264, 265, 266, 267, 268,
        269, 125, 271, 127, 128, 274, 153, 171, -1, 42,
        89, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, 256, 257, 256, 257, 150, -1, 260, 256,
        125, 263, 257, 258, 259, -1, 256, 269, 270, 271,
        -1, 256, 274, 275, 276, 277, 256, 279, 256, 257,
        256, 125, 260, -1, 256, 263, 256, 257, 258, 259,
        256, 269, 270, 271, 256, -1, 274, 275, 276, 277,
        -1, 279, 256, 256, 257, 258, 259, -1, 278, -1,
        256, 257, 258, 259, 256, -1, -1, 256, -1, -1,
        6, 257, 258, 259, -1, 278, -1, 266, 267, 268,
        -1, -1, 278, 275, 276, 277, -1, 279, 256, 257,
        -1, -1, 260, -1, -1, 263, 256, 257, 258, 259,
        -1, 269, 270, 271, 256, 257, 274, 43, 260, 45,
        -1, 263, -1, -1, 256, -1, 52, 269, 270, 271,
        -1, -1, 274, -1, 266, 267, 268, 257, 258, 259,
        -1, 256, 257, 258, 259, 257, 258, 259, 257, 258,
        259, 256, 257, -1, -1, 260, -1, -1, 263, 264,
        265, 266, 267, 268, 269, -1, 271, -1, -1, 274,
        -1, -1, 98, 257, -1, -1, 260, -1, -1, 263,
        -1, -1, -1, 109, 110, 269, -1, 271, -1, -1,
        274, -1, 256, 257, -1, 121, 260, -1, -1, 263,
        264, 265, 266, 267, 268, 269, -1, 271, -1, -1,
        274,
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
      "conjuntoSentenciasEjecutables : conjuntoSentenciasEjecutables sentenciaEjecutable",
      "conjuntoSentenciasEjecutables : sentenciaEjecutable",
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
      "sentenciaEjecutable : clausula_IMPL",
      "sentenciaEjecutable : sentencia_de_Control",
      "sentenciaEjecutable : RETURN",
      "sentenciaDeclarativa : declaracion",
      "sentenciaDeclarativa : declaracionFuncion",
      "sentenciaDeclarativa : declaracionObjeto",
      "sentenciaDeclarativa : declaracionClase",
      "metodo_objeto : ID '.' invocacionFuncion",
      "atributo_objeto : ID '.' ID",
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
      "clausula_seleccion : IF condicion bloque_de_Sentencias ELSE bloque_de_Sentencias END_IF",
      "clausula_seleccion : IF condicion bloque_de_Sentencias END_IF",
      "clausula_seleccion : IF condicion bloque_de_Sentencias error",
      "clausula_seleccion : IF condicion bloque_de_Sentencias ELSE bloque_de_Sentencias error",
      "clausula_seleccion : IF condicion sentencia ELSE bloque_de_Sentencias END_IF",
      "clausula_seleccion : IF condicion bloque_de_Sentencias ELSE sentencia END_IF",
      "clausula_seleccion : IF condicion sentencia ELSE sentencia END_IF",
      "clausula_seleccion : IF bloque_de_Sentencias error",
      "clausula_seleccion : IF condicion sentencia END_IF",
      "sentencia_de_Control : DO bloque_de_SentenciasEjecutables UNTIL condicion",
      "sentencia_de_Control : DO sentenciaEjecutable UNTIL condicion",
      "sentencia_de_Control : DO sentenciaEjecutable UNTIL error",
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

  // #line 185 "gramatica.y"

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
      System.out.println(token.toString());
      return token.getIdToken();
    } else
      TS.imprimirContenido();
    return 0;
  }

  public void yyerror(String error) {
    System.out.println(error);
  }

  // #line 458 "Parser.java"
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
        // #line 15 "gramatica.y"
        {
          System.out.println("Programa completamente reconocido");
        }
          break;
        case 7:
        // #line 29 "gramatica.y"
        {
          System.out.println("Bloque de Sentencias reconocido");
        }
          break;
        case 12:
        // #line 40 "gramatica.y"
        {
          System.out.println("Falto la coma al final de la sentencia");
        }
          break;
        case 13:
        // #line 43 "gramatica.y"
        {
          System.out.println("Se reconocio una asignacion");
        }
          break;
        case 14:
        // #line 44 "gramatica.y"
        {
          System.out.println("Se reconocio una asignacion a un atributo objeto");
        }
          break;
        case 15:
        // #line 45 "gramatica.y"
        {
          System.out.println("Se reconocio una asignacion a un atributo objeto");
        }
          break;
        case 18:
        // #line 50 "gramatica.y"
        {
          System.out.println("No es valido el signo de asignacion");
        }
          break;
        case 27:
        // #line 67 "gramatica.y"
        {
          System.out.println("Se reconocio constante negativa en linea " + Linea.getLinea());
        }
          break;
        case 32:
        // #line 77 "gramatica.y"
        {
          System.out.println("Clase con herencia por composicion");
        }
          break;
        case 37:
        // #line 88 "gramatica.y"
        {
          System.out.println("Funcion VOID");
        }
          break;
        case 38:
        // #line 91 "gramatica.y"
        {
          System.out.println("Funcion VOID vacia");
        }
          break;
        case 41:
        // #line 98 "gramatica.y"
        {
          System.out.println("Se reconocio una invocacion de una funcion en linea " + Linea.getLinea());
        }
          break;
        case 42:
        // #line 99 "gramatica.y"
        {
          System.out.println("Se reconocio una clausula de seleccion IF en linea " + Linea.getLinea());
        }
          break;
        case 43:
        // #line 100 "gramatica.y"
        {
          System.out.println("Se reconocio una impresion por pantalla en linea " + Linea.getLinea());
        }
          break;
        case 44:
        // #line 101 "gramatica.y"
        {
          System.out.println("Se reconocio la invocacion de un metodo de un objeto en linea " + Linea.getLinea());
        }
          break;
        case 45:
        // #line 102 "gramatica.y"
        {
          System.out.println("Se reconocio sentencia IMPL FOR en linea " + Linea.getLinea());
        }
          break;
        case 46:
        // #line 103 "gramatica.y"
        {
          System.out.println("Se reconocio sentencia de control DO UNTIL en linea " + Linea.getLinea());
        }
          break;
        case 47:
        // #line 104 "gramatica.y"
        {
          System.out.println("Se reconocio sentencia de retorno RETURN en linea " + Linea.getLinea());
        }
          break;
        case 48:
        // #line 107 "gramatica.y"
        {
          System.out.println("Se reconocio una declaracion simple en linea " + Linea.getLinea());
        }
          break;
        case 49:
        // #line 108 "gramatica.y"
        {
          System.out.println("Se reconocio una funcion en linea " + Linea.getLinea());
        }
          break;
        case 50:
        // #line 109 "gramatica.y"
        {
          System.out.println("Se reconocio una declaracion de un objeto de una clase en linea " + Linea.getLinea());
        }
          break;
        case 51:
        // #line 110 "gramatica.y"
        {
          System.out.println("Se reconocio una clase en linea " + Linea.getLinea());
        }
          break;
        case 60:
        // #line 125 "gramatica.y"
        {
          System.out.println("Error: El caracter no se reconoce como comparador  en linea " + Linea.getLinea());
        }
          break;
        case 61:
        // #line 128 "gramatica.y"
        {
          System.out.println("Se reconoció una condicion  en linea " + Linea.getLinea());
        }
          break;
        case 62:
        // #line 129 "gramatica.y"
        {
          System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());
        }
          break;
        case 63:
        // #line 130 "gramatica.y"
        {
          System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());
        }
          break;
        case 66:
        // #line 135 "gramatica.y"
        {
          System.out.println("Falta el END_IF");
        }
          break;
        case 67:
        // #line 136 "gramatica.y"
        {
          System.out.println("Falta el END_IF");
        }
          break;
        case 71:
        // #line 140 "gramatica.y"
        {
          System.out.println("Falto la condicion del IF");
        }
          break;
        case 75:
        // #line 146 "gramatica.y"
        {
          System.out.println("Falta la condicion de la sentencia de control");
        }
          break;
        case 82:
        // #line 161 "gramatica.y"
        {
          System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());
        }
          break;
        case 83:
        // #line 162 "gramatica.y"
        {
          System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());
        }
          break;
        case 84:
        // #line 163 "gramatica.y"
        {
          System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());
        }
          break;
        case 85:
        // #line 164 "gramatica.y"
        {
          System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());
        }
          break;
        case 88:
        // #line 169 "gramatica.y"
        {
          System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());
        }
          break;
        case 89:
        // #line 170 "gramatica.y"
        {
          System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());
        }
          break;
        case 90:
        // #line 171 "gramatica.y"
        {
          System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());
        }
          break;
        case 91:
        // #line 172 "gramatica.y"
        {
          System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());
        }
          break;
        case 95:
        // #line 178 "gramatica.y"
        {
          System.out.println("Error: No es un tipo definido en linea " + Linea.getLinea());
        }
          break;
        // #line 763 "Parser.java"
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
