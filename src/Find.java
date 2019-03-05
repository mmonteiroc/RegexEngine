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
import java.util.Arrays;
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

        for (int i = 0; i < piezas.length; i++) {

            if (texto.contains(piezas[i].getCar())){
                return true;
            }
        }


        return false;
    }
}


class Pieza{

    private enum Type{

        cuantificador, caracterLiteral

    }
    private String car;
    private Type type;

    private Pieza(){}

    private static Pieza car(char[] c){
        Pieza p = new Pieza();
        String t="";
        for (int i = 0; i < c.length; i++) {
            t+=c[i];
        }
        p.car = t;
        p.type = Type.caracterLiteral;
        return p;
    }

    static public Pieza[] getPieza(String expresion){
        List<Pieza> devolver = new ArrayList<>();
        StringBuilder temp= new StringBuilder();
        for (int i = 0; i < expresion.length(); i++) {
            if ((expresion.charAt(i)>=65 && expresion.charAt(i)<=90) | (expresion.charAt(i)>=97 && expresion.charAt(i)<=122) ){
                // Letra
                temp.append(expresion.charAt(i));


            }else {
                devolver.add(car(temp.toString().toCharArray()));
            }

        }
        devolver.add(car(temp.toString().toCharArray()));
        return devolver.toArray(new Pieza[0]);
    }



    public String getCar(){
        return this.car;
    }

    @Override
    public String toString(){
        return car.toString();
    }

}