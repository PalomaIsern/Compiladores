%{
import compiladores.Lexico;
import compiladores.TablaSimbolos;
import compiladores.Linea;
import compiladores.Token;
import compiladores.Terceto;
import compiladores.Simbolo;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Stack;

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

conjuntoSentenciasEjecutables: conjuntoSentenciasEjecutables sentenciaEjecutable fin_sentencia
                             | sentenciaEjecutable fin_sentencia
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

asignacion : ID simboloAsignacion expresion {   $$.sval = '[' + Integer.toString(crear_terceto($2.sval, Integer.toString(TS.pertenece($1.sval)), $3.sval)) + ']';}
            | atributo_objeto '=' atributo_objeto {System.out.println("Se reconocio una asignacion a un atributo objeto en linea "+ Linea.getLinea());}
            | atributo_objeto '=' factor {System.out.println("Se reconocio una asignacion a un atributo objeto en linea "+ Linea.getLinea());}
;

simboloAsignacion : '=' {System.out.println("Se reconocio una asignacion en linea "+ Linea.getLinea());
                        $$.sval = "=";}
                  | '+=' {System.out.println("Se reconocio una asignacion suma en linea "+ Linea.getLinea());
                        $$.sval = "+=";}
                  | error {System.out.println("No es valido el signo de asignacion");}
;

expresion : expresion operadorMasMenos termino {$$.sval = '[' + Integer.toString(crear_terceto($2.sval, $1.sval, $3.sval)) + ']';}
          | termino { $$.sval = $1.sval; }
;

termino : termino simboloTermino factor {$$.sval = '['+ Integer.toString(crear_terceto( $2.sval, $1.sval, $3.sval))+ ']';}
        | factor {$$.sval = $1.sval;}
;

simboloTermino : '*' { $$.sval = "*";}
               | '/' { $$.sval = "/";}
;

factor : ID {$$.sval = Integer.toString(TS.pertenece($1.sval));
            setear_Uso("identificador", $1.sval);}
       | CTE    {System.out.println("Se reconocio una constante en linea "+Linea.getLinea());
                chequearRangoPositivo($1.sval, $$);}
       | '-' CTE {System.out.println("Se reconocio constante negativa en linea "+ Linea.getLinea());
                chequearRangoNegativo($2.sval, $$);;}
       | CTEPOS {setear_Uso("ConstantePositiva", $1.sval);
                $$.sval = Integer.toString(TS.pertenece($1.sval));}
;

operadorMasMenos : '+' { $$.sval = "+";}
                 | '-' { $$.sval = "-";}
;


declaracionClase : CLASS ID bloque_de_Sentencias { setear_Uso("Clase", $2.sval); }
                 | CLASS ID '{' conjuntoSentencias ID ',' '}' {System.out.println("Clase con herencia por composicion en linea "+Linea.getLinea()); setear_Uso("Clase", $2.sval);}
                 | CLASS ID {setear_Uso("Clase", $2.sval);}
;

declaracionObjeto : ID lista_Variables {guardar_Tipo($1.sval); setear_Tipo();}
;

declaracionFuncion: funcion_VOID
                  | funcion_VOID_vacia
;

funcion_VOID: VOID ID parametro_formal '{' cuerpo_funcion '}' {System.out.println("Se reconocio una invocacion de una funcion VOID en linea "+ Linea.getLinea());
                                                                setear_Uso("Metodo", $2.sval);}
;

funcion_VOID_vacia: VOID ID parametro_formal {System.out.println("Se reconocio una invocacion de una funcion VOID vacia en linea "+ Linea.getLinea());
                                            setear_Uso("Metodo", $2.sval);}
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

comparador : '>' {$$.sval = ">";}
           | '<' {$$.sval = "<";}
           | '>=' {$$.sval = ">=";}
           | '<=' {$$.sval = "<=";}
           | '!!' {$$.sval = "!!";}
           | '==' {$$.sval = "==";}
           | error {System.out.println("Error: El caracter no se reconoce como comparador  en linea "+ Linea.getLinea());}
;

condicion : '(' expresion comparador expresion ')' {System.out.println("Se reconoció una condicion  en linea "+ Linea.getLinea());
                                                    $$.sval = '[' + Integer.toString(crear_terceto($3.sval, $2.sval, $4.sval)) + ']';
                                                    int aux = crear_terceto("BF", $$.sval);
                                                    pila.push(aux);}
          | '(' expresion comparador expresion error  {System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());
                                                        $$.sval = '[' + Integer.toString(crear_terceto($3.sval, $2.sval, $4.sval)) + ']';
                                                        int aux = crear_terceto("BF", $$.sval);
                                                        pila.push(aux); }
          |  expresion comparador expresion ')' error {System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());
                                                      $$.sval = '[' + Integer.toString(crear_terceto($2.sval, $1.sval, $3.sval)) + ']';
                                                      int aux = crear_terceto("BF", $$.sval);
                                                      pila.push(aux);}
