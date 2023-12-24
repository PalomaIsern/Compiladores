%{
import compiladores.Lexico;
import compiladores.TablaSimbolos;
import compiladores.Linea;
import compiladores.Token;
import compiladores.Terceto;
import compiladores.Simbolo;
import compiladores.Conversion;
import compiladores.Assembler;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Arrays;
import java.util.Iterator;
import java.util.HashSet;


import java.io.IOException;

%}

%token ID CTE CTEPOS IF END_IF ELSE PRINT CLASS VOID LONG USHORT DOUBLE DO UNTIL IMPL FOR CADENA RETURN ">=" "<=" "==" "+="  "!!" '>' '<'

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

bloque_de_Sentencias: '{' conjuntoSentencias '}'
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
                                                        CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(TS.get_Simbolo(TS.buscar_por_ambito($1.sval+ambito)).get_Tipo());
                                                        $$.sval = '[' + Integer.toString(crear_terceto("=", Integer.toString(TS.pertenece($1.sval)), aux)) + ']';}
                                                    else 
                                                        $$.sval = '[' + Integer.toString(crear_terceto($2.sval, Integer.toString(TS.pertenece($1.sval)), $3.sval)) + ']';
                                                    CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(convertible.devolverTipoAConvertir(TS.get_Simbolo(TS.pertenece($1.sval)).get_Tipo()));
                                                    if (!ver_ElementoDeclarado($1.sval)){
                                                        System.out.println("ERROR: linea "+ Linea.getLinea() + " - " + $1.sval + " no fue declarado");
                                                        error = true;}
                                                    else
                                                        verificarUso($1.sval);
                                                    }
            | atributo_objeto simboloAsignacion atributo_objeto   {
                                                    String conv = convertirAsignacionAtributo($1.sval, $3.sval);
                                                    if (conv != "-"){
                                                        $3.sval = "["+ Integer.toString(crear_terceto(conv, $3.sval, "-")) +"]";
                                                        CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(convertible.devolverTipoAConvertir(conv));
                                                    }
                                                    if ($2.sval == "+="){
                                                        String aux = "[" + Integer.toString(crear_terceto("+", $1.sval, $3.sval)) + "]";
                                                        CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(TS.get_Simbolo(Integer.parseInt($1.sval)).get_Tipo());
                                                        $$.sval = '[' + Integer.toString(crear_terceto("=", $1.sval, aux)) + ']';}
                                                    else 
                                                        if (($1.sval != null) && ($3.sval != null))
                                                            $$.sval = '[' + Integer.toString(crear_terceto("=", $1.sval, $3.sval))+']';
                                                    CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(convertible.devolverTipoAConvertir(TS.get_Simbolo(Integer.parseInt($1.sval)).get_Tipo()));
                                                    }
            | atributo_objeto simboloAsignacion factor            {
                                                    String conv = convertirAsignacionAtributo($1.sval, $3.sval);
                                                    if (conv != "-"){
                                                        $3.sval = "["+ Integer.toString(crear_terceto(conv, $3.sval, "-")) +"]";
                                                        CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(convertible.devolverTipoAConvertir(conv));
                                                    }
                                                    if ($2.sval == "+="){
                                                        String aux = "[" + Integer.toString(crear_terceto("+", $1.sval, $3.sval)) + "]";
                                                        CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(TS.get_Simbolo(Integer.parseInt($1.sval)).get_Tipo());
                                                        $$.sval = '[' + Integer.toString(crear_terceto("=", $1.sval, aux)) + ']';}
                                                    else
                                                        if ($1.sval != null)
                                                            $$.sval = '[' + Integer.toString(crear_terceto("=", $1.sval, $3.sval))+']';
                                                    CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(convertible.devolverTipoAConvertir(TS.get_Simbolo(Integer.parseInt($1.sval)).get_Tipo()));
                                                    }
            
;

simboloAsignacion : '='     {$$.sval = "=";}
                  | '+='    {$$.sval = "+=";}
                  | error   {System.out.println("ERROR: linea " + Linea.getLinea() + " No es valido el signo de asignacion");
                            error = true;}
;

expresion : expresion operadorMasMenos termino  {realizar_Conversion($1.sval, $3.sval, $2.sval, $$);}
          | termino                             {$$.sval = $1.sval;}
;

termino : termino simboloTermino factor     {realizar_Conversion($1.sval, $3.sval, $2.sval, $$);}
        | factor                            {$$.sval = $1.sval;}
;

simboloTermino : '*' { $$.sval = "*";}
               | '/' { $$.sval = "/";}
;

factor : ID     {
                if (!ver_ElementoDeclarado($1.sval)){
                    System.out.println("ERROR: linea "+ Linea.getLinea() + " - " + $1.sval + " no fue declarado");
                    error = true;}
                else {  $$.sval = Integer.toString(obtenerReferenciaId($1.sval+ambito));
                        setear_Uso("identificador", $1.sval+ambito); }
                }
       | CTE    {chequearRangoPositivo($1.sval, $$);}
       | '-' CTE    {chequearRangoNegativo($2.sval, $$);}
       | CTEPOS     {$$.sval = Integer.toString(TS.buscarConstante($1.sval, "USHORT"));
                    if ($$.sval != null)
                        TS.get_Simbolo(Integer.parseInt($$.sval)).set_Uso("ConstantePositiva");
                    //setear_Uso("ConstantePositiva", $1.sval+ambito);
                    //$$.sval = Integer.toString(TS.pertenece($1.sval));
                    }
;

operadorMasMenos : '+' { $$.sval = "+";}
                 | '-' { $$.sval = "-";}
;


declaracionClase : inicioClase bloque_de_Sentencias {volver_Ambito();
                                                    if ($1.sval != " ") {
                                                        setear_Uso("Clase", $1.sval); 
                                                        agregarClase($1.sval, metodosTemp, metodosTempNoImp, atributosTemp);
                                                        metodosTemp = new ArrayList<Integer>();
                                                        atributosTemp = new ArrayList<Integer>();
                                                        metodosTempNoImp = new ArrayList<Integer>();
                                                        } else {eliminarElementos();}
                                                    }
                 | inicioClase '{' conjuntoSentencias ID ',' '}'        {//System.out.println("Clase con herencia por composicion en linea "+Linea.getLinea()); 
                                                                        volver_Ambito();
                                                                        if ($1.sval != " ") 
                                                                            {   clavePadre = verificarExisteClasePadre($1.sval, $4.sval);
                                                                                if (clavePadre != -1){
                                                                                    agregarClase($1.sval, metodosTemp, metodosTempNoImp, atributosTemp);
                                                                                    metodosTemp = new ArrayList<Integer>();
                                                                                    atributosTemp = new ArrayList<Integer>();
                                                                                    metodosTempNoImp = new ArrayList<Integer>();
                                                                                    setear_Uso("Clase", $1.sval);
                                                                                    setear_Tipo($1.sval, $4.sval+ambito);
                                                                                } else {
                                                                                    int claveAux = TS.pertenece($4.sval);
                                                                                    if (claveAux != -1) 
                                                                                        TS.remove_Simbolo(claveAux);
                                                                                    }
                                                                            } else eliminarElementos();
                                                                        }
                 | inicioClase  {if ($1.sval != " ") {
                                    int clave = TS.buscar_por_ambito($1.sval);
                                        if (! claseVacia(clave)) {
                                        setear_Uso("Clase", $1.sval);
                                        agregarClase($1.sval, metodosTemp, metodosTempNoImp, atributosTemp);
                                        metodosTemp = new ArrayList<Integer>();
                                        atributosTemp = new ArrayList<Integer>();
                                        metodosTempNoImp = new ArrayList<Integer>();
                                        } else {
                                            System.out.println("ERROR: linea "+Linea.getLinea()+" se debe declarar la clase"+$1.sval);
                                            error = true;}
                                    }
                                    volver_Ambito();
                                }
