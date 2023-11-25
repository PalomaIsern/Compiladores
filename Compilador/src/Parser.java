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
import compiladores.Conversion;
import compiladores.Assembler;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Arrays;
import java.util.Iterator;
import java.util.HashSet;


import java.io.IOException;

//#line 36 "Parser.java"




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
    5,    9,    9,    9,    9,   35,   35,   35,   35,   35,
   35,   35,   36,   36,   36,   31,   31,   31,   31,   31,
   31,   31,   31,   31,   37,   39,   38,   38,   33,   33,
   33,   40,   34,   21,   21,   30,   23,   23,   23,   23,
   23,   23,   28,   28,   28,   28,   28,   28,   41,   41,
   41,   41,   32,
};
final static short yylen[] = {                            2,
    1,    2,    1,    3,    2,    1,    3,    3,    2,    2,
    1,    1,    3,    3,    3,    1,    1,    1,    3,    1,
    3,    1,    1,    1,    1,    1,    2,    1,    1,    1,
    2,    6,    1,    2,    2,    4,    3,    1,    1,    5,
    2,    2,    8,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    5,    5,    5,    6,    6,    4,    4,    4,
    4,    6,    6,    3,    1,    1,    1,    1,    4,    5,
    4,    1,    2,    3,    1,    2,    3,    2,    3,    3,
    2,    2,    4,    2,    4,    4,    2,    2,    1,    1,
    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    1,  102,    0,    0,    0,    0,    0,  101,
  100,   99,   82,    0,   51,    0,    3,    0,    0,   44,
    0,   55,    0,   54,   48,   53,   38,   39,    0,   49,
   45,   46,   47,   50,   52,    0,    0,   18,    0,   26,
   28,   17,   16,    0,    0,    0,    0,    0,    0,   22,
    0,    0,   86,   25,    0,   75,    0,    0,    0,  103,
   34,   42,    0,    7,    2,   12,   11,   10,    9,    0,
    0,   31,    0,    0,    0,    0,    0,    0,    0,    0,
   85,    0,   27,    0,   91,   88,    0,   92,    0,   30,
   29,    0,    0,   23,   24,    0,    0,    0,   62,   58,
   59,   61,   60,   56,   57,    0,   76,    0,    0,   74,
    0,    0,   14,   15,    0,    0,   94,    0,   98,    0,
    0,    0,    0,    0,    0,   36,   89,   87,   90,    0,
   21,   84,    0,    0,   70,   68,    0,   71,   69,    0,
    0,    0,    0,    0,    0,    0,    0,    8,    0,    5,
    0,   81,   79,    0,    0,   77,   78,    0,    0,    0,
   37,    0,   95,   93,   40,   96,    4,   80,   64,   63,
   65,   72,   66,   73,   67,    0,    0,   32,    0,    0,
   43,
};
final static short yydgoto[] = {                          2,
   56,   16,   17,  122,   18,   68,  146,   80,   19,   20,
   48,   49,   21,   50,   93,   51,   96,   22,   23,   24,
   52,   25,   53,   26,   27,   28,   29,   75,   30,   31,
   32,   33,   34,   35,  106,   58,   59,  158,  109,   36,
   37,
};
final static short yysindex[] = {                       -99,
 -159,    0,    0,    0,  -23,   20, -225, -198, -166,    0,
    0,    0,    0, -179,    0,   49,    0,  -42,  -42,    0,
   65,    0,  -20,    0,    0,    0,    0,    0,    2,    0,
    0,    0,    0,    0,    0,  -65, -155,    0,    0,    0,
    0,    0,    0, -131, -126,   35, -120,   42,   89,    0,
   19,   81,    0,    0,   42,    0,  -16,  -84, -115,    0,
    0,    0, -113,    0,    0,    0,    0,    0,    0,   45,
 -159,    0,    8, -114,   22, -111,   -5, -146,  -42, -123,
    0,   81,    0,   23,    0,    0,   40,    0,   34,    0,
    0, -108,   42,    0,    0,   42, -107,  -16,    0,    0,
    0,    0,    0,    0,    0,   42,    0, -143, -140,    0,
   91,  105,    0,    0,   68,    0,    0, -105,    0, -159,
  112,   83,  -42, -116,   27,    0,    0,    0,    0,   19,
    0,    0,   42,   94,    0,    0,  -84,    0,    0,  -84,
   32, -101,  -40,  -33, -159,   33,  -97,    0,  -42,    0,
   30,    0,    0,   41,  -96,    0,    0, -161, -132, -104,
    0,   37,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -42,    2,    0,   38,   22,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -41,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   10,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -34,  -25,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -24,    0,    0,    0,    0,    0,
    0,  -19,    0,   96,    0,    0,    0,    0,  -12,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   -7,    0,    0,    0,  -31,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -29,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   43,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
   73,  -21,   62,    0,   11,   15,    0,    0,    0,    0,
    0,  301,   95,   -8,    0,   74,    0,    0,    0,    0,
  129,    0,   85,    0,   26,    0,   28,   -6,    0,    0,
    0,    0,    0,    0,   72,  -80,  117,   54,    0,    0,
    1,
};
final static int YYTABLESIZE=452;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         46,
   47,   67,   33,  162,   44,   45,   20,  164,   20,   20,
   20,   19,   97,   19,   19,   19,   46,   47,   35,   41,
   43,   44,   45,    1,   83,   20,   91,   20,   90,   76,
   19,   13,   19,   69,   46,   47,   25,   43,    1,   44,
   45,   73,   74,  105,  153,  104,   79,   60,  117,  115,
   25,   25,   25,   85,   25,   43,   25,   78,   61,   55,
   94,  114,   46,   47,   44,   95,   55,   44,   85,   55,
  168,   44,    3,  118,   44,   86,   91,   65,   90,   44,
  128,  170,   91,   91,   90,   90,   44,  131,  123,   44,
   62,   97,   63,  124,  172,   72,    4,    5,  145,  173,
    6,   81,   71,    7,    8,    9,   10,   11,   12,   13,
   77,   14,  135,    6,   15,  138,    7,  136,  137,  107,
  139,  140,   13,  174,   14,   70,   83,   15,  175,   92,
   84,   91,  149,   90,  155,   88,   91,  150,   90,   97,
  110,  119,    1,  111,  120,  121,  125,  129,  141,  132,
  142,  144,  147,  151,  160,  161,   37,  165,  166,  171,
    9,  178,  181,  167,  113,   82,  130,    6,  126,  133,
  180,    4,    5,   64,  108,    6,   65,   76,    7,    8,
    9,   10,   11,   12,   13,  176,   14,  177,    0,   15,
  179,   77,   64,  159,    6,    0,    0,    7,  157,    0,
    0,  157,    0,   13,    0,   14,   65,  148,   15,  156,
    0,    0,  156,   66,   33,   38,   39,   40,   41,    0,
    0,   20,  163,    0,   97,  102,   19,    0,    0,    0,
   35,   41,   38,   39,   40,   41,   83,   42,    0,   99,
   20,   20,   20,   13,   20,   19,   19,   19,   25,   19,
   38,   54,   40,   41,   42,    0,    0,    4,  100,  101,
  102,    0,  103,  116,    0,   85,    0,   10,   11,   12,
    0,    0,   42,   10,   11,   12,   54,   40,   41,   54,
   40,   41,  152,   54,   40,   41,   54,   40,   41,    0,
   85,   54,   40,   41,    0,  127,  169,    0,   54,   40,
   41,  112,   40,   41,    4,    5,   57,    0,    6,    0,
    0,    7,    8,    9,   10,   11,   12,   13,    0,   14,
    0,    0,   15,    4,  143,    0,    0,    6,    0,    0,
    7,    8,    9,   10,   11,   12,   13,    0,   14,   77,
    0,   15,    6,    0,    0,    7,   87,    0,   89,    0,
    0,   13,    0,   14,    0,   98,   15,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  134,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   57,    0,    0,    0,    0,
    0,    0,    0,  154,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   57,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   41,   44,   44,   44,   45,   46,   41,   41,   43,   44,
   45,   41,   44,   43,   44,   45,   40,   41,   44,   44,
   61,   45,   46,  123,   44,   60,   43,   62,   45,   29,
   60,   44,   62,   19,   40,   41,   44,   61,  123,   45,
   46,   40,   41,   60,  125,   62,   36,  273,   41,   71,
   41,   42,   43,   44,   45,   61,   47,  123,  257,   40,
   42,   70,   40,   41,   45,   47,   40,   45,   59,   40,
  151,   45,    0,   73,   45,   41,   43,   16,   45,   45,
   41,   41,   43,   43,   45,   45,   45,   96,   78,   45,
  257,  123,  272,   79,  256,   23,  256,  257,  120,  261,
  260,  257,  123,  263,  264,  265,  266,  267,  268,  269,
  257,  271,  256,  260,  274,  256,  263,  261,  262,   58,
  261,  262,  269,  256,  271,   61,  258,  274,  261,   41,
  257,   43,  122,   45,   41,  256,   43,  123,   45,   59,
  256,  256,  123,  257,  123,  257,  270,  256,   58,  257,
   46,  257,   41,  270,  123,  257,   61,  125,  256,  256,
  265,  125,  125,  149,   70,   37,   93,  125,   84,   98,
  177,  256,  257,  125,   58,  260,  115,  177,  263,  264,
  265,  266,  267,  268,  269,  160,  271,  160,   -1,  274,
  176,  257,  125,  140,  260,   -1,   -1,  263,  137,   -1,
   -1,  140,   -1,  269,   -1,  271,  145,  125,  274,  137,
   -1,   -1,  140,  256,  256,  256,  257,  258,  259,   -1,
   -1,  256,  256,   -1,  256,  257,  256,   -1,   -1,   -1,
  256,  256,  256,  257,  258,  259,  256,  278,   -1,  256,
  275,  276,  277,  256,  279,  275,  276,  277,  256,  279,
  256,  257,  258,  259,  278,   -1,   -1,  256,  275,  276,
  277,   -1,  279,  256,   -1,  256,   -1,  266,  267,  268,
   -1,   -1,  278,  266,  267,  268,  257,  258,  259,  257,
  258,  259,  256,  257,  258,  259,  257,  258,  259,   -1,
  256,  257,  258,  259,   -1,  256,  256,   -1,  257,  258,
  259,  257,  258,  259,  256,  257,    6,   -1,  260,   -1,
   -1,  263,  264,  265,  266,  267,  268,  269,   -1,  271,
   -1,   -1,  274,  256,  257,   -1,   -1,  260,   -1,   -1,
  263,  264,  265,  266,  267,  268,  269,   -1,  271,  257,
   -1,  274,  260,   -1,   -1,  263,   46,   -1,   48,   -1,
   -1,  269,   -1,  271,   -1,   55,  274,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  106,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  125,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  133,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  151,
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
"CADENA","RETURN","\">=\"","\"<=\"","\"==\"","\"+=\"","\"!!\"",
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
"metodo_objeto : ID '.' ID parametro_real",
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
"sentencia_de_Control : inicio_DO sentenciaEjecutable fin_sentencia UNTIL condicion",
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