;

clausula_seleccion : IF condicion bloque_IF ELSE bloque_ELSE END_IF {int primero = pila.pop();
                                                                    completarTerceto(primero, puntero_Terceto);}
                   | IF condicion sentencia_IF ELSE bloque_ELSE END_IF {int primero = pila.pop();
                                                                    completarTerceto(primero, puntero_Terceto);}
                   | IF condicion bloque_IF END_IF {int primero = pila.pop();
                                                    completarTerceto(primero, puntero_Terceto);}
                   | IF condicion sentencia_IF END_IF {int primero = pila.pop();
                                                    completarTerceto(primero, puntero_Terceto);}
                   | IF condicion bloque_IF error {System.out.println("Falta el END_IF");
                                                    int primero = pila.pop();
                                                    completarTerceto(primero, puntero_Terceto);}
                   | IF condicion sentencia_IF error {System.out.println("Falta el END_IF");
                                                    int primero = pila.pop();
                                                    completarTerceto(primero, puntero_Terceto);}
                   | IF condicion bloque_IF ELSE bloque_ELSE error {System.out.println("Falta el END_IF");
                                                                    int primero = pila.pop();
                                                                    completarTerceto(primero, puntero_Terceto);}
                   | IF condicion sentencia_IF ELSE bloque_ELSE error {System.out.println("Falta el END_IF");
                                                                    int primero = pila.pop();
                                                                    completarTerceto(primero, puntero_Terceto);}
                   | IF bloque_IF error {System.out.println("Falto la condicion del IF");}
;

bloque_IF: bloque_de_Sentencias {int primero = pila.pop();
                                int aux = crear_terceto("BI", "-");
                                completarTerceto(primero, aux+1);
                                pila.push(aux);}
;

sentencia_IF : sentencia {int primero = pila.pop();
                                int aux = crear_terceto("BI", "-");
                                completarTerceto(primero, aux+1);
                                pila.push(aux);}
;

bloque_ELSE: bloque_de_Sentencias
            | sentencia
;

sentencia_de_Control : inicio_DO bloque_de_SentenciasEjecutables UNTIL condicion {int primero = pila.pop();
                                                                completarTerceto(primero, $1.ival);}
                        | inicio_DO sentenciaEjecutable UNTIL condicion {int primero = pila.pop();
                                                                completarTerceto(primero, $1.ival);}
                     | inicio_DO bloque_de_SentenciasEjecutables UNTIL error {System.out.println("Falta la condicion de la sentencia de control");}
;

inicio_DO: DO {$$.ival = puntero_Terceto;}
;

declaracion: tipo lista_Variables {setear_Tipo();}
;

lista_Variables : lista_Variables ';' ID {setear_Uso("Variable", $3.sval); guardar_Var($3.sval);}
                | ID {setear_Uso("Variable", $1.sval); guardar_Var($1.sval);}
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

parametro_formal: '(' tipo ID ')' {setear_Uso("Parametro formal", $3.sval);}
                | '(' ')'
                | '(' tipo ID error {System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea()); setear_Uso("Parametro formal", $3.sval);}
                | tipo ID ')' error {System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea()); setear_Uso("Parametro formal", $2.sval);}
                | '(' error {System.out.println("Falta el parentesis que cierra en linea: " + Linea.getLinea());}
                | ')' error {System.out.println("Falta el parentesis que abre en linea: " + Linea.getLinea());}
;

tipo : DOUBLE {guardar_Tipo("DOUBLE");}
     | USHORT {guardar_Tipo("USHORT");}
     | LONG {guardar_Tipo("LONG");}
     | error {System.out.println("Error: No es un tipo definido en linea "+ Linea.getLinea());}
;

print : PRINT CADENA {setear_Uso("Cadena", $2.sval);
                     int aux = crear_terceto("PRINT", $2.sval);}
;

