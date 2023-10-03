%{

package compiladores;
import compiladores.Lexico;
import compiladores.TablaSimbolos;


%}

%token ID CTE IF END_IF ELSE PRINT CLASS VOID LONG USHORT DOUBLE DO UNTIL IMPL FOR CADENA <= >= == += !!
%start program

%%

program : { sentencia }
;
sentencia  : sentenciaDeclarativa
            | sentenciaDeclarativa sentenciaEjecutable
            | sentenciaEjecutable
            
;
expresion : expresion '+' termino
          | expresion '-' termino
          | expresion '*' termino
          | expresion '/' termino
          | termino
;
termino : numero

operadores:

letras:

digito:

numero: digito
      | entero
      | double

declaracion:  <tipo> <lista_Variables>
           |  <tipo> <lista_Varibales> = <valor>

sentenciaEjecutable:
funcion:

parametro:

cuerpo:

print:


%%

