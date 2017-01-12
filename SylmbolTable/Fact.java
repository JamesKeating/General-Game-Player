package SylmbolTable;

import GDLTokens.IdToken;
import GDLTokens.IntToken;
import GDLTokens.Token;
import GDLTokens.VarToken;
import com.sun.deploy.util.StringUtils;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by siavj on 30/10/2016.
 */
public class Fact {

    public Fact(Fact original){
        for (Token token : original.getFact()){
            this.fact.add(token.copy());
        }
    }

    public Fact(Description baseDescription, ArrayList<Token> variables){
        ArrayList<Token> base = new ArrayList<>();

        int parcount = -1;
        int variableIndex = 0;
        for (Token term : baseDescription.getDescription()){

            if (term.getValue() == -1)
                parcount++;

            if (!term.getID().equals("base") && parcount < 1){
                base.add(term);
            }

            if (term.getValue() == -2){
                if (parcount > 0){
                    base.add(variables.get(variableIndex));
                    variableIndex++;
                }
                parcount--;

            }
        }


        this.fact = base;
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
        return str;
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
