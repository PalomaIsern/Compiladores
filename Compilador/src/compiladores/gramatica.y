%{

package compiladores;
import compiladores.Lexico;
import compiladores.TablaSimbolos;
import java.io.IOException;

%}

%token ID CTE IF END_IF ELSE PRINT CLASS VOID LONG USHORT DOUBLE DO UNTIL IMPL FOR CADENA RETURN 
%start program

%%

program : { sentencia }
;
        
sentencia  : sentenciaDeclarativa ','
           | sentenciaDeclarativa sentenciaEjecutable
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

letras:
;

digito:
;

numero: digito
      | entero
      | double
;


declaracionClase : CLASS ID '{' conjuntoSentenciasDeclarativas declaracionFuncion '}'
                 | CLASS ID '{' conjuntoSentenciasDeclarativas declaracionFuncion ID ',' '}'  
                 | CLASS ID '{' declaracionFuncion '}'
                 | CLASS ID 
;

declaracionObjeto : ID lista_Variables

declaracionFuncion: funcion_VOID
;

funcion_VOID: VOID ID '(' parametro_formal ')' '{' cuerpo_funcion RETURN'}'
            | VOID ID '(' ')' '{' cuerpo_funcion RETURN'}'
            | VOID ID '(' ')' 
;

cuerpo_funcion: conjuntoSentenciasDeclarativas 
              | conjuntoSentenciasDeclarativas conjuntoSentenciasEjecutables
              | conjuntoSentenciasEjecutables
;

clausula_IMPL : IMPL FOR ID ':' '{' declaracionFuncion '}'

conjuntoSentenciasDeclarativas : conjuntoSentenciasDeclarativas declaracion
                               | sentenciaDeclarativa
;

conjuntoSentenciasEjecutables : conjuntoSentenciasEjecutables sentenciaEjecutable
                              | sentenciaEjecutable
;

bloque_de_Sentencias_ejecutables: '{' conjuntoSentenciasEjecutables '}'
;

bloque_de_Sentencias_declarativas: '{' conjuntoSentenciasDeclarativas '}'
;

sentenciaEjecutable : asignacion
                    | invocacionFuncion
                    | clausula_seleccion
                    | metodo_objeto
                    | atributo_objeto
;

sentenciaDeclarativa: declaracion
                    | declaracionFuncion
                    | declaracionClase
                    | declaracionObjeto
;

metodo_objeto : ID '.' invocacionFuncion
;

atributo_objeto : ID '.' ID '=' 
;

clausula_seleccion : IF '(' condicion ')' bloque_de_Sentencias_ejecutables ELSE bloque_de_Sentencias_ejecutables END_IF
                   | IF '(' condicion ')' bloque_de_Sentencias_ejecutables 
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

sentencia_de_Control : DO bloque_de_Sentencias_ejecutables UNTIL '(' condicion ')'
;

declaracion:  tipo lista_Variables
;

lista_Variables : lista_Variables ';' ID
                | ID
;

invocacionFuncion : ID '(' parametro_real ')'
                  | ID '(' ')'
;

parametro_real  : expresion
;



funcion:
;

parametro_formal: tipo ID
;

tipo : DOUBLE
     | USHORT
     | LONG
;


print : PRINT CADENA
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
    
