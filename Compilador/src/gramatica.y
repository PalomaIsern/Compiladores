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

programa : bloque_de_Sentencias {System.out.println("progrm");}
;

conjuntoSentencias: conjuntoSentencias sentencia
                  | sentencia
;

cuerpo_funcion: conjuntoSentencias
;

llave_que_abre: '{' {System.out.println("Se reconocio una llave que abre");}
;

llave_que_cierra: '}' {System.out.println("Se reconocio una llave que cierra");}
;

bloque_de_Sentencias : llave_que_abre conjuntoSentencias llave_que_cierra {System.out.println("Bloque reconocido");}
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


declaracionClase : CLASS ID bloque_de_Sentencias {System.out.println("Se reconocio una clase");}
                 | CLASS ID llave_que_abre conjuntoSentencias ID ',' llave_que_cierra {System.out.println("Se reconocio una clase con herencia por composicion");}
                 | CLASS ID {System.out.println("Se reconocio una clase");}
;

declaracionObjeto : ID lista_Variables
;

declaracionFuncion: funcion_VOID
                  | funcion_VOID_vacia
;

funcion_VOID: VOID ID parametro_formal llave_que_abre cuerpo_funcion llave_que_cierra {System.out.println("Se reconocio una funcion void");}
;

funcion_VOID_vacia: VOID ID parametro_formal {System.out.println("Se reconocio una funcion void vacia");}
;

clausula_IMPL : IMPL FOR ID ':' llave_que_abre funcion_VOID llave_que_cierra {System.out.println("Se reconocio sentencia IMPL FOR");}
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

metodo_objeto : ID '.' invocacionFuncion {System.out.println("se reconocio la invocacion de un metodo de un objeto en linea " + Linea.getLinea());}
;

atributo_objeto : ID '.' ID
;

comparador : '>'
           | '<'
           | '>='
           | '<='
           | '!!'
           | '=='
           | error {System.out.println("No se reconoce como comparador");}
;

condicion : '(' expresion comparador expresion ')' {System.out.println("Se reconoció una condicion");}
          | '(' expresion comparador expresion error  {System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());}
          |  expresion comparador expresion ')' error {System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());}
;

clausula_seleccion : IF condicion bloque_de_Sentencias ELSE bloque_de_Sentencias END_IF {System.out.println("Se reconocio un IF");}
                   | IF condicion bloque_de_Sentencias END_IF {System.out.println("Se reconocio un IF");}
                   | IF condicion bloque_de_Sentencias error {System.out.println("Falta el END_IF");}
                   | IF condicion bloque_de_Sentencias ELSE bloque_de_Sentencias error {System.out.println("Falta el END_IF");}
                   | IF condicion sentencia ELSE bloque_de_Sentencias END_IF {System.out.println("Se reconocio un IF");}
                   | IF condicion bloque_de_Sentencias ELSE sentencia END_IF {System.out.println("Se reconocio un IF");}
                   | IF condicion sentencia ELSE sentencia END_IF {System.out.println("Se reconocio un IF");}
                   | IF bloque_de_Sentencias error {System.out.println("Falto la condicion del IF");}
                   | IF condicion sentencia END_IF {System.out.println("Se reconocio un IF");}
;

sentencia_de_Control : DO bloque_de_Sentencias UNTIL condicion {System.out.println("Se reconocio sentencia DO UNTIL");}
                     | DO sentencia UNTIL condicion {System.out.println("Se reconocio sentencia DO UNTIL");}
                     | DO sentencia UNTIL error {System.out.println("Falta la condicion en linea: " + Linea.getLinea());}
;

declaracion: tipo lista_Variables {System.out.println("Se reconocio una declaracion");}
;

lista_Variables : lista_Variables ';' ID
                | ID
;

invocacionFuncion : ID parametro_real {System.out.println("Se reconocio una invocacion de una funcion");}
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
     | error {System.out.println("No es un tipo definido");}
;

print : PRINT CADENA {System.out.println("Se reconocio una impresion por pantalla");}
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
    

    
