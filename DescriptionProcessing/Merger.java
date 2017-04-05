package DescriptionProcessing;

import GDLTokens.ImplicationToken;
import GDLTokens.Token;
import GDLTokens.VarToken;
import DeductiveDatabase.Description;
import DeductiveDatabase.Fact;

import java.util.ArrayList;

/**
 * Created by siavj on 10/01/2017.
 */

public class Merger
{

    public Substitution merge(Fact x, Description y) {
        Substitution theta = new Substitution();
        boolean isGood;

        if(y.getDescription().get(1) instanceof ImplicationToken){
            isGood = mergeAtom(x.getFact(), y.getFacts().get(0).getFact(), theta);
        }

        else
            isGood = mergeAtom(x.getFact(), y.getDescription(), theta);

        if(isGood){
            return theta;
        }
        else
            return null;
    }

    private boolean mergeAtom(ArrayList<Token> x, ArrayList<Token> y, Substitution theta) {

        if(x.equals(y))
            return true;

        Token yTerm, xTerm;
        int j = 0;
        if (x.get(1).getID().equals("true"))
            j = 3;
        for (int i = 0; i < x.size() - j; i++){

            try {
                xTerm = x.get(i);
                yTerm = y.get(i);


                if (x.get(1).getID().equals("true"))
                    xTerm = x.get(i + 2);

                if (y.get(1).getID().equals("true"))
                    yTerm = y.get(i + 2);
            }catch (Exception e){
                return false;
            }

            if (!(xTerm instanceof VarToken) && !(yTerm instanceof VarToken)) {
                if (!xTerm.toString().equals(yTerm.toString())) {
                    return false;
                }
            }

            else if (xTerm instanceof VarToken) {
                if (!mergeVariable( xTerm, yTerm, theta))
                    return false;
            }

            else{
                if (!mergeVariable( yTerm, xTerm, theta))
                    return false;
            }

            if((xTerm instanceof VarToken) && (yTerm instanceof VarToken)){
                return false;
            }
        }

        return true;
    }

    private boolean mergeVariable(Token var, Token token, Substitution substitution) {

        ArrayList<Token> z = new ArrayList<>();
        ArrayList<Token> w = new ArrayList<>();
        ArrayList<Token> v = new ArrayList<>();
        z.add(substitution.get(var.getID()));
        w.add(token);

        if (substitution.contains(var.getID())) {
            return mergeAtom(z, w, substitution);
        }

        else if ((token instanceof VarToken) && substitution.contains(token.getID())){
            v.add(substitution.get(token.getID()));
            return mergeAtom(z, v, substitution);
        }

        else {
            substitution.put(var.getID(), token);
            return true;
        }
    }

}