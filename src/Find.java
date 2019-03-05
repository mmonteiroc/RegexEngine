/*
* CARACTERES ESPECIALES
* lo que usamos --> originales de regexOriginal
* ? --> .
* @ --> \
* []
* % --> INICIO
* $ --> FINAL
* + --> 1 to ∞
* * --> 0 to ∞
*
* */

import java.util.ArrayList;
import java.util.List;

public class Find {
    private String texto;
    private String regexOriginal;
    private Pieza[] piezas;

    public Find(String texto) {
        this.texto=texto;
    }

    public boolean match(String regex) {
        this.regexOriginal =regex;
        piezas = Pieza.getPieza(regex);


        return false;
    }
}


class Pieza{

    private enum Type{

        cuantificador, caracterLiteral

    }
    private char car;
    private Type type;

    private Pieza(){}

    private static Pieza car(char c){
        Pieza p = new Pieza();
        p.car = c;
        p.type = Type.caracterLiteral;
        return p;
    }

    static public Pieza[] getPieza(String expresion){
        List<Pieza> devolver = new ArrayList<>();

        for (int i = 0; i < expresion.length(); i++) {
            if ((expresion.charAt(i)>=65 && expresion.charAt(i)<=90) | (expresion.charAt(i)>=97 && expresion.charAt(i)<=122) ){
                // Letra
                devolver = devolver.add(car(expresion.charAt(i)));

            }
        }



        return devolver.toArray(new Pieza[0]);
    }
}