//#line 502 "gramatica.y"

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
    Stack<String> tipos = new Stack<String>();
    HashMap<Integer, Integer> funciones = new HashMap<Integer, Integer>();
    HashMap<Integer, ArrayList<Integer>> metodosClases = new HashMap<Integer, ArrayList<Integer>>();
    HashMap<Integer, ArrayList<Integer>> metodosNoImplementados = new HashMap<Integer, ArrayList<Integer>>();
    HashMap<Integer, ArrayList<Integer>> atributosClases = new HashMap<Integer, ArrayList<Integer>>();
    ArrayList<Integer> metodosTemp = new ArrayList<Integer>();
    ArrayList<Integer> metodosTempNoImp = new ArrayList<Integer>();
    ArrayList<Integer> atributosTemp = new ArrayList<Integer>();
    int clavePadre = -1;
    boolean dentroFuncion = false;
    Conversion convertible = new Conversion();
    Assembler CodigoAssembler;

    public boolean ver_ElementoDeclarado(String elemento){
        int clave = TS.buscar_por_ambito(elemento+ambito);
        if (clave==-1)
            clave = TS.pertenece(elemento);
        String aux = ambito;
        if (clave != -1 && TS.get_Simbolo(clave).get_Ambito()!="-"){
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
            if (!s.get_Ambito().equals(elemento+aux)){
                System.out.println("ERROR: linea "+ Linea.getLinea() + " - " + elemento + " está fuera del alcance");
            }
            else {
                System.out.println("El elemento " + elemento+ " está al alcance");
            }
            return true;
        }
        else
            return false;
    }


    private void ver_ObjetoDeclarado(String objeto) {
        int clave = TS.buscar_por_ambito(objeto+ambito);
        if (clave == -1) {
            String a = ambito;
            int index = a.lastIndexOf(":");
            while (clave == -1 && index > 0) {
                a = a.substring(0, index);
                clave = TS.buscar_por_ambito(objeto+a);
                index = a.lastIndexOf(":");
            }
            if (clave == -1) {
                System.out.println("ERROR: linea "+Linea.getLinea()+" el objeto \""+objeto+"\" no se encuentra declarado o no está al alcance");
            } else {
                System.out.println("objeto \""+objeto+"\" está declarado y al alcance");
            }
        } else {
            System.out.println("objeto \""+objeto+"\" está declarado y al alcance");
        }
        
    }
    
    public String obtenerAmbito(String ambito) 
    {   //saca la ultima parte del ambito 
        if (ambito.contains(":")) {
            String[] partes = ambito.split(":");
            return String.join(":", Arrays.copyOf(partes, partes.length - 1));
        }
        return " ";
    }

    public int obtenerClase(String id) {
        //Dado el ID de un objeto, obtenemos la referencia a la TS de la clase a la cual pertenece
        int clave = TS.buscar_por_ambito(id);
        while (clave == -1 && id.contains(":")) {
            id = id.substring(0, id.lastIndexOf(":"));
            clave = TS.buscar_por_ambito(id);
        }
        if (clave != -1) {
            String nombreClase = TS.get_Simbolo(clave).get_Tipo();
            return TS.buscar_por_ambito(nombreClase);
        } else return clave;
    }

    private void eliminarElementos() {
        for (Integer i : metodosTemp) {
            TS.remove_Simbolo(i);
            funciones.remove(i);
        }
        metodosTemp = new ArrayList<Integer>();
        for (Integer i: atributosTemp) {
            TS.remove_Simbolo(i);
        }
        atributosTemp  = new ArrayList<Integer>();
        for (Integer i : metodosTempNoImp) {
            TS.remove_Simbolo(i);
            funciones.remove(i);
        }
        metodosTempNoImp  = new ArrayList<Integer>();

    }

    public int verificarExisteClasePadre(String hijo, String padre) {
        // en el caso de que una clase herede de otra, se verifica que la clase padre de la cual se va a heredar haya sido declarada
        ArrayList<Integer> clases = obtenerClasesDeclaradas();
        boolean existe = false;
        for (Integer i : clases) {
            Simbolo s = TS.get_Simbolo(i);
            if (s.get_Lex().equals(padre)) {
                String aux = s.get_Ambito();
                aux = aux.substring(aux.indexOf(":"));
                if (alAlcance(aux)) {
                    return i;
                } else existe = true;
            } 
        }
        if (existe) 
            System.out.println("ERROR: linea "+ Linea.getLinea()+ " - La clase \""+padre+ "\" no se encuentra al alcance");
        else 
            System.out.println("ERROR: linea "+ Linea.getLinea()+ " la clase \""+ padre+ "\" no existe");
        return -1;
    }

    public boolean verificar_Limite(){
        ambitos_Programa = ambito.split(":");
        int num_a = ambitos_Programa.length;
        if (num_a+1 > limite_Anidamiento)
            {System.out.println("ERROR: linea" + Linea.getLinea()+ " No es posible generar mas anidamientos");
            return false;}
        else
            return true;
    }

    public boolean setear_Ambito(String a, String lex){
        int clave = TS.buscar_por_ambito(a);
        if (clave != -1){
            if (claseVacia(clave)) {
                    System.out.println("se completo la FORWARD DECLARATION de "+a+lex);
                    return true;                
            } else {
                    System.out.println("ERROR: linea " + Linea.getLinea() + " - Redeclaracion de " + lex + " en el ambito " + a);
                    return false;
            }
        }
        else{
            clave = TS.pertenece(lex);
            if (clave != -1){
                 Simbolo s = TS.get_Simbolo(clave);
                String amb= a.substring(a.indexOf(":")+1);
                if (s.get_Ambito().equals(a)) {
                    if (claseVacia(clave)) {
                            System.out.println("se completo la FORWARD DECLARATION de "+s.get_Ambito());
                            return true;
                        } else {
                            System.out.println("ERROR: linea " + Linea.getLinea() + " - Redeclaracion de " + s.get_Lex()+ " en el ambito "+ amb);
                            return false;
                        }
                }
                else
                    if (s.get_Ambito()=="-")
                        s.set_Ambito(a);
                    else
                        {Simbolo nuevo = new Simbolo(s.get_Token(), s.get_Lex());
                        nuevo.set_Ambito(a);
                        TS.agregar_sin_chequear(nuevo);}
            }
            return true;
        }
    }

    /*public boolean setear_Ambito(String a, String lex){
        int clave = TS.pertence(lex);
        if (clave!=-1){
            Simbolo s = TS.get_Simbolo(clave);
            String amb= a.substring(a.indexOf(":")+1);
            if (s.get_Ambito().equals(a)) {
                if (claseVacia(clave)) {
                        System.out.println("se completo la FORWARD DECLARATION de "+s.get_Ambito());
                        return true;
                    } else {
                        System.out.println("ERROR: linea " + Linea.getLinea() + " - Redeclaracion de " + s.get_Lex()+ " en el ambito "+ amb);
                        return false;
                    }
            }
            else
                if (s.get_Ambito()=="-")
                    s.set_Ambito(a);
                else
                    {Simbolo nuevo = new Simbolo(s.get_Token(), s.get_Lex());
                    nuevo.set_Ambito(a);
                    TS.agregar_sin_chequear(nuevo);}
        }
        return true;
    }*/

    public void agregarClase(String ID, ArrayList<Integer> mets, ArrayList<Integer> metsNoImp, ArrayList<Integer> atrs) {
        int clave = TS.buscar_por_ambito(ID);
        //chequeamos que no se sobreescriban los metodos del padre, si es que tiene
        if (clavePadre != -1) {
            ArrayList<String> metodosPadre = obtenerNombresMetodos(clavePadre);
            Iterator<Integer> iterator = mets.iterator();
            while (iterator.hasNext()) {
                Integer i = iterator.next();
                String nombre = TS.get_Simbolo(i).get_Lex();
                if (metodosPadre.contains(nombre)) {
                    System.out.println("ERROR: linea "+ funciones.get(i)+" no se puede sobrescribir el método \"" + nombre + "\"");
                    TS.remove_Simbolo(i);
                    funciones.remove(i);
                    iterator.remove(); 
                }
            }
            iterator = metsNoImp.iterator();
            while (iterator.hasNext()) {
                Integer i = iterator.next();
                String nombre = TS.get_Simbolo(i).get_Lex();
                if (metodosPadre.contains(nombre)) {
                    System.out.println("ERROR: linea "+ funciones.get(i)+" no se puede sobrescribir el método \"" + nombre + "\"");
                    TS.remove_Simbolo(i);
                    funciones.remove(i);
                    iterator.remove(); 
                }
            }
        }
        metodosClases.put(clave, mets);
        metodosNoImplementados.put(clave, metsNoImp);
        atributosClases.put(clave, atrs);
        clavePadre = -1;
    }

    private ArrayList<Integer> obtenerClasesDeclaradas() {
        HashSet<Integer> keys = new HashSet<>();
        keys.addAll(metodosClases.keySet());
        keys.addAll(metodosNoImplementados.keySet());
        keys.addAll(atributosClases.keySet());
        return new ArrayList<Integer>(keys);
    }

    private boolean alAlcance(String a) {
        String amb= ":" + a.substring(a.indexOf(":")+1);
        String actual = ambito;
        if (amb.equals(actual)) 
            return true;
        else { int index = actual.lastIndexOf(":");
                while (index != -1) {
                    actual = actual.substring(0, index);
                    if (amb.equals(actual))
                        return true;
                    else    
                        index = actual.lastIndexOf(":");
                }
        }
        return false;
    }

    public String obtenerTipo(String idClase) {
        //A partir de un ID de una clase, detecta la clase al alcance y devuelve lex+ambito correspondiente
        ArrayList<Integer> clases = obtenerClasesDeclaradas();
        for (Integer i: clases) {
            Simbolo s = TS.get_Simbolo(i);
            if (s.get_Lex().equals(idClase)) {
                if (alAlcance(s.get_Ambito())) {
                    return s.get_Ambito();
                }
            }
        }
        return " ";
    }

    public ArrayList<String> obtenerNombresMetodos(int clave) {
        //devuelve los nombres de todos los metodos de una clase (declarados e implementados)
        ArrayList<String> retorno = new ArrayList<String>();
        ArrayList<Integer> aux = metodosClases.get(clave);        
        for (Integer m : aux){
            retorno.add(TS.get_Simbolo(m).get_Lex());
        }
        aux = metodosNoImplementados.get(clave);
        for (Integer m : aux){
            retorno.add(TS.get_Simbolo(m).get_Lex());
        }
        return retorno;
    }
    
    public void agregarMetodoImplementado(String clase, String metodo) 
    {   // si se implementa un metodo con IMPL, lo pasa a "implementado" y lo saca de la lista de "No implementados" 
        int clave = TS.buscar_por_ambito(clase);
        int clavemet = TS.buscar_por_ambito(metodo);
        ArrayList<Integer> metodos = metodosClases.get(clave);
        metodos.add(clavemet);
        metodos = metodosNoImplementados.get(clave);
        metodos.remove(Integer.valueOf(clavemet));
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

     public void imprimirClases() {
        System.out.println("CLASES");
        for (HashMap.Entry<Integer, ArrayList<Integer>> e : metodosClases.entrySet()) {
            int ref = e.getKey();
            String r = "ref: "+e.getKey()+" - nombre: "+ TS.get_Simbolo(ref).get_Ambito() + " | metodos implementados : ";
            ArrayList<Integer> mets = e.getValue();
            if (mets != null) {
                for (Integer i: mets) {
                    r +=  TS.get_Simbolo(i).get_Lex() + " ; ";
                }
            }
            mets = metodosNoImplementados.get(ref);
            r += "\n"+"\t \t \t \t";
            r += "metodos no implementados : ";
            if (mets != null) {
                for (Integer i: mets) {
                    r +=  TS.get_Simbolo(i).get_Lex() + " ; ";
                }
            } 
            mets = atributosClases.get(ref);
            r += "\n"+"\t \t \t \t";
            r += "atributos : ";
            if (mets != null) {
                for (Integer i: mets) {
                    r += TS.get_Simbolo(i).get_Lex() + " ; ";
                }
            }
            System.out.println(r);
        }
    }
    
    public int verificarExistencia(int clase, String nombre, String objeto) 
    {   ArrayList<Integer> o = new ArrayList<Integer>();
        if (objeto.equals("atributo")) 
            o = atributosClases.get(clase);
        else if (objeto.equals("metodo"))
            o = metodosClases.get(clase);
        else if (objeto.equals("metodoNoImpl"))
            o = metodosNoImplementados.get(clase);
        if (o != null) {
            for (Integer elemento : o) {
                if (TS.get_Simbolo(elemento).get_Lex().equals(nombre)) {
                    return elemento;
                }
            }
            
            return -1;

        }
        return -1;
    }
    
    private boolean claseVacia(int clave) {
        //una calse se considera "vacia" cuando se ha declarado pero no se ha definido su cuerpo
        ArrayList<Integer> aux = metodosClases.get(clave);
        if (aux != null && aux.size() == 0) {
            aux = metodosNoImplementados.get(clave);
            if (aux != null && aux.size() == 0) {
                aux = atributosClases.get(clave);
                if (aux != null && aux.size() == 0) {
                    return true;
                }
            }
        }  
        return false;
    }

    public void imprimirAtributosClases() {
        System.out.println("ATRIBUTOS CLASES");
        for (HashMap.Entry<Integer, ArrayList<Integer>> e : atributosClases.entrySet()) {
            ArrayList<Integer> atrs = e.getValue();
            int ref = e.getKey();
            String r = "ref: "+e.getKey()+ " - nombre: "+TS.get_Simbolo(ref).get_Ambito() +  " | atributos: ";
            for (Integer a: atrs) {
                r += TS.get_Simbolo(a).get_Lex() + " ; ";
            };
            System.out.println(r);
        }
    }

    public void imprimirFunciones() {
        System.out.println("FUNCIONES");
        for (HashMap.Entry<Integer, Integer> e : funciones.entrySet()) {
            Simbolo s = TS.get_Simbolo(e.getKey());
            System.out.println(" ref: "+e.getKey() + " - "+ s.get_Ambito() +" - linea: "+ e.getValue());
        }
    }

    public int buscar_Parametro(String id, String amb){
        //busca el parametro de la funcion que tenga al alcance
        //primero busca por nombre y luego verifica el ambito en el hashmap de funciones
        String parametro = "-";
        int clave;
        System.out.println("buscar_parametro "+id+" "+amb);
        for (HashMap.Entry<Integer, Integer> e : funciones.entrySet()) {
            Simbolo s = TS.get_Simbolo(e.getKey());
            System.out.println(s.get_Lex());
            if (s.get_Lex().equals(id)) {
                String ambitoSimbolo = s.get_Ambito();
                ambitoSimbolo = ":" + ambitoSimbolo.substring(ambitoSimbolo.indexOf(":")+1); 
                System.out.println("Ambito simbolo: " + ambitoSimbolo+ " Ambito parametro " + amb);
                if (ambitoSimbolo.equals(amb)) {
                    parametro = s.get_Parametro();
                }
            }
        }
        if (parametro != "-")
            clave = Integer.parseInt(parametro);
        else clave = -1;
        return clave;
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
                System.out.println("Referencia: " + i.getKey() + ", Terceto: (" + i.getValue().get_Operador() + " , " + j + " , "+ s +")" + " Tipo: " + i.getValue().get_Tipo() + " VA: " + i.getValue().get_VA());         
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
        else{
            TS.imprimirContenido();
            System.out.println(" "); 
            imprimirCodigoIntermedio(); 
            System.out.println(" ");             
            imprimirClases();
            System.out.println(" ");
            imprimirFunciones();
            transformar();
            }
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
                        System.out.println("WARNING: linea " + Linea.getLinea() + " El double positivo es menor al limite permitido. Tiene valor: "+num + ". El valor será reemplazado");
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
                System.out.println("WARNING: linea "+ Linea.getLinea() + " El double positivo es mayor al limite permitido. Tiene valor: "+num+ ". El valor será reemplazado");
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
                System.out.println("WARNING: linea " + Linea.getLinea() + " El entero largo positivo es mayor al limite permitido. Tiene valor: "+entero+ ". El valor será reemplazado");
                TS.eliminarConstante(Long.toString(entero), "LONG");
                String nuevo = "2147483647";
                TS.agregarConstante(nuevo, 258, "LONG");
                System.out.println("El numero fue actualizado en la TS a un valor permitido");
                setear_Uso("Constante", nuevo+ambito);
                factor.sval = Integer.toString(TS.buscarConstante(nuevo, "LONG"));
            }
            else
                {setear_Uso("Constante", numero+ambito);
                factor.sval = Integer.toString(TS.buscarConstante(numero, "LONG"));}
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
                        System.out.println("WARNING: linea "+ Linea.getLinea() + " El double negativo es mayor al limite permitido. Tiene valor: "+num+ ". El valor será reemplazado");
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
                System.out.println("WARNING: linea "+ Linea.getLinea() + " El double positivo es menor al limite permitido. Tiene valor: "+num+ ". El valor será reemplazado");
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
                System.out.println("WARNING: linea "+ Linea.getLinea() + " El entero largo negativo es menor al limite permitido. Tiene valor: "+entero+ ". El valor será reemplazado");
                TS.eliminarConstante(Long.toString(entero*(-1)), "LONG");
                String nuevo = "-2147483648";
                TS.agregarConstante(nuevo, 258, "LONG");//Lo agrego a la tabla de simbolos en negativo borrando el numero positivo
                setear_Uso("Constante negativa", nuevo+ambito);
                factor.sval = Integer.toString(TS.buscarConstante(nuevo, "LONG"));
            } else
            {
                //En caso de respetar el rango solo le agrega el menos en la tabla de simbolos
                TS.eliminarConstante(Long.toString(entero*(-1)), "LONG");
                String nuevo = Long.toString(entero);
                TS.agregarConstante(nuevo, 258, "LONG");
                setear_Uso("Constante negativa", nuevo+ambito);
                factor.sval = Integer.toString(TS.buscarConstante("-"+numero, "LONG"));
            }
        }
    }

    public void VerificarSalto(){
        if (lex.salto())
            System.out.println("ERROR: linea " + (Linea.getLinea()-1) + " Falto la coma al final de la sentencia");
        else
            System.out.println("ERROR: linea " + (Linea.getLinea()-1) + " Falto la coma al final de la sentencia");
    }

    public void agregar_ParametroTS(String lexema, String tipo, String a){
        int clave = TS.pertenece(lexema);
        Simbolo elemento = TS.get_Simbolo(clave);
        System.out.println("tipo: " + tipo);
        if ((clave != -1) && (elemento.get_Ambito() == "-")){
            elemento.set_Ambito(a);
            elemento.set_Tipo(tipo);
            elemento.set_Uso("Parametro formal");
        }
        else{
            Simbolo s = new Simbolo(257, lexema, tipo);
            s.set_Ambito(a);
            s.set_Uso("Parametro formal");
            TS.agregar_sin_chequear(s);
        }
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

    public void setear_Tipo(String var, String tipo) {
        int clave = TS.buscar_por_ambito(var);
        Simbolo s = TS.get_Simbolo(clave);
        s.set_Tipo(tipo);
    }

    public void guardar_Var(String id){
        variables.add(id);
    }

    public void completarTerceto(int pos, int aux){
        Terceto t = CodigoIntermedio.get(pos);
        String operando = "[" + Integer.toString(aux) + "]";
        t.set_Op(operando);
    }

    public String borrarParentesis(String palabra){
        StringBuilder builder = new StringBuilder(palabra);
        for (int i = 0; i < builder.length(); i++) {
            if (builder.charAt(i) == '[' || builder.charAt(i) == ']' )
                builder.deleteCharAt(i);
        }
        String punt1 = builder.toString();
        return punt1;
    }

    public void realizar_Conversion(String elemento1, String elemento2, String operador, ParserVal valorfinal){
        String tipo1 = "-", tipo2 = "-";
        if (elemento1.contains("[")){
            String ref1 = borrarParentesis(elemento1);
            tipo1 = CodigoIntermedio.get(Integer.parseInt(ref1)).get_Tipo();}
        else 
            tipo1 = TS.get_Simbolo(Integer.parseInt(elemento1)).get_Tipo();
        if (elemento2.contains("[")){
            String ref2 = borrarParentesis(elemento2); 
            tipo2 = CodigoIntermedio.get(Integer.parseInt(ref2)).get_Tipo();}
        else
            tipo2 = TS.get_Simbolo(Integer.parseInt(elemento2)).get_Tipo();
        System.out.println("Tipo1: "+tipo1+ " Tipo2: " +tipo2);
        String OperacionTipo = convertible.Convertir(tipo1, tipo2);
        if (OperacionTipo!="-"){
            String elemento = convertible.devolverElementoAConvertir(elemento1, tipo1, elemento2, tipo2);
            if (elemento == elemento1){
                String aux = '[' + Integer.toString(crear_terceto(OperacionTipo, elemento1, "-")) + ']';
                CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(convertible.devolverTipoAConvertir(OperacionTipo));
                valorfinal.sval = '['+ Integer.toString(crear_terceto(operador, aux, elemento2))+ ']';
            }
            else{
            String aux = '[' + Integer.toString(crear_terceto(OperacionTipo, elemento2, "-")) + ']';
            CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(convertible.devolverTipoAConvertir(OperacionTipo));
            valorfinal.sval = '['+ Integer.toString(crear_terceto(operador, elemento1, aux))+ ']';}}
        else
            {valorfinal.sval = '['+ Integer.toString(crear_terceto(operador, elemento1, elemento2))+ ']';
            OperacionTipo = tipo2;}
        CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(convertible.devolverTipoAConvertir(OperacionTipo));
    }

    public String convertirTipoAsignacion(String id, String expresion){
        String tipoId = TS.get_Simbolo(TS.pertenece(id)).get_Tipo();
        String tipoExpresion;
        String conversion = "-";
        if (expresion.contains("[")){
            String refTerceto = borrarParentesis(expresion);
            tipoExpresion = CodigoIntermedio.get(Integer.parseInt(refTerceto)).get_Tipo();
        }
        else
            tipoExpresion = TS.get_Simbolo(Integer.parseInt(expresion)).get_Tipo();
        if (tipoId == "USHORT")
                if (tipoExpresion == "USHORT")
                    System.out.println("Los tipos son compatibles. La asignacion puede realizarse sin conversiones");
                else
                    System.out.println("ERROR: linea " + Linea.getLinea() + " Tipos incompatibles para realizar la asignacion. Se pretende convertir " + tipoExpresion + " a USHORT");
        else if (tipoId == "LONG")
            if (tipoExpresion == "LONG")
                    System.out.println("Los tipos son compatibles. La asignacion puede realizarse sin conversiones");
            else
                if (tipoExpresion == "USHORT"){
                    System.out.println("Los tipos son compatibles. La asignacion puede realizarse, debe convetirse USHORT a LONG");
                    conversion = "UStoL";
                }
                else
                    System.out.println("ERROR: linea " + Linea.getLinea() + " Tipos incompatibles para realizar la asignacion. Se pretende convertir " + tipoExpresion + " a USHORT");
        else
            if (tipoExpresion == "LONG"){
                    System.out.println("Los tipos son compatibles. La asignacion puede realizarse, deve convertirse LONG a DOUBLE");
                    conversion = "LtoD";
            }
            else
                if (tipoExpresion == "USHORT"){
                    System.out.println("Los tipos son compatibles. La asignacion puede realizarse, debe convetirse USHORT a DOUBLE");
                    conversion = "UStoD";
                }
                else
                    System.out.println("Los tipos son compatibles. La asignacion puede realizarse sin conversiones");
        return conversion;
    }


public void verificarUso(String elemento){
        int clave = TS.buscar_por_ambito(elemento+ambito);
        boolean x = false;
        if (clave==-1)
            clave = TS.pertenece(elemento);
        String aux = ambito;
        if (clave != -1){
            Simbolo s = TS.get_Simbolo(clave);
            ambitos_Programa = aux.split(":");
            int cantidad = ambitos_Programa.length;
            while (cantidad > 1){
                if (!s.get_Ambito().equals(elemento+aux)){
                    if (!seUsoLadoDerecho(elemento)){
                         x = true;
                    }
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
        }
        if (x == true)
           System.out.println("La variable: " + elemento + " no aparece en el lado derecho del ámbito dónde se declaro");
    }   

    private boolean seUsoLadoDerecho(String variable) 
    {   
        for (HashMap.Entry<Integer, Terceto> i : CodigoIntermedio.entrySet()) {
                String j = i.getValue().get_Operador();
                String s = i.getValue().get_Op2();
                if (!s.contains("[") && s!="-") {
                    int k = Integer.parseInt(s);
                    s = TS.get_Simbolo(k).get_Lex();
                }
                if (j.equals("=") && s.equals(variable))
                    return true;
        }
        return false;
    }

    public void transformar(){
        CodigoAssembler = new Assembler(CodigoIntermedio, TS);
        CodigoAssembler.GenerarAssembler();
    }
        
//#line 1262 "Parser.java"
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
//#line 28 "gramatica.y"
{System.out.println("Programa completamente reconocido");}
break;
case 7:
//#line 42 "gramatica.y"
{ /*System.out.println("Bloque de Sentencias reconocido")*/
                                                }
break;
case 12:
//#line 54 "gramatica.y"
{VerificarSalto();}
break;
case 13:
//#line 57 "gramatica.y"
{String conv = convertirTipoAsignacion(val_peek(2).sval, val_peek(0).sval);
                                                    if (conv != "-"){
                                                        val_peek(0).sval = "["+ Integer.toString(crear_terceto(conv, val_peek(0).sval, "-")) +"]";
                                                        CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(convertible.devolverTipoAConvertir(conv));
                                                    }
                                                    if (val_peek(1).sval == "+="){
                                                        String aux = "[" + Integer.toString(crear_terceto("+", Integer.toString(TS.pertenece(val_peek(2).sval)), val_peek(0).sval)) + "]";
                                                        yyval.sval = '[' + Integer.toString(crear_terceto("=", Integer.toString(TS.pertenece(val_peek(2).sval)), aux)) + ']';}
                                                    else 
                                                        yyval.sval = '[' + Integer.toString(crear_terceto(val_peek(1).sval, Integer.toString(TS.pertenece(val_peek(2).sval)), val_peek(0).sval)) + ']';
                                                    CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(convertible.devolverTipoAConvertir(TS.get_Simbolo(TS.pertenece(val_peek(2).sval)).get_Tipo()));
                                                    if (!ver_ElementoDeclarado(val_peek(2).sval))
                                                        System.out.println("ERROR: linea "+ Linea.getLinea() + " - " + val_peek(2).sval + " no fue declarado");
                                                    else
                                                        verificarUso(val_peek(2).sval);
                                                    }
break;
case 14:
//#line 73 "gramatica.y"
{/*System.out.println("Se reconocio una asignacion a un atributo objeto en linea "+ Linea.getLinea());*/
                                                    if ((val_peek(2).sval != null) && (val_peek(0).sval != null))
                                                        yyval.sval = '[' + Integer.toString(crear_terceto("=", val_peek(2).sval, val_peek(0).sval))+']';}
break;
case 15:
//#line 76 "gramatica.y"
{/*System.out.println("Se reconocio una asignacion a un atributo objeto en linea "+ Linea.getLinea());*/
                                                    if (val_peek(2).sval != null) 
                                                        yyval.sval = '[' + Integer.toString(crear_terceto("=", val_peek(2).sval, val_peek(0).sval))+']';}
break;
case 16:
//#line 81 "gramatica.y"
{/*System.out.println("Se reconocio una asignacion en linea "+ Linea.getLinea());*/
                            yyval.sval = "=";}
break;
case 17:
//#line 83 "gramatica.y"
{/*System.out.println("Se reconocio una asignacion suma en linea "+ Linea.getLinea());*/
                            yyval.sval = "+=";}
break;
case 18:
//#line 85 "gramatica.y"
{System.out.println("ERROR: linea " + Linea.getLinea() + " No es valido el signo de asignacion");}
break;
case 19:
//#line 88 "gramatica.y"
{realizar_Conversion(val_peek(2).sval, val_peek(0).sval, val_peek(1).sval, yyval);}
break;
case 20:
//#line 89 "gramatica.y"
{yyval.sval = val_peek(0).sval;}
break;
case 21:
//#line 92 "gramatica.y"
{realizar_Conversion(val_peek(2).sval, val_peek(0).sval, val_peek(1).sval, yyval);}
break;
case 22:
//#line 93 "gramatica.y"
{yyval.sval = val_peek(0).sval;}
break;
case 23:
//#line 96 "gramatica.y"
{ yyval.sval = "*";}
break;
case 24:
//#line 97 "gramatica.y"
{ yyval.sval = "/";}
break;
case 25:
//#line 100 "gramatica.y"
{yyval.sval = Integer.toString(TS.buscar_por_ambito(val_peek(0).sval+ambito));
                setear_Uso("identificador", val_peek(0).sval+ambito);
                if (!ver_ElementoDeclarado(val_peek(0).sval))
                    System.out.println("ERROR: linea "+ Linea.getLinea() + " - " + val_peek(0).sval + " no fue declarado");
                /*TS.get_Simbolo(Integer.parseInt($1.sval)).set_Usada("Lado derecho");*/
                }
break;
case 26:
//#line 106 "gramatica.y"
{/*System.out.println("Se reconocio una constante en linea "+Linea.getLinea());*/
                chequearRangoPositivo(val_peek(0).sval, yyval);
                }
break;
case 27:
//#line 109 "gramatica.y"
{/*System.out.println("Se reconocio constante negativa en linea "+ Linea.getLinea());*/
                    chequearRangoNegativo(val_peek(0).sval, yyval);}
break;
case 28:
//#line 111 "gramatica.y"
{setear_Uso("ConstantePositiva", val_peek(0).sval+ambito);
                    yyval.sval = Integer.toString(TS.pertenece(val_peek(0).sval));}
break;
case 29:
//#line 115 "gramatica.y"
{ yyval.sval = "+";}
break;
case 30:
//#line 116 "gramatica.y"
{ yyval.sval = "-";}
break;
case 31:
//#line 120 "gramatica.y"
{volver_Ambito();
                                                    if (val_peek(1).sval != " ") {
                                                        setear_Uso("Clase", val_peek(1).sval); 
                                                        agregarClase(val_peek(1).sval, metodosTemp, metodosTempNoImp, atributosTemp);
                                                        metodosTemp = new ArrayList<Integer>();
                                                        atributosTemp = new ArrayList<Integer>();
                                                        metodosTempNoImp = new ArrayList<Integer>();
                                                        } else {eliminarElementos();}
                                                    }
break;
case 32:
//#line 129 "gramatica.y"
{/*System.out.println("Clase con herencia por composicion en linea "+Linea.getLinea()); */
                                                                        volver_Ambito();
                                                                        if (val_peek(5).sval != " ") 
                                                                            {   clavePadre = verificarExisteClasePadre(val_peek(5).sval, val_peek(2).sval);
                                                                                if (clavePadre != -1){
                                                                                    agregarClase(val_peek(5).sval, metodosTemp, metodosTempNoImp, atributosTemp);
                                                                                    metodosTemp = new ArrayList<Integer>();
                                                                                    atributosTemp = new ArrayList<Integer>();
                                                                                    metodosTempNoImp = new ArrayList<Integer>();
                                                                                    setear_Uso("Clase", val_peek(5).sval);
                                                                                    setear_Tipo(val_peek(5).sval, val_peek(2).sval+ambito);
                                                                                } else {
                                                                                    int claveAux = TS.pertenece(val_peek(2).sval);
                                                                                    if (claveAux != -1) 
                                                                                        TS.remove_Simbolo(claveAux);
                                                                                    }
                                                                            } else eliminarElementos();
                                                                        }
break;
case 33:
//#line 147 "gramatica.y"
{if (val_peek(0).sval != " ") {
                                    int clave = TS.buscar_por_ambito(val_peek(0).sval);
                                        if (! claseVacia(clave)) {
                                        setear_Uso("Clase", val_peek(0).sval);
                                        agregarClase(val_peek(0).sval, metodosTemp, metodosTempNoImp, atributosTemp);
                                        metodosTemp = new ArrayList<Integer>();
                                        atributosTemp = new ArrayList<Integer>();
                                        metodosTempNoImp = new ArrayList<Integer>();
                                        } else 
                                            System.out.println("ERROR: linea "+Linea.getLinea()+" se debe declarar la clase"+val_peek(0).sval);
                                    } 
                                    volver_Ambito();
                                }
break;
case 34:
//#line 163 "gramatica.y"
{   metodosTemp = new ArrayList<Integer>();
                                atributosTemp = new ArrayList<Integer>();
                                metodosTempNoImp = new ArrayList<Integer>(); 
                                if (setear_Ambito(val_peek(0).sval+ambito, val_peek(0).sval)) {
                                    yyval.sval = val_peek(0).sval+ambito;
                                    clavePadre = -1;
                                } else  
                                    yyval.sval = " ";
                                ambito += ":" + val_peek(0).sval;
                        }
break;
case 35:
//#line 175 "gramatica.y"
{String t = obtenerTipo(val_peek(1).sval);
                                        if (t != " ") {
                                            guardar_Tipo(t);
                                            setear_Tipo();
                                        } else variables.clear();
                                        }
break;
case 36:
//#line 183 "gramatica.y"
{  ver_ObjetoDeclarado(val_peek(3).sval);
                                            int clase = obtenerClase(val_peek(3).sval+ambito);
                                            if (clase != -1) {
                                                if (! claseVacia(clase)) {
                                                    int padre = -1;
                                                    String tipo = TS.get_Simbolo(clase).get_Tipo();
                                                    if ( tipo != " "); /*hereda de otra clase*/
                                                        padre = TS.buscar_por_ambito(tipo);
                                                    int ref = verificarExistencia(clase, val_peek(1).sval, "metodo"); /*obtengo la referencia al metodo (si existe)*/
                                                    if (ref == -1 && padre != -1)
                                                        ref = verificarExistencia(padre, val_peek(1).sval, "metodo");
                                                    if (ref != -1) {
                                                        /*int param = buscar_Parametro($3.sval, ambito);*/
                                                        String param = TS.get_Simbolo(ref).get_Parametro();
                                                        String terceto = "-";
                                                        if ((param == "-" && val_peek(0).sval=="-") || (param != "-" && !val_peek(0).sval.equals("-"))) /*si los parametros no coinciden avisa*/
                                                            {
                                                                String tipo_real = "-";
                                                                String tipo_formal = "-";
                                                                if (param != "-"){
                                                                    System.out.println(val_peek(0).sval);
                                                                    if (val_peek(0).sval.contains("["))
                                                                        tipo_real = CodigoIntermedio.get(Integer.parseInt(borrarParentesis(val_peek(0).sval))).get_Tipo();
                                                                    else
                                                                        tipo_real = TS.get_Simbolo(Integer.parseInt(val_peek(0).sval)).get_Tipo();
                                                                    System.out.println("Tipo parametro real: " + tipo_real+", Tipo formal: " + TS.get_Simbolo(Integer.parseInt(param)).get_Tipo());
                                                                    tipo_formal = TS.get_Simbolo(Integer.parseInt(param)).get_Tipo();
                                                                    if (tipo_formal.equals(tipo_real)){
                                                                        String auxiliar = Integer.toString(crear_terceto("=", param, val_peek(0).sval));
                                                                        System.out.println("No fue necesario hacer conversiones de tipos en los parámetros");
                                                                        yyval.sval = "[" + Integer.toString(crear_terceto ("CALLMetodoClase", Integer.toString(TS.pertenece(val_peek(1).sval)), val_peek(0).sval)) + "]";}
                                                                    else
                                                                    {
                                                                        if (tipo_formal == "USHORT" || (tipo_formal == "LONG" && tipo_real =="DOUBLE"))
                                                                            System.out.println("ERROR: linea " + Linea.getLinea() + " Los tipos de los parámetros son incompatibles");
                                                                        else{ 
                                                                            String conversion = convertible.Convertir(tipo_formal, tipo_real);
                                                                            String terceto1 = '['+ Integer.toString(crear_terceto(conversion, val_peek(0).sval, "-")) + ']';
                                                                            String auxiliar = Integer.toString(crear_terceto("=", param, terceto1));
                                                                            System.out.println("La conversión pudo realizarse y fue de " + tipo_real + " a " + tipo_formal );
                                                                            yyval.sval = "[" + Integer.toString(crear_terceto ("CALLMetodoClase", Integer.toString(TS.pertenece(val_peek(1).sval)), terceto1)) + "]";}
                                                                    }}
                                                                else
                                                                    yyval.sval = "[" + Integer.toString(crear_terceto ("CALLMetodoClase", Integer.toString(ref), "-")) + "]";
                                                            }
                                                        else
                                                            System.out.println("ERROR: linea "+ Linea.getLinea() + " Los parámetros no coinciden");
                                                    }else
                                                        System.out.println("ERROR: linea "+ Linea.getLinea()+ " - el metodo "+val_peek(1).sval+ " no se encuentra al alcance o no fue declarado");
                                                }else
                                                        System.out.println("ERROR: linea "+ Linea.getLinea()+ " - No se puede invocar al metodo \""+val_peek(1).sval+ "\" porque la clase \""+TS.get_Simbolo(clase).get_Ambito()+"\" no se encuentra implementada");
}
}
break;
case 37:
//#line 238 "gramatica.y"
{   int clase = obtenerClase(val_peek(2).sval+ambito);
                                if (clase != -1) {
                                    if (! claseVacia(clase)) {
                                        int padre = -1;
                                        String tipo = TS.get_Simbolo(clase).get_Tipo();
                                        if ( tipo != " "); /*hereda de otra clase*/
                                            padre = TS.buscar_por_ambito(tipo);
                                        int ref = verificarExistencia(clase, val_peek(0).sval, "atributo");
                                        if (ref == -1 && padre != -1) 
                                            ref = verificarExistencia(padre, val_peek(0).sval, "atributo");
                                        if (ref != -1) 
                                        { yyval.sval = '[' + Integer.toString(crear_terceto("atributo_objeto", Integer.toString(TS.pertenece(val_peek(2).sval)), Integer.toString(ref))) + ']';

                                        } else {System.out.println("ERROR: linea "+ Linea.getLinea()+ " el atributo \""+val_peek(0).sval+ "\" no se encuentra al alcance o no fue declarado");
                                                yyval.sval = null;}
                                        } else
                                                System.out.println("ERROR: linea "+ Linea.getLinea()+ " - No se puede acceder al atributo \""+val_peek(0).sval+ "\"  porque la clase \""+TS.get_Simbolo(clase).get_Ambito()+"\" no se encuentra implementada");
                                    }
                            }
break;
case 38:
//#line 259 "gramatica.y"
{dentroFuncion = false;}
break;
case 39:
//#line 260 "gramatica.y"
{ dentroFuncion = false;}
break;
case 40:
//#line 263 "gramatica.y"
{/*System.out.println("Se reconocio una declaracion de una funcion VOID en linea "+ Linea.getLinea());*/
                                                                String idFuncion = obtenerAmbito(val_peek(4).sval+ambito);
                                                                int clave = TS.buscar_por_ambito(idFuncion);
                                                                metodosTemp.add(clave);
                                                                TS.get_Simbolo(clave).set_Parametro(val_peek(3).sval);
                                                                funciones.put(clave, Linea.getLinea());
                                                                volver_Ambito();
                                                                }
break;
case 41:
//#line 273 "gramatica.y"
{/*System.out.println("Se reconocio una declaracion de una funcion VOID vacia en linea "+ Linea.getLinea());*/
                                            String idFuncion = obtenerAmbito(val_peek(1).sval+ambito);
                                            int clave = TS.buscar_por_ambito(idFuncion);
                                            metodosTempNoImp.add(clave);
                                            TS.get_Simbolo(clave).set_Parametro(val_peek(0).sval); /*DUDAAA*/
                                            funciones.put(clave, Linea.getLinea());
                                            volver_Ambito();
                                            }
break;
case 42:
//#line 283 "gramatica.y"
{yyval.sval = val_peek(0).sval;
                    setear_Ambito(val_peek(0).sval+ambito, val_peek(0).sval);
                    setear_Uso("Metodo", val_peek(0).sval+ambito);
                    setear_Tipo(val_peek(0).sval+ambito, "VOID");
                    ambito += ":" + val_peek(0).sval;
                    dentroFuncion = true;
}
break;
case 43:
//#line 292 "gramatica.y"
{   int idClase = TS.buscar_por_ambito(val_peek(5).sval+ambito);
                                                                        if (!ver_ElementoDeclarado(val_peek(5).sval)) /*verificar que la clase exista*/
                                                                            System.out.println("ERROR: linea "+ Linea.getLinea() + " - " + val_peek(5).sval + " no fue declarado");
                                                                        if (verificarExistencia(idClase, val_peek(2).sval, "metodoNoImpl") != -1) 
                                                                            agregarMetodoImplementado(val_peek(5).sval+ambito, val_peek(2).sval+ambito+":"+val_peek(5).sval);
                                                                        int clave = TS.buscar_por_ambito(val_peek(2).sval+ambito);
                                                                        metodosClases.get(idClase).remove(Integer.valueOf(clave));
                                                                        funciones.remove(clave);            
                                                                        TS.remove_Simbolo(clave);
                                                                        
                                                                    }
break;
case 46:
//#line 307 "gramatica.y"
{/*System.out.println("Se reconocio una clausula de seleccion IF en linea "+ Linea.getLinea());*/
                    }
break;
case 47:
//#line 309 "gramatica.y"
{/*System.out.println("Se reconocio una impresion por pantalla en linea "+ Linea.getLinea());*/
                    }
break;
case 48:
//#line 311 "gramatica.y"
{/*System.out.println("Se reconocio la invocacion de un metodo de un objeto en linea " + Linea.getLinea());*/
                    }
break;
case 49:
//#line 313 "gramatica.y"
{/*System.out.println("Se reconocio sentencia IMPL FOR en linea "+ Linea.getLinea());*/
                    }
break;
case 50:
//#line 315 "gramatica.y"
{/*System.out.println("Se reconocio sentencia de control DO UNTIL en linea "+ Linea.getLinea());*/
                    }
break;
case 51:
//#line 317 "gramatica.y"
{/*System.out.println("Se reconocio sentencia de retorno RETURN en linea "+ Linea.getLinea());*/
                            int aux = crear_terceto("RETURN", "-", "-");}
break;
case 52:
//#line 321 "gramatica.y"
{/*System.out.println("Se reconocio una declaracion simple en linea "+ Linea.getLinea());*/
}
break;
case 54:
//#line 324 "gramatica.y"
{/*System.out.println("Se reconocio una declaracion de un objeto de una clase en linea "+ Linea.getLinea());*/
                    }
break;
case 55:
//#line 326 "gramatica.y"
{/*System.out.println("Se reconocio una clase en linea "+ Linea.getLinea());*/
                    }
break;
case 56:
//#line 330 "gramatica.y"
{yyval.sval = ">";}
break;
case 57:
//#line 331 "gramatica.y"
{yyval.sval = "<";}
break;
case 58:
//#line 332 "gramatica.y"
{yyval.sval = ">=";}
break;
case 59:
//#line 333 "gramatica.y"
{yyval.sval = "<=";}
break;
case 60:
//#line 334 "gramatica.y"
{yyval.sval = "!!";}
break;
case 61:
//#line 335 "gramatica.y"
{yyval.sval = "==";}
break;
case 62:
//#line 336 "gramatica.y"
{System.out.println("ERROR: linea " + Linea.getLinea() +" Comparador no valido");}
break;
case 63:
//#line 339 "gramatica.y"
{/*System.out.println("Se reconoció una condicion  en linea "+ Linea.getLinea());*/
                                                    realizar_Conversion(val_peek(3).sval, val_peek(1).sval, val_peek(2).sval, yyval);
                                                    int aux = crear_terceto("BF", yyval.sval, "-");
                                                    pila.push(aux);}
break;
case 64:
//#line 343 "gramatica.y"
{System.out.println("ERROR: linea" + Linea.getLinea() + " Falta el parentesis que cierra");
                                                        yyval.sval = '[' + Integer.toString(crear_terceto(val_peek(2).sval, val_peek(3).sval, val_peek(1).sval)) + ']';
                                                        int aux = crear_terceto("BF", yyval.sval, "-");
                                                        pila.push(aux); }
break;
case 65:
//#line 347 "gramatica.y"
{System.out.println("ERROR: linea "+ Linea.getLinea() + " Falta el parentesis que abre");
                                                      yyval.sval = '[' + Integer.toString(crear_terceto(val_peek(3).sval, val_peek(4).sval, val_peek(2).sval)) + ']';
                                                      int aux = crear_terceto("BF", yyval.sval, "-");
                                                      pila.push(aux);}
break;
case 66:
//#line 353 "gramatica.y"
{int primero = pila.pop();
                                                                    completarTerceto(primero, puntero_Terceto);
                                                                    int aux = crear_terceto("Label"+puntero_Terceto, "-","-");}
break;
case 67:
//#line 356 "gramatica.y"
{int primero = pila.pop();
                                                                    completarTerceto(primero, puntero_Terceto);
                                                                    int aux = crear_terceto("Label"+puntero_Terceto, "-","-");}
break;
case 68:
//#line 359 "gramatica.y"
{int primero = pila.pop();
                                                    completarTerceto(primero, puntero_Terceto);
                                                    int aux = crear_terceto("Label"+puntero_Terceto, "-","-");}
break;
case 69:
//#line 362 "gramatica.y"
{int primero = pila.pop();
                                                    completarTerceto(primero, puntero_Terceto);
                                                    int aux = crear_terceto("Label"+puntero_Terceto, "-","-");}
break;
case 70:
//#line 365 "gramatica.y"
{System.out.println("Falta el END_IF");
                                                    int primero = pila.pop();
                                                    completarTerceto(primero, puntero_Terceto);
                                                    int aux = crear_terceto("Label"+puntero_Terceto, "-","-");}
break;
case 71:
//#line 369 "gramatica.y"
{System.out.println("Falta el END_IF");
                                                    int primero = pila.pop();
                                                    completarTerceto(primero, puntero_Terceto);
                                                    int aux = crear_terceto("Label"+puntero_Terceto, "-","-");}
break;
case 72:
//#line 373 "gramatica.y"
{System.out.println("Falta el END_IF");
                                                                    int primero = pila.pop();
                                                                    completarTerceto(primero, puntero_Terceto);
                                                                    int aux = crear_terceto("Label"+puntero_Terceto, "-","-");}
break;
case 73:
//#line 377 "gramatica.y"
{System.out.println("Falta el END_IF");
                                                                    int primero = pila.pop();
                                                                    completarTerceto(primero, puntero_Terceto);
                                                                    int aux = crear_terceto("Label"+puntero_Terceto, "-","-");}
break;
case 74:
//#line 381 "gramatica.y"
{System.out.println("ERROR: linea " + Linea.getLinea() + " Falto la condicion del IF");}
break;
case 75:
//#line 384 "gramatica.y"
{int primero = pila.pop();
                                int aux = crear_terceto("BI", "-", "-");
                                completarTerceto(primero, aux+1);
                                int aux2 = crear_terceto("Label"+Integer.toString(aux+1), "-", "-");
                                pila.push(aux);}
break;
case 76:
//#line 391 "gramatica.y"
{      int primero = pila.pop();
                                int aux = crear_terceto("BI", "-", "-");
                                completarTerceto(primero, aux+1);
                                int aux2 = crear_terceto("Label"+Integer.toString(aux+1), "-", "-");
                                pila.push(aux);}
break;
case 79:
//#line 402 "gramatica.y"
{int primero = pila.pop();
                                                                                completarTerceto(primero, val_peek(3).ival);}
break;
case 80:
//#line 404 "gramatica.y"
{int primero = pila.pop();
                                                                completarTerceto(primero, val_peek(4).ival);}
break;
case 81:
//#line 406 "gramatica.y"
{System.out.println("ERROR: linea " + Linea.getLinea() + " Falta la condicion de la sentencia de control");}
break;
case 82:
//#line 409 "gramatica.y"
{yyval.ival = puntero_Terceto;
                crear_terceto("Label"+puntero_Terceto, "-","-");}
break;
case 83:
//#line 413 "gramatica.y"
{setear_Tipo();}
break;
case 84:
//#line 416 "gramatica.y"
{  boolean declarado = setear_Ambito(val_peek(0).sval+ambito, val_peek(0).sval); 
                                            if (declarado) {
                                                setear_Uso("Variable", val_peek(0).sval+ambito); 
                                                guardar_Var(val_peek(0).sval+ambito);
                                                if (! dentroFuncion) 
                                                    atributosTemp.add(TS.buscar_por_ambito(val_peek(0).sval+ambito));
                                            }                                                
                                        }
break;
case 85:
//#line 424 "gramatica.y"
{  boolean declarado = setear_Ambito(val_peek(0).sval+ambito, val_peek(0).sval); 
                        if (declarado) {
                            setear_Uso("Variable", val_peek(0).sval+ambito); 
                            guardar_Var(val_peek(0).sval+ambito);
                            if (! dentroFuncion) 
                                atributosTemp.add(TS.buscar_por_ambito(val_peek(0).sval+ambito));
                        }
                }
break;
case 86:
//#line 434 "gramatica.y"
{
                                    if (!ver_ElementoDeclarado(val_peek(1).sval))
                                        System.out.println("ERROR: linea "+ Linea.getLinea() + " - " + val_peek(1).sval + " no fue declarado");
                                    int aux = buscar_Parametro(val_peek(1).sval, ambito);
                                    String tipo_real;
                                    if ((aux == -1 && val_peek(0).sval=="-") || (aux != -1 && val_peek(0).sval != "-")) /*si la cantidad de parametros no coinciden avisa*/
                                        {if (aux != -1){
                                            if (val_peek(0).sval.contains("["))
                                                tipo_real = CodigoIntermedio.get(Integer.parseInt(borrarParentesis(val_peek(0).sval))).get_Tipo();
                                            else
                                                tipo_real = TS.get_Simbolo(TS.buscar_por_ambito(TS.get_Simbolo(Integer.parseInt(val_peek(0).sval)).get_Lex()+ambito)).get_Tipo();
                                            System.out.println("Tipo parametro real: " + tipo_real+", Tipo formal: " + TS.get_Simbolo(aux).get_Tipo());
                                            String tipo_formal = TS.get_Simbolo(aux).get_Tipo();
                                            if (tipo_formal.equals(tipo_real)){
                                                String auxiliar = Integer.toString(crear_terceto("=", Integer.toString(aux), val_peek(0).sval));
                                                System.out.println("No fue necesario hacer conversiones de tipos en los parámetros");
                                                yyval.sval = "[" + Integer.toString(crear_terceto ("CALL", Integer.toString(TS.pertenece(val_peek(1).sval)), val_peek(0).sval)) + "]";}
                                            else
                                            {
                                                if (tipo_formal == "USHORT" || (tipo_formal == "LONG" && tipo_real =="DOUBLE"))
                                                    System.out.println("ERROR: linea " + Linea.getLinea() + " Los tipos de los parámetros son incompatibles");
                                                else{ 
                                                    String conversion = convertible.Convertir(tipo_formal, tipo_real);
                                                    String terceto = '['+ Integer.toString(crear_terceto(conversion, val_peek(0).sval, "-")) + ']';
                                                    String auxiliar = Integer.toString(crear_terceto("=", Integer.toString(aux), terceto));
                                                    System.out.println("La conversión pudo realizarse y fue de " + tipo_real + " a " + tipo_formal );
                                                    yyval.sval = "[" + Integer.toString(crear_terceto ("CALL", Integer.toString(TS.pertenece(val_peek(1).sval)), terceto)) + "]";}
                                            }}
                                        else
                                            yyval.sval = "[" + Integer.toString(crear_terceto ("CALL", Integer.toString(TS.pertenece(val_peek(1).sval)), val_peek(0).sval)) + "]";}
                                    else
                                        System.out.println("ERROR: linea " + Linea.getLinea() + " Los parámetros no coinciden");
                                    if ((aux != -1 && val_peek(0).sval == "-") || (aux == -1 && val_peek(0).sval != "-"))
                                         System.out.println("ERROR: linea " + Linea.getLinea() + " La cantidad de parámetros reales con los formales no coinciden");
                                    }
break;
case 87:
//#line 471 "gramatica.y"
{yyval.sval = val_peek(1).sval;}
break;
case 88:
//#line 472 "gramatica.y"
{yyval.sval = "-";}
break;
case 89:
//#line 473 "gramatica.y"
{System.out.println("ERROR: linea "+ Linea.getLinea()+ " Falta el parentesis que cierra");}
break;
case 90:
//#line 474 "gramatica.y"
{System.out.println("ERROR: linea "+ Linea.getLinea() + " Falta el parentesis que abre");}
break;
case 91:
//#line 475 "gramatica.y"
{System.out.println("ERROR: linea "+ Linea.getLinea()+ " Falta el parentesis que cierra");}
break;
case 92:
//#line 476 "gramatica.y"
{System.out.println("ERROR: linea "+ Linea.getLinea() + " Falta el parentesis que abre");}
break;
case 93:
//#line 479 "gramatica.y"
{ agregar_ParametroTS(val_peek(1).sval, val_peek(2).sval, val_peek(1).sval+ambito);
                                    yyval.sval = Integer.toString(TS.buscar_por_ambito(val_peek(1).sval+ambito));}
break;
case 94:
//#line 481 "gramatica.y"
{yyval.sval = "-";}
break;
case 95:
//#line 482 "gramatica.y"
{System.out.println("ERROR: linea "+ Linea.getLinea()+ " Falta el parentesis que cierra"); setear_Uso("Parametro formal", val_peek(1).sval);}
break;
case 96:
//#line 483 "gramatica.y"
{System.out.println("ERROR: linea "+ Linea.getLinea()+ " Falta el parentesis que abre"); setear_Uso("Parametro formal", val_peek(2).sval);}
break;
case 97:
//#line 484 "gramatica.y"
{System.out.println("ERROR: linea "+ Linea.getLinea() + " Falta el parentesis que cierra.");}
break;
case 98:
//#line 485 "gramatica.y"
{System.out.println("ERROR: linea "+ Linea.getLinea() + " Falta el parentesis que abre");}
break;
case 99:
//#line 488 "gramatica.y"
{guardar_Tipo("DOUBLE");
                yyval.sval = "DOUBLE";}
break;
case 100:
//#line 490 "gramatica.y"
{guardar_Tipo("USHORT");
                yyval.sval = "USHORT";}
break;
case 101:
//#line 492 "gramatica.y"
{guardar_Tipo("LONG");
                yyval.sval = "LONG";}
break;
case 102:
//#line 494 "gramatica.y"
{System.out.println("Error: linea " + Linea.getLinea() +  " No es un tipo definido");}
break;
case 103:
//#line 497 "gramatica.y"
{setear_Uso("Cadena", val_peek(0).sval);
                    int aux = crear_terceto("PRINT", Integer.toString(TS.pertenece(val_peek(0).sval)), "-");}
break;
//#line 2051 "Parser.java"
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
