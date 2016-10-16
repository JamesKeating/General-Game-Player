package SylmbolTable;

import GDLTokens.Token;

import java.util.ArrayList;

/**
 * Created by siavj on 16/10/2016.
 */
public class Description {
    private ArrayList<Token> description = new ArrayList<>();

    public Description(ArrayList<Token> description){
        this.description = description;
    }

    public int getArity(){
        int parCount = 0, arity = -1;
        for(Token tok : description){
            if(tok.getValue() == -1)
                parCount +=1;
            else if(tok.getValue() == -2) {
                parCount -= 1;
                if (parCount == 0)
                    return arity;
                arity +=1;
            }
            else if(parCount == 1)
                arity += 1;

        }
        return arity;
    }

    public Token getLeadAtom(){
        for(Token tok : description){
            if(tok.getValue() == -3) {
                return tok;
            }
        }
        return null;
    }

    public String toString(){
        String str = "";
        for (Token tok : description){
            str += tok.toString() + ", ";
        }
        return str + "\n";

    }
}
