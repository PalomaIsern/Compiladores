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
import compiladores.Lexico;
import compiladores.TablaSimbolos;
import compiladores.Linea;
import compiladores.Token;
import compiladores.Terceto;
import compiladores.Simbolo;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Arrays;


import java.io.IOException;

//#line 32 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short CTE=258;
public final static short CTEPOS=259;
public final static short IF=260;
public final static short END_IF=261;
public final static short ELSE=262;
public final static short PRINT=263;
public final static short CLASS=264;
public final static short VOID=265;
public final static short LONG=266;
public final static short USHORT=267;
public final static short DOUBLE=268;
public final static short DO=269;
public final static short UNTIL=270;
public final static short IMPL=271;
public final static short FOR=272;
public final static short CADENA=273;
public final static short RETURN=274;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    2,    2,    4,    4,    7,    1,    8,    3,    3,
    6,    6,   10,   10,   10,   11,   11,   11,   12,   12,
   16,   16,   17,   17,   14,   14,   14,   14,   15,   15,
   18,   18,   18,   19,   20,   22,   13,   24,   24,   25,
   26,   27,   29,    5,    5,    5,    5,    5,    5,    5,
    5,    9,    9,    9,    9,   34,   34,   34,   34,   34,
   34,   34,   35,   35,   35,   30,   30,   30,   30,   30,
   30,   30,   30,   30,   36,   38,   37,   37,   32,   32,
   32,   39,   33,   21,   21,   23,   41,   41,   41,   41,
   41,   41,   28,   28,   28,   28,   28,   28,   40,   40,
   40,   40,   31,
};
final static short yylen[] = {                            2,
    1,    2,    1,    3,    2,    1,    3,    3,    2,    2,
    1,    1,    3,    3,    3,    1,    1,    1,    3,    1,
    3,    1,    1,    1,    1,    1,    2,    1,    1,    1,
    2,    6,    1,    2,    2,    3,    3,    1,    1,    5,
    2,    2,    8,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    5,    5,    5,    6,    6,    4,    4,    4,
    4,    6,    6,    3,    1,    1,    1,    1,    4,    4,
    4,    1,    2,    3,    1,    2,    3,    2,    3,    3,
    2,    2,    4,    2,    4,    4,    2,    2,    1,    1,
    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    1,  102,    0,    0,    0,    0,    0,  101,
  100,   99,   82,    0,   51,    0,    3,    0,    0,   44,
    0,   55,    0,   54,   48,   45,   53,   38,   39,    0,
   49,   46,   47,   50,   52,    0,    0,   18,    0,   26,
   28,   17,   16,    0,    0,    0,    0,    0,    0,   22,
    0,    0,   86,   25,    0,   75,    0,    0,    0,  103,
   34,   42,    0,    7,    2,   12,   11,   10,    9,    0,
    0,   31,    0,    0,    0,    0,    0,    0,    0,    0,
   85,    0,   27,    0,   36,   91,   88,    0,   92,    0,
   30,   29,    0,    0,   23,   24,    0,    0,    0,   62,
   59,   58,   60,   61,   56,   57,    0,   76,    0,    0,
   74,    0,    0,   14,   15,    0,    0,   94,    0,   98,
    0,    0,    0,    0,    0,    0,   89,   87,   90,    0,
   21,   84,    0,    0,   70,   68,    0,   71,   69,    0,
    0,    0,    0,    0,    0,    0,    0,    8,    0,    5,
   80,   81,   79,    0,    0,   77,   78,    0,    0,    0,
   37,    0,   95,   93,   40,   96,    4,   64,   63,   65,
   72,   66,   73,   67,    0,    0,   32,    0,    0,   43,
};
final static short yydgoto[] = {                          2,
   56,   16,   17,  123,   18,   68,  146,   80,   19,   20,
   48,   49,   21,   50,   94,   51,   97,   22,   23,   24,
   52,   25,   26,   27,   28,   29,   30,   75,   31,   32,
   33,   34,   35,  107,   58,   59,  158,  110,   36,   37,
   53,
};
final static short yysindex[] = {                      -114,
 -133,    0,    0,    0,   -2,   16, -253, -234, -216,    0,
    0,    0,    0, -204,    0,  -68,    0,  -26,  -26,    0,
   36,    0,  -28,    0,    0,    0,    0,    0,    0,  -35,
    0,    0,    0,    0,    0,  -89, -150,    0,    0,    0,
    0,    0,    0, -157, -143,   42, -144,   13,   77,    0,
    7,   56,    0,    0,   13,    0,   10, -109, -131,    0,
    0,    0, -120,    0,    0,    0,    0,    0,    0,   48,
 -133,    0,  -40, -113,   26, -112,    5, -161, -118, -117,
    0,   56,    0,   22,    0,    0,    0,  -15,    0,   35,
    0,    0, -106,   13,    0,    0,   13,  -91,   10,    0,
    0,    0,    0,    0,    0,    0,   13,    0, -158, -145,
    0,  109,  123,    0,    0,   52,    0,    0,  -87,    0,
 -133,  132,   65,  -26,   37,   34,    0,    0,    0,    7,
    0,    0,   13,   99,    0,    0, -109,    0,    0, -109,
   53,  -82,   -9,  -34, -133,   54,  -78,    0,  -26,    0,
    0,    0,    0,   28,  -75,    0,    0, -175, -156,  -79,
    0,   58,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -26,  -35,    0,   62,   26,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    4,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   47,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -41,   21,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   32,    0,    0,    0,    0,    0,
    0,   41,    0,  130,    0,    0,    0,    0,    0,  -31,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -11,    0,    0,    0,   -4,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -33,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   69,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   24,  -46,   68,    0,  -14,   -3,    0,    0,    0,    0,
    0,  296,  134,  -55,    0,  108,    0,    0,    0,    0,
  156,    0,  162,    0,   49,    0,   50,   38,    0,    0,
    0,    0,    0,  112,    3,  154,   78,    0,    0,  -13,
    0,
};
final static int YYTABLESIZE=429;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         20,
  118,   20,   20,   20,   73,   74,  164,   19,    1,   19,
   19,   19,   13,    1,  115,   69,   76,   67,   20,   60,
   20,   79,   61,    3,  116,  128,   19,   92,   19,   91,
   46,   47,   25,   78,  162,   44,   45,   46,   47,   97,
   62,  131,   44,   45,   46,   47,   72,   33,   95,   44,
   45,   43,   92,   96,   91,   55,   64,   44,   43,  119,
   44,   46,   47,  124,   35,   43,   44,   63,  169,  106,
   92,  105,   91,   55,  145,   41,   55,   92,   44,   91,
  171,   44,   87,   65,   83,  172,   44,   25,   25,   25,
   85,   25,   44,   25,   71,   77,   70,  135,    6,  173,
   83,    7,  136,  137,  174,   85,   81,   13,  149,   14,
  138,   89,   15,   84,   98,  139,  140,   93,   97,   92,
  150,   91,    4,    5,  111,  108,    6,  151,  153,    7,
    8,    9,   10,   11,   12,   13,  112,   14,    1,  155,
   15,   92,  120,   91,  122,  167,    4,    5,  121,  129,
    6,  125,  126,    7,    8,    9,   10,   11,   12,   13,
  156,   14,   76,  156,   15,  132,  141,   77,  142,  144,
    6,  178,  147,    7,  161,  160,   64,  166,  165,   13,
  170,   14,  177,   65,   15,    9,  180,    4,    5,  148,
   37,    6,   82,    6,    7,    8,    9,   10,   11,   12,
   13,  130,   14,  114,  157,   15,   85,  157,  175,  176,
  133,  109,   65,  179,   20,  117,    0,  159,    0,    0,
    4,  163,   19,    0,   13,   10,   11,   12,   20,   66,
   10,   11,   12,   20,   20,   20,   19,   20,   13,    0,
  127,   19,   19,   19,   25,   19,   38,   39,   40,   41,
    0,   97,  102,   38,   39,   40,   41,    0,   25,   33,
   38,   54,   40,   41,    0,  100,    0,    0,   42,   54,
   40,   41,   54,   40,   41,   42,   35,    0,   54,   40,
   41,    0,   42,  168,  101,  102,  103,   41,  104,  152,
   54,   40,   41,   54,   40,   41,   83,   86,   54,   40,
   41,   57,   85,    0,  113,   40,   41,    4,  143,    0,
    0,    6,    0,    0,    7,    8,    9,   10,   11,   12,
   13,   77,   14,    0,    6,   15,    0,    7,    0,    0,
    0,    0,    0,   13,    0,   14,    0,    0,   15,    0,
    0,   88,    0,   90,    0,    0,    0,    0,    0,    0,
   99,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  134,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   57,   57,    0,    0,    0,    0,    0,    0,  154,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   41,   43,   44,   45,   40,   41,   41,   41,  123,   43,
   44,   45,   44,  123,   70,   19,   30,   44,   60,  273,
   62,   36,  257,    0,   71,   41,   60,   43,   62,   45,
   40,   41,   44,  123,   44,   45,   46,   40,   41,   44,
  257,   97,   45,   46,   40,   41,   23,   44,   42,   45,
   46,   61,   43,   47,   45,   40,  125,   45,   61,   73,
   45,   40,   41,   78,   44,   61,   45,  272,   41,   60,
   43,   62,   45,   40,  121,   44,   40,   43,   45,   45,
  256,   45,   41,   16,   44,  261,   45,   41,   42,   43,
   44,   45,   45,   47,  123,  257,   61,  256,  260,  256,
  258,  263,  261,  262,  261,   59,  257,  269,  123,  271,
  256,  256,  274,  257,   59,  261,  262,   41,  123,   43,
  124,   45,  256,  257,  256,   58,  260,  125,  126,  263,
  264,  265,  266,  267,  268,  269,  257,  271,  123,   41,
  274,   43,  256,   45,  257,  149,  256,  257,  123,  256,
  260,  270,  270,  263,  264,  265,  266,  267,  268,  269,
  137,  271,  176,  140,  274,  257,   58,  257,   46,  257,
  260,  175,   41,  263,  257,  123,  125,  256,  125,  269,
  256,  271,  125,  116,  274,  265,  125,  256,  257,  125,
   61,  260,   37,  125,  263,  264,  265,  266,  267,  268,
  269,   94,  271,   70,  137,  274,   45,  140,  160,  160,
   99,   58,  145,  176,  256,  256,   -1,  140,   -1,   -1,
  256,  256,  256,   -1,  256,  266,  267,  268,  270,  256,
  266,  267,  268,  275,  276,  277,  270,  279,  270,   -1,
  256,  275,  276,  277,  256,  279,  256,  257,  258,  259,
   -1,  256,  257,  256,  257,  258,  259,   -1,  270,  256,
  256,  257,  258,  259,   -1,  256,   -1,   -1,  278,  257,
  258,  259,  257,  258,  259,  278,  256,   -1,  257,  258,
  259,   -1,  278,  256,  275,  276,  277,  256,  279,  256,
  257,  258,  259,  257,  258,  259,  256,  256,  257,  258,
  259,    6,  256,   -1,  257,  258,  259,  256,  257,   -1,
   -1,  260,   -1,   -1,  263,  264,  265,  266,  267,  268,
  269,  257,  271,   -1,  260,  274,   -1,  263,   -1,   -1,
   -1,   -1,   -1,  269,   -1,  271,   -1,   -1,  274,   -1,
   -1,   46,   -1,   48,   -1,   -1,   -1,   -1,   -1,   -1,
   55,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  107,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  125,  126,   -1,   -1,   -1,   -1,   -1,   -1,  133,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=279;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,"':'","';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"ID","CTE","CTEPOS","IF","END_IF","ELSE",
