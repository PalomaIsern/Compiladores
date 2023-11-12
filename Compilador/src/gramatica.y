%{
import compiladores.Lexico;
import compiladores.TablaSimbolos;
import compiladores.Linea;
import compiladores.Token;
import compiladores.Terceto;
import compiladores.Simbolo;
import compiladores.Conversion;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Arrays;


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

bloque_de_Sentencias: '{' conjuntoSentencias '}' { //System.out.println("Bloque de Sentencias reconocido")
                                                }
;

bloque_de_SentenciasEjecutables : '{' conjuntoSentenciasEjecutables '}'
;
        
sentencia  : sentenciaDeclarativa fin_sentencia
           | sentenciaEjecutable fin_sentencia
;

fin_sentencia : ','
                | error {VerificarSalto();}
;

asignacion : ID simboloAsignacion expresion         {String conv = convertirTipoAsignacion($1.sval, $3.sval);
                                                    if (conv != "-"){
                                                        $3.sval = "["+ Integer.toString(crear_terceto(conv, $3.sval, "-")) +"]";
                                                        CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(convertible.devolverTipoAConvertir(conv));
                                                    }
                                                    if ($2.sval == "+="){
                                                        String aux = "[" + Integer.toString(crear_terceto("+", Integer.toString(TS.pertenece($1.sval)), $3.sval)) + "]";
                                                        $$.sval = '[' + Integer.toString(crear_terceto("=", Integer.toString(TS.pertenece($1.sval)), aux)) + ']';}
                                                    else 
                                                        $$.sval = '[' + Integer.toString(crear_terceto($2.sval, Integer.toString(TS.pertenece($1.sval)), $3.sval)) + ']';
                                                    CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(convertible.devolverTipoAConvertir(TS.get_Simbolo(TS.pertenece($1.sval)).get_Tipo()));
                                                    ver_ElementoDeclarado($1.sval);
                                                    }
            | atributo_objeto '=' atributo_objeto   {//System.out.println("Se reconocio una asignacion a un atributo objeto en linea "+ Linea.getLinea());
                                                    if (($1.sval != null) && ($3.sval != null))
                                                        $$.sval = '[' + Integer.toString(crear_terceto("=", $1.sval, $3.sval))+']';}
            | atributo_objeto '=' factor            {//System.out.println("Se reconocio una asignacion a un atributo objeto en linea "+ Linea.getLinea());
                                                    if (($1.sval != null) && ($3.sval != null))
                                                        $$.sval = '[' + Integer.toString(crear_terceto("=", $1.sval, $3.sval))+']';}
;

simboloAsignacion : '='     {//System.out.println("Se reconocio una asignacion en linea "+ Linea.getLinea());
                            $$.sval = "=";}
                  | '+='    {//System.out.println("Se reconocio una asignacion suma en linea "+ Linea.getLinea());
                            $$.sval = "+=";}
                  | error   {System.out.println("ERROR: linea " + Linea.getLinea() + " No es valido el signo de asignacion");}
;

expresion : expresion operadorMasMenos termino  {realizar_Conversion($1.sval, $3.sval, $2.sval, $$);}
          | termino                             {$$.sval = $1.sval;}
;

termino : termino simboloTermino factor     { realizar_Conversion($1.sval, $3.sval, $2.sval, $$);}
        | factor                            {$$.sval = $1.sval;}
;

simboloTermino : '*' { $$.sval = "*";}
               | '/' { $$.sval = "/";}
;

factor : ID     {$$.sval = Integer.toString(TS.pertenece($1.sval));
                setear_Uso("identificador", $1.sval+ambito);
                ver_ElementoDeclarado($1.sval);}
       | CTE    {//System.out.println("Se reconocio una constante en linea "+Linea.getLinea());
                chequearRangoPositivo($1.sval, $$);}
       | '-' CTE    {//System.out.println("Se reconocio constante negativa en linea "+ Linea.getLinea());
                    chequearRangoNegativo($2.sval, $$);;}
       | CTEPOS     {setear_Uso("ConstantePositiva", $1.sval+ambito);
                    $$.sval = Integer.toString(TS.pertenece($1.sval));}
;

operadorMasMenos : '+' { $$.sval = "+";}
                 | '-' { $$.sval = "-";}
;


declaracionClase : inicioClase bloque_de_Sentencias { setear_Uso("Clase", $1.sval+ambito); 
                                                     metodosTemp = new ArrayList<Integer>();
                                                    metodosTempNoImp = new ArrayList<Integer>();
                                                    atributosTemp = new ArrayList<Integer>();
                                                    volver_Ambito();
                                                    }
                 | inicioClase '{' conjuntoSentencias ID ',' '}'        {//System.out.println("Clase con herencia por composicion en linea "+Linea.getLinea()); 
                                                                        volver_Ambito();
                                                                        metodosTemp = new ArrayList<Integer>();
                                                                        metodosTempNoImp = new ArrayList<Integer>();
                                                                        atributosTemp = new ArrayList<Integer>();
                                                                        clavePadre = verificarExisteClasePadre($$.sval+ambito, $4.sval+ambito);
                                                                        if (clavePadre != -1);{
                                                                            setear_Uso("Clase", $1.sval+ambito);
                                                                            setear_Tipo($$.sval+ambito, $4.sval+ambito);
                                                                        }
                                                                        }
                 | inicioClase {setear_Uso("Clase", $1.sval+ambito);
                                volver_Ambito();}
;


inicioClase: CLASS ID   { if (setear_Ambito($2.sval+ambito, $2.sval))
                            metodosTemp = new ArrayList<Integer>();
                            atributosTemp = new ArrayList<Integer>();
                            metodosTempNoImp = new ArrayList<Integer>();
                            agregarClase($2.sval+ambito, metodosTemp, metodosTempNoImp, atributosTemp);
                            ambito += ":" + $2.sval;
                            $$.sval = $2.sval;
                            clavePadre = -1;
                        }    
;

declaracionObjeto : ID lista_Variables {guardar_Tipo($1.sval+ambito); setear_Tipo();
                                        ver_ElementoDeclarado($1.sval);}
;

metodo_objeto : ID '.' ID parametro_real {  ver_ElementoDeclarado($1.sval);
                                            int clase = obtenerClase($1.sval);
                                            if (clase != -1) {
                                                int padre = -1;
                                                String tipo = TS.get_Simbolo(clase).get_Tipo();
                                                if ( tipo != " "); //hereda de otra clase
                                                    padre = TS.buscar_por_ambito(tipo);
                                                if (verificarExistencia(clase, $3.sval, "metodo") || verificarExistencia(padre, $3.sval, "metodo")) // si la funcion no existe en la clase (ni en la clase padre), no se crean tercetos
                                                {   String param = buscar_Parametro($3.sval, ambito);
                                                    String terceto = " ";
                                                    if ((param == "-" && $4.sval=="-") || (param != null && $4.sval!=null)) //si los parametros no coinciden avisa
                                                        terceto = "[" + Integer.toString(crear_terceto ("CALL", Integer.toString(TS.pertenece($3.sval)), "-")) + "]";
                                                    else
                                                        System.out.println("ERROR: linea "+ Linea.getLinea() + " Los parámetros no coinciden");
                                                    int tercetoAux = crear_terceto("CALLMetodoClase", Integer.toString(TS.pertenece($1.sval)), terceto); 
                                                } else {
                                                    System.out.println("ERROR: linea "+ Linea.getLinea()+ " el metodo "+$3.sval+ " no se encuentra al alcance o no fue declarado");
                                                }
                                            }
                                        }

;

atributo_objeto : ID '.' ID {   int clase = obtenerClase($1.sval);
                                int padre = -1;
                                String tipo = TS.get_Simbolo(clase).get_Tipo();
                                if ( tipo != " "); //hereda de otra clase
                                    padre = TS.buscar_por_ambito(tipo);
                                if (verificarExistencia(clase, $3.sval, "atributo") || verificarExistencia(padre, $3.sval, "atributo"))
                                { $$.sval = '[' + Integer.toString(crear_terceto("atributo_objeto", Integer.toString(TS.pertenece($1.sval)), Integer.toString(TS.pertenece($3.sval)) )) + ']';

                                } else {System.out.println("ERROR: linea "+ Linea.getLinea()+ " el atributo \""+$3.sval+ "\" no se encuentra al alcance o no fue declarado");
                                        $$.sval = null;}
                            }
;

declaracionFuncion: funcion_VOID    {   dentroFuncion = false;}
                  | funcion_VOID_vacia { dentroFuncion = false;}
;

funcion_VOID: inicio_Void parametro_formal '{' cuerpo_funcion '}' {//System.out.println("Se reconocio una declaracion de una funcion VOID en linea "+ Linea.getLinea());
                                                                String idFuncion = obtenerAmbito($1.sval+ambito);
                                                                agregarFuncion(idFuncion, $2.sval);
                                                                metodosTemp.add(TS.buscar_por_ambito(idFuncion));
                                                                volver_Ambito();
                                                                $$.sval = $1.sval;
                                                                }
;

funcion_VOID_vacia: inicio_Void parametro_formal {//System.out.println("Se reconocio una declaracion de una funcion VOID vacia en linea "+ Linea.getLinea());
                                            String idFuncion = obtenerAmbito($1.sval+ambito);
                                            metodosTempNoImp.add(TS.buscar_por_ambito(idFuncion));
                                            volver_Ambito();
                                            }
;

inicio_Void: VOID ID {$$.sval = $2.sval;
                    setear_Ambito($2.sval+ambito, $2.sval);
                    setear_Uso("Metodo", $2.sval+ambito);
                    setear_Tipo($2.sval+ambito, "VOID");
                    ambito += ":" + $2.sval;
                    dentroFuncion = true;
}
;

clausula_IMPL : IMPL FOR ID ':' '{' funcion_VOID fin_sentencia '}'  {   int idClase = TS.buscar_por_ambito($3.sval+ambito);
                                                                        ver_ElementoDeclarado($3.sval); //verificar que la clase exista
                                                                        if (verificarExistencia(idClase, $6.sval, "metodoNoImpl")) {
                                                                            agregarMetodoImplementado($3.sval+ambito, $6.sval+ambito+":"+$3.sval);
                                                                        } else {    int clave = TS.buscar_por_ambito($6.sval+ambito);
                                                                                    funciones.remove(clave);
                                                                                    TS.remove_Simbolo(clave);
                                                                        }
                                                                    }
;

sentenciaEjecutable : asignacion
                    | invocacionFuncion
                    | clausula_seleccion {//System.out.println("Se reconocio una clausula de seleccion IF en linea "+ Linea.getLinea());
                    }
                    | print  {//System.out.println("Se reconocio una impresion por pantalla en linea "+ Linea.getLinea());
                    }
                    | metodo_objeto {//System.out.println("Se reconocio la invocacion de un metodo de un objeto en linea " + Linea.getLinea());
                    }
                    | clausula_IMPL {//System.out.println("Se reconocio sentencia IMPL FOR en linea "+ Linea.getLinea());
                    }
                    | sentencia_de_Control {//System.out.println("Se reconocio sentencia de control DO UNTIL en linea "+ Linea.getLinea());
                    }
                    | RETURN {//System.out.println("Se reconocio sentencia de retorno RETURN en linea "+ Linea.getLinea());
                            int aux = crear_terceto("RETURN", "-", "-");}
;

sentenciaDeclarativa: declaracion {//System.out.println("Se reconocio una declaracion simple en linea "+ Linea.getLinea());
}
                    | declaracionFuncion
                    | declaracionObjeto {//System.out.println("Se reconocio una declaracion de un objeto de una clase en linea "+ Linea.getLinea());
                    }
                    | declaracionClase {//System.out.println("Se reconocio una clase en linea "+ Linea.getLinea());
                    }
;

comparador : '>' {$$.sval = ">";}
           | '<' {$$.sval = "<";}
           | '>=' {$$.sval = ">=";}
           | '<=' {$$.sval = "<=";}
           | '!!' {$$.sval = "!!";}
           | '==' {$$.sval = "==";}
           | error {System.out.println("ERROR: linea " + Linea.getLinea() +" Comparador no valido");}
;

condicion : '(' expresion comparador expresion ')' {//System.out.println("Se reconoció una condicion  en linea "+ Linea.getLinea());
                                                    $$.sval = '[' + Integer.toString(crear_terceto($3.sval, $2.sval, $4.sval)) + ']';
                                                    int aux = crear_terceto("BF", $$.sval, "-");
                                                    pila.push(aux);}
          | '(' expresion comparador expresion error  {System.out.println("ERROR: linea" + Linea.getLinea() + " Falta el parentesis que cierra");
                                                        $$.sval = '[' + Integer.toString(crear_terceto($3.sval, $2.sval, $4.sval)) + ']';
                                                        int aux = crear_terceto("BF", $$.sval, "-");
                                                        pila.push(aux); }
          |  expresion comparador expresion ')' error {System.out.println("ERROR: linea "+ Linea.getLinea() + " Falta el parentesis que abre");
                                                      $$.sval = '[' + Integer.toString(crear_terceto($2.sval, $1.sval, $3.sval)) + ']';
                                                      int aux = crear_terceto("BF", $$.sval, "-");
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
                   | IF bloque_IF error {System.out.println("ERROR: linea " + Linea.getLinea() + " Falto la condicion del IF");}
;

bloque_IF: bloque_de_Sentencias {int primero = pila.pop();
                                int aux = crear_terceto("BI", "-", "-");
                                completarTerceto(primero, aux+1);
                                pila.push(aux);}
;

sentencia_IF : sentencia {      int primero = pila.pop();
                                int aux = crear_terceto("BI", "-", "-");
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
                     | inicio_DO bloque_de_SentenciasEjecutables UNTIL error {System.out.println("ERROR: linea " + Linea.getLinea() + " Falta la condicion de la sentencia de control");}
;

inicio_DO: DO {$$.ival = puntero_Terceto;}
;

declaracion: tipo lista_Variables {setear_Tipo();}
;

lista_Variables : lista_Variables ';' ID {  setear_Ambito($3.sval+ambito, $3.sval); 
                                            setear_Uso("Variable", $3.sval+ambito); 
                                            guardar_Var($3.sval+ambito);
                                            if (!dentroFuncion)
                                                atributosTemp.add(TS.buscar_por_ambito($3.sval+ambito));
                                            }
                | ID {  setear_Ambito($1.sval+ambito, $1.sval); 
                        setear_Uso("Variable", $1.sval+ambito); 
                        guardar_Var($1.sval+ambito);
                        if (!dentroFuncion)
                            atributosTemp.add(TS.buscar_por_ambito($1.sval+ambito));
                        }
;

invocacionFuncion : ID parametro_real {
                                    ver_ElementoDeclarado($1.sval);
                                    String aux = buscar_Parametro($1.sval, ambito);
                                    if ((aux == "-" && $2.sval=="-") || (aux != null && $2.sval!=null)) //si los parametros no coinciden avisa
                                        {$$.sval = "[" + Integer.toString(crear_terceto ("CALL", Integer.toString(TS.pertenece($1.sval)), $2.sval)) + "]";}
                                    else
                                        System.out.println("ERROR: linea " + Linea.getLinea() + " Los parámetros no coinciden");
                                     if ((aux != "-" && $2.sval == "-") || (aux == "-" && $2.sval != "-"))
                                         System.out.println("ERROR: linea " + Linea.getLinea() + " La cantidad de parámetros reales con los formales no coinciden");
                                    }
;

parametro_real  : '(' expresion ')' {$$.sval = $2.sval; $$.ival= 1;}
                | '(' ')' {$$.sval = "-"; $$.ival = 0;}
                | '(' expresion error {System.out.println("ERROR: linea "+ Linea.getLinea()+ " Falta el parentesis que cierra");}
                | expresion ')' error {System.out.println("ERROR: linea "+ Linea.getLinea() + " Falta el parentesis que abre");}
                | '(' error {System.out.println("ERROR: linea "+ Linea.getLinea()+ " Falta el parentesis que cierra");}
                | ')' error {System.out.println("ERROR: linea "+ Linea.getLinea() + " Falta el parentesis que abre");}
;

parametro_formal: '(' tipo ID ')' {setear_Uso("Parametro formal", $3.sval);
                                    $$.sval = $3.sval; $$.ival = 1;}
                | '(' ')' {$$.sval = "-"; $$.ival = 0;}
                | '(' tipo ID error {System.out.println("ERROR: linea "+ Linea.getLinea()+ " Falta el parentesis que cierra"); setear_Uso("Parametro formal", $3.sval);}
                | tipo ID ')' error {System.out.println("ERROR: linea "+ Linea.getLinea()+ " Falta el parentesis que abre"); setear_Uso("Parametro formal", $2.sval);}
                | '(' error {System.out.println("ERROR: linea "+ Linea.getLinea() + " Falta el parentesis que cierra.");}
                | ')' error {System.out.println("ERROR: linea "+ Linea.getLinea() + " Falta el parentesis que abre");}
;

tipo : DOUBLE {guardar_Tipo("DOUBLE");}
     | USHORT {guardar_Tipo("USHORT");}
     | LONG {guardar_Tipo("LONG");}
     | error {System.out.println("Error: linea " + Linea.getLinea() +  " No es un tipo definido");}
;

print : PRINT CADENA {setear_Uso("Cadena", $2.sval);
                    int aux = crear_terceto("PRINT", Integer.toString(TS.pertenece($2.sval)), "-");}
;

%%

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
    HashMap<Integer, String> funciones = new HashMap<Integer, String>();
    HashMap<Integer, ArrayList<Integer>> metodosClases = new HashMap<Integer, ArrayList<Integer>>();
    HashMap<Integer, ArrayList<Integer>> metodosNoImplementados = new HashMap<Integer, ArrayList<Integer>>();
    HashMap<Integer, ArrayList<Integer>> atributosClases = new HashMap<Integer, ArrayList<Integer>>();
    ArrayList<Integer> metodosTemp = new ArrayList<Integer>();
    ArrayList<Integer> metodosTempNoImp = new ArrayList<Integer>();
    ArrayList<Integer> atributosTemp = new ArrayList<Integer>();
    int clavePadre = -1;
    boolean dentroFuncion = false;
    Conversion convertible = new Conversion();

    public void ver_ElementoDeclarado(String elemento){
        int clave = TS.buscar_por_ambito(elemento+ambito);
        if (clave==-1)
            clave = TS.pertenece(elemento);
        String aux = ambito;
        if (clave != -1){
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
            if (!s.get_Ambito().equals(elemento+aux))
                System.out.println("ERROR: linea "+ Linea.getLinea() + " - " + elemento + " está fuera del alcance");
            else
                System.out.println("El elemento " + elemento+ " está al alcance");
        }
        else System.out.println("ERROR: linea "+ Linea.getLinea() + " - " + elemento + " no fue declarado");
    }

    public String obtenerAmbito(String ambito) 
    {   //saca la ultima parte del ambito 
        if (ambito.contains(":")) {
            String[] partes = ambito.split(":");
            return String.join(":", Arrays.copyOf(partes, partes.length - 1));
        }
        return " ";
    }

    public Integer obtenerClase(String id) {
        //Dado el ID de un objeto, obtenemos la referencia a la TS de la clase a la cual pertenece
        int clave = TS.buscar_por_ambito(id+ambito);
        String nombreClase = TS.get_Simbolo(clave).get_Tipo();
        return TS.buscar_por_ambito(nombreClase);
    }

    public int verificarExisteClasePadre(String hijo, String padre) {
        String clasePadre = padre.split(":")[0];
        Integer clave = TS.buscar_por_ambito(padre);
        if (clave != -1) {
            String ambitoPadre = ":" + padre.substring(padre.indexOf(":")+1); 
            if (!ambitoPadre.equals(ambito)) {
                System.out.println("La clase \""+clasePadre+ "\"no se encuentra al alcance");
                return -1;
            }
        } else {
            System.out.println("ERROR: linea "+ Linea.getLinea()+ " la clase \""+ clasePadre+ "\" no existe");
            return -1;
        }
        return clave;
    }

    public void agregarFuncion(String id, String parametro) 
    { //agrega la funcion al hashmap de funciones, le setea el parametro en la TS
        int clave = TS.buscar_por_ambito(id);
        funciones.put(clave, parametro);
        TS.get_Simbolo(clave).set_Parametro(parametro);
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
        int clave = TS.pertenece(lex);
        if (clave!=-1){
            Simbolo s = TS.get_Simbolo(clave);
            String amb= a.substring(a.indexOf(":")+1);
            if (s.get_Ambito().equals(a))
                {System.out.println("ERROR: linea " + Linea.getLinea() + " - Redeclaracion de " + s.get_Lex()+ " en el ambito "+ amb);
                return false;
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

    public void agregarClase(String ID, ArrayList<Integer> mets, ArrayList<Integer> metsNoImp, ArrayList<Integer> atrs) {
        int clave = TS.buscar_por_ambito(ID);
        //chequeamos que no se sobreescriban los metodos del padre
        metodosClases.put(clave, mets);
        metodosNoImplementados.put(clave, metsNoImp);
        atributosClases.put(clave, atrs);
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

    public void imprimirMetodosClases() {
        System.out.println("METODOS CLASES");
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
            System.out.println(r);
        }
    }
    public boolean verificarExistencia(int clase, String nombre, String objeto) 
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
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    
    public void imprimirAtributosClases() {
        System.out.println("ATRIBUTOS CLASES");
        for (HashMap.Entry<Integer, ArrayList<Integer>> e : atributosClases.entrySet()) {
            ArrayList<Integer> atrs = e.getValue();
            int ref = e.getKey();
            String r = "ref: "+e.getKey()+ " - nombre: "+TS.get_Simbolo(ref).get_Ambito() +  " | atributos:";
            for (Integer a: atrs) {
                r += TS.get_Simbolo(a).get_Lex() + " ; ";
            };
            System.out.println(r);
        }
    }

    public void imprimirFunciones() {
        System.out.println("FUNCIONES");
        for (HashMap.Entry<Integer, String> e : funciones.entrySet()) {
            Simbolo s = TS.get_Simbolo(e.getKey());
            System.out.println(" ref: "+e.getKey() + " - nombre: "+ s.get_Lex() +" - parametro: "+ e.getValue());
        }
    }

    public String buscar_Parametro(String id, String amb){
        // busca el parametro de la funcion que tenga al alcance
        //primero busca por nombre y luego verifica el ambito
        for (HashMap.Entry<Integer, String> e : funciones.entrySet()) {
            Simbolo s = TS.get_Simbolo(e.getKey());
            if (s.get_Lex().equals(id)) {
                String ambitoSimbolo = s.get_Ambito();
                ambitoSimbolo = ":" + ambitoSimbolo.substring(ambitoSimbolo.indexOf(":")+1); 
                if (ambitoSimbolo.equals(amb)) {
                    return s.get_Parametro();
                }
            }
        }    
        return "-";
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
                System.out.println("Referencia: " + i.getKey() + ", Terceto: (" + i.getValue().get_Operador() + " , " + j + " , "+ s +")" + " Tipo: " + i.getValue().get_Tipo());
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
        else
            TS.imprimirContenido();
            System.out.println(" "); 
            imprimirCodigoIntermedio(); 
            System.out.println(" ");             
            imprimirMetodosClases();
            System.out.println(" ");
            imprimirAtributosClases();
            System.out.println(" ");
            imprimirFunciones();   
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
                TS.eliminar(Long.toString(entero));
                String nuevo = "2147483647";
                TS.agregar(nuevo, 258);
                System.out.println("El numero fue actualizado en la TS a un valor permitido");
                setear_Uso("Constante", nuevo+ambito);
                factor.sval = Integer.toString(TS.pertenece(nuevo));
            }
            else
                {setear_Uso("Constante", numero+ambito);
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
                TS.eliminar(Long.toString(entero*(-1)));
                String nuevo = "-2147483648";
                TS.agregar(nuevo, 258);//Lo agrego a la tabla de simbolos en negativo borrando el numero positivo
                setear_Uso("Constante negativa", nuevo+ambito);
                factor.sval = Integer.toString(TS.pertenece(nuevo));
            } else
            {
                //En caso de respetar el rango solo le agrega el menos en la tabla de simbolos
                TS.eliminar(Long.toString(entero*(-1)));
                String nuevo = Long.toString(entero);
                TS.agregar(nuevo, 258);
                setear_Uso("Constante negativa", nuevo+ambito);
                factor.sval = Integer.toString(TS.pertenece("-"+numero));
            }
        }
    }

    public void VerificarSalto(){
        if (lex.salto())
            System.out.println("ERROR: linea " + (Linea.getLinea()-1) + " Falto la coma al final de la sentencia");
        else
            System.out.println("ERROR: linea " + (Linea.getLinea()-1) + " Falto la coma al final de la sentencia");
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
        
