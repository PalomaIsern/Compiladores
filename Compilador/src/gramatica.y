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
                | error {VerificarSalto();}
;

asignacion : ID simboloAsignacion expresion
            | atributo_objeto '=' atributo_objeto {System.out.println("Se reconocio una asignacion a un atributo objeto en linea "+ Linea.getLinea());}
            | atributo_objeto '=' factor {System.out.println("Se reconocio una asignacion a un atributo objeto en linea "+ Linea.getLinea());}
;

simboloAsignacion : '=' {System.out.println("Se reconocio una asignacion en linea "+ Linea.getLinea());}
                  | '+=' {System.out.println("Se reconocio una asignacion suma en linea "+ Linea.getLinea());}
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
       | CTE    {System.out.println("Se reconocio una constante en linea "+Linea.getLinea());
                chequearRangoPositivo($1.sval);}
       | '-' CTE {System.out.println("Se reconocio constante negativa en linea "+ Linea.getLinea());
                chequearRangoNegativo($2.sval);}
       | CTEPOS
;

operadorMasMenos : '+'
                 | '-'
;


declaracionClase : CLASS ID bloque_de_Sentencias
                 | CLASS ID '{' conjuntoSentencias ID ',' '}' {System.out.println("Clase con herencia por composicion en linea "+Linea.getLinea());}
                 | CLASS ID
;

declaracionObjeto : ID lista_Variables
;

declaracionFuncion: funcion_VOID
                  | funcion_VOID_vacia
;

funcion_VOID: VOID ID parametro_formal '{' cuerpo_funcion '}' {System.out.println("Se reconocio una invocacion de una funcion VOID en linea "+ Linea.getLinea());}
;

funcion_VOID_vacia: VOID ID parametro_formal {System.out.println("Se reconocio una invocacion de una funcion VOID vacia en linea "+ Linea.getLinea());}
;

clausula_IMPL : IMPL FOR ID ':' '{' funcion_VOID fin_sentencia '}'
;

sentenciaEjecutable : asignacion
                    | invocacionFuncion
                    | clausula_seleccion {System.out.println("Se reconocio una clausula de seleccion IF en linea "+ Linea.getLinea());}
                    | print  {System.out.println("Se reconocio una impresion por pantalla en linea "+ Linea.getLinea());}
                    | metodo_objeto {System.out.println("Se reconocio la invocacion de un metodo de un objeto en linea " + Linea.getLinea());}
                    | clausula_IMPL {System.out.println("Se reconocio sentencia IMPL FOR en linea "+ Linea.getLinea());}
                    | sentencia_de_Control {System.out.println("Se reconocio sentencia de control DO UNTIL en linea "+ Linea.getLinea());}
                    | RETURN {System.out.println("Se reconocio sentencia de retorno RETURN en linea "+ Linea.getLinea());}
;

sentenciaDeclarativa: declaracion {System.out.println("Se reconocio una declaracion simple en linea "+ Linea.getLinea());}
                    | declaracionFuncion
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
            System.out.println(token.toString());
            return token.getIdToken();
        }
        else
            TS.imprimirContenido();
        return 0;
    }
    public void yyerror(String error) {
        System.out.println(error);
    }
    
    public void chequearRangoPositivo(String numero) {
        if (numero.contains(".")) //DOUBLE 
        {
            double num = Double.parseDouble(numero);
            if (num < 2.2250738585072014e-308)
                {
                    if (num != 0.0)
                    {
                        System.out.println("El double positivo es menor al limite permitido. Tiene valor: "+num);
                        if (TS.pertenece(Double.toString(num))) {
                            TS.eliminar(Double.toString(num));
                            String nuevo = "2.2250738585072014e-308"  ;
                            TS.agregar(nuevo, 258);
                        }
                    }
                    System.out.println("El numero fue actualizado en la TS a un valor permitido");
                }
            else if (num > 1.7976931348623157e+308)
            {
                System.out.println("El double positivo es mayor al limite permitido. Tiene valor: "+num);
                    if (TS.pertenece(Double.toString(num))) {
                        TS.eliminar(Double.toString(num));
                        String nuevo = "1.7976931348623157e+308";
                        TS.agregar(nuevo, 258);
                    }
            }
        }
        else
        {   Long entero = Long.valueOf(numero);
            if (entero > 2147483647L) {
                System.out.println("El entero largo positivo es mayor al limite permitido. Tiene valor: "+entero);
                if (TS.pertenece(Long.toString(entero))) {
                    TS.eliminar(Long.toString(entero));
                    String nuevo = "2147483647";
                    TS.agregar(nuevo, 258);
                }
                System.out.println("El numero fue actualizado en la TS a un valor permitido");
            }
        }
    }

public void chequearRangoNegativo(String numero) {
    if (numero.contains(".")) //DOUBLE 
    {
        //Convertimos el double en negativo para chequear rango negativo
        double num = Double.parseDouble(numero) * (-1.0); 
            if (num > -2.2250738585072014e-308)
                {
                    if (num != 0.0) //Si esta fuera del rango todavia puede ser válido por el 0.0
                    {
                        System.out.println("El double negativo es mayor al limite permitido. Tiene valor: "+num);
                        if (TS.pertenece(Double.toString(num*(-1.0)))) {
                            TS.eliminar(Double.toString(num * (-1.0)));
                            String nuevo = "-2.2250738585072014e-308";
                            TS.agregar(nuevo, 258);//Lo agrego a la tabla de simbolos con el signo
                        }
                    }
                    System.out.println("El numero fue actualizado en la TS a un valor permitido");
                }
            else if (num < -1.7976931348623157e+308)
            {
                System.out.println("El double positivo es menor al limite permitido. Tiene valor: "+num);
                    if (TS.pertenece(Double.toString(num*(-1.0)))) {
                        TS.eliminar(Double.toString(num *(-1.0)));
                        String nuevo = "-1.7976931348623157e+308";
                        TS.agregar(nuevo, 258);
                    } 
            }
            else 
            {//En caso de respetar el rango solo le agrega el menos en la tabla de simbolos
                if (TS.pertenece(Double.toString(num * (-1.0)))) {
                        TS.eliminar(Double.toString(num *(-1.0)));
                        String nuevo = Double.toString(num);
                        TS.agregar(nuevo, 258);
                }
                System.out.println("Se actualizo el double dentro del rango a negativo");
            }
    }
        else //LONG
        {
            //Convierto el numero a negativo y verifico el rango
            Long entero = Long.valueOf(numero);
            entero = entero * (-1);
            if (entero < -2147483648L) {
                System.out.println("El entero largo negativo es menor al limite permitido. Tiene valor: "+entero);
                if (TS.pertenece(Long.toString(entero * (-1)))) {
                    TS.eliminar(Long.toString(entero*(-1)));
                    String nuevo = "-2147483648";
                    TS.agregar(nuevo, 258);//Lo agrego a la tabla de simbolos en negativo borrando el numero positivo
                }
            } else
            { 
                //En caso de respetar el rango solo le agrega el menos en la tabla de simbolos
                if (TS.pertenece(Long.toString(entero * (-1)))) {
                    TS.eliminar(Long.toString(entero*(-1)));
                    String nuevo = Long.toString(entero);
                    TS.agregar(nuevo, 258);
                }

            }
        }
    }

    public void VerificarSalto(){
        if (lex.salto())
            System.out.println("Falto la coma en la linea " + (Linea.getLinea()-1) +" al final de la sentencia");
        else
            System.out.println("Falto la coma en la linea " + Linea.getLinea() +" al final de la sentencia");
    }
