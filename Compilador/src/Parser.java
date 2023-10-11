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
import java.io.IOException;

//#line 24 "Parser.java"




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
    0,    2,    2,    4,    5,    6,    1,    3,    3,    8,
    8,   10,   10,   10,   11,   11,   11,   12,   12,   16,
   16,   17,   17,   14,   14,   14,   14,   15,   15,   18,
   18,   18,   19,   21,   21,   22,   23,   25,    9,    9,
    9,    9,    9,    9,    9,    9,    9,    7,    7,    7,
    7,   29,   13,   32,   32,   32,   32,   32,   32,   32,
   33,   33,   33,   27,   27,   27,   27,   27,   27,   27,
   27,   27,   30,   30,   30,   31,   20,   20,   26,   35,
   35,   35,   35,   35,   35,   24,   24,   24,   24,   24,
   24,   34,   34,   34,   34,   28,
};
final static short yylen[] = {                            2,
    1,    2,    1,    1,    1,    1,    3,    2,    2,    1,
    1,    3,    3,    3,    1,    1,    1,    3,    1,    3,
    1,    1,    1,    1,    1,    2,    1,    1,    1,    3,
    7,    2,    2,    1,    1,    6,    3,    7,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    3,    3,    1,    1,    1,    1,    1,    1,    1,
    5,    5,    5,    6,    4,    4,    6,    6,    6,    6,
    3,    4,    4,    4,    4,    2,    3,    1,    2,    3,
    2,    3,    3,    2,    2,    4,    2,    4,    4,    2,
    2,    1,    1,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    5,    0,    1,    0,   95,    0,    0,    0,    0,    0,
   94,   93,   92,    0,    0,   47,    0,    3,    0,    0,
   39,    0,   51,   50,   49,   34,   35,   45,   40,   41,
   42,   43,   46,   48,    0,   17,    0,   25,   27,   16,
   15,    0,    0,    0,    0,    0,    0,   21,    0,    0,
   79,   24,    0,    0,    0,    0,   96,    0,    0,    0,
    0,    0,    6,    2,    7,   11,   10,    8,    9,    0,
   78,    0,   26,    0,   52,   84,   81,    0,   85,    0,
   29,   28,    0,    0,   22,   23,    0,    0,    0,   71,
   60,   59,   58,   56,   57,   54,   55,    0,    0,    0,
   30,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   13,   14,   82,   80,   83,    0,   20,   77,    0,    0,
   66,   65,    0,   72,    0,    0,    0,   87,    0,   91,
    0,    0,   73,   75,   74,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   53,
   62,   61,   63,   67,   64,   69,   68,   70,    0,   88,
   86,   36,   89,    0,    0,   31,    0,   38,    0,
};
final static short yydgoto[] = {                          2,
    3,   17,   18,  147,    4,   65,   19,   68,   20,   21,
   46,   47,   22,   48,   84,   49,   87,   23,   24,   50,
   25,   26,   27,  105,   28,   29,   30,   31,   32,   33,
   34,   98,   56,   35,   51,
};
final static short yysindex[] = {                       -92,
    0,    0,    0, -155,    0,   -1,  -28, -239, -211, -195,
    0,    0,    0, -109, -193,    0,  -90,    0,  -17,  -17,
    0,   42,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -151,    0,    0,    0,    0,    0,
    0, -141, -137,   22, -135,   33,   84,    0,   49,   67,
    0,    0,   33, -128,  -32, -109,    0,  -92,    9, -136,
 -134, -125,    0,    0,    0,    0,    0,    0,    0,   36,
    0,   67,    0,  -25,    0,    0,    0,   16,    0,  -20,
    0,    0, -119,   33,    0,    0,   33, -115,  -32,    0,
    0,    0,    0,    0,    0,    0,    0,   33, -138, -197,
    0, -155,  -40, -118,  -92, -114,   11,    3,   86,   99,
    0,    0,    0,    0,    0,   49,    0,    0,   33,   90,
    0,    0, -109,    0, -109,  -70,    0,    0, -111,    0,
 -155,  108,    0,    0,    0,  -92, -107,   32, -104, -157,
 -108, -100,  -98,   -8,  -35, -155,   39,  -88,  -96,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   39,    0,
    0,    0,    0,  -86,   39,    0,    9,    0,  -92,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   -2,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   27,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -41,    8,
    0,    0,    0,    0,    0,    0,    0,   10,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   40,    0,   -3,    0,    0,    0,    0,    0,   41,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   43,    0,    0,    0,    0,   44,
    0,    0,    0,    0,    0,  -36,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -34,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   47,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   83,  -84,   66,    0,  -29,  -65,    0,  160,    0,    0,
    0,  264,  112,   28,    0,  101,    0,    0,    0,  148,
    0,   51,    0,   21,    0,  159,    0,    0,    0,    0,
    0,  114,  -15,  -37,    0,
};
final static int YYTABLESIZE=383;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         19,
  128,   19,   19,   19,   18,  161,   18,   18,   18,   90,
   82,   53,   81,    1,   44,   45,   42,  126,   19,   42,
   19,  106,   82,   18,   81,   18,   67,   97,  102,   96,
    1,   44,   45,   57,   63,  159,   42,   43,   44,   45,
   53,   44,   53,   42,   43,   58,  146,   42,  103,  104,
   53,   33,   41,   32,   63,   42,  114,   53,   82,   41,
   81,   59,   77,  124,  125,  129,   42,   24,   24,   24,
   78,   24,  152,   24,   82,  131,   81,   42,   62,   61,
   42,  162,   64,   76,   12,   78,   37,   24,   90,   54,
   85,  133,  135,  166,    1,   86,   60,  112,  154,  168,
    5,    6,   70,  155,    7,   71,  149,    8,    9,   10,
   11,   12,   13,   14,  117,   15,   73,  121,   16,   74,
   79,  100,  122,  123,   83,   88,   82,   90,   81,  106,
  139,  109,   82,  107,   81,  108,  115,  130,   99,  131,
  101,  118,  132,  136,  137,  145,    5,    6,  148,  150,
    7,  153,  156,    8,    9,   10,   11,   12,   13,   14,
  157,   15,  158,   63,   16,    5,    6,  163,  164,    7,
  167,    4,    8,    9,   10,   11,   12,   13,   14,   69,
   15,  111,   72,   16,  116,    5,  144,  169,  141,    7,
  143,   64,    8,    9,   10,   11,   12,   13,   14,  165,
   15,   75,  119,   16,    0,  140,    0,  142,    0,    0,
    0,   64,    0,    0,   19,  127,    0,    0,    0,   18,
  160,   90,   95,   91,    0,   11,   12,   13,   52,   38,
   39,   52,   38,   39,   19,   19,   19,   19,   66,   18,
   18,   18,   18,   92,   93,   94,   95,   36,   37,   38,
   39,    0,   53,   44,   36,   37,   38,   39,  134,   52,
   38,   39,    0,   33,    5,   32,   40,   52,   38,   39,
   55,  113,    0,   40,   11,   12,   13,   76,   52,   38,
   39,    0,   78,    0,    0,    0,    0,  151,    0,   52,
   38,   39,  110,   38,   39,   76,   12,    0,   37,   24,
    0,    0,    0,    0,    0,    0,    0,   78,    0,   80,
    0,    0,    0,    0,    0,    0,   89,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  120,    0,    0,    0,    0,    0,    0,    0,    0,
   55,   55,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  138,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   41,   43,   44,   45,   41,   41,   43,   44,   45,   44,
   43,   40,   45,  123,   40,   41,   45,  102,   60,   45,
   62,   59,   43,   60,   45,   62,   44,   60,   58,   62,
  123,   40,   41,  273,  125,   44,   45,   46,   40,   41,
   44,   44,   40,   45,   46,  257,  131,   45,   40,   41,
   40,   44,   61,   44,  125,   45,   41,   61,   43,   61,
   45,  257,   41,  261,  262,  103,   45,   41,   42,   43,
   44,   45,   41,   47,   43,  105,   45,   45,  272,   14,
   45,  147,   17,   44,   44,   59,   44,   44,  123,    7,
   42,  107,  108,  159,  123,   47,   14,   70,  256,  165,
  256,  257,   61,  261,  260,  257,  136,  263,  264,  265,
  266,  267,  268,  269,   87,  271,  258,  256,  274,  257,
  256,   56,  261,  262,   41,   59,   43,  256,   45,  167,
   41,  257,   43,  270,   45,  270,  256,  256,   56,  169,
   58,  257,  257,   58,   46,  257,  256,  257,   41,  257,
  260,  256,  261,  263,  264,  265,  266,  267,  268,  269,
  261,  271,  261,  125,  274,  256,  257,  256,  265,  260,
  257,  125,  263,  264,  265,  266,  267,  268,  269,   20,
  271,   70,   35,  274,   84,  256,  257,  167,  123,  260,
  125,  126,  263,  264,  265,  266,  267,  268,  269,  149,
  271,   43,   89,  274,   -1,  123,   -1,  125,   -1,   -1,
   -1,  146,   -1,   -1,  256,  256,   -1,   -1,   -1,  256,
  256,  256,  257,  256,   -1,  266,  267,  268,  257,  258,
  259,  257,  258,  259,  276,  277,  278,  279,  256,  276,
  277,  278,  279,  276,  277,  278,  279,  256,  257,  258,
  259,   -1,  256,  256,  256,  257,  258,  259,  256,  257,
  258,  259,   -1,  256,  256,  256,  275,  257,  258,  259,
    7,  256,   -1,  275,  266,  267,  268,  256,  257,  258,
  259,   -1,  256,   -1,   -1,   -1,   -1,  256,   -1,  257,
  258,  259,  257,  258,  259,  256,  256,   -1,  256,  256,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   44,   -1,   46,
   -1,   -1,   -1,   -1,   -1,   -1,   53,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   98,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  107,  108,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  119,
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
"CADENA","RETURN","\"+=\"","\"==\"","\"!!\"","\">=\"","\"<=\"",
};
final static String yyrule[] = {
"$accept : programa",
"programa : bloque_de_Sentencias",
"conjuntoSentencias : conjuntoSentencias sentencia",
"conjuntoSentencias : sentencia",
"cuerpo_funcion : conjuntoSentencias",
"llave_que_abre : '{'",
"llave_que_cierra : '}'",
"bloque_de_Sentencias : llave_que_abre conjuntoSentencias llave_que_cierra",
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
"declaracionClase : CLASS ID llave_que_abre conjuntoSentencias ID ',' llave_que_cierra",
"declaracionClase : CLASS ID",
"declaracionObjeto : ID lista_Variables",
"declaracionFuncion : funcion_VOID",
"declaracionFuncion : funcion_VOID_vacia",
"funcion_VOID : VOID ID parametro_formal llave_que_abre cuerpo_funcion llave_que_cierra",
"funcion_VOID_vacia : VOID ID parametro_formal",
"clausula_IMPL : IMPL FOR ID ':' llave_que_abre funcion_VOID llave_que_cierra",
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

//#line 186 "gramatica.y"

    Lexico lex;
    TablaSimbolos TS = new TablaSimbolos();

    public Parser(Lexico lexico){
        this.lex = lexico;
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
            return token.getIdToken();
        }
        else
            TS.imprimirContenido();
        return 0;
    }
    public void yyerror(String error) {
        System.out.println(error);
    }
    

    