"PRINT","CLASS","VOID","LONG","USHORT","DOUBLE","DO","UNTIL","IMPL","FOR",
"CADENA","RETURN","\"<=\"","\">=\"","\"!!\"","\"+=\"","\"==\"",
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
"declaracionClase : inicioClase bloque_de_Sentencias",
"declaracionClase : inicioClase '{' conjuntoSentencias ID ',' '}'",
"declaracionClase : inicioClase",
"inicioClase : CLASS ID",
"declaracionObjeto : ID lista_Variables",
"metodo_objeto : ID '.' invocacionFuncion",
"atributo_objeto : ID '.' ID",
"declaracionFuncion : funcion_VOID",
"declaracionFuncion : funcion_VOID_vacia",
"funcion_VOID : inicio_Void parametro_formal '{' cuerpo_funcion '}'",
"funcion_VOID_vacia : inicio_Void parametro_formal",
"inicio_Void : VOID ID",
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

//#line 312 "gramatica.y"

    Lexico lex;
    TablaSimbolos TS = new TablaSimbolos();
    HashMap<Integer, Terceto> CodigoIntermedio = new HashMap<Integer, Terceto>();
    int puntero_Terceto = 0;
    static String ambito = ":main";
    int limite_Anidamiento = 4;
    boolean avance = false;
    String[] ambitos_Programa;
    String tipo;
    ArrayList<String> variables = new ArrayList<String>();
    Stack<Integer> pila = new Stack<Integer>();
    HashMap<Integer, String> funciones = new HashMap<Integer, String>();
    HashMap<Integer, ArrayList<Integer>> metodosClases = new HashMap<Integer, ArrayList<Integer>>();
    HashMap<Integer, ArrayList<Integer>> atributosClases = new HashMap<Integer, ArrayList<Integer>>();
    ArrayList<Integer> metodosTemp = new ArrayList<Integer>();
    ArrayList<Integer> atributosTemp = new ArrayList<Integer>();
    
    public void ver_ElementoDeclarado(String elemento){
        int clave = TS.buscar_por_ambito(elemento+ambito);
        if (clave==-1)
            clave = TS.pertenece(elemento);
        String aux = ambito;
        if (clave != -1){
            //Si está en la tabla de simbolos me fijo si es alcanzable
            Simbolo s = TS.get_Simbolo(clave);
            ambitos_Programa = aux.split(":");
            int cantidad = ambitos_Programa.length;
            while (cantidad > 1){
                if (!s.get_Ambito().equals(elemento+aux)){
                    String nuevo="";
                    int i = 1;
                    while (i<cantidad-1) {
                        nuevo += ":" + ambitos_Programa[i];
                        i = i+1;
                    }
                    aux = nuevo;
                }
                cantidad = cantidad -1;
            }
            if (!s.get_Ambito().equals(elemento+aux))
                System.out.println("ERROR: linea "+ Linea.getLinea() + " - " + elemento + " está fuera del alcance");
            else
                System.out.println("El elemento " + elemento+ " está al alcance");
        }
        else System.out.println("ERROR: linea "+ Linea.getLinea() + " - " + elemento + " no fue declarada");
    }

    public String obtenerAmbito(String ambito) 
    {   //saca la ultima parte del ambito 
        if (ambito.contains(":")) {
            String[] partes = ambito.split(":");
            return String.join(":", Arrays.copyOf(partes, partes.length - 1));
        }
        return " ";
    }

    public Integer obtenerClase(String id) {
        //revisar
        int clave = TS.buscar_por_ambito(id+ambito);
        String nombreClase = TS.get_Simbolo(clave).get_Tipo();
        return TS.buscar_por_ambito(nombreClase+ambito);
    }
    public void verificarExisteClasePadre(String hijo, String padre) {
        String clasePadre = padre.split(":")[0];
        Integer clave = TS.buscar_por_ambito(padre);
        if (clave != -1) {
            String ambitoPadre = ":" + padre.substring(padre.indexOf(":")+1); 
            if (!ambitoPadre.equals(ambito)) {
                System.out.println("La clase \""+clasePadre+ "\"no se encuentra al alcance");
            }
        } else {
            System.out.println("La clase \""+ clasePadre+ "\" no existe");
        }
    }

    public void agregarFuncion(String id, String parametro) 
    { //agrega la funcion al hashmap de funciones, le setea el parametro en la TS
        int clave = TS.buscar_por_ambito(id);
        funciones.put(clave, parametro);
        TS.get_Simbolo(clave).set_Parametro(parametro);
    }

    public boolean verificar_Limite(){
        ambitos_Programa = ambito.split(":");
        int num_a = ambitos_Programa.length;
        if (num_a+1 > limite_Anidamiento)
            {System.out.println("No es posible generar mas anidamientos en linea "+ Linea.getLinea());
            return false;}
        else
            return true;
    }

    public void setear_Ambito(String a, String lex){
        int clave = TS.pertenece(lex);
        if (clave!=-1){
            Simbolo s = TS.get_Simbolo(clave);
            String amb= a.substring(a.indexOf(":")+1);
            if (s.get_Ambito().equals(a))
                System.out.println("ERROR: linea " + Linea.getLinea() + " - Redeclaracion de " + s.get_Lex()+ " en el ambito "+ amb);
            else
                if (s.get_Ambito()=="-")
                    s.set_Ambito(a);
                else
                    {Simbolo nuevo = new Simbolo(s.get_Token(), s.get_Lex());
                    nuevo.set_Ambito(a);
                    TS.agregar_sin_chequear(nuevo);}
        }
    }

    public void agregarClase(String ID, ArrayList<Integer> metodos, ArrayList<Integer> atributos) {
        System.out.println("agregarClase: " + ID+ " atrib "+atributos.size()+ " mets "+metodos.size());
        int clave = TS.buscar_por_ambito(ID);
        metodosClases.put(clave, metodos);
        atributosClases.put(clave, atributos);
    }

    public void volver_Ambito(){
        ambitos_Programa = ambito.split(":");
        int num_a = ambitos_Programa.length;
        String nuevo="";
        int i = 1;
        while (i<num_a-1) {
            nuevo += ":" + ambitos_Programa[i];
            i = i+1;
        }
        ambito = nuevo;
    }

    public void imprimirMetodosClases() {
        System.out.println("METODOS CLASES");
        for (HashMap.Entry<Integer, ArrayList<Integer>> e : metodosClases.entrySet()) {
            ArrayList<Integer> mets = e.getValue();
            int ref = e.getKey();
            String r = "ref: "+e.getKey()+" - nombre: "+ TS.get_Simbolo(ref).get_Ambito() + " | metodos : ";
            for (Integer i: mets) {
                r +=  TS.get_Simbolo(i).get_Lex() + " ; ";
            }
            System.out.println(r);
        }
    }
    public void verificarExisteAtributo(int clase, String atributo) 
    {   ArrayList<Integer> atributos = atributosClases.get(clase);
        for (Integer a : atributos) {
            if (TS.get_Simbolo(a).get_Lex().equals(atributo)) {
                System.out.println("EL atributo "+atributo+" existe");
                return;
            }
        }
        System.out.println("ERROR: El atributo: "+atributo+" no existe en la clase "+ TS.get_Simbolo(clase).get_Ambito());
    }
    
    public void imprimirAtributosClases() {
        System.out.println("ATRIBUTOS CLASES");
        for (HashMap.Entry<Integer, ArrayList<Integer>> e : atributosClases.entrySet()) {
            ArrayList<Integer> atrs = e.getValue();
            int ref = e.getKey();
            String r = "ref: "+e.getKey()+ " - nombre: "+TS.get_Simbolo(ref).get_Ambito() +  " | atributos:";
            for (Integer a: atrs) {
                r += TS.get_Simbolo(a).get_Lex() + " ; ";
            };
            System.out.println(r);
        }
    }

    public void imprimirFunciones() {
        System.out.println("FUNCIONES");
        for (HashMap.Entry<Integer, String> e : funciones.entrySet()) {
            Simbolo s = TS.get_Simbolo(e.getKey());
            System.out.println(" ref: "+e.getKey() + " - nombre: "+ s.get_Lex() +" - parametro: "+ e.getValue());
        }
    }

    public String buscar_Parametro(String id, String amb){
        for (HashMap.Entry<Integer, String> e : funciones.entrySet()) {
            Simbolo s = TS.get_Simbolo(e.getKey());
            if (s.get_Lex().equals(id)) {
                String ambitoSimbolo = s.get_Ambito();
                ambitoSimbolo = ":" + ambitoSimbolo.substring(ambitoSimbolo.indexOf(":")+1); 
                if (ambitoSimbolo.equals(amb)) {
                    return s.get_Parametro();
                }
            }
        }    
        return "-";
    }

    public int crear_terceto(String operador, String punt1, String punt2){
        Terceto t = new Terceto(operador, punt1, punt2);
        CodigoIntermedio.put(puntero_Terceto, t);
        puntero_Terceto = puntero_Terceto + 1;
        return puntero_Terceto-1;
    }

    public Parser(Lexico lexico){
        this.lex = lexico;
    }

    private void imprimirCodigoIntermedio() 
    {   System.out.println("CODIGO INTERMEDIO");
        for (HashMap.Entry<Integer, Terceto> i : CodigoIntermedio.entrySet()) {
                String j = i.getValue().get_Op1();
                String s = i.getValue().get_Op2();
                 if (!s.contains("[") && s!="-") {
                    int k = Integer.parseInt(s);
                    s = TS.get_Simbolo(k).get_Lex();
                }
                if (!j.contains("[") && j!="-") {
                    int l = Integer.parseInt(j);
                    j = TS.get_Simbolo(l).get_Lex();
                }
                System.out.println("Referencia: " + i.getKey() + ", Terceto: (" + i.getValue().get_Operador() + " , " + j + " , "+ s +")");
        }
    }

    private int yylex() {
        Token token = new Token();
        try {
            token = lex.getToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (token != null){
            yylval = new ParserVal(token.getLexema());
            //System.out.println(token.toString()); Este metodo quedo comentado porque era el que imprimía todos los token reconocidos.
            return token.getIdToken();
        }
        else
            TS.imprimirContenido();
            System.out.println(" ");   
            imprimirMetodosClases();
            System.out.println(" ");
            imprimirAtributosClases();
            System.out.println(" ");
            imprimirFunciones();   
            imprimirCodigoIntermedio();           
        return 0;
    }
    public void yyerror(String error) {
        System.out.println(error);
    }
    
    public void chequearRangoPositivo(String numero, ParserVal factor) {
        if (numero.contains(".")) //DOUBLE
        {
            double num = Double.parseDouble(numero);
            if (num < 2.2250738585072014e-308)
                {   
                    if (num != 0.0)
                    {
                        System.out.println("El double positivo es menor al limite permitido. Tiene valor: "+num);
                        TS.eliminar(Double.toString(num));
                        String nuevo = "2.2250738585072014e-308"  ;
                        TS.agregar(nuevo, 258);
                        System.out.println("El numero fue actualizado en la TS a un valor permitido");
                        setear_Uso("Constante", nuevo+ambito);
                        factor.sval = Integer.toString(TS.pertenece(nuevo));
                    }
                    else {  setear_Uso("Constante", numero+ambito);
                            factor.sval = Integer.toString(TS.pertenece(numero));}
                }
            else if (num > 1.7976931348623157e+308)
            {
                System.out.println("El double positivo es mayor al limite permitido. Tiene valor: "+num);
                TS.eliminar(Double.toString(num));
                String nuevo = "1.7976931348623157e+308";
                TS.agregar(nuevo, 258);
                setear_Uso("Constante", nuevo+ambito);
                factor.sval = Integer.toString(TS.pertenece(nuevo));
            }
            else
                {setear_Uso("Constante", numero+ambito);
                factor.sval = Integer.toString(TS.pertenece(numero));}
        }
        else
        {   Long entero = Long.valueOf(numero);
            if (entero > 2147483647L) {
                System.out.println("El entero largo positivo es mayor al limite permitido. Tiene valor: "+entero);
                TS.eliminar(Long.toString(entero));
                String nuevo = "2147483647";
                TS.agregar(nuevo, 258);
                System.out.println("El numero fue actualizado en la TS a un valor permitido");
                setear_Uso("Constante", nuevo+ambito);
                factor.sval = Integer.toString(TS.pertenece(nuevo));
            }
            else
                {setear_Uso("Constante", numero+ambito);
                factor.sval = Integer.toString(TS.pertenece(numero));}
        }
    }

public void chequearRangoNegativo(String numero, ParserVal factor) {
    if (numero.contains(".")) //DOUBLE 
    {
        //Convertimos el double en negativo para chequear rango negativo
        double num = Double.parseDouble(numero) * (-1.0); 
            if (num > -2.2250738585072014e-308)
                {
                    if (num != 0.0) //Si esta fuera del rango todavia puede ser válido por el 0.0
                    {
                        System.out.println("El double negativo es mayor al limite permitido. Tiene valor: "+num);
                        TS.eliminar(Double.toString(num * (-1.0)));
                        String nuevo = "-2.2250738585072014e-308";
                        TS.agregar(nuevo, 258);//Lo agrego a la tabla de simbolos con el signo
                        setear_Uso("Constante negativa", nuevo+ambito);
                        System.out.println("El numero fue actualizado en la TS a un valor permitido");
                        factor.sval = Integer.toString(TS.pertenece(nuevo));
                    }
                }
            else if (num < -1.7976931348623157e+308)
            {
                System.out.println("El double positivo es menor al limite permitido. Tiene valor: "+num);
                TS.eliminar(Double.toString(num *(-1.0)));
                String nuevo = "-1.7976931348623157e+308";
                TS.agregar(nuevo, 258);
                setear_Uso("Constante negativa", nuevo+ambito);
                factor.sval = Integer.toString(TS.pertenece(nuevo));
            }
            else 
            {//En caso de respetar el rango solo le agrega el menos en la tabla de simbolos
                TS.eliminar(Double.toString(num *(-1.0)));
                String nuevo = Double.toString(num);
                TS.agregar(nuevo, 258);
                setear_Uso("Constante negativa", nuevo+ambito);
                System.out.println("Se actualizo el double dentro del rango a negativo");
                factor.sval = Integer.toString(TS.pertenece("-"+numero));
            }
    }
        else //LONG
        {
            //Convierto el numero a negativo y verifico el rango
            Long entero = Long.valueOf(numero);
            entero = entero * (-1);
            if (entero < -2147483648L) {
                System.out.println("El entero largo negativo es menor al limite permitido. Tiene valor: "+entero);
                TS.eliminar(Long.toString(entero*(-1)));
                String nuevo = "-2147483648";
                TS.agregar(nuevo, 258);//Lo agrego a la tabla de simbolos en negativo borrando el numero positivo
                setear_Uso("Constante negativa", nuevo+ambito);
                factor.sval = Integer.toString(TS.pertenece(nuevo));
            } else
            {
                //En caso de respetar el rango solo le agrega el menos en la tabla de simbolos
                TS.eliminar(Long.toString(entero*(-1)));
                String nuevo = Long.toString(entero);
                TS.agregar(nuevo, 258);
                setear_Uso("Constante negativa", nuevo+ambito);
                factor.sval = Integer.toString(TS.pertenece("-"+numero));
            }
        }
    }

    public void VerificarSalto(){
        if (lex.salto())
            System.out.println("Falto la coma en la linea " + (Linea.getLinea()-1) +" al final de la sentencia");
        else
            System.out.println("Falto la coma en la linea " + Linea.getLinea() +" al final de la sentencia");
    }

    public void setear_Uso(String uso, String a){
        int clave = TS.buscar_por_ambito(a);
        if (clave ==-1){
            ambitos_Programa = a.split(":");
            String lexema = ambitos_Programa[0];
            clave = TS.pertenece(lexema);
        }
        Simbolo s = TS.get_Simbolo(clave);
        s.set_Uso(uso);
    }

    public void guardar_Tipo(String t){
        tipo = t;
    }

    public void setear_Tipo(){
        for (String s: variables)
        {
        int clave = TS.buscar_por_ambito(s);
        Simbolo sim = TS.get_Simbolo(clave);
        sim.set_Tipo(tipo);
        }
        variables.clear();
    }

    public void guardar_Var(String id){
        variables.add(id);
    }

    public void completarTerceto(int pos, int aux){
        Terceto t = CodigoIntermedio.get(pos);
        String operando = "[" + Integer.toString(aux) + "]";
        t.set_Op(operando);
    }
//#line 843 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 23 "gramatica.y"
{System.out.println("Programa completamente reconocido");}
break;
case 7:
//#line 37 "gramatica.y"
{System.out.println("Bloque de Sentencias reconocido");}
break;
case 12:
//#line 48 "gramatica.y"
{VerificarSalto();}
break;
case 13:
//#line 51 "gramatica.y"
{   if (val_peek(1).sval == "+=") 
                                                    {String aux = "[" + Integer.toString(crear_terceto("+", Integer.toString(TS.pertenece(val_peek(2).sval)), val_peek(0).sval)) + "]";
                                                    yyval.sval = '[' + Integer.toString(crear_terceto("=", Integer.toString(TS.pertenece(val_peek(2).sval)), aux)) + ']';}
                                                else 
                                                    yyval.sval = '[' + Integer.toString(crear_terceto(val_peek(1).sval, Integer.toString(TS.pertenece(val_peek(2).sval)), val_peek(0).sval)) + ']';
                                                ver_ElementoDeclarado(val_peek(2).sval);}
break;
case 14:
//#line 57 "gramatica.y"
{System.out.println("Se reconocio una asignacion a un atributo objeto en linea "+ Linea.getLinea());}
break;
case 15:
//#line 58 "gramatica.y"
{System.out.println("Se reconocio una asignacion a un atributo objeto en linea "+ Linea.getLinea());}
break;
case 16:
//#line 61 "gramatica.y"
{System.out.println("Se reconocio una asignacion en linea "+ Linea.getLinea());
                        yyval.sval = "=";}
break;
case 17:
//#line 63 "gramatica.y"
{System.out.println("Se reconocio una asignacion suma en linea "+ Linea.getLinea());
                        yyval.sval = "+=";}
break;
case 18:
//#line 65 "gramatica.y"
{System.out.println("No es valido el signo de asignacion");}
break;
case 19:
//#line 68 "gramatica.y"
{yyval.sval = '[' + Integer.toString(crear_terceto(val_peek(1).sval, val_peek(2).sval, val_peek(0).sval)) + ']';}
break;
case 20:
//#line 69 "gramatica.y"
{ yyval.sval = val_peek(0).sval; }
break;
case 21:
//#line 72 "gramatica.y"
{yyval.sval = '['+ Integer.toString(crear_terceto( val_peek(1).sval, val_peek(2).sval, val_peek(0).sval))+ ']';}
break;
case 22:
//#line 73 "gramatica.y"
{yyval.sval = val_peek(0).sval;}
break;
case 23:
//#line 76 "gramatica.y"
{ yyval.sval = "*";}
break;
case 24:
//#line 77 "gramatica.y"
{ yyval.sval = "/";}
break;
case 25:
//#line 80 "gramatica.y"
{yyval.sval = Integer.toString(TS.pertenece(val_peek(0).sval));
            setear_Uso("identificador", val_peek(0).sval+ambito);
            ver_ElementoDeclarado(val_peek(0).sval);}
break;
case 26:
//#line 83 "gramatica.y"
{System.out.println("Se reconocio una constante en linea "+Linea.getLinea());
                chequearRangoPositivo(val_peek(0).sval, yyval);}
break;
case 27:
//#line 85 "gramatica.y"
{System.out.println("Se reconocio constante negativa en linea "+ Linea.getLinea());
                chequearRangoNegativo(val_peek(0).sval, yyval);;}
break;
case 28:
//#line 87 "gramatica.y"
{setear_Uso("ConstantePositiva", val_peek(0).sval+ambito);
                yyval.sval = Integer.toString(TS.pertenece(val_peek(0).sval));}
break;
case 29:
//#line 91 "gramatica.y"
{ yyval.sval = "+";}
break;
case 30:
//#line 92 "gramatica.y"
{ yyval.sval = "-";}
break;
case 31:
//#line 96 "gramatica.y"
{ setear_Uso("Clase", val_peek(1).sval+ambito); 
                                                    metodosTemp = new ArrayList<Integer>();
                                                    atributosTemp = new ArrayList<Integer>();
                                                    volver_Ambito();
                                                    }
break;
case 32:
//#line 101 "gramatica.y"
{System.out.println("Clase con herencia por composicion en linea "+Linea.getLinea()); 
                                                                        setear_Uso("Clase", val_peek(5).sval+ambito);
                                                                        metodosTemp = new ArrayList<Integer>();  
                                                                        atributosTemp = new ArrayList<Integer>();
                                                                        volver_Ambito();
                                                                        verificarExisteClasePadre(yyval.sval+ambito, val_peek(2).sval+ambito);
                                                                        }
break;
case 33:
//#line 108 "gramatica.y"
{setear_Uso("Clase", val_peek(0).sval+ambito);
                                volver_Ambito();}
break;
case 34:
//#line 113 "gramatica.y"
{metodosTemp = new ArrayList<Integer>();
                        atributosTemp = new ArrayList<Integer>();
                        setear_Ambito(val_peek(0).sval+ambito, val_peek(0).sval);
                        agregarClase(val_peek(0).sval+ambito, metodosTemp, atributosTemp);
                        ambito += ":" + val_peek(0).sval;
                        yyval.sval = val_peek(0).sval;}
break;
case 35:
//#line 121 "gramatica.y"
{guardar_Tipo(val_peek(1).sval); setear_Tipo();
                                        ver_ElementoDeclarado(val_peek(1).sval);}
break;
case 36:
//#line 125 "gramatica.y"
{ int aux = crear_terceto("CALLMetodoClase", Integer.toString(TS.pertenece(val_peek(2).sval)), val_peek(0).sval); 
                                           int clase = obtenerClase(val_peek(2).sval);
                                          System.out.println("METODO_OBJETO "+ val_peek(0).sval);}
break;
case 37:
//#line 131 "gramatica.y"
{ int clase = obtenerClase(val_peek(2).sval);
                              verificarExisteAtributo(clase, val_peek(0).sval);
                            }
break;
case 40:
//#line 140 "gramatica.y"
{System.out.println("Se reconocio una declaracion de una funcion VOID en linea "+ Linea.getLinea());
                                                                String idFuncion = obtenerAmbito(val_peek(4).sval+ambito);
                                                                agregarFuncion(idFuncion, val_peek(3).sval);
                                                                metodosTemp.add(TS.buscar_por_ambito(idFuncion));
                                                                volver_Ambito();
                                                                }
break;
case 41:
//#line 148 "gramatica.y"
{System.out.println("Se reconocio una declaracion de una funcion VOID vacia en linea "+ Linea.getLinea());
                                            String idFuncion = obtenerAmbito(val_peek(1).sval+ambito);
                                            agregarFuncion(idFuncion, val_peek(0).sval);
                                            metodosTemp.add(TS.buscar_por_ambito(idFuncion));
                                            volver_Ambito();
                                            }
break;
case 42:
//#line 156 "gramatica.y"
{yyval.sval = val_peek(0).sval;
                    setear_Ambito(val_peek(0).sval+ambito, val_peek(0).sval);
                    setear_Uso("Metodo", val_peek(0).sval+ambito);
                    guardar_Var(val_peek(0).sval+ambito);
                    guardar_Tipo("VOID");
                    setear_Tipo();
                    ambito += ":" + val_peek(0).sval;
                    
}
break;
case 46:
//#line 172 "gramatica.y"
{System.out.println("Se reconocio una clausula de seleccion IF en linea "+ Linea.getLinea());}
break;
case 47:
//#line 173 "gramatica.y"
{System.out.println("Se reconocio una impresion por pantalla en linea "+ Linea.getLinea());}
break;
case 48:
//#line 174 "gramatica.y"
{System.out.println("Se reconocio la invocacion de un metodo de un objeto en linea " + Linea.getLinea());}
break;
case 49:
//#line 175 "gramatica.y"
{System.out.println("Se reconocio sentencia IMPL FOR en linea "+ Linea.getLinea());}
break;
case 50:
//#line 176 "gramatica.y"
{System.out.println("Se reconocio sentencia de control DO UNTIL en linea "+ Linea.getLinea());}
break;
case 51:
//#line 177 "gramatica.y"
{System.out.println("Se reconocio sentencia de retorno RETURN en linea "+ Linea.getLinea());
                            int aux = crear_terceto("RETURN", "-", "-");}
break;
case 52:
//#line 181 "gramatica.y"
{System.out.println("Se reconocio una declaracion simple en linea "+ Linea.getLinea());}
break;
case 54:
//#line 183 "gramatica.y"
{System.out.println("Se reconocio una declaracion de un objeto de una clase en linea "+ Linea.getLinea());}
break;
case 55:
//#line 184 "gramatica.y"
{System.out.println("Se reconocio una clase en linea "+ Linea.getLinea());}
break;
case 56:
//#line 187 "gramatica.y"
{yyval.sval = ">";}
break;
case 57:
//#line 188 "gramatica.y"
{yyval.sval = "<";}
break;
case 58:
//#line 189 "gramatica.y"
{yyval.sval = ">=";}
break;
case 59:
//#line 190 "gramatica.y"
{yyval.sval = "<=";}
break;
case 60:
//#line 191 "gramatica.y"
{yyval.sval = "!!";}
break;
case 61:
//#line 192 "gramatica.y"
{yyval.sval = "==";}
break;
case 62:
//#line 193 "gramatica.y"
{System.out.println("Error: El caracter no se reconoce como comparador  en linea "+ Linea.getLinea());}
break;
case 63:
//#line 196 "gramatica.y"
{System.out.println("Se reconoció una condicion  en linea "+ Linea.getLinea());
                                                    yyval.sval = '[' + Integer.toString(crear_terceto(val_peek(2).sval, val_peek(3).sval, val_peek(1).sval)) + ']';
                                                    int aux = crear_terceto("BF", yyval.sval, "-");
                                                    pila.push(aux);}
break;
case 64:
//#line 200 "gramatica.y"
{System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());
                                                        yyval.sval = '[' + Integer.toString(crear_terceto(val_peek(2).sval, val_peek(3).sval, val_peek(1).sval)) + ']';
                                                        int aux = crear_terceto("BF", yyval.sval, "-");
                                                        pila.push(aux); }