%%

    Lexico lex;
    TablaSimbolos TS = new TablaSimbolos();
    HashMap<Integer, Terceto> CodigoIntermedio = new HashMap<Integer, Terceto>();
    int puntero_Terceto = 0;
    String tipo;
    ArrayList<String> variables = new ArrayList<String>();
    Stack<Integer> pila = new Stack<Integer>();

    public int crear_terceto(String operador, String punt1, String punt2){
        Terceto t = new Terceto(operador, punt1, punt2);
        CodigoIntermedio.put(puntero_Terceto, t);
        puntero_Terceto = puntero_Terceto + 1;
        return puntero_Terceto-1;
    }

    public int crear_terceto(String operador, String punt1){
        Terceto t = new Terceto(operador, punt1);
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
                System.out.println("Referencia: " + i.getKey() + ", Terceto: (" + i.getValue().get_Operador() + " , " + j + " , "+ s +")");
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
            System.out.println(token.toString());
            return token.getIdToken();
        }
        else
            TS.imprimirContenido();
            imprimirCodigoIntermedio();            
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
                        System.out.println("El double positivo es menor al limite permitido. Tiene valor: "+num);
                        TS.eliminar(Double.toString(num));
                        String nuevo = "2.2250738585072014e-308"  ;
                        TS.agregar(nuevo, 258);
                        System.out.println("El numero fue actualizado en la TS a un valor permitido");
                        setear_Uso("Constante", nuevo);
                        factor.sval = Integer.toString(TS.pertenece(nuevo));
                    }
                    else {  setear_Uso("Constante", numero);
                            factor.sval = Integer.toString(TS.pertenece(numero));}
                }
            else if (num > 1.7976931348623157e+308)
            {
                System.out.println("El double positivo es mayor al limite permitido. Tiene valor: "+num);
                TS.eliminar(Double.toString(num));
                String nuevo = "1.7976931348623157e+308";
                TS.agregar(nuevo, 258);
                setear_Uso("Constante", nuevo);
                factor.sval = Integer.toString(TS.pertenece(nuevo));
            }
            else
                {setear_Uso("Constante", numero);
                factor.sval = Integer.toString(TS.pertenece(numero));}
        }
        else
        {   Long entero = Long.valueOf(numero);
            if (entero > 2147483647L) {
                System.out.println("El entero largo positivo es mayor al limite permitido. Tiene valor: "+entero);
                TS.eliminar(Long.toString(entero));
                String nuevo = "2147483647";
                TS.agregar(nuevo, 258);
                System.out.println("El numero fue actualizado en la TS a un valor permitido");
                setear_Uso("Constante", nuevo);
                factor.sval = Integer.toString(TS.pertenece(nuevo));
            }
            else
                {setear_Uso("Constante", numero);
                factor.sval = Integer.toString(TS.pertenece(numero));}
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
                        System.out.println("El double negativo es mayor al limite permitido. Tiene valor: "+num);
                        TS.eliminar(Double.toString(num * (-1.0)));
                        String nuevo = "-2.2250738585072014e-308";
                        TS.agregar(nuevo, 258);//Lo agrego a la tabla de simbolos con el signo
                        setear_Uso("Constante negativa", nuevo);
                        System.out.println("El numero fue actualizado en la TS a un valor permitido");
                        factor.sval = Integer.toString(TS.pertenece(nuevo));
                    }
                }
            else if (num < -1.7976931348623157e+308)
            {
                System.out.println("El double positivo es menor al limite permitido. Tiene valor: "+num);
                TS.eliminar(Double.toString(num *(-1.0)));
                String nuevo = "-1.7976931348623157e+308";
                TS.agregar(nuevo, 258);
                setear_Uso("Constante negativa", nuevo);
                factor.sval = Integer.toString(TS.pertenece(nuevo));
            }
            else 
            {//En caso de respetar el rango solo le agrega el menos en la tabla de simbolos
                TS.eliminar(Double.toString(num *(-1.0)));
                String nuevo = Double.toString(num);
                TS.agregar(nuevo, 258);
                setear_Uso("Constante negativa", nuevo);
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
                System.out.println("El entero largo negativo es menor al limite permitido. Tiene valor: "+entero);
                TS.eliminar(Long.toString(entero*(-1)));
                String nuevo = "-2147483648";
                TS.agregar(nuevo, 258);//Lo agrego a la tabla de simbolos en negativo borrando el numero positivo
                setear_Uso("Constante negativa", nuevo);
                factor.sval = Integer.toString(TS.pertenece(nuevo));
            } else
            {
                //En caso de respetar el rango solo le agrega el menos en la tabla de simbolos
                TS.eliminar(Long.toString(entero*(-1)));
                String nuevo = Long.toString(entero);
                TS.agregar(nuevo, 258);
                setear_Uso("Constante negativa", nuevo);
                factor.sval = Integer.toString(TS.pertenece("-"+numero));
            }
        }
    }

    public void VerificarSalto(){
        if (lex.salto())
            System.out.println("Falto la coma en la linea " + (Linea.getLinea()-1) +" al final de la sentencia");
        else
            System.out.println("Falto la coma en la linea " + Linea.getLinea() +" al final de la sentencia");
    }

    public void setear_Uso(String uso, String pos){
        int clave = TS.pertenece(pos);
        Simbolo s = TS.get_Simbolo(clave);
        s.set_Uso(uso);
    }

    public void guardar_Tipo(String t){
        System.out.println("tipo: " + t);
        tipo = t;
    }

    public void setear_Tipo(){
        for (String s: variables)
        {
        int clave = TS.pertenece(s);
        Simbolo sim = TS.get_Simbolo(clave);
        sim.set_Tipo(tipo);
        }
        variables.clear();
    }

    public void guardar_Var(String id){
        variables.add(id);
    }

    public void completarTerceto(int pos, int aux){
        Terceto t = CodigoIntermedio.get(pos);
        String operando = "[" + Integer.toString(aux) + "]";
        t.set_Op(operando);
    }