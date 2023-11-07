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
      18, 18, 18, 19, 21, 13, 23, 23, 24, 25,
      27, 5, 5, 5, 5, 5, 5, 5, 5, 9,
      9, 9, 9, 32, 32, 32, 32, 32, 32, 32,
      33, 33, 33, 28, 28, 28, 28, 28, 28, 28,
      28, 28, 34, 36, 35, 35, 30, 30, 30, 37,
      31, 20, 20, 22, 39, 39, 39, 39, 39, 39,
      26, 26, 26, 26, 26, 26, 38, 38, 38, 38,
      29,
  };
  final static short yylen[] = { 2,
      1, 2, 1, 3, 2, 1, 3, 3, 2, 2,
      1, 1, 3, 3, 3, 1, 1, 1, 3, 1,
      3, 1, 1, 1, 1, 1, 2, 1, 1, 1,
      3, 7, 2, 2, 3, 3, 1, 1, 6, 3,
      8, 1, 1, 1, 1, 1, 1, 1, 1, 1,
      1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
      5, 5, 5, 6, 6, 4, 4, 4, 4, 6,
      6, 3, 1, 1, 1, 1, 4, 4, 4, 1,
      2, 3, 1, 2, 3, 2, 3, 3, 2, 2,
      4, 2, 4, 4, 2, 2, 1, 1, 1, 1,
      2,
  };
  final static short yydefred[] = { 0,
      0, 0, 1, 100, 0, 0, 0, 0, 0, 99,
      98, 97, 80, 0, 49, 0, 3, 0, 0, 42,
      0, 53, 52, 46, 43, 51, 37, 38, 47, 44,
      45, 48, 50, 0, 0, 18, 0, 26, 28, 17,
      16, 0, 0, 0, 0, 0, 0, 22, 0, 0,
      84, 25, 0, 73, 0, 0, 0, 101, 0, 0,
      0, 7, 2, 12, 11, 10, 9, 0, 0, 0,
      0, 0, 83, 0, 27, 0, 35, 89, 86, 0,
      90, 0, 30, 29, 0, 0, 23, 24, 0, 0,
      0, 60, 57, 56, 58, 59, 54, 55, 0, 74,
      0, 0, 72, 0, 31, 0, 0, 0, 0, 0,
      0, 14, 15, 0, 0, 0, 0, 87, 85, 88,
      0, 21, 82, 0, 0, 68, 66, 0, 69, 67,
      0, 0, 0, 92, 0, 96, 0, 0, 0, 0,
      8, 0, 5, 78, 79, 77, 0, 0, 75, 76,
      0, 0, 0, 0, 0, 0, 0, 0, 36, 4,
      62, 61, 63, 70, 64, 71, 65, 0, 93, 91,
      39, 94, 0, 0, 32, 0, 0, 0, 41,
  };
  final static short yydgoto[] = { 2,
      54, 16, 17, 114, 18, 66, 156, 72, 19, 20,
      46, 47, 21, 48, 86, 49, 89, 22, 23, 50,
      24, 25, 26, 27, 28, 108, 29, 30, 31, 32,
      33, 99, 56, 57, 151, 102, 34, 35, 51,
  };
  final static short yysindex[] = { -98,
      -156, 0, 0, 0, -2, 16, -226, -203, -197, 0,
      0, 0, 0, -208, 0, -68, 0, -26, -26, 0,
      23, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, -89, -171, 0, 0, 0, 0, 0,
      0, -163, -161, 42, -158, 13, 82, 0, 33, 40,
      0, 0, 13, 0, 10, -109, -122, 0, 19, -35,
      -114, 0, 0, 0, 0, 0, 0, 48, 5, -143,
      -126, -125, 0, 40, 0, 22, 0, 0, 0, -15,
      0, -28, 0, 0, -110, 13, 0, 0, 13, -108,
      10, 0, 0, 0, 0, 0, 0, 0, 13, 0,
      -159, -132, 0, -156, 0, -40, -106, 29, -104, 103,
      117, 0, 0, 65, -26, 37, 34, 0, 0, 0,
      33, 0, 0, 13, 92, 0, 0, -109, 0, 0,
      -109, 52, 0, 0, -91, 0, -156, 126, 46, -87,
      0, -26, 0, 0, 0, 0, 28, -84, 0, 0,
      -140, -120, -9, -34, -156, 50, -83, -86, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 51, 0, 0,
      0, 0, -79, -26, 0, -35, 56, 29, 0,
  };
  final static short yyrindex[] = { 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 47, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, -41, 4,
      0, 0, 0, 0, 0, 0, 0, 0, 21, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 32, 0, 122, 0, 0, 0, 0,
      0, -31, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 41, 0, 0,
      -11, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      -33, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, -4, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 59, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0,
  };
  final static short yygindex[] = { 0,
      81, -88, 286, 0, 8, -10, 0, 0, 0, 0,
      0, 304, 118, -48, 0, 101, 0, 0, 0, 156,
      0, 150, 0, 36, 0, 26, 0, 0, 0, 0,
      0, 113, -93, 149, 76, 0, 0, -38, 0,
  };
  final static int YYTABLESIZE = 441;
  static short yytable[];
  static {
    yytable();
  }

  static void yytable() {
    yytable = new short[] { 20,
        134, 20, 20, 20, 106, 107, 170, 19, 67, 19,
        19, 19, 13, 1, 84, 132, 83, 65, 20, 113,
        20, 109, 144, 146, 1, 119, 19, 84, 19, 83,
        44, 45, 25, 70, 168, 42, 43, 44, 45, 95,
        122, 71, 42, 43, 44, 45, 58, 34, 155, 42,
        43, 41, 84, 59, 83, 53, 62, 42, 41, 60,
        42, 44, 45, 61, 33, 41, 42, 135, 162, 98,
        84, 97, 83, 53, 87, 81, 53, 115, 42, 88,
        3, 42, 79, 68, 40, 73, 42, 25, 25, 25,
        83, 25, 42, 25, 75, 76, 126, 81, 90, 4,
        5, 127, 128, 6, 143, 83, 7, 8, 9, 10,
        11, 12, 13, 69, 14, 164, 6, 15, 95, 7,
        165, 142, 85, 129, 84, 13, 83, 14, 130, 131,
        15, 160, 148, 103, 84, 166, 83, 109, 1, 105,
        167, 104, 110, 116, 117, 120, 4, 5, 123, 136,
        6, 137, 138, 7, 8, 9, 10, 11, 12, 13,
        139, 14, 140, 177, 15, 154, 157, 69, 158, 159,
        6, 163, 172, 7, 171, 175, 62, 176, 173, 13,
        179, 14, 36, 6, 15, 112, 121, 4, 5, 141,
        74, 6, 77, 174, 7, 8, 9, 10, 11, 12,
        13, 178, 14, 124, 101, 15, 152, 0, 149, 0,
        0, 149, 0, 0, 20, 133, 0, 0, 0, 0,
        4, 169, 19, 0, 13, 10, 11, 12, 20, 64,
        10, 11, 12, 20, 20, 20, 19, 20, 13, 0,
        118, 19, 19, 19, 25, 19, 36, 37, 38, 39,
        0, 95, 100, 36, 37, 38, 39, 0, 25, 34,
        36, 52, 38, 39, 0, 92, 0, 0, 40, 52,
        38, 39, 52, 38, 39, 40, 33, 0, 52, 38,
        39, 0, 40, 161, 93, 94, 95, 81, 96, 145,
        52, 38, 39, 52, 38, 39, 40, 78, 52, 38,
        39, 63, 83, 0, 111, 38, 39, 4, 153, 55,
        0, 6, 0, 0, 7, 8, 9, 10, 11, 12,
        13, 69, 14, 0, 6, 15, 0, 7, 0, 0,
        0, 0, 0, 13, 0, 14, 0, 0, 15, 0,
        0, 100, 0, 0, 0, 0, 0, 80, 0, 82,
        0, 0, 0, 0, 0, 0, 91, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 125, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 150, 0, 0, 150, 63, 0, 55,
        55, 0, 0, 0, 0, 0, 0, 147, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        63,
    };
  }

  static short yycheck[];
  static {
    yycheck();
  }

  static void yycheck() {
    yycheck = new short[] { 41,
        41, 43, 44, 45, 40, 41, 41, 41, 19, 43,
        44, 45, 44, 123, 43, 104, 45, 44, 60, 68,
        62, 60, 116, 117, 123, 41, 60, 43, 62, 45,
        40, 41, 44, 123, 44, 45, 46, 40, 41, 44,
        89, 34, 45, 46, 40, 41, 273, 44, 137, 45,
        46, 61, 43, 257, 45, 40, 125, 45, 61, 257,
        45, 40, 41, 272, 44, 61, 45, 106, 41, 60,
        43, 62, 45, 40, 42, 44, 40, 70, 45, 47,
        0, 45, 41, 61, 44, 257, 45, 41, 42, 43,
        44, 45, 45, 47, 258, 257, 256, 256, 59, 256,
        257, 261, 262, 260, 115, 59, 263, 264, 265, 266,
        267, 268, 269, 257, 271, 256, 260, 274, 123, 263,
        261, 114, 41, 256, 43, 269, 45, 271, 261, 262,
        274, 142, 41, 256, 43, 256, 45, 176, 123, 59,
        261, 123, 257, 270, 270, 256, 256, 257, 257, 256,
        260, 123, 257, 263, 264, 265, 266, 267, 268, 269,
        58, 271, 46, 174, 274, 257, 41, 257, 123, 257,
        260, 256, 256, 263, 125, 125, 125, 257, 265, 269,
        125, 271, 61, 125, 274, 68, 86, 256, 257, 125,
        35, 260, 43, 158, 263, 264, 265, 266, 267, 268,
        269, 176, 271, 91, 56, 274, 131, -1, 128, -1,
        -1, 131, -1, -1, 256, 256, -1, -1, -1, -1,
        256, 256, 256, -1, 256, 266, 267, 268, 270, 256,
        266, 267, 268, 275, 276, 277, 270, 279, 270, -1,
        256, 275, 276, 277, 256, 279, 256, 257, 258, 259,
        -1, 256, 257, 256, 257, 258, 259, -1, 270, 256,
        256, 257, 258, 259, -1, 256, -1, -1, 278, 257,
        258, 259, 257, 258, 259, 278, 256, -1, 257, 258,
        259, -1, 278, 256, 275, 276, 277, 256, 279, 256,
        257, 258, 259, 257, 258, 259, 256, 256, 257, 258,
        259, 16, 256, -1, 257, 258, 259, 256, 257, 6,
        -1, 260, -1, -1, 263, 264, 265, 266, 267, 268,
        269, 257, 271, -1, 260, 274, -1, 263, -1, -1,
        -1, -1, -1, 269, -1, 271, -1, -1, 274, -1,
        -1, 56, -1, -1, -1, -1, -1, 44, -1, 46,
        -1, -1, -1, -1, -1, -1, 53, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, 99, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, 128, -1, -1, 131, 132, -1, 116,
        117, -1, -1, -1, -1, -1, -1, 124, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        155,
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
      "declaracionClase : CLASS ID bloque_de_Sentencias",
      "declaracionClase : CLASS ID '{' conjuntoSentencias ID ',' '}'",
      "declaracionClase : CLASS ID",
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

  // #line 252 "gramatica.y"

  Lexico lex;
  TablaSimbolos TS = new TablaSimbolos();
  HashMap<Integer, Terceto> CodigoIntermedio = new HashMap<Integer, Terceto>();
  HashMap<String, String> funciones = new HashMap<String, String>();
  int puntero_Terceto = 0;
  String tipo;
  ArrayList<String> variables = new ArrayList<String>();
  Stack<Integer> pila = new Stack<Integer>();

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

  // #line 658 "Parser.java"
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
          yyval.sval = '['
              + Integer.toString(
                  crear_terceto(val_peek(1).sval, Integer.toString(TS.pertenece(val_peek(2).sval)), val_peek(0).sval))
              + ']';
        }
          break;
        case 14:
        // #line 50 "gramatica.y"
        {
          System.out.println("Se reconocio una asignacion a un atributo objeto en linea " + Linea.getLinea());
        }
          break;
        case 15:
        // #line 51 "gramatica.y"
        {
          System.out.println("Se reconocio una asignacion a un atributo objeto en linea " + Linea.getLinea());
        }
          break;
        case 16:
        // #line 54 "gramatica.y"
        {
          System.out.println("Se reconocio una asignacion en linea " + Linea.getLinea());
          yyval.sval = "=";
        }
          break;
        case 17:
        // #line 56 "gramatica.y"
        {
          System.out.println("Se reconocio una asignacion suma en linea " + Linea.getLinea());
          yyval.sval = "+=";
        }
          break;
        case 18:
        // #line 58 "gramatica.y"
        {
          System.out.println("No es valido el signo de asignacion");
        }
          break;
        case 19:
        // #line 61 "gramatica.y"
        {
          yyval.sval = '[' + Integer.toString(crear_terceto(val_peek(1).sval, val_peek(2).sval, val_peek(0).sval))
              + ']';
        }
          break;
        case 20:
        // #line 62 "gramatica.y"
        {
          yyval.sval = val_peek(0).sval;
        }
          break;
        case 21:
        // #line 65 "gramatica.y"
        {
          yyval.sval = '[' + Integer.toString(crear_terceto(val_peek(1).sval, val_peek(2).sval, val_peek(0).sval))
              + ']';
        }
          break;
        case 22:
        // #line 66 "gramatica.y"
        {
          yyval.sval = val_peek(0).sval;
        }
          break;
        case 23:
        // #line 69 "gramatica.y"
        {
          yyval.sval = "*";
        }
          break;
        case 24:
        // #line 70 "gramatica.y"
        {
          yyval.sval = "/";
        }
          break;
        case 25:
        // #line 73 "gramatica.y"
        {
          yyval.sval = Integer.toString(TS.pertenece(val_peek(0).sval));
          setear_Uso("identificador", val_peek(0).sval);
        }
          break;
        case 26:
        // #line 75 "gramatica.y"
        {
          System.out.println("Se reconocio una constante en linea " + Linea.getLinea());
          chequearRangoPositivo(val_peek(0).sval, yyval);
        }
          break;
        case 27:
        // #line 77 "gramatica.y"
        {
          System.out.println("Se reconocio constante negativa en linea " + Linea.getLinea());
          chequearRangoNegativo(val_peek(0).sval, yyval);
          ;
        }
          break;
        case 28:
        // #line 79 "gramatica.y"
        {
          setear_Uso("ConstantePositiva", val_peek(0).sval);
          yyval.sval = Integer.toString(TS.pertenece(val_peek(0).sval));
        }
          break;
        case 29:
        // #line 83 "gramatica.y"
        {
          yyval.sval = "+";
        }
          break;
        case 30:
        // #line 84 "gramatica.y"
        {
          yyval.sval = "-";
        }
          break;
        case 31:
        // #line 88 "gramatica.y"
        {
          setear_Uso("Clase", val_peek(1).sval);
        }
          break;
        case 32:
        // #line 89 "gramatica.y"
        {
          System.out.println("Clase con herencia por composicion en linea " + Linea.getLinea());
          setear_Uso("Clase", val_peek(5).sval);
        }
          break;
        case 33:
        // #line 90 "gramatica.y"
        {
          setear_Uso("Clase", val_peek(0).sval);
        }
          break;
        case 34:
        // #line 93 "gramatica.y"
        {
          guardar_Tipo(val_peek(1).sval);
          setear_Tipo();
        }
          break;
        case 35:
        // #line 96 "gramatica.y"
        {
          int aux = crear_terceto("CALLMetodoClase", Integer.toString(TS.pertenece(val_peek(2).sval)),
              val_peek(0).sval);
        }
          break;
        case 39:
        // #line 106 "gramatica.y"
        {
          System.out.println("Se reconocio una invocacion de una funcion VOID en linea " + Linea.getLinea());
          setear_Uso("Metodo", val_peek(4).sval);
          funciones.put(val_peek(4).sval, val_peek(3).sval);
        }
          break;
        case 40:
        // #line 111 "gramatica.y"
        {
          System.out.println("Se reconocio una invocacion de una funcion VOID vacia en linea " + Linea.getLinea());
          setear_Uso("Metodo", val_peek(1).sval);
          funciones.put(val_peek(1).sval, val_peek(0).sval);
        }
          break;
        case 44:
        // #line 121 "gramatica.y"
        {
          System.out.println("Se reconocio una clausula de seleccion IF en linea " + Linea.getLinea());
        }
          break;
        case 45:
        // #line 122 "gramatica.y"
        {
          System.out.println("Se reconocio una impresion por pantalla en linea " + Linea.getLinea());
        }
          break;
        case 46:
        // #line 123 "gramatica.y"
        {
          System.out.println("Se reconocio la invocacion de un metodo de un objeto en linea " + Linea.getLinea());
        }
          break;
        case 47:
        // #line 124 "gramatica.y"
        {
          System.out.println("Se reconocio sentencia IMPL FOR en linea " + Linea.getLinea());
        }
          break;
        case 48:
        // #line 125 "gramatica.y"
        {
          System.out.println("Se reconocio sentencia de control DO UNTIL en linea " + Linea.getLinea());
        }
          break;
        case 49:
        // #line 126 "gramatica.y"
        {
          System.out.println("Se reconocio sentencia de retorno RETURN en linea " + Linea.getLinea());
          int aux = crear_terceto("RETURN", "-", "-");
        }
          break;
        case 50:
        // #line 130 "gramatica.y"
        {
          System.out.println("Se reconocio una declaracion simple en linea " + Linea.getLinea());
        }
          break;
        case 52:
        // #line 132 "gramatica.y"
        {
          System.out.println("Se reconocio una declaracion de un objeto de una clase en linea " + Linea.getLinea());
        }
          break;
        case 53:
        // #line 133 "gramatica.y"
        {
          System.out.println("Se reconocio una clase en linea " + Linea.getLinea());
        }
          break;
        case 54:
        // #line 136 "gramatica.y"
        {
          yyval.sval = ">";
        }
          break;
        case 55:
        // #line 137 "gramatica.y"
        {
          yyval.sval = "<";
        }
          break;
        case 56:
        // #line 138 "gramatica.y"
        {
          yyval.sval = ">=";
        }
          break;
        case 57:
        // #line 139 "gramatica.y"
        {
          yyval.sval = "<=";
        }
          break;
        case 58:
        // #line 140 "gramatica.y"
        {
          yyval.sval = "!!";
        }
          break;
        case 59:
        // #line 141 "gramatica.y"
        {
          yyval.sval = "==";
        }
          break;
        case 60:
        // #line 142 "gramatica.y"
        {
          System.out.println("Error: El caracter no se reconoce como comparador  en linea " + Linea.getLinea());
        }
          break;
        case 61:
        // #line 145 "gramatica.y"
        {
          System.out.println("Se reconoció una condicion  en linea " + Linea.getLinea());
          yyval.sval = '[' + Integer.toString(crear_terceto(val_peek(2).sval, val_peek(3).sval, val_peek(1).sval))
              + ']';
          int aux = crear_terceto("BF", yyval.sval, "-");
          pila.push(aux);
        }
          break;
        case 62:
        // #line 149 "gramatica.y"
        {
          System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());
          yyval.sval = '[' + Integer.toString(crear_terceto(val_peek(2).sval, val_peek(3).sval, val_peek(1).sval))
              + ']';
          int aux = crear_terceto("BF", yyval.sval, "-");
          pila.push(aux);
        }
          break;
        case 63:
        // #line 153 "gramatica.y"
        {
          System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());
          yyval.sval = '[' + Integer.toString(crear_terceto(val_peek(3).sval, val_peek(4).sval, val_peek(2).sval))
              + ']';
          int aux = crear_terceto("BF", yyval.sval, "-");
          pila.push(aux);
        }
          break;
        case 64:
        // #line 159 "gramatica.y"
        {
          int primero = pila.pop();
          completarTerceto(primero, puntero_Terceto);
        }
          break;
        case 65:
        // #line 161 "gramatica.y"
        {
          int primero = pila.pop();
          completarTerceto(primero, puntero_Terceto);
        }
          break;
        case 66:
        // #line 163 "gramatica.y"
        {
          int primero = pila.pop();
          completarTerceto(primero, puntero_Terceto);
        }
          break;
        case 67:
        // #line 165 "gramatica.y"
        {
          int primero = pila.pop();
          completarTerceto(primero, puntero_Terceto);
        }
          break;
        case 68:
        // #line 167 "gramatica.y"
        {
          System.out.println("Falta el END_IF");
          int primero = pila.pop();
          completarTerceto(primero, puntero_Terceto);
        }
          break;
        case 69:
        // #line 170 "gramatica.y"
        {
          System.out.println("Falta el END_IF");
          int primero = pila.pop();
          completarTerceto(primero, puntero_Terceto);
        }
          break;
        case 70:
        // #line 173 "gramatica.y"
        {
          System.out.println("Falta el END_IF");
          int primero = pila.pop();
          completarTerceto(primero, puntero_Terceto);
        }
          break;
        case 71:
        // #line 176 "gramatica.y"
        {
          System.out.println("Falta el END_IF");
          int primero = pila.pop();
          completarTerceto(primero, puntero_Terceto);
        }
          break;
        case 72:
        // #line 179 "gramatica.y"
        {
          System.out.println("Falto la condicion del IF");
        }
          break;
        case 73:
        // #line 182 "gramatica.y"
        {
          int primero = pila.pop();
          int aux = crear_terceto("BI", "-", "-");
          completarTerceto(primero, aux + 1);
          pila.push(aux);
        }
          break;
        case 74:
        // #line 188 "gramatica.y"
        {
          int primero = pila.pop();
          int aux = crear_terceto("BI", "-", "-");
          completarTerceto(primero, aux + 1);
          pila.push(aux);
        }
          break;
        case 77:
        // #line 198 "gramatica.y"
        {
          int primero = pila.pop();
          completarTerceto(primero, val_peek(3).ival);
        }
          break;
        case 78:
        // #line 200 "gramatica.y"
        {
          int primero = pila.pop();
          completarTerceto(primero, val_peek(3).ival);
        }
          break;
        case 79:
        // #line 202 "gramatica.y"
        {
          System.out.println("Falta la condicion de la sentencia de control");
        }
          break;
        case 80:
        // #line 205 "gramatica.y"
        {
          yyval.ival = puntero_Terceto;
        }
          break;
        case 81:
        // #line 208 "gramatica.y"
        {
          setear_Tipo();
        }
          break;
        case 82:
        // #line 211 "gramatica.y"
        {
          setear_Uso("Variable", val_peek(0).sval);
          guardar_Var(val_peek(0).sval);
        }
          break;
        case 83:
        // #line 212 "gramatica.y"
        {
          setear_Uso("Variable", val_peek(0).sval);
          guardar_Var(val_peek(0).sval);
        }
          break;
        case 84:
        // #line 215 "gramatica.y"
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
        case 85:
        // #line 224 "gramatica.y"
        {
          yyval.sval = val_peek(1).sval;
        }
          break;
        case 86:
        // #line 225 "gramatica.y"
        {
          yyval.sval = null;
        }
          break;
        case 87:
        // #line 226 "gramatica.y"
        {
          System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());
        }
          break;
        case 88:
        // #line 227 "gramatica.y"
        {
          System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());
        }
          break;
        case 89:
        // #line 228 "gramatica.y"
        {
          System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());
        }
          break;
        case 90:
        // #line 229 "gramatica.y"
        {
          System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());
        }
          break;
        case 91:
        // #line 232 "gramatica.y"
        {
          setear_Uso("Parametro formal", val_peek(1).sval);
          yyval.sval = val_peek(1).sval;
        }
          break;
        case 92:
        // #line 234 "gramatica.y"
        {
          yyval.sval = null;
        }
          break;
        case 93:
        // #line 235 "gramatica.y"
        {
          System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());
          setear_Uso("Parametro formal", val_peek(1).sval);
        }
          break;
        case 94:
        // #line 236 "gramatica.y"
        {
          System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());
          setear_Uso("Parametro formal", val_peek(2).sval);
        }
          break;
        case 95:
        // #line 237 "gramatica.y"
        {
          System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());
        }
          break;
        case 96:
        // #line 238 "gramatica.y"
        {
          System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());
        }
          break;
        case 97:
        // #line 241 "gramatica.y"
        {
          guardar_Tipo("DOUBLE");
        }
          break;
        case 98:
        // #line 242 "gramatica.y"
        {
          guardar_Tipo("USHORT");
        }
          break;
        case 99:
        // #line 243 "gramatica.y"
        {
          guardar_Tipo("LONG");
        }
          break;
        case 100:
        // #line 244 "gramatica.y"
        {
          System.out.println("Error: No es un tipo definido en linea " + Linea.getLinea());
        }
          break;
        case 101:
        // #line 247 "gramatica.y"
        {
          setear_Uso("Cadena", val_peek(0).sval);
          int aux = crear_terceto("PRINT", Integer.toString(TS.pertenece(val_peek(0).sval)), "-");
        }
          break;
        // #line 1187 "Parser.java"
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
