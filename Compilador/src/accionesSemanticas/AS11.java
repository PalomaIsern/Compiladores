package accionesSemanticas;

import java.io.IOException;

import compiladores.Lexico;
import compiladores.Main;
import compiladores.Token;

public class AS11 extends AccionSemantica {
    private int estado;

    @Override
    public Token ejecutarAS(char c) throws IOException {
        estado = Lexico.getEstadoActual();
        if (estado == 5)
            System.out.print("Linea " + Main.getLinea() + " no puede haber un " + lexema.charAt(0) +
                    " despues de un !");
        else if (estado == 7)
            System.out.print("Linea " + Main.getLinea() + " no puede haber un " + lexema.charAt(0)
                    + "dentro de un entero");
        else if (estado == 10)
            System.out.print("Linea" + Main.getLinea() + " no puede haber un " + lexema.charAt(0)
                    + "dentro de un float");
        else if (estado == 15)
            System.out.print("Linea" + Main.getLinea() + " no puede haber un " + lexema.charAt(0)
                    + "dentro de un entero, en este estado debe llegar una 'l' o una 'u'");
        else if (estado == 16)
            System.out.print("Linea" + Main.getLinea() + " no puede haber un " + lexema.charAt(0)
                    + "dentro de un entero, en este estado debe llegar una 's' unicamente");
        Lexico.setVolverALeer(true);
        return null;
    }
}
