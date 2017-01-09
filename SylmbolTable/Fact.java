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

}