break;
case 65:
//#line 204 "gramatica.y"
{System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());
                                                      yyval.sval = '[' + Integer.toString(crear_terceto(val_peek(3).sval, val_peek(4).sval, val_peek(2).sval)) + ']';
                                                      int aux = crear_terceto("BF", yyval.sval, "-");
                                                      pila.push(aux);}
break;
case 66:
//#line 210 "gramatica.y"
{int primero = pila.pop();
                                                                    completarTerceto(primero, puntero_Terceto);}
break;
case 67:
//#line 212 "gramatica.y"
{int primero = pila.pop();
                                                                    completarTerceto(primero, puntero_Terceto);}
break;
case 68:
//#line 214 "gramatica.y"
{int primero = pila.pop();
                                                    completarTerceto(primero, puntero_Terceto);}
break;
case 69:
//#line 216 "gramatica.y"
{int primero = pila.pop();
                                                    completarTerceto(primero, puntero_Terceto);}
break;
case 70:
//#line 218 "gramatica.y"
{System.out.println("Falta el END_IF");
                                                    int primero = pila.pop();
                                                    completarTerceto(primero, puntero_Terceto);}
break;
case 71:
//#line 221 "gramatica.y"
{System.out.println("Falta el END_IF");
                                                    int primero = pila.pop();
                                                    completarTerceto(primero, puntero_Terceto);}
