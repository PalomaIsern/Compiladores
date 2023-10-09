%{
import compiladores.Lexico;
import compiladores.TablaSimbolos;
import compiladores.Main;
import compiladores.Token;
import java.io.IOException;

%}

%token ID CTE CTEPOS IF END_IF ELSE PRINT CLASS VOID LONG USHORT DOUBLE DO UNTIL IMPL FOR CADENA RETURN
%start program

%%

program : bloque_de_Sentencias
;

conjuntoSentencias: conjuntoSentencias sentencia
                  | sentencia
;

cuerpo_funcion: conjuntoSentencias
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
       | '-' CTE {System.out.print("Se reconocio constante negativa en linea "+ Main.getLinea());}
       | CTEPOS
;

operadorMasMenos : '+'
                 | '-'
;

declaracionClase : CLASS ID bloque_de_Sentencias {System.out.print("Se reconocio una clase");}
                 | CLASS ID '{' conjuntoSentencias ID ',' '}' {System.out.print("Se reconocio una clase con herencia por composicion");}
                 | CLASS ID {System.out.print("Se reconocio una clase");}
;

declaracionObjeto : ID lista_Variables
;

declaracionFuncion: funcion_VOID
                  | funcion_VOID_vacia
;

funcion_VOID: VOID ID parametro_formal '{' cuerpo_funcion '}' {System.out.print("Se reconocio una funcion void");}
;

funcion_VOID_vacia: VOID ID parametro_formal {System.out.print("Se reconocio una funcion void vacia");}
;

clausula_IMPL : IMPL FOR ID ':' '{' funcion_VOID '}' {System.out.print("Se reconocio sentencia IMPL FOR");}
;

sentenciaEjecutable : asignacion
                    | invocacionFuncion
                    | clausula_seleccion
                    | print
                    | metodo_objeto
                    | atributo_objeto
                    | clausula_IMPL
                    | sentencia_de_Control
                    | RETURN
;

sentenciaDeclarativa: declaracion
                    | declaracionFuncion
                    | declaracionObjeto
                    | declaracionClase
;

metodo_objeto : ID '.' invocacionFuncion {System.out.print("se reconocio la invocacion de un metodo de un objeto en linea " + Main.getLinea());}
;

atributo_objeto : ID '.' ID
;

clausula_seleccion : IF condicion bloque_de_Sentencias ELSE bloque_de_Sentencias END_IF {System.out.print("Se reconocio un IF");}
                   | IF condicion bloque_de_Sentencias END_IF {System.out.print("Se reconocio un IF");}
                   | IF condicion bloque_de_Sentencias error {System.out.print("Falta el END_IF");}
                   | IF condicion bloque_de_Sentencias ELSE bloque_de_Sentencias error {System.out.print("Falta el END_IF");}
                   | IF condicion sentencia ELSE bloque_de_Sentencias END_IF {System.out.print("Se reconocio un IF");}
                   | IF condicion bloque_de_Sentencias ELSE sentencia END_IF {System.out.print("Se reconocio un IF");}
                   | IF condicion sentencia ELSE sentencia END_IF {System.out.print("Se reconocio un IF");}
                   | IF bloque_de_Sentencias error {System.out.print("Falto la condicion del IF");}
;

condicion : '(' expresion comparador expresion ')'
          | '(' expresion comparador expresion error  {System.out.print("Falta el parentesis que cierra en linea: " + Main.getLinea());}
          |  expresion comparador expresion ')' error {System.out.print("Falta el parentesis que abre en linea: " + Main.getLinea());}
;

comparador : '>'
           | '<'
           | '>='
           | '<='
           | '!!'
           | '=='
           | error {System.out.print("No se reconoce como comparador");}
;

sentencia_de_Control : DO bloque_de_Sentencias UNTIL condicion {System.out.print("Se reconocio sentencia DO UNTIL");}
                     | DO sentencia UNTIL condicion {System.out.print("Se reconocio sentencia DO UNTIL");}
                     | DO sentencia UNTIL error {System.out.print("Falta la condicion en linea: " + Main.getLinea());}
;

declaracion: tipo lista_Variables
;

lista_Variables : lista_Variables ';' ID
                | ID
;

invocacionFuncion : ID parametro_real {System.out.print("Se reconocio una invocacion de una funcion");}
;

parametro_real  : '(' expresion ')'
                | '(' ')'
                | '(' expresion error {System.out.print("Falta el parentesis que cierra en linea: " + Main.getLinea());}
                | expresion ')' error {System.out.print("Falta el parentesis que abre en linea: " + Main.getLinea());}
                | '(' error {System.out.print("Falta el parentesis que cierra en linea: " + Main.getLinea());}
                | ')' error {System.out.print("Falta el parentesis que abre en linea: " + Main.getLinea());}
;

parametro_formal: '(' tipo ID ')'
                | '(' ')'
                | '(' tipo ID error {System.out.print("Falta el parentesis que cierra en linea: " + Main.getLinea());}
                | tipo ID ')' error {System.out.print("Falta el parentesis que abre en linea: " + Main.getLinea());}
                | '(' error {System.out.print("Falta el parentesis que cierra en linea: " + Main.getLinea());}
                | ')' error {System.out.print("Falta el parentesis que abre en linea: " + Main.getLinea());}
;

tipo : DOUBLE
     | USHORT
     | LONG
     | error {System.out.print("No es un tipo definido");}
;

print : PRINT CADENA {System.out.print("Se reconocio una impresion por pantalla");}
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
    
