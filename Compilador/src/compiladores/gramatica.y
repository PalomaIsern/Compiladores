%{

package compiladores;
import compiladores.Lexico;
import compiladores.TablaSimbolos;
import java.io.IOException;

%}

%token ID CTE IF END_IF ELSE PRINT CLASS VOID LONG USHORT DOUBLE DO UNTIL IMPL FOR CADENA <= >= == += !!
%start program

%%

program : { sentencia }
;
        
sentencia  : sentenciaDeclarativa ','
           | sentenciaDeclarativa sentenciaEjecutable
           | sentenciaEjecutable ','
;

asignacion : ID simboloAsignacion expresion
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

sentenciaDeclarativa: declaracion
                    | declaracionFuncion
                    | declaracionClase
;

declaracionClase : CLASS ID '{' conjuntoSentenciasDeclarativas declaracionFuncion '}'
;

declaracionFuncion: funcion_VOID
;

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

funcion_VOID: VOID ID '(' parametro_formal ')' '{' cuerpo_funcion '}'
            | VOID ID '(' ')' '{' cuerpo_funcion '}'
;

funcion:
;

parametro_formal: tipo ID
;

tipo : DOUBLE
     | USHORT
     | LONG
;

cuerpo_funcion: conjuntoSentenciasDeclarativas
              | conjuntoSentenciasDeclarativas conjuntoSentenciasEjecutables
              | conjuntoSentenciasEjecutables
;

print : PRINT CADENA
;


%%

