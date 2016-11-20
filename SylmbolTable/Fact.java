package SylmbolTable;

import GDLTokens.Token;

import java.util.ArrayList;

/**
 * Created by siavj on 30/10/2016.
 */
public class Fact {

    public ArrayList<Token> getFact() {
        return fact;
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



}
