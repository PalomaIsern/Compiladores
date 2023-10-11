%{
import compiladores.Lexico;
import compiladores.TablaSimbolos;
import compiladores.Linea;
import compiladores.Token;
import java.io.IOException;

%}

%token ID CTE CTEPOS IF END_IF ELSE PRINT CLASS VOID LONG USHORT DOUBLE DO UNTIL IMPL FOR CADENA RETURN '<=' '>=' '!!' '+=' '==' '>' '<'
%start programa

%%

programa : bloque_de_Sentencias {System.out.println("Programa completamente reconocido");}
;

conjuntoSentencias: conjuntoSentencias sentencia
                  | sentencia
;

conjuntoSentenciasEjecutables: conjuntoSentenciasEjecutables sentenciaEjecutable
                             | sentenciaEjecutable
;

cuerpo_funcion: conjuntoSentencias
;

bloque_de_Sentencias : '{' conjuntoSentencias '}' {System.out.println("Bloque de Sentencias reconocido");}
;

bloque_de_SentenciasEjecutables : '{' conjuntoSentenciasEjecutables '}'
;
        
sentencia  : sentenciaDeclarativa fin_sentencia
           | sentenciaEjecutable fin_sentencia
;

fin_sentencia : ','
              | error {System.out.println("Falto la coma al final de la sentencia");}
;

asignacion : ID simboloAsignacion expresion {System.out.println("Se reconocio una asignacion");}
            | atributo_objeto '=' atributo_objeto {System.out.println("Se reconocio una asignacion a un atributo objeto");}
            | atributo_objeto '=' factor {System.out.println("Se reconocio una asignacion a un atributo objeto");}
;

simboloAsignacion : '='
                  | '+='
                  | error {System.out.println("No es valido el signo de asignacion");}
;

expresion : expresion operadorMasMenos termino
          | termino
;

termino : termino simboloTermino factor
        | factor
;

simboloTermino : '*'
               | '/'
;

factor : ID
       | CTE
       | '-' CTE {System.out.println("Se reconocio constante negativa en linea "+ Linea.getLinea());}
       | CTEPOS
;

operadorMasMenos : '+'
                 | '-'
;


declaracionClase : CLASS ID bloque_de_Sentencias
                 | CLASS ID '{' conjuntoSentencias ID ',' '}' {System.out.println("Clase con herencia por composicion");}
                 | CLASS ID
;

declaracionObjeto : ID lista_Variables
;

declaracionFuncion: funcion_VOID
                  | funcion_VOID_vacia
;

funcion_VOID: VOID ID parametro_formal '{' cuerpo_funcion '}' {System.out.println("Funcion VOID");}
;

funcion_VOID_vacia: VOID ID parametro_formal {System.out.println("Funcion VOID vacia");}
;

clausula_IMPL : IMPL FOR ID ':' '{' funcion_VOID '}'
;

sentenciaEjecutable : asignacion
                    | invocacionFuncion {System.out.println("Se reconocio una invocacion de una funcion en linea "+ Linea.getLinea());}
                    | clausula_seleccion {System.out.println("Se reconocio una clausula de seleccion IF en linea "+ Linea.getLinea());}
                    | print  {System.out.println("Se reconocio una impresion por pantalla en linea "+ Linea.getLinea());}
                    | metodo_objeto {System.out.println("Se reconocio la invocacion de un metodo de un objeto en linea " + Linea.getLinea());}
                    | clausula_IMPL {System.out.println("Se reconocio sentencia IMPL FOR en linea "+ Linea.getLinea());}
                    | sentencia_de_Control {System.out.println("Se reconocio sentencia de control DO UNTIL en linea "+ Linea.getLinea());}
                    | RETURN {System.out.println("Se reconocio sentencia de retorno RETURN en linea "+ Linea.getLinea());}
;

sentenciaDeclarativa: declaracion {System.out.println("Se reconocio una declaracion simple en linea "+ Linea.getLinea());}
                    | declaracionFuncion {System.out.println("Se reconocio una funcion en linea "+ Linea.getLinea());}
                    | declaracionObjeto {System.out.println("Se reconocio una declaracion de un objeto de una clase en linea "+ Linea.getLinea());}
                    | declaracionClase {System.out.println("Se reconocio una clase en linea "+ Linea.getLinea());}
;

metodo_objeto : ID '.' invocacionFuncion
;

atributo_objeto : ID '.' ID
;

comparador : '>'
           | '<'
           | '>='
           | '<='
           | '!!'
           | '=='
           | error {System.out.println("Error: El caracter no se reconoce como comparador  en linea "+ Linea.getLinea());}
;

condicion : '(' expresion comparador expresion ')' {System.out.println("Se reconoció una condicion  en linea "+ Linea.getLinea());}
          | '(' expresion comparador expresion error  {System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());}
          |  expresion comparador expresion ')' error {System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());}
;

clausula_seleccion : IF condicion bloque_de_Sentencias ELSE bloque_de_Sentencias END_IF
                   | IF condicion bloque_de_Sentencias END_IF
                   | IF condicion bloque_de_Sentencias error {System.out.println("Falta el END_IF");}
                   | IF condicion bloque_de_Sentencias ELSE bloque_de_Sentencias error {System.out.println("Falta el END_IF");}
                   | IF condicion sentencia ELSE bloque_de_Sentencias END_IF
                   | IF condicion bloque_de_Sentencias ELSE sentencia END_IF
                   | IF condicion sentencia ELSE sentencia END_IF
                   | IF bloque_de_Sentencias error {System.out.println("Falto la condicion del IF");}
                   | IF condicion sentencia END_IF
;

sentencia_de_Control : DO bloque_de_SentenciasEjecutables UNTIL condicion
                     | DO sentenciaEjecutable UNTIL condicion
                     | DO sentenciaEjecutable UNTIL error {System.out.println("Falta la condicion de la sentencia de control");}
;

declaracion: tipo lista_Variables
;

lista_Variables : lista_Variables ';' ID
                | ID
;

invocacionFuncion : ID parametro_real
;

parametro_real  : '(' expresion ')'
                | '(' ')'
                | '(' expresion error {System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());}
                | expresion ')' error {System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());}
                | '(' error {System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());}
                | ')' error {System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());}
;

parametro_formal: '(' tipo ID ')'
                | '(' ')'
                | '(' tipo ID error {System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());}
                | tipo ID ')' error {System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());}
                | '(' error {System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());}
                | ')' error {System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());}
;

tipo : DOUBLE
     | USHORT
     | LONG
     | error {System.out.println("Error: No es un tipo definido en linea "+ Linea.getLinea());}
;

print : PRINT CADENA
;

%%

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
    

    
