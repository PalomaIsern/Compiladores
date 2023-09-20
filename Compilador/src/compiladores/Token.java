package compiladores;

public class Token {
  //Atributos
    private int idToken;
    private int referenciaAllexema; //Referencia a la tabla de simbolos

    //Constructores
    public Token() {
    }

    public Token(int idToken) {
        this.idToken = idToken;
        this.referenciaAllexema = -1;
    }

    public Token(int idToken, int referenciaAllexema) {
        this.idToken = idToken;
        this.referenciaAllexema = referenciaAllexema;
    }

    //Getters & Setters
    public int getReferenciaAllexema() {
        return referenciaAllexema;
    }

    public int getIdToken() {
        return idToken;
    }

    public String getLexema(int referenciaAllexema){

    }

    public String getLexema(){

    }

    public void setIdToken(int idToken) {
        this.idToken = idToken;
    }

    @Override
    public String toString(){
        if (referenciaAllexema != -1){
            String explicacion = "Token "+ " ";
        }
    }

}