break;
case 72:
//#line 224 "gramatica.y"
{System.out.println("Falta el END_IF");
                                                                    int primero = pila.pop();
                                                                    completarTerceto(primero, puntero_Terceto);}
break;
case 73:
//#line 227 "gramatica.y"
{System.out.println("Falta el END_IF");
                                                                    int primero = pila.pop();
                                                                    completarTerceto(primero, puntero_Terceto);}
break;
case 74:
//#line 230 "gramatica.y"
{System.out.println("Falto la condicion del IF");}
break;
case 75:
//#line 233 "gramatica.y"
{int primero = pila.pop();
                                int aux = crear_terceto("BI", "-", "-");
                                completarTerceto(primero, aux+1);
                                pila.push(aux);}
break;
case 76:
//#line 239 "gramatica.y"
{      int primero = pila.pop();
                                int aux = crear_terceto("BI", "-", "-");
                                completarTerceto(primero, aux+1);
                                pila.push(aux);}
break;
case 79:
//#line 249 "gramatica.y"
{int primero = pila.pop();
                                                                completarTerceto(primero, val_peek(3).ival);}
break;
case 80:
//#line 251 "gramatica.y"
{int primero = pila.pop();
                                                                completarTerceto(primero, val_peek(3).ival);}
break;
case 81:
//#line 253 "gramatica.y"
{System.out.println("Falta la condicion de la sentencia de control");}
break;
case 82:
//#line 256 "gramatica.y"
{yyval.ival = puntero_Terceto;}
break;
case 83:
//#line 259 "gramatica.y"
{setear_Tipo();}
break;
case 84:
//#line 262 "gramatica.y"
{  setear_Ambito(val_peek(0).sval+ambito, val_peek(0).sval); 
                                            setear_Uso("Variable", val_peek(0).sval+ambito); 
                                            guardar_Var(val_peek(0).sval+ambito);
                                            atributosTemp.add(TS.buscar_por_ambito(val_peek(0).sval+ambito));
                                            }
