package accionesSemanticas;

import java.io.IOException;

import compiladores.Lexico;
import compiladores.TablaSimbolos;
import compiladores.TablaToken;
import compiladores.Token;

public class AS10 extends AccionSemantica {
    // Verificar el rango de la constante (pto flot) y
    // comprobar que su estructura sea correcta. Devolver el último caracter leído
    private double min = 1.7976931348623157E+308;
    private double max = 2.2250738585072014E-308;
    double d = 0.0;

    @Override
    public Token ejecutarAS(char c) throws IOException {
        Lexico.setVolverALeer(true); // devolver lo ultimo leido
        if (lexema.length() > 1 && lexema.charAt(lexema.length() - 1) == '.') { // 1.
            lexema.append('0');
            d = Double.parseDouble(lexema.toString());
        }
        if (lexema.length() > 1 && lexema.charAt(0) == '.') { // .6
            lexema.insert(0, '0'); // Lo convierto en 0.6
            d = Double.parseDouble(lexema.toString());
        }
        if (lexema.toString().contains(String.valueOf('d'))) { // reemplazo las 'd' por 'e'
            lexema.insert(lexema.toString().indexOf('d'), 'e');
        } else if (lexema.toString().contains(String.valueOf('D'))) {
            lexema.insert(lexema.toString().indexOf('D'), 'E');
        }
        if (lexema.length() == 1 && lexema.charAt(0) == '.') {
            System.out.println("El digito no posee parte entera ni decimal");
            d = 0.0;// Evitamos el error informamos con warning
        } else {
            d = Double.parseDouble(lexema.toString());
        }

        // verifico el rango
        if (d < min && d != 0.0) {
            System.out.println("El numero es menor al rango permitido. Consideramos el valor minimo");
            d = min;
        }
        if (d > max && d != 0.0) {
            System.out.println("El numero es mayor al rango permitido. Consideramos el valor maximo");
            d = max;
        }

        if (TablaSimbolos.pertenece(Double.toString(d)) == false) {
            TablaSimbolos.agregar(Double.toString(d));
        }
        return new Token(TablaToken.getId("CTE"), Double.toString(d));
    }
}

/*
 * En JAVA se usa e y no d para los numeros de punto flotante
 * hay que reemplazar las D por E
 */