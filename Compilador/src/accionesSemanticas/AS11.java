package accionesSemanticas;

import java.io.IOException;

import compiladores.Lexico;
import compiladores.Linea;
import compiladores.Token;

public class AS11 extends AccionSemantica {
    private int estado;

    @Override
    public Token ejecutarAS(char c) throws IOException {
        estado = Lexico.getEstadoActual();
        if (estado == 5)
            System.out.println("Linea " + Linea.getLinea() + ": no puede haber un " + c +
                    " despues de un !");
        else if (estado == 7)
            System.out.println("Linea " + Linea.getLinea() + ": no puede haber un " + c
                    + " dentro de un entero");
        else if (estado == 10)
            System.out.println("Linea" + Linea.getLinea() + ": no puede haber un " + c
                    + " dentro de un float");
        else if (estado == 15)
            System.out.println("Linea" + Linea.getLinea() + ": no puede haber un " + c
                    + " dentro de un entero, en este estado debe llegar una 'l' o una 'u'");
        else if (estado == 16)
            System.out.println("Linea" + Linea.getLinea() + ": no puede haber un " + c
                    + " dentro de un entero, en este estado debe llegar una 's' unicamente");
        Lexico.setVolverALeer(true);
        Lexico.VolverAtras();
        return null;
    }
}
