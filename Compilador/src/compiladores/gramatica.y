%{

package compiladores;
import compiladores.Lexico;
import compiladores.TablaSimbolos;
import java.io.IOException;

%}

%token ID CTE IF END_IF ELSE PRINT CLASS VOID LONG USHORT DOUBLE DO UNTIL IMPL FOR CADENA RETURN 
%start program

%%

program : '{' conjuntoSentencias '}'
;

conjuntoSentencias: conjuntoSentencias sentencia
                  | sentencia
;

bloque_de_Sentencias: '{' conjuntoSentencias '}'
;
        
sentencia  : sentenciaDeclarativa ','
           | sentenciaEjecutable ','
;

asignacion : ID simboloAsignacion expresion
            | atributo_objeto '=' atributo_objeto
            | atributo_objeto '=' factor
;

simboloAsignacion : '='
                  | '+='
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
;

operadorMasMenos : '+'
                 | '-'
;

declaracionClase : CLASS ID '{' conjuntoSentencias '}' {System.out.print("Se reconoció una clase")}
                 | CLASS ID '{' conjuntoSentencias ID ',' '}' {System.out.print("Se reconoció una clase")}
                 | CLASS ID {System.out.print("Se reconoció una clase")}
;

declaracionObjeto : ID lista_Variables

declaracionFuncion: funcion_VOID
;

funcion_VOID: VOID ID '(' parametro_formal ')' '{' cuerpo_funcion '}' {System.out.print("Se reconoció una funcion void")}
            | VOID ID '(' ')' '{' cuerpo_funcion '}' {System.out.print("Se reconoció una funcion void")}
            | VOID ID '(' ')' {System.out.print("Se reconoció una funcion void")} 
;

cuerpo_funcion: cuerpo_funcion conjuntoSentencias
              | sentencia
;

clausula_IMPL : IMPL FOR ID ':' '{' declaracionFuncion '}' {System.out.print("Se reconocio sentencia IMPL FOR")}

sentenciaEjecutable : asignacion
                    | invocacionFuncion
                    | clausula_seleccion
                    | print
                    | metodo_objeto
                    | atributo_objeto
                    | RETURN
;

sentenciaDeclarativa: declaracion
                    | declaracionFuncion
                    | declaracionClase
                    | declaracionObjeto
;

metodo_objeto : ID '.' invocacionFuncion
;

atributo_objeto : ID '.' ID '=' expresion
                : ID '.' ID '=' ID '.' ID
;

clausula_seleccion : IF '(' condicion ')' bloque_de_Sentencias ELSE bloque_de_Sentencias END_IF {System.out.print("Se reconoció un IF")}
                   | IF '(' condicion ')' bloque_de_Sentencias END_IF {System.out.print("Se reconoció un IF")}
                   | IF '(' condicion ')' bloque_de_Sentencias {System.out.print("Falta el END_IF")}
                   | IF '(' condicion ')' bloque_de_Sentencias ELSE bloque_de_Sentencias {System.out.print("Falta el END_IF")}
;

condicion : expresion comparador expresion
;

comparador : '>'
           | '<'
           | '>='
           | '<='
           | '!!'
           | '=='
;

sentencia_de_Control : DO bloque_de_Sentencias UNTIL '(' condicion ')' {System.out.print("Se reconoció sentencia DO UNTIL)}
;

declaracion:  tipo lista_Variables
;

lista_Variables : lista_Variables ';' ID
                | ID
;

invocacionFuncion : ID '(' parametro_real ')' {System.out.print("Se reconoció una invocación de una function")}
                  | ID '(' ')' {System.out.print("Se reconoció una invocación de una function")}
;

parametro_real  : expresion
;

parametro_formal: tipo ID
;

tipo : DOUBLE
     | USHORT
     | LONG
;

print : PRINT CADENA {System.out.print("Se reconoció una impresion por pantalla")}
;


%%

    Lexico lex;

    private int yylex() {
        Token token = new Token();
        try {
            token = lexico.getToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
        . . .
    }
    