//#line 449 "Parser.java"
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
//#line 15 "gramatica.y"
{System.out.println("progrm");}
break;
case 5:
//#line 25 "gramatica.y"
{System.out.println("Se reconocio una llave que abre");}
break;
case 6:
//#line 28 "gramatica.y"
{System.out.println("Se reconocio una llave que cierra");}
break;
case 7:
//#line 31 "gramatica.y"
{System.out.println("Bloque reconocido");}
break;
case 11:
//#line 39 "gramatica.y"
{System.out.println("Falto la coma al final de la sentencia");}
break;
case 12:
//#line 42 "gramatica.y"
{System.out.println("Se reconocio una asignacion");}
break;
case 13:
//#line 43 "gramatica.y"
{System.out.println("Se reconocio una asignacion a un atributo objeto");}
break;
case 14:
//#line 44 "gramatica.y"
{System.out.println("Se reconocio una asignacion a un atributo objeto");}
break;
case 17:
//#line 49 "gramatica.y"
{System.out.println("No es valido el signo de asignacion");}
break;
case 26:
//#line 66 "gramatica.y"
{System.out.println("Se reconocio constante negativa en linea "+ Linea.getLinea());}
break;
case 30:
//#line 75 "gramatica.y"
{System.out.println("Se reconocio una clase");}
break;
case 31:
//#line 76 "gramatica.y"
{System.out.println("Se reconocio una clase con herencia por composicion");}
break;
case 32:
//#line 77 "gramatica.y"
{System.out.println("Se reconocio una clase");}
break;
case 36:
//#line 87 "gramatica.y"
{System.out.println("Se reconocio una funcion void");}
break;
case 37:
//#line 90 "gramatica.y"
{System.out.println("Se reconocio una funcion void vacia");}
break;
case 38:
//#line 93 "gramatica.y"
{System.out.println("Se reconocio sentencia IMPL FOR");}
break;
case 52:
//#line 113 "gramatica.y"
{System.out.println("se reconocio la invocacion de un metodo de un objeto en linea " + Linea.getLinea());}
break;
case 60:
//#line 125 "gramatica.y"
{System.out.println("No se reconoce como comparador");}
break;
case 61:
//#line 128 "gramatica.y"
{System.out.println("Se reconoció una condicion");}
break;
case 62:
//#line 129 "gramatica.y"
{System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());}
break;
case 63:
//#line 130 "gramatica.y"
{System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());}
break;
case 64:
//#line 133 "gramatica.y"
{System.out.println("Se reconocio un IF");}
break;
case 65:
//#line 134 "gramatica.y"
{System.out.println("Se reconocio un IF");}
break;
case 66:
//#line 135 "gramatica.y"
{System.out.println("Falta el END_IF");}
break;
case 67:
//#line 136 "gramatica.y"
{System.out.println("Falta el END_IF");}
break;
case 68:
//#line 137 "gramatica.y"
{System.out.println("Se reconocio un IF");}
break;
case 69:
//#line 138 "gramatica.y"
{System.out.println("Se reconocio un IF");}
break;
case 70:
//#line 139 "gramatica.y"
{System.out.println("Se reconocio un IF");}
break;
case 71:
//#line 140 "gramatica.y"
{System.out.println("Falto la condicion del IF");}
break;
case 72:
//#line 141 "gramatica.y"
{System.out.println("Se reconocio un IF");}
break;
case 73:
//#line 144 "gramatica.y"
{System.out.println("Se reconocio sentencia DO UNTIL");}
break;
case 74:
//#line 145 "gramatica.y"
{System.out.println("Se reconocio sentencia DO UNTIL");}
break;
case 75:
//#line 146 "gramatica.y"
{System.out.println("Falta la condicion en linea: " + Linea.getLinea());}
break;
case 79:
//#line 156 "gramatica.y"
{System.out.println("Se reconocio una invocacion de una funcion");}
break;
case 82:
//#line 161 "gramatica.y"
{System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());}
break;
case 83:
//#line 162 "gramatica.y"
{System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());}
break;
case 84:
//#line 163 "gramatica.y"
{System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());}
break;
case 85:
//#line 164 "gramatica.y"
{System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());}
break;
case 88:
//#line 169 "gramatica.y"
{System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());}
break;
case 89:
//#line 170 "gramatica.y"
{System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());}
break;
case 90:
//#line 171 "gramatica.y"
{System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());}
break;
case 91:
//#line 172 "gramatica.y"
{System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());}
break;
case 95:
//#line 178 "gramatica.y"
{System.out.println("No es un tipo definido");}
break;
case 96:
//#line 181 "gramatica.y"
{System.out.println("Se reconocio una impresion por pantalla");}
break;
//#line 774 "Parser.java"
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