;


    inicioClase: CLASS ID   {   metodosTemp = new ArrayList<Integer>();
                                atributosTemp = new ArrayList<Integer>();
                                metodosTempNoImp = new ArrayList<Integer>();
                                if (setear_Ambito($2.sval+ambito, $2.sval)) {
                                    $$.sval = $2.sval+ambito;
                                    setear_Uso("Clase", $$.sval);
                                    clavePadre = -1;
                                } else
                                    $$.sval = " ";
                                ambito += ":" + $2.sval;
                        }
;

declaracionObjeto : ID lista_Variables {String t = obtenerTipo($1.sval);
                                        if (t != " ") {
                                            guardar_Tipo(t);
                                            setear_Tipo();
                                        } else variables.clear();
                                        }
;

metodo_objeto : ID '.' ID parametro_real {  int refObjeto = verObjetoDeclarado($1.sval);
                                            int clase = obtenerClase($1.sval+ambito);
                                            if (refObjeto != -1 && clase != -1) {
                                                if (! claseVacia(clase)) {
                                                    int padre = -1;
                                                    String tipo = TS.get_Simbolo(clase).get_Tipo();
                                                    if ( tipo != " "); //hereda de otra clase
                                                        padre = TS.buscar_por_ambito(tipo);
                                                    int ref = verificarExistencia(clase, $3.sval, "metodo"); //obtengo la referencia al metodo (si existe)
                                                    if (ref == -1 && padre != -1)
                                                        ref = verificarExistencia(padre, $3.sval, "metodo"); //busco el metodo en la clase padre
                                                    if (ref != -1) {
                                                        String param = TS.get_Simbolo(ref).get_Parametro();
                                                        String terceto = "-";
                                                        if ((param == "-" && $4.sval=="-") || (param != "-" && !$4.sval.equals("-"))) //si los parametros no coinciden avisa
                                                            {
                                                                String tipo_real = "-";
                                                                String tipo_formal = "-";
                                                                if (param != "-"){
                                                                    if ($4.sval.contains("["))
                                                                        tipo_real = CodigoIntermedio.get(Integer.parseInt(borrarCorchetes($4.sval))).get_Tipo();
                                                                    else
                                                                        tipo_real = TS.get_Simbolo(Integer.parseInt($4.sval)).get_Tipo();
                                                                    tipo_formal = TS.get_Simbolo(Integer.parseInt(param)).get_Tipo();
                                                                    if (tipo_formal.equals(tipo_real)){
                                                                        String auxiliar = Integer.toString(crear_terceto("=", param, $4.sval));
                                                                        $$.sval = "[" + Integer.toString(crear_terceto ("CALLMetodoClase", Integer.toString(ref), Integer.toString(refObjeto))) + "]";}
                                                                    else
                                                                    {
                                                                        if (tipo_formal == "USHORT" || (tipo_formal == "LONG" && tipo_real =="DOUBLE"))
                                                                            System.out.println("ERROR: linea " + Linea.getLinea() + " Los tipos de los parámetros son incompatibles");
                                                                        else{ 
                                                                            String conversion = convertible.Convertir(tipo_formal, tipo_real);
                                                                            String terceto1 = '['+ Integer.toString(crear_terceto(conversion, $4.sval, "-")) + ']';
                                                                            String auxiliar = Integer.toString(crear_terceto("=", param, terceto1));
                                                                            System.out.println("La conversión pudo realizarse y fue de " + tipo_real + " a " + tipo_formal );
                                                                            $$.sval = "[" + Integer.toString(crear_terceto ("CALLMetodoClase", Integer.toString(ref), Integer.toString(refObjeto))) + "]";}
                                                                    }}
                                                                else
                                                                    $$.sval = "[" + Integer.toString(crear_terceto ("CALLMetodoClase", Integer.toString(ref),Integer.toString(refObjeto))) + "]";
                                                            } else {
                                                                System.out.println("ERROR: linea "+ Linea.getLinea() + " Los parámetros no coinciden");
                                                                error = true;
                                                            }
                                                    } else{
                                                        System.out.println("ERROR: linea "+ Linea.getLinea()+ " - el metodo "+$3.sval+ " no se encuentra al alcance o no fue declarado");
                                                        error = true;}
                                                } else {
                                                        System.out.println("ERROR: linea "+ Linea.getLinea()+ " - No se puede invocar al metodo \""+$3.sval+ "\" porque la clase \""+TS.get_Simbolo(clase).get_Ambito()+"\" no se encuentra implementada");
                                                        error = true;}
                                                }
                                            }
;

atributo_objeto : ID '.' ID {   int refObjeto = verObjetoDeclarado($1.sval);
                                int clase = obtenerClase($1.sval+ambito);
                                if (refObjeto != -1 && clase != -1) {
                                    if (! claseVacia(clase)) {
                                        int padre = -1;
                                        setear_Ambito_atributo($3.sval+":"+TS.get_Simbolo(refObjeto).get_Ambito(),$3.sval);
                                        String tipo = TS.get_Simbolo(clase).get_Tipo();
                                        if ( tipo != " "); //hereda de otra clase
                                            padre = TS.buscar_por_ambito(tipo);
                                        int ref = verificarExistencia(clase, $3.sval, "atributo");
                                        if (ref == -1 && padre != -1) 
                                            ref = verificarExistencia(padre, $3.sval, "atributo");
                                        if (ref != -1) { 
                                            int ref_atr = TS.buscar_por_ambito($3.sval+":"+TS.get_Simbolo(refObjeto).get_Ambito());
                                            TS.get_Simbolo(ref_atr).set_Tipo(TS.get_Simbolo(ref).get_Tipo());
                                            TS.get_Simbolo(ref_atr).set_Uso(TS.get_Simbolo(ref).get_Uso());
                                            //$$.sval = '[' + Integer.toString(crear_terceto("atributo_objeto", Integer.toString(refObjeto), Integer.toString(ref))) + ']';
                                            $$.sval = Integer.toString(ref_atr);
                                        } else 
                                            {System.out.println("ERROR: linea "+ Linea.getLinea()+ " el atributo \""+$3.sval+ "\" no se encuentra al alcance o no fue declarado");
                                            error = true;
                                            $$.sval = null;}
                                    } else{
                                        System.out.println("ERROR: linea "+ Linea.getLinea()+ " - No se puede acceder al atributo \""+$3.sval+ "\"  porque la clase \""+TS.get_Simbolo(clase).get_Ambito()+"\" no se encuentra implementada");
                                        error = true;
                                        $$.sval = null;}
                                } else {$$.sval = null;}

                            }
;


declaracionFuncion: funcion_VOID    {dentroFuncion = false;
                                    if ($1.sval != " ") {
                                        int aux = crear_terceto($1.sval, "-","-");}}
                  | funcion_VOID_vacia { dentroFuncion = false;
                                    if ($1.sval != " ") {
                                        int aux = crear_terceto($1.sval, "-","-");}}
;

