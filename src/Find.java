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
* */

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Find {
    private String texto;
    private  Pieza[] piezas;


    public Find(String texto) {
        this.texto=texto;
    }

    public boolean match(String regex) {



        Pieza[] piezas = Pieza.getPieza(regex);
        if (piezas == null) {
            return false;
        }


        boolean InterroganteUsado = false;
        boolean PrincipioUsado = false;
        boolean FinUsado = false;

        int x = 0;
        for (int i = 0; i < this.texto.length(); i++) {





            if (x==piezas.length) {
                return true;
            }else if (piezas[x].getType()== Pieza.Type.cualquierCaracter){

                InterroganteUsado=true;
                if (i==this.texto.length()){
                    if (PrincipioUsado){
                        return false;
                    }
                    x=0;
                }else {
                    x++;
                }

            }else if (piezas[x].getType()==Pieza.Type.finalFrase){
                FinUsado = true;
                x++;
                if (x==texto.length()){
                    return true;
                }else {
                    return false;
                }

            }else if(piezas[x].getType()==Pieza.Type.inicioFrase){
                PrincipioUsado = true;
                x++;
                i--;

            }else if(piezas[x].getType()==Pieza.Type.multiplesCaracteres){
                // []

                if (piezas[x].getCaracteres().contains(Character.toString(this.texto.charAt(i)))){
                    x++;
                }else {
                    x=0;
                }

            }else if(piezas[x].getType()==Pieza.Type.cuantificador){

                int cont=0;
                Pieza piezaCuantificada = piezas[x].getPiezaCuantificada();


                while (true){







                  break;
                }

                x++;

            }else if(this.texto.charAt(i)==piezas[x].getCaracter()){
                x++;


            }else{
                x=0;
                if (PrincipioUsado){
                    return false;
                }
                if (InterroganteUsado){
                    i--;
                    InterroganteUsado=false;
                }

            }


        }

        if (x==piezas.length) {
            return true;
        }

        return piezas[x].getType() == Pieza.Type.finalFrase;

    }





}

































class Pieza{

    enum Type{

        cuantificador, caracterLiteral, cualquierCaracter, inicioFrase, finalFrase, multiplesCaracteres

    }
    private char caracter;
    private Type type;
    private String caracteres;
    private Pieza piezaCuantificada;


    //Atributo de cuantificador
    private boolean minimo;

    private Pieza(){}

    private static Pieza caracter(char c){
        Pieza p = new Pieza();
        p.caracter = c;
        p.type = Type.caracterLiteral;
        return p;
    }

    private static  Pieza cualquierCaracter(){
        Pieza p = new Pieza();
        p.type = Type.cualquierCaracter;
        return p;
    }

    private static Pieza posicion(char c){
        Pieza p = new Pieza();

        if (c=='%'){
            p.type=Type.inicioFrase;
        }else {
            p.type=Type.finalFrase;
        }
        p.caracter=c;
        return p;
    }

    private static Pieza caracteres(String s){
        Pieza p = new Pieza();
        p.type=Type.multiplesCaracteres;
        p.caracteres=s;
        return p;
    }

    private static Pieza cuantificador(char c, Pieza pp){
        Pieza p = new Pieza();
        p.piezaCuantificada = pp;

        p.type=Type.cuantificador;
        p.caracter=c;

        if (c=='+'){
            p.minimo=true;
        }
        return p;
    }



    static public Pieza[] getPieza(String expresion){
        List<Pieza> devolver = new ArrayList<>();
        StringBuilder temp= new StringBuilder();

        if (expresion==""){
            return null;
        }
        for (int i = 0; i < expresion.length(); i++) {
            if ((expresion.charAt(i) >= 65 && expresion.charAt(i) <= 90) | (expresion.charAt(i) >= 97 && expresion.charAt(i) <= 122)) {
                devolver.add(caracter(expresion.charAt(i)));
            } else if (expresion.charAt(i)=='@'){
                i++;
                devolver.add(caracter(expresion.charAt(i)));
            }else if(expresion.charAt(i)=='?'){
                devolver.add(cualquierCaracter());
            }else if((expresion.charAt(i)=='%' && i==0) | (expresion.charAt(i)=='$' && i==expresion.length()-1)){
                devolver.add(posicion(expresion.charAt(i)));
            }else if (expresion.charAt(i)=='+'){

                Pieza pp = devolver.get(devolver.size()-1);
                devolver.remove(devolver.size()-1);
                devolver.add(cuantificador(expresion.charAt(i),pp));


            }else if(expresion.charAt(i) == '['){
                StringBuilder temporal = new StringBuilder();
                i++;
                for (int j = i; j < expresion.length(); j++) {
                    if (expresion.charAt(j)==']'){
                        i+=(j-i);
                        break;
                    }else{
                        if (expresion.charAt(j)=='-'){

                            char caracterAnterior = expresion.charAt(j-1);
                            char caracterPosterior = expresion.charAt(j+1);

                            if (caracterAnterior=='@'){
                                temporal.deleteCharAt(temporal.length()-1);

                                temporal.append('-');
                            }else {

                                char temp1 ;
                                for (int k = caracterAnterior+1; k < caracterPosterior; k++) {
                                    temp1 =(char) k;
                                    temporal.append(temp1);
                                }
                            }
                        }else {
                            temporal.append(expresion.charAt(j));
                        }
                    }
                }

                devolver.add(caracteres(temporal.toString()));

            }else{
                devolver.add(caracter(expresion.charAt(i)));
            }

        }

        System.out.println(Arrays.toString(devolver.toArray(new Pieza[0])));

        return devolver.toArray(new Pieza[0]);
    }


    public String getCaracteres() {
        return caracteres;
    }
    public char getCaracter(){
        return this.caracter;
    }
    public Type getType() {
        return this.type;
    }
    public boolean isMinimo() {
        return minimo;
    }
    public Pieza getPiezaCuantificada() {
        return piezaCuantificada;
    }

    @Override
    public String toString(){
        return Character.toString(caracter) + " " + caracteres+" "+piezaCuantificada;
    }

}