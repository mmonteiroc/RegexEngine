import java.util.ArrayList;
import java.util.List;

public class Find {
    private String texto;

    public Find(String texto) {
        this.texto=texto;
    }


    /**
     * @param regex Expreison regular en forma de string
     * @return   True or False dependiendo si ha dado match o no
     *
     *     En este método lo que hacemos es recibir una string la
     *     cual llamaremos a Piezas para que nos retorne el array
     *     con todas la piezas de dicha String.
     *
     *     Entonces lo que haremos será ir recorriendo el texto y
     *     ir comparándolo con la primera pieza, hasta que nos de
     *     algún match, entonces seguiremos igual pero con la segunda
     *     pieza y así iremos sucesivamente.
     *
     *     Dependiendo de qué pieza toque haremos una de las siguientes
     *         cosas, si es un carácter literal, simplemente miraremos
     *     q ese carácter de esa pieza sea el mismo al que el del texto
     *     de dicha posición, si es una pieza de Cualquier Carácter lo
     *     que haremos es dar match con cualquier carácter del texto.
     *     Si es una pieza de inicio de frase lo que hacemos es que
     *     continuamos normal pero a la primera que la que falle ya
     *     no dará match y no reiniciamos.
     *     Si es de final de texto, lo que hacemos es que al acabar
     *     el array de piezas, también tendremos que haber acabado el texto.
     *     Si es de múltiples caracteres hacemos lo mismo que con
     *     carácter literal, pero que el char del texto puede coincidir
     *     con más de uno de la pieza.
     *     Y si es cuantificador hacemos lo mismo que antes pero esa misma
     *     pieza se puede repetir hasta que deje de hacer match
     *     y después cambiamos de pieza
     */
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
                x++;

            }else if (piezas[x].getType()==Pieza.Type.finalFrase){
                x++;
                return x == texto.length();

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
                    if (piezas[x].getCaracter()=='+'){
                        if (i==this.texto.length())break;
                    }else if (piezas[x].getCaracter()=='*'){
                        if (i==this.texto.length()-1)break;
                    }
                    if (piezaCuantificada.getType()==Pieza.Type.multiplesCaracteres){
                        if (piezaCuantificada.getCaracteres().contains(Character.toString(this.texto.charAt(i)))){
                            i++;
                        }else {
                            break;
                        }
                    }else if (piezaCuantificada.getType()==Pieza.Type.cualquierCaracter){
                        break;
                    }else if (piezaCuantificada.getType()==Pieza.Type.caracterLiteral){

                        if(this.texto.charAt(i)==piezaCuantificada.getCaracter()){
                            i++;
                        }else {
                            break;
                        }
                    }
                    cont++;
                }

                if (piezas[x].getCaracter()=='+'){
                    if (cont>0){
                        x++;
                        i--;
                    }
                }else if (piezas[x].getCaracter()=='*'){
                    if (cont>=0){
                        x++;
                        i--;
                    }
                }
            }else if(this.texto.charAt(i)==piezas[x].getCaracter()){
                x++;
            }else{
                x=0;
                if (PrincipioUsado){
                    return false;
                }
                if (InterroganteUsado) {
                    i--;
                    InterroganteUsado = false;
                }
            }
        }

        if (x==piezas.length) {
            return true;
        }
        return piezas[x].getType() == Pieza.Type.finalFrase;
    }
}


/**
 * En esta clase, lo que hacemos es tener el constructor privado para que
 * no se pueda instanciar una pieza desde fuera de la clase, entonces lo
 * que hacemos es con un método alternativo, recibir una string la cual
 * sera la expresion regular que hemos de cambiar a un array de piezas.
 */
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


    /**
     * @param expresion Expresion regular en forma de string
     * @return  expresion regular en forma de aray
     *
     *     Vamos recorriendo dicha string, si nos encontramos una
     *     letra normal lo que hacemos es simplemente añadir una
     *     pieza de tipo carácter Literal, si nos encontramos una
     *     @, la ignoramos y añadimos el siguiente carácter como
     *     tipo carácter Literal, si nos encontramos un interrogante,
     *     lo que hacemos es añadir una pieza de tipo cualquier Carácter,
     *     si lo que encontramos es un % o un $ en la primera o última
     *     posición respectivamente, añadimos una pieza de tipo Principio/final
     *     de frase, si no están en esas posiciones se toman como carácter literal,
     *     si nos encontramos un [] lo que hacemos es crear una pieza de tipo
     *     múltiplesCaracteres guardando todos los caracteres de dentro, y si
     *     encontramos un + o un * añadimos una pieza de tipo cuantificador
     */
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
            }else if (expresion.charAt(i)=='+'|expresion.charAt(i)=='*'){

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

