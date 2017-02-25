package SylmbolTable;

import GDLTokens.IdToken;
import GDLTokens.IntToken;
import GDLTokens.Token;
import GDLTokens.VarToken;
import com.sun.deploy.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by siavj on 30/10/2016.
 */
public class Fact implements Serializable{

    public Fact(Fact original){
        for (Token token : original.getFact()){
            this.fact.add(token.copy());
        }
    }



    public ArrayList<Token> getFact() {
        return fact;
    }

//    public void setVarValue(String varID, String value){
//        ArrayList<Token> newValues = new ArrayList<>();
//        for (Token t : this.fact){
//
//            if (t.getID().equals(varID)){
//                if (isNumeric(value))
//                    t = new IntToken(value);
//                else
//                    t = new IdToken(value);
//            }
//            newValues.add(t);
//        }
//        this.setFact(newValues);
//    }

    public Token getLeadAtom(){
        for(Token tok : this.fact){
            if(tok.getValue() == -3 && !tok.getID().equals("<=")) {
                return tok;
            }
        }
        return null;
    }

    public void setFact(ArrayList<Token> fact) {
        this.fact = fact;
    }

    private ArrayList<Token> fact = new ArrayList<>();

    public Fact(ArrayList<Token> fact){
        this.fact = fact;
    }


    public String toString(){
        String str = "";
        for(Token t  :this.fact) {
            str += t.getID()+ " ";
        }

        return str.trim();
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

    public boolean isGround() {

        for (Token fact : this.fact) {
            if (fact instanceof VarToken)
                return false;
        }

        return true;
    }


}