funcion_VOID: inicio_Void parametro_formal '{' cuerpo_funcion '}' {
                                                                int clave = TS.buscar_por_ambito($1.sval);
                                                                if ($1.sval != " " && clave != -1) {
                                                                    metodosTemp.add(clave);
                                                                    TS.get_Simbolo(clave).set_Parametro($2.sval);
                                                                    funciones.put(clave, Linea.getLinea());
                                                                }
                                                                $$.sval = $1.sval;
                                                                volver_Ambito();
                                                                }
;

funcion_VOID_vacia: inicio_Void parametro_formal {
                                            int clave = TS.buscar_por_ambito($1.sval);
                                            if ($1.sval != " " && clave != -1) {
                                                metodosTempNoImp.add(clave);
                                                TS.get_Simbolo(clave).set_Parametro($2.sval);
                                                funciones.put(clave, Linea.getLinea());
                                            }
                                            $$.sval = $1.sval;
                                            volver_Ambito();
                                            }
;

inicio_Void: VOID ID {
                    if (!dentroIMPL) {
                        if (setear_Ambito($2.sval+ambito, $2.sval)) {
                            $$.sval = $2.sval+ambito;
                            setear_Uso("Metodo", $2.sval+ambito);
                            setear_Tipo($2.sval+ambito, "VOID");
                            int aux = crear_terceto($2.sval+ambito, "-","-");
                        } else $$.sval = " ";
                    } else {$$.sval = $2.sval+ambito;
                            int aux = crear_terceto($2.sval+ambito, "-","-");
                    }
                    ambito += ":" + $2.sval;
                    dentroFuncion = true;
}
;

clausula_IMPL : inicio_IMPL '{' funcion_VOID fin_sentencia '}'  {   int idClase =  $1.ival;
                                                                        String metodo = $3.sval.split(":")[0];
                                                                        String clase = " ";
                                                                        if (idClase != -1) 
                                                                            clase = TS.get_Simbolo(idClase).get_Ambito();
                                                                        if (verificarExistencia(idClase, metodo, "metodo") != -1) {
                                                                            System.out.println("ERROR: Linea "+Linea.getLinea()+" -  el metodo "+$3.sval+" ya se encuentra implementado en la clase "+clase);
                                                                            error = true;
                                                                        } else if (verificarExistencia(idClase, metodo, "metodoNoImpl") != -1) {
                                                                            agregarMetodoImplementado(idClase, $3.sval);
                                                                        } else { error = true;
                                                                                System.out.println("ERROR: Linea "+Linea.getLinea()+" - no se puede implementar el metodo "+$3.sval+" en la clase "+clase);
                                                                                }
                                                                        int aux = crear_terceto($3.sval, "-", "-");
                                                                        dentroIMPL = false;
                                                                        volver_Ambito();
                                                                    }
;

