package accionesSemanticas;

import java.io.IOException;

import compiladores.Lexico;
import compiladores.Main;
import compiladores.TablaSimbolos;
import compiladores.TablaToken;
import compiladores.Token;

public class AS10 extends AccionSemantica {
    // Verificar el rango de la constante (pto flot) y
    // comprobar que su estructura sea correcta. Devolver el último caracter leído
    private double min = 2.2250738585072014e-308;
    private double max = 1.7976931348623157e+308;
    double d = 0.0;

    @Override
    public Token ejecutarAS(char c) throws IOException {
        Lexico.setVolverALeer(true); // devolver lo ultimo leido
        Lexico.VolverAtras();
        if (lexema.length() > 1 && lexema.charAt(lexema.length() - 1) == '.') { // 1.
            lexema.append('0');
            d = Double.parseDouble(lexema.toString());
        }
        if (lexema.length() > 1 && lexema.charAt(0) == '.') { // .6
            lexema.insert(0, '0'); // Lo convierto en 0.6
            d = Double.parseDouble(lexema.toString());
        }
        String lexemaStr = lexema.toString();
        lexemaStr = lexemaStr.replace('d', 'e').replace('D', 'E');
        if (lexema.length() == 1 && lexema.charAt(0) == '.') {
            System.out.println("WARNING - Linea " + Main.getLinea() + ": el digito no posee parte entera ni decimal");
            d = 0.0;// Evitamos el error informamos con warning
        } else {
            d = Double.parseDouble(lexemaStr);
        }

        // verifico el rango
        if (d <= min) {
            System.out.println("WARNING - Linea " + Main.getLinea()
                    + ": el numero es menor al rango permitido. Consideramos el valor minimo");
            d = min;
        } else if (d >= max) {
            System.out.println("WARNING - Linea " + Main.getLinea()
                    + ": el numero es mayor al rango permitido. Consideramos el valor maximo");
            d = max;
        }

        if (TablaSimbolos.pertenece(Double.toString(d)) == false) {
            TablaSimbolos.agregar(Double.toString(d), TablaToken.getId("DOUBLE"));
        }
        return new Token(TablaToken.getId("DOUBLE"), Double.toString(d));
    }
}
