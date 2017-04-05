package DeductiveDatabase;

import GDLTokens.Token;
import GDLTokens.VarToken;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by siavj on 30/10/2016.
 */

/**
 * Stores a sigle proposition
 */
public class Fact{

    private ArrayList<Token> fact = new ArrayList<>();

    public Fact(ArrayList<Token> fact){
        this.fact = fact;
    }

    //copy constructor
    public Fact(Fact original){
        for (Token token : original.getFact()){
            this.fact.add(token.copy());
        }
    }

    public Token getLeadAtom(){
        for(Token tok : this.fact){
            if(tok.getValue() == -3 && !tok.getID().equals("<=")) {
                return tok;
            }
        }
        return null;
    }

    public ArrayList<Token> getFact() {
        return fact;
    }

    public void setFact(ArrayList<Token> fact) {
        this.fact = fact;
    }

    private boolean isNumeric(String str) {

        try {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe) {
            return false;
        }

        return true;
    }

    //checks fact contains no variables
    public boolean isGround() {

        for (Token fact : this.fact) {
            if (fact instanceof VarToken)
                return false;
        }

        return true;
    }

    public String toString(){
        String str = "";
        for(Token t  :this.fact) {
            str += t.getID()+ " ";
        }

        return str.trim();
    }

}