inicio_IMPL : IMPL FOR ID ':'   {dentroIMPL = true;
                                int idClase = verClaseDeclarada($3.sval);
                                if (idClase == -1) 
                                    {System.out.println("ERROR: linea "+ Linea.getLinea() + " - La clase " + $3.sval + " no fue declarada");
                                    error = true;}
                                ambito += ":" + $3.sval;
                                $$.ival = idClase;
                                $$.sval = $3.sval;
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
           | error {System.out.println("ERROR: linea " + Linea.getLinea() +" Comparador no valido");
                    error = true;}
;

condicion : '(' expresion comparador expresion ')' {
                                                    realizar_Conversion($2.sval, $4.sval, $3.sval, $$);
                                                    int aux;
                                                    if ($$.sval == "")
                                                        aux = crear_terceto("BF", "-", "-");
                                                    else{
                                                        aux = crear_terceto("BF", $$.sval, "-");
                                                        if ($$.sval.contains("["))
                                                            {String ref = borrarCorchetes($$.sval);
                                                            CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(CodigoIntermedio.get(Integer.parseInt(ref)).get_Tipo());
                                                            }
                                                        else
                                                            CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(CodigoIntermedio.get(Integer.parseInt($$.sval)).get_Tipo());
                                                    }
                                                    pila.push(aux);}
          | '(' expresion comparador expresion error  {System.out.println("ERROR: linea" + Linea.getLinea() + " Falta el parentesis que cierra");
                                                        error = true;
                                                        $$.sval = '[' + Integer.toString(crear_terceto($3.sval, $2.sval, $4.sval)) + ']';
                                                        int aux = crear_terceto("BF", $$.sval, "-");
                                                        if ($$.sval.contains("["))
                                                            {String ref = borrarCorchetes($$.sval);
                                                            CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(CodigoIntermedio.get(Integer.parseInt(ref)).get_Tipo());
                                                            }
                                                        else
                                                            CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(CodigoIntermedio.get(Integer.parseInt($$.sval)).get_Tipo());
                                                        pila.push(aux); }
          |  expresion comparador expresion ')' error {System.out.println("ERROR: linea "+ Linea.getLinea() + " Falta el parentesis que abre");
                                                        error = true;
                                                      $$.sval = '[' + Integer.toString(crear_terceto($2.sval, $1.sval, $3.sval)) + ']';
                                                      int aux = crear_terceto("BF", $$.sval, "-");
                                                      if ($$.sval.contains("["))
                                                            {String ref = borrarCorchetes($$.sval);
                                                            CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(CodigoIntermedio.get(Integer.parseInt(ref)).get_Tipo());
                                                            }
                                                      else
                                                            CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(CodigoIntermedio.get(Integer.parseInt($$.sval)).get_Tipo());
                                                      pila.push(aux);}
        | '(' atributo_objeto comparador expresion ')' {  realizar_Conversion($2.sval, $4.sval, $3.sval, $$);
                                                            int aux;
                                                            if ($$.sval == "")
                                                                aux = crear_terceto("BF", "-", "-");
                                                            else{
                                                                aux = crear_terceto("BF", $$.sval, "-");
                                                                if ($$.sval.contains("["))
                                                                    {String ref = borrarCorchetes($$.sval);
                                                                    CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(CodigoIntermedio.get(Integer.parseInt(ref)).get_Tipo());
                                                                    }
                                                                else
                                                                    CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(CodigoIntermedio.get(Integer.parseInt($$.sval)).get_Tipo());
                                                                }
                                                        pila.push(aux);}
                                                        
          | '(' expresion comparador atributo_objeto ')' {  realizar_Conversion($2.sval, $4.sval, $3.sval, $$);
                                                            int aux;
                                                            if ($$.sval == "")
                                                                aux = crear_terceto("BF", "-", "-");
                                                            else{
                                                                aux = crear_terceto("BF", $$.sval, "-");
                                                                if ($$.sval.contains("["))
                                                                    {String ref = borrarCorchetes($$.sval);
                                                                    CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(CodigoIntermedio.get(Integer.parseInt(ref)).get_Tipo());
                                                                    }
                                                                else
                                                                    CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(CodigoIntermedio.get(Integer.parseInt($$.sval)).get_Tipo());
                                                                }
                                                        pila.push(aux);}
          | '(' atributo_objeto comparador atributo_objeto ')' {  realizar_Conversion($2.sval, $4.sval, $3.sval, $$);
                                                            int aux;
                                                    if ($$.sval == "")
                                                                aux = crear_terceto("BF", "-", "-");
                                                            else{
                                                                aux = crear_terceto("BF", $$.sval, "-");
                                                                if ($$.sval.contains("["))
                                                                    {String ref = borrarCorchetes($$.sval);
                                                                    CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(CodigoIntermedio.get(Integer.parseInt(ref)).get_Tipo());
                                                                    }
                                                                else
                                                                    CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(CodigoIntermedio.get(Integer.parseInt($$.sval)).get_Tipo());
                                                                }
                                                        pila.push(aux);}
;

clausula_seleccion : IF condicion bloque_IF ELSE bloque_ELSE END_IF {int primero = pila.pop();
                                                                    completarTerceto(primero, puntero_Terceto);
                                                                    int aux = crear_terceto("Label"+puntero_Terceto, "-","-");}
                   | IF condicion sentencia_IF ELSE bloque_ELSE END_IF {int primero = pila.pop();
                                                                    completarTerceto(primero, puntero_Terceto);
                                                                    int aux = crear_terceto("Label"+puntero_Terceto, "-","-");}
                   | IF condicion bloque_IF END_IF {int primero = pila.pop();
                                                    completarTerceto(primero, puntero_Terceto);
                                                    int aux = crear_terceto("Label"+puntero_Terceto, "-","-");}
                   | IF condicion sentencia_IF END_IF {int primero = pila.pop();
                                                    completarTerceto(primero, puntero_Terceto);
                                                    int aux = crear_terceto("Label"+puntero_Terceto, "-","-");}
                   | IF condicion bloque_IF error {System.out.println("ERROR: linea " + Linea.getLinea()+ " Falta el END_IF");
                                                    int primero = pila.pop();
                                                    completarTerceto(primero, puntero_Terceto);
                                                    int aux = crear_terceto("Label"+puntero_Terceto, "-","-");}
                   | IF condicion sentencia_IF error {System.out.println("ERROR: linea " + Linea.getLinea()+ " Falta el END_IF");
                                                    int primero = pila.pop();
                                                    completarTerceto(primero, puntero_Terceto);
                                                    int aux = crear_terceto("Label"+puntero_Terceto, "-","-");}
                   | IF condicion bloque_IF ELSE bloque_ELSE error {System.out.println("ERROR: linea " + Linea.getLinea()+ " Falta el END_IF");
                                                                    int primero = pila.pop();
                                                                    completarTerceto(primero, puntero_Terceto);
                                                                    int aux = crear_terceto("Label"+puntero_Terceto, "-","-");}
                   | IF condicion sentencia_IF ELSE bloque_ELSE error {System.out.println("ERROR: linea " + Linea.getLinea()+ " Falta el END_IF");
                                                                    int primero = pila.pop();
                                                                    completarTerceto(primero, puntero_Terceto);
                                                                    int aux = crear_terceto("Label"+puntero_Terceto, "-","-");}
                   | IF bloque_IF error {System.out.println("ERROR: linea " + Linea.getLinea() + " Falto la condicion del IF");
                                        error = true;}
;

bloque_IF: bloque_de_Sentencias {int primero = pila.pop();
                                int aux = crear_terceto("BI", "-", "-");
                                completarTerceto(primero, aux+1);
                                int aux2 = crear_terceto("Label"+Integer.toString(aux+1), "-", "-");
                                pila.push(aux);}
;

sentencia_IF : sentencia {      int primero = pila.pop();
                                int aux = crear_terceto("BI", "-", "-");
                                completarTerceto(primero, aux+1);
                                int aux2 = crear_terceto("Label"+Integer.toString(aux+1), "-", "-");
                                pila.push(aux);}
;

bloque_ELSE: bloque_de_Sentencias
            | sentencia
;

sentencia_de_Control : inicio_DO bloque_de_SentenciasEjecutables UNTIL condicion {int primero = pila.pop();
                                                                                completarTerceto(primero, $1.ival);}
                        | inicio_DO sentenciaEjecutable fin_sentencia UNTIL condicion {int primero = pila.pop();
                                                                completarTerceto(primero, $1.ival);}
                     | inicio_DO bloque_de_SentenciasEjecutables UNTIL error {System.out.println("ERROR: linea " + Linea.getLinea() + " Falta la condicion de la sentencia de control");
                                                                            error = true;}
;

inicio_DO: DO {$$.ival = puntero_Terceto;
                crear_terceto("Label"+puntero_Terceto, "-","-");}
;

declaracion: tipo lista_Variables {setear_Tipo();}
;

lista_Variables : lista_Variables ';' ID {  boolean declarado = setear_Ambito($3.sval+ambito, $3.sval); 
                                            if (declarado) {
                                                String ultimo = get_UltimoAmbito();
                                                if (TS.buscarClase(ultimo))
                                                    setear_Uso("Atributo", $1.sval+ambito);
                                                setear_Uso("Variable", $3.sval+ambito); 
                                                guardar_Var($3.sval+ambito);
                                                if (! dentroFuncion) 
                                                    atributosTemp.add(TS.buscar_por_ambito($3.sval+ambito));
                                            }
                                        }
                | ID {  boolean declarado = setear_Ambito($1.sval+ambito, $1.sval); 
                        if (declarado) {
                            String ultimo = get_UltimoAmbito();
                            if (TS.buscarClase(ultimo))
                                setear_Uso("Atributo", $1.sval+ambito);
                            setear_Uso("Variable", $1.sval+ambito); 
                            guardar_Var($1.sval+ambito);
                            if (! dentroFuncion)
                                atributosTemp.add(TS.buscar_por_ambito($1.sval+ambito));
                        }
                }
;

invocacionFuncion : ID parametro_real  {
                                        if (!ver_ElementoDeclarado($1.sval))
                                            System.out.println("ERROR: linea "+ Linea.getLinea() + " - " + $1.sval + " no fue declarado");
                                        int ref = obtenerReferenciaFuncion($1.sval, ambito);
                                        int aux = buscar_Parametro($1.sval, ambito);
                                        String tipo_real = " ";
                                        if (ref != -1) {
                                            if ((aux == -1 && $2.sval=="-") || (aux != -1 && $2.sval != "-")) //si la cantidad de parametros no coinciden avisa
                                                {if (aux != -1){
                                                    if ($2.sval.contains("["))
                                                        tipo_real = CodigoIntermedio.get(Integer.parseInt(borrarCorchetes($2.sval))).get_Tipo();
                                                    else{
                                                        String parametro = TS.get_Simbolo(Integer.parseInt($2.sval)).get_Lex();
                                                        String nuevo_ambito = AlcanceMayor(parametro);
                                                        if (nuevo_ambito != "-")
                                                            tipo_real = TS.get_Simbolo(TS.buscar_por_ambito(nuevo_ambito)).get_Tipo();
                                                        else
                                                            {
                                                                System.out.println("ERROR: linea " + Linea.getLinea() + " El parámetro real que se quiere pasar no está al alcance");
                                                                error = true;
                                                            }
                                                    }
                                                    String tipo_formal = TS.get_Simbolo(aux).get_Tipo();
                                                    if (tipo_formal.equals(tipo_real)){
                                                        String auxiliar = Integer.toString(crear_terceto("=", Integer.toString(aux), $2.sval));
                                                        CodigoIntermedio.get(puntero_Terceto-1).set_Tipo(tipo_formal);
                                                        $$.sval = "[" + Integer.toString(crear_terceto ("CALL", Integer.toString(ref), $2.sval)) + "]";}
                                                    else
                                                        {
                                                        if (tipo_formal == "USHORT" || (tipo_formal == "LONG" && tipo_real =="DOUBLE")){
                                                            System.out.println("ERROR: linea " + Linea.getLinea() + " Los tipos de los parámetros son incompatibles");
                                                            error = true;}
                                                        else{
                                                            String conversion = convertible.Convertir(tipo_formal, tipo_real);
                                                            String terceto = '['+ Integer.toString(crear_terceto(conversion, $2.sval, "-")) + ']';
                                                            String auxiliar = Integer.toString(crear_terceto("=", Integer.toString(aux), terceto));
                                                            $$.sval = "[" + Integer.toString(crear_terceto ("CALL", Integer.toString(ref), terceto)) + "]";
                                                            }
                                                        }
                                                    }
                                                else
                                                    $$.sval = "[" + Integer.toString(crear_terceto ("CALL", Integer.toString(ref), $2.sval)) + "]";
                                                    }
                                            else{
                                                System.out.println("ERROR: linea " + Linea.getLinea() + " Los parámetros no coinciden");
                                                error = true;
                                            }
                                            if ((aux != -1 && $2.sval == "-") || (aux == -1 && $2.sval != "-")){
                                                System.out.println("ERROR: linea " + Linea.getLinea() + " La cantidad de parámetros reales con los formales no coinciden");
                                                error = true;
                                            }
                                            }
                                        }
;

parametro_real  : '(' expresion ')' {$$.sval = $2.sval;}
                | '(' ')' {$$.sval = "-";}
                | '(' expresion error {System.out.println("ERROR: linea "+ Linea.getLinea()+ " Falta el parentesis que cierra");}
                | expresion ')' error {System.out.println("ERROR: linea "+ Linea.getLinea() + " Falta el parentesis que abre");}
                | '(' error {System.out.println("ERROR: linea "+ Linea.getLinea()+ " Falta el parentesis que cierra");}
                | ')' error {System.out.println("ERROR: linea "+ Linea.getLinea() + " Falta el parentesis que abre");}
;

parametro_formal: '(' tipo ID ')' { agregar_ParametroTS($3.sval, $2.sval, $3.sval+ambito);
                                    $$.sval = Integer.toString(TS.buscar_por_ambito($3.sval+ambito));}
                | '(' ')' {$$.sval = "-";}
                | '(' tipo ID error {System.out.println("ERROR: linea "+ Linea.getLinea()+ " Falta el parentesis que cierra"); setear_Uso("Parametro formal", $3.sval);}
                | tipo ID ')' error {System.out.println("ERROR: linea "+ Linea.getLinea()+ " Falta el parentesis que abre"); setear_Uso("Parametro formal", $2.sval);}
                | '(' error {System.out.println("ERROR: linea "+ Linea.getLinea() + " Falta el parentesis que cierra.");}
                | ')' error {System.out.println("ERROR: linea "+ Linea.getLinea() + " Falta el parentesis que abre");}
;

tipo : DOUBLE {guardar_Tipo("DOUBLE");
                $$.sval = "DOUBLE";}
     | USHORT {guardar_Tipo("USHORT");
                $$.sval = "USHORT";}
     | LONG {guardar_Tipo("LONG");
                $$.sval = "LONG";}
     | error {System.out.println("ERROR: linea " + Linea.getLinea() +  " No es un tipo definido");
            error = true;}
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
    HashMap<Integer, Integer> funciones = new HashMap<Integer, Integer>();
    HashMap<Integer, ArrayList<Integer>> metodosClases = new HashMap<Integer, ArrayList<Integer>>();
    HashMap<Integer, ArrayList<Integer>> metodosNoImplementados = new HashMap<Integer, ArrayList<Integer>>();
    HashMap<Integer, ArrayList<Integer>> atributosClases = new HashMap<Integer, ArrayList<Integer>>();
    ArrayList<Integer> metodosTemp = new ArrayList<Integer>();
    ArrayList<Integer> metodosTempNoImp = new ArrayList<Integer>();
    ArrayList<Integer> atributosTemp = new ArrayList<Integer>();
    int clavePadre = -1;
    boolean dentroFuncion = false;
    boolean dentroIMPL = false; 
    Conversion convertible = new Conversion();
    Assembler CodigoAssembler;
    boolean error = false;


    public String get_UltimoAmbito(){
        String[] ambitos = ambito.split(":");
        int cantidad = ambitos.length;
        return ambitos[cantidad-1];
    }

    public boolean ver_ElementoDeclarado(String elemento){
        int clave = TS.buscar_por_ambito(elemento+ambito);
        if (clave==-1)
            clave = TS.pertenece(elemento);
        else 
            return true;
        String aux = ambito;
        if (clave != -1 && TS.get_Simbolo(clave).get_Ambito()!="-"){
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
            if (!s.get_Ambito().equals(elemento+aux)){
                System.out.println("ERROR: linea "+ Linea.getLinea() + " - " + elemento + " está fuera del alcance");
                error = true;
            }
            return true;
        }
        else
            return false;
    }


    private int verObjetoDeclarado(String objeto) {
        int clave = TS.buscar_por_ambito(objeto+ambito);
        if (clave == -1) {
            String a = ambito;
            int index = a.lastIndexOf(":");
            while (clave == -1 && index > 0) {
                a = a.substring(0, index);
                clave = TS.buscar_por_ambito(objeto+a);
                index = a.lastIndexOf(":");
            }
        }
        if (clave != -1) {
            String tipo = TS.get_Simbolo(clave).get_Tipo();
            if (tipo != "LONG" && tipo != "USHORT" && tipo != "DOUBLE") 
                return clave;
            else {
                System.out.println("ERROR: linea "+Linea.getLinea()+" el objeto \""+objeto+"\" no se encuentra declarado o no está al alcance");
                error = true;
            }
        } else {
                System.out.println("ERROR: linea "+Linea.getLinea()+" el objeto \""+objeto+"\" no se encuentra declarado o no está al alcance");
                error = true;
        }
        return clave;        
    }
    
    public int obtenerClase(String id) {
        //Dado el ID de un objeto, obtenemos la referencia a la TS de la clase a la cual pertenece
        int clave = TS.buscar_por_ambito(id);
        while (clave == -1 && id.contains(":")) {
            id = id.substring(0, id.lastIndexOf(":"));
            clave = TS.buscar_por_ambito(id);
        }
        if (clave != -1) {
            String nombreClase = TS.get_Simbolo(clave).get_Tipo();
            return TS.buscar_por_ambito(nombreClase);
        } else return clave;
    }

    private void eliminarElementos() {
        for (Integer i : metodosTemp) {
            TS.remove_Simbolo(i);
            funciones.remove(i);
        }
        metodosTemp = new ArrayList<Integer>();
        for (Integer i: atributosTemp) {
            TS.remove_Simbolo(i);
        }
        atributosTemp  = new ArrayList<Integer>();
        for (Integer i : metodosTempNoImp) {
            TS.remove_Simbolo(i);
            funciones.remove(i);
        }
        metodosTempNoImp  = new ArrayList<Integer>();

    }

    public int verificarExisteClasePadre(String hijo, String padre) {
        // en el caso de que una clase herede de otra, se verifica que la clase padre de la cual se va a heredar haya sido declarada
        ArrayList<Integer> clases = obtenerClasesDeclaradas();
        boolean existe = false;
        for (Integer i : clases) {
            Simbolo s = TS.get_Simbolo(i);
            if (s.get_Lex().equals(padre)) {
                String aux = s.get_Ambito();
                aux = aux.substring(aux.indexOf(":"));
                if (alAlcance(aux)) {
                    return i;
                } else existe = true;
            } 
        }
        if (existe) {
            System.out.println("ERROR: linea "+ Linea.getLinea()+ " - La clase \""+padre+ "\" no se encuentra al alcance");
            error = true;
        }
        else {
            System.out.println("ERROR: linea "+ Linea.getLinea()+ " la clase \""+ padre+ "\" no existe");
            error = true;
        }
        return -1;
    }

    public boolean verificar_Limite(){
        ambitos_Programa = ambito.split(":");
        int num_a = ambitos_Programa.length;
        if (num_a+1 > limite_Anidamiento)
            {System.out.println("ERROR: linea" + Linea.getLinea()+ " No es posible generar mas anidamientos");
            error = true;
            return false;}
        else
            return true;
    }

    public void setear_Ambito_atributo(String a, String lex) {
        int clave = TS.buscar_por_ambito(a);
        if (clave == -1) {
            clave = TS.pertenece(lex);
            if (clave != -1){
                Simbolo s = TS.get_Simbolo(clave);
                String amb= a.substring(a.indexOf(":")+1);
                if (s.get_Ambito()=="-")
                    s.set_Ambito(a);
                else
                    {
                    Simbolo nuevo = new Simbolo(s.get_Token(), s.get_Lex());
                    nuevo.set_Ambito(a);
                    TS.agregar_sin_chequear(nuevo);}
            }
        }
    }
    public boolean setear_Ambito(String a, String lex){
        int clave = TS.buscar_por_ambito(a);
        if (clave != -1){
            if (claseVacia(clave)) 
                return true;
            else {
                System.out.println("ERROR: linea " + Linea.getLinea() + " - Redeclaracion de " + lex + " en el ambito " + a);
                error = true;
                return false;
            }
        }
        else{
            clave = TS.pertenece(lex);
            if (clave != -1){
                Simbolo s = TS.get_Simbolo(clave);
                String amb= a.substring(a.indexOf(":")+1);
                if (s.get_Ambito()=="-")
                    s.set_Ambito(a);
                else
                    {
                    Simbolo nuevo = new Simbolo(s.get_Token(), s.get_Lex());
                    nuevo.set_Ambito(a);
                    TS.agregar_sin_chequear(nuevo);}
            }
            return true;
        }
    }

    
  
    public void agregarClase(String ID, ArrayList<Integer> mets, ArrayList<Integer> metsNoImp, ArrayList<Integer> atrs) {
        int clave = TS.buscar_por_ambito(ID);
        //chequeamos que no se sobreescriban los metodos del padre, si es que tiene
        if (clavePadre != -1) {
            ArrayList<String> metodosPadre = obtenerNombresMetodos(clavePadre);
            Iterator<Integer> iterator = mets.iterator();
            while (iterator.hasNext()) {
                Integer i = iterator.next();
                String nombre = TS.get_Simbolo(i).get_Lex();
                if (metodosPadre.contains(nombre)) {
                    System.out.println("ERROR: Linea "+ funciones.get(i)+" - no se puede sobrescribir el metodo \"" + nombre + "\" debido a que existe en la clase padre");
                    error = true;
                    TS.remove_Simbolo(i);
                    funciones.remove(i);
                    iterator.remove(); 
                }
            }
            iterator = metsNoImp.iterator();
            while (iterator.hasNext()) {
                Integer i = iterator.next();
                String nombre = TS.get_Simbolo(i).get_Lex();
                if (metodosPadre.contains(nombre)) {
                    System.out.println("ERROR: linea "+ funciones.get(i)+" no se puede sobrescribir el método \"" + nombre + "\" debido a que existe en la clase padre");
                    error = true;
                    TS.remove_Simbolo(i);
                    funciones.remove(i);
                    iterator.remove(); 
                }
            }
        }
        metodosClases.put(clave, mets);
        metodosNoImplementados.put(clave, metsNoImp);
        atributosClases.put(clave, atrs);
        clavePadre = -1;
    }

    private ArrayList<Integer> obtenerClasesDeclaradas() {
        HashSet<Integer> keys = new HashSet<>();
        keys.addAll(metodosClases.keySet());
        keys.addAll(metodosNoImplementados.keySet());
        keys.addAll(atributosClases.keySet());
        return new ArrayList<Integer>(keys);
    }

    private boolean alAlcance(String a) {
        String amb= ":" + a.substring(a.indexOf(":")+1);
        String actual = ambito;
        if (amb.equals(actual)) 
            return true;
        else { int index = actual.lastIndexOf(":");
                while (index != -1) {
                    actual = actual.substring(0, index);
                    if (amb.equals(actual))
                        return true;
                    else    
                        index = actual.lastIndexOf(":");
                }
        }
        return false;
    }

    private String AlcanceMayor(String lexema) {
        String actual = lexema + ambito;
        if (TS.buscar_por_ambito(actual) != -1)
            return actual;
        else { int index = actual.lastIndexOf(":");
                while (actual != lexema) {
                    actual = actual.substring(0, index);
                    if (TS.buscar_por_ambito(actual) != -1)
                        return actual;
                    else
                        index = actual.lastIndexOf(":");
                }
        }
        return "-";
    }

    public String obtenerTipo(String idClase) {
        //A partir de un ID de una clase, detecta la clase al alcance y devuelve lex+ambito correspondiente
        ArrayList<Integer> clases = obtenerClasesDeclaradas();
        for (Integer i: clases) {
            Simbolo s = TS.get_Simbolo(i);
            if (s.get_Lex().equals(idClase)) {
                if (alAlcance(s.get_Ambito())) {
                    return s.get_Ambito();
                }
            }
        }
        System.out.println("ERROR: linea "+Linea.getLinea()+" - la clase no se encuentra declarada o al alcance");
        error = true;
        return " ";
    }

    public ArrayList<String> obtenerNombresMetodos(int clave) {
        //devuelve los nombres de todos los metodos de una clase (declarados e implementados)
        ArrayList<String> retorno = new ArrayList<String>();
        ArrayList<Integer> aux = metodosClases.get(clave);        
        for (Integer m : aux){
            retorno.add(TS.get_Simbolo(m).get_Lex());
        }
        aux = metodosNoImplementados.get(clave);
        for (Integer m : aux){
            retorno.add(TS.get_Simbolo(m).get_Lex());
        }
        return retorno;
    }
    
    public void agregarMetodoImplementado(int clase, String metodo) 
    {   // si se implementa un metodo con IMPL, lo pasa a "implementado" y lo saca de la lista de "No implementados" 
        int clavemet = TS.buscar_por_ambito(metodo);
        ArrayList<Integer> metodos = metodosClases.get(clase);
        metodos.add(clavemet);
        metodos = metodosNoImplementados.get(clase);
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

     public void imprimirClases() {
        System.out.println("CLASES");
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
            mets = atributosClases.get(ref);
            r += "\n"+"\t \t \t \t";
            r += "atributos : ";
            if (mets != null) {
                for (Integer i: mets) {
                    r += TS.get_Simbolo(i).get_Lex() + " ; ";
                }
            }
            System.out.println(r);
        }
    }
    
    public int verificarExistencia(int clase, String nombre, String objeto) 
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
                    return elemento;
                }
            }
            return -1;
        }
        return -1;
    }
    
    private boolean claseVacia(int clave) {
        //una clase se considera "vacia" cuando se ha declarado pero no se ha definido su cuerpo
        ArrayList<Integer> aux = metodosClases.get(clave);
        if (aux != null && aux.size() == 0) {
            aux = metodosNoImplementados.get(clave);
            if (aux != null && aux.size() == 0) {
                aux = atributosClases.get(clave);
                if (aux != null && aux.size() == 0) {
                    return true;
                }
            }
        }  
        return false;
    }

    public void imprimirAtributosClases() {
        System.out.println("ATRIBUTOS CLASES");
        for (HashMap.Entry<Integer, ArrayList<Integer>> e : atributosClases.entrySet()) {
            ArrayList<Integer> atrs = e.getValue();
            int ref = e.getKey();
            String r = "ref: "+e.getKey()+ " - nombre: "+TS.get_Simbolo(ref).get_Ambito() +  " | atributos: ";
            for (Integer a: atrs) {
                r += TS.get_Simbolo(a).get_Lex() + " ; ";
            };
            System.out.println(r);
        }
    }

    public void imprimirFunciones() {
        System.out.println("FUNCIONES");
        for (HashMap.Entry<Integer, Integer> e : funciones.entrySet()) {
            Simbolo s = TS.get_Simbolo(e.getKey());
            System.out.println(" ref: "+e.getKey() + " - "+ s.get_Ambito() +" - linea: "+ e.getValue());
        }
    }

    private int obtenerReferenciaFuncion(String nombre, String ambito) {
        for (HashMap.Entry<Integer, Integer> e : funciones.entrySet()) {
            Simbolo s = TS.get_Simbolo(e.getKey());
            if (s.get_Lex().equals(nombre)) {
                if (alAlcance(s.get_Ambito())) {
                    return e.getKey();
                }
            }
        } 
        return -1;
    }

    private int obtenerReferenciaId(String variable){
        int clave = TS.buscar_por_ambito(variable);
        String var_Original = variable;
        if (clave == -1) {
            int index = variable.lastIndexOf(":");
            while (index != -1) {
                variable = variable.substring(0, index);
                clave = TS.pertenece(variable);
                if (clave != -1)
                    return clave;
                else if (variable != var_Original)    
                    index = variable.lastIndexOf(":");
                else {
                    System.out.println("ERROR: linea " + Linea.getLinea() + " La variable no fue declarada o no está al alcance");
                    error = true;
                    return -1;
                }
            }
        }
        return clave;            
    }

    public int buscar_Parametro(String id, String amb){
        //busca el parametro de la funcion que tenga al alcance
        //primero busca por nombre y luego verifica el ambito en el hashmap de funciones
        String parametro = "-";
        int clave;
        for (HashMap.Entry<Integer, Integer> e : funciones.entrySet()) {
            Simbolo s = TS.get_Simbolo(e.getKey());
            if (s.get_Lex().equals(id)) {
                String ambitoSimbolo = s.get_Ambito();
                ambitoSimbolo = ":" + ambitoSimbolo.substring(ambitoSimbolo.indexOf(":")+1); 
                if (ambitoSimbolo.equals(amb)) {
                    parametro = s.get_Parametro();
                }
            }
        }
        if (parametro != "-")
            clave = Integer.parseInt(parametro);
        else clave = -1;
        return clave;
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
        else{
            TS.imprimirContenido();
            System.out.println(" ");
            imprimirCodigoIntermedio();
            System.out.println(" ");
            imprimirClases();
            System.out.println(" ");
            imprimirFunciones();
            System.out.println(" ");
            transformar();
            }
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
                        setear_Uso("Constante", nuevo+ambito);
                        setear_Tipo(nuevo+ambito, "DOUBLE");
                        factor.sval = Integer.toString(TS.pertenece(nuevo));
                    }
                    else {  setear_Uso("Constante", numero+ambito);
                            setear_Tipo(numero+ambito, "DOUBLE");
                            factor.sval = Integer.toString(TS.pertenece(numero));}
                }
            else if (num > 1.7976931348623157e+308)
            {
                System.out.println("WARNING: linea "+ Linea.getLinea() + " El double positivo es mayor al limite permitido. Tiene valor: "+num+ ". El valor será reemplazado");
                TS.eliminar(Double.toString(num));
                String nuevo = "1.7976931348623157e+308";
                TS.agregar(nuevo, 258);
                setear_Uso("Constante", nuevo+ambito);
                setear_Tipo(nuevo+ambito, "DOUBLE");
                factor.sval = Integer.toString(TS.pertenece(nuevo));
            }
            else
                {setear_Uso("Constante", numero+ambito);
                setear_Tipo(numero+ambito, "DOUBLE");
                factor.sval = Integer.toString(TS.pertenece(numero));}
        }
        else
        {   Long entero = Long.valueOf(numero);
            if (entero > 2147483647L) {
                System.out.println("WARNING: linea " + Linea.getLinea() + " El entero largo positivo es mayor al limite permitido. Tiene valor: "+entero+ ". El valor será reemplazado");
                TS.eliminarConstante(Long.toString(entero), "LONG");
                String nuevo = "2147483647";
                TS.agregarConstante(nuevo, 258, "LONG");
                setear_Uso("Constante", nuevo+ambito);
                setear_Tipo(nuevo+ambito, "LONG");
                factor.sval = Integer.toString(TS.buscarConstante(nuevo, "LONG"));
            }
            else
                {setear_Uso("Constante", numero+ambito);
                setear_Tipo(numero+ambito, "LONG");
                factor.sval = Integer.toString(TS.buscarConstante(numero, "LONG"));}
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
                        setear_Tipo(nuevo+ambito, "DOUBLE");
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
                setear_Tipo(nuevo+ambito, "DOUBLE");
                factor.sval = Integer.toString(TS.pertenece(nuevo));
            }
            else 
            {//En caso de respetar el rango solo le agrega el menos en la tabla de simbolos
                TS.eliminar(Double.toString(num *(-1.0)));
                String nuevo = Double.toString(num);
                TS.agregar(nuevo, 258);
                setear_Uso("Constante negativa", nuevo+ambito);
                setear_Tipo(nuevo+ambito, "DOUBLE");
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
                TS.eliminarConstante(Long.toString(entero*(-1)), "LONG");
                String nuevo = "-2147483648";
                TS.agregarConstante(nuevo, 258, "LONG");//Lo agrego a la tabla de simbolos en negativo borrando el numero positivo
                setear_Uso("Constante negativa", nuevo+ambito);
                setear_Tipo(nuevo+ambito, "LONG");
                factor.sval = Integer.toString(TS.buscarConstante(nuevo, "LONG"));
            } else
            {
                //En caso de respetar el rango solo le agrega el menos en la tabla de simbolos
                TS.eliminarConstante(Long.toString(entero*(-1)), "LONG");
                String nuevo = Long.toString(entero);
                TS.agregarConstante(nuevo, 258, "LONG");
                setear_Uso("Constante negativa", nuevo+ambito);
                setear_Tipo(nuevo+ambito, "LONG");
                factor.sval = Integer.toString(TS.buscarConstante("-"+numero, "LONG"));
            }
        }
    }

    public void VerificarSalto(){
        if (lex.salto())
            System.out.println("ERROR: linea " + (Linea.getLinea()-1) + " Falto la coma al final de la sentencia");
        else
            System.out.println("ERROR: linea " + (Linea.getLinea()-1) + " Falto la coma al final de la sentencia");
        error = true;
    }

    public void agregar_ParametroTS(String lexema, String tipo, String a){
        int clave = TS.pertenece(lexema);
        Simbolo elemento = TS.get_Simbolo(clave);
        if ((clave != -1) && (elemento.get_Ambito() == "-")){
            elemento.set_Ambito(a);
            elemento.set_Tipo(tipo);
            elemento.set_Uso("Parametro formal");
        }
        else{
            Simbolo s = new Simbolo(257, lexema, tipo);
            s.set_Ambito(a);
            s.set_Uso("Parametro formal");
            TS.agregar_sin_chequear(s);
        }
    }

    public void setear_Uso(String uso, String a){
        int clave = TS.buscar_por_ambito(a);
        if (clave ==-1){
            ambitos_Programa = a.split(":");
            String lexema = ambitos_Programa[0];
            clave = TS.pertenece(lexema);
        }
        Simbolo s = TS.get_Simbolo(clave);
        if (s.get_Uso().equals(""))
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
        if (clave ==-1){
            ambitos_Programa = var.split(":");
            String lexema = ambitos_Programa[0];
            clave = TS.pertenece(lexema);
        }
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

    public String borrarCorchetes(String palabra){
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
            String ref1 = borrarCorchetes(elemento1);
            tipo1 = CodigoIntermedio.get(Integer.parseInt(ref1)).get_Tipo();}
        else{
            if (elemento1.matches("\\d+"))
                tipo1 = TS.get_Simbolo(Integer.parseInt(elemento1)).get_Tipo();
            else{
                System.out.println("ERROR: linea " + Linea.getLinea() + " No se puede realizar la conversión porque el tipo no fue declarado");
                error = true;
                elemento1 = "-1";}
        }
        if (elemento2.contains("[")){
            String ref2 = borrarCorchetes(elemento2); 
            tipo2 = CodigoIntermedio.get(Integer.parseInt(ref2)).get_Tipo();}
        else{
            if (elemento2.matches("\\d+"))
                tipo2 = TS.get_Simbolo(Integer.parseInt(elemento2)).get_Tipo();
            else{
                System.out.println("ERROR: linea " + Linea.getLinea() + " No se puede realizar la conversión porque el tipo no fue declarado");
                error = true;
                elemento2 = "-1";}
        }
        if (elemento1 != "-1" && elemento2 != "-1"){
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
    }

    public String convertirAsignacionAtributo(String objeto, String expresion){
        String tipoObjeto = TS.get_Simbolo(Integer.parseInt(objeto)).get_Tipo();
        String tipoExpresion= "-";
        String conversion = "-";
        if (expresion.contains("[")){
            String refTerceto = borrarCorchetes(expresion);
            tipoExpresion = CodigoIntermedio.get(Integer.parseInt(refTerceto)).get_Tipo();
        }
        else
            tipoExpresion = TS.get_Simbolo(Integer.parseInt(expresion)).get_Tipo();
        if (tipoObjeto.equals("USHORT")){
                if (!tipoExpresion.equals("USHORT")){
                    System.out.println("ERROR: linea " + Linea.getLinea() + " Tipos incompatibles para realizar la asignacion. Se pretende convertir " + tipoExpresion + " a USHORT");
                    error = true;}}
        else if (tipoObjeto.equals("LONG")){
                if (tipoExpresion == "USHORT")
                    conversion = "UStoL";
                else if (tipoExpresion != "LONG"){
                    System.out.println("ERROR: linea " + Linea.getLinea() + " Tipos incompatibles para realizar la asignacion. Se pretende convertir " + tipoExpresion + " a LONG");
                    error = true;}}
        else 
            if (tipoExpresion.equals("LONG"))
                conversion = "LtoD";
            else
                if (tipoExpresion.equals("USHORT"))
                    conversion = "UStoD";
        return conversion;
    }

    public String convertirTipoAsignacion(String id, String expresion){
        String tipoId = TS.get_Simbolo(TS.pertenece(id)).get_Tipo();
        String tipoExpresion= "-";
        String conversion = "-";
        if (expresion.contains("[")){
            String refTerceto = borrarCorchetes(expresion);
            tipoExpresion = CodigoIntermedio.get(Integer.parseInt(refTerceto)).get_Tipo();
        }
        else
            tipoExpresion = TS.get_Simbolo(Integer.parseInt(expresion)).get_Tipo();
        if (tipoId.equals("USHORT")){
                if (!tipoExpresion.equals("USHORT")){
                    System.out.println("ERROR: linea " + Linea.getLinea() + " Tipos incompatibles para realizar la asignacion. Se pretende convertir " + tipoExpresion + " a USHORT");
                    error = true;}}
        else if (tipoId.equals("LONG")){
                if (tipoExpresion == "USHORT")
                    conversion = "UStoL";
                else if (tipoExpresion != "LONG"){
                    System.out.println("ERROR: linea " + Linea.getLinea() + " Tipos incompatibles para realizar la asignacion. Se pretende convertir " + tipoExpresion + " a LONG");
                    error = true;}}
        else 
            if (tipoExpresion.equals("LONG"))
                conversion = "LtoD";
            else
                if (tipoExpresion.equals("USHORT"))
                    conversion = "UStoD";
        return conversion;
    }


public void verificarUso(String elemento){
        int clave = TS.buscar_por_ambito(elemento+ambito);
        boolean x = false;
        if (clave==-1)
            clave = TS.pertenece(elemento);
        String aux = ambito;
        if (clave != -1){
            Simbolo s = TS.get_Simbolo(clave);
            ambitos_Programa = aux.split(":");
            int cantidad = ambitos_Programa.length;
            while (cantidad > 1){
                if (!s.get_Ambito().equals(elemento+aux)){
                    if (!seUsoLadoDerecho(elemento) && TS.get_Simbolo(clave).get_Uso() != "Atributo"){
                         x = true;
                    }
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
        }
        if (x == true)
           System.out.println("La variable: " + elemento + " no aparece en el lado derecho del ámbito dónde se declaro");
    }   

    private boolean seUsoLadoDerecho(String variable) 
    {   
        for (HashMap.Entry<Integer, Terceto> i : CodigoIntermedio.entrySet()) {
                String j = i.getValue().get_Operador();
                String s = i.getValue().get_Op2();
                if (!s.contains("[") && s!="-") {
                    int k = Integer.parseInt(s);
                    s = TS.get_Simbolo(k).get_Lex();
                }
                if (j.equals("=") && s.equals(variable))
                    return true;
        }
        return false;
    }
        
    public int verClaseDeclarada(String nombre) {
        // en el caso de que una clase herede de otra, se verifica que la clase padre de la cual se va a heredar haya sido declarada
        ArrayList<Integer> clases = obtenerClasesDeclaradas();
        for (Integer i : clases) {
            Simbolo s = TS.get_Simbolo(i);
            if (s.get_Lex().equals(nombre)) {
                String aux = s.get_Ambito();
                aux = aux.substring(aux.indexOf(":"));
                if (alAlcance(aux)) {
                    return i;
                } 
            } 
        }
        error = true;
        return -1;
    }

    public void transformar(){
        CodigoAssembler = new Assembler(CodigoIntermedio, TS);
        if (error)
            System.out.println("El codigo presenta errores semánticos, no es posible generar Código Assembler");
        else
            CodigoAssembler.GenerarAssembler();
    }