break;
case 85:
//#line 267 "gramatica.y"
{  setear_Ambito(val_peek(0).sval+ambito, val_peek(0).sval); 
                        setear_Uso("Variable", val_peek(0).sval+ambito); 
                        guardar_Var(val_peek(0).sval+ambito);
                        atributosTemp.add(TS.buscar_por_ambito(val_peek(0).sval+ambito));
                        }
break;
case 86:
//#line 274 "gramatica.y"
{
                                    ver_ElementoDeclarado(val_peek(1).sval);
                                    String aux = buscar_Parametro(val_peek(1).sval, ambito);
                                    if ((aux == "-" && val_peek(0).sval=="-") || (aux != null && val_peek(0).sval!=null)) /*si los parametros no coinciden avisa*/
                                        {yyval.sval = "[" + Integer.toString(crear_terceto ("CALL", Integer.toString(TS.pertenece(val_peek(1).sval)), "-")) + "]";}
                                    else
                                        System.out.println("Los parámetros no coinciden");
                                    }
break;
case 87:
//#line 284 "gramatica.y"
{yyval.sval = val_peek(1).sval; yyval.ival= 1;}
break;
case 88:
//#line 285 "gramatica.y"
{yyval.sval = "-"; yyval.ival = 0;}
break;
case 89:
//#line 286 "gramatica.y"
{System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());}
break;
case 90:
//#line 287 "gramatica.y"
{System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());}
break;
case 91:
//#line 288 "gramatica.y"
{System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());}
break;
case 92:
//#line 289 "gramatica.y"
{System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());}
break;
case 93:
//#line 292 "gramatica.y"
{setear_Uso("Parametro formal", val_peek(1).sval);
                                    yyval.sval = val_peek(1).sval; yyval.ival = 1;}
break;
case 94:
//#line 294 "gramatica.y"
{yyval.sval = "-"; yyval.ival = 0;}
break;
case 95:
//#line 295 "gramatica.y"
{System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea()); setear_Uso("Parametro formal", val_peek(1).sval);}
break;
case 96:
//#line 296 "gramatica.y"
{System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea()); setear_Uso("Parametro formal", val_peek(2).sval);}
break;
case 97:
//#line 297 "gramatica.y"
{System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());}
break;
case 98:
//#line 298 "gramatica.y"
{System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());}
break;
case 99:
//#line 301 "gramatica.y"
{guardar_Tipo("DOUBLE");}
break;
case 100:
//#line 302 "gramatica.y"
{guardar_Tipo("USHORT");}
break;
case 101:
//#line 303 "gramatica.y"
{guardar_Tipo("LONG");}
break;
case 102:
//#line 304 "gramatica.y"
{System.out.println("Error: No es un tipo definido en linea "+ Linea.getLinea());}
break;
case 103:
//#line 307 "gramatica.y"
{setear_Uso("Cadena", val_peek(0).sval);
                    int aux = crear_terceto("PRINT", Integer.toString(TS.pertenece(val_peek(0).sval)), "-");}
break;
//#line 1434 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
