package DescriptionProcessing;

import GDLTokens.ImplicationToken;
import GDLTokens.Token;
import GDLTokens.VarToken;
import SylmbolTable.Description;
import SylmbolTable.Fact;

import java.util.ArrayList;

/**
 * Created by siavj on 10/01/2017.
 */
public class Unifier
{

    public Substitution unify(Fact x, Description y) {
        Substitution theta = new Substitution();
        boolean isGood;

        if(y.getDescription().get(1) instanceof ImplicationToken){
            isGood = unifyTerm(x.getFact(), y.getFacts().get(0).getFact(), theta);
        }

        else
            isGood = unifyTerm(x.getFact(), y.getDescription(), theta);

        if(isGood)
            return theta;
        else
            return null;
    }

    private boolean unifyTerm(ArrayList<Token> x, ArrayList<Token> y, Substitution theta) {

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
            //System.out.println(xTerm + " -=-" + yTerm);/
            if (!(xTerm instanceof VarToken) && !(yTerm instanceof VarToken)) {
                if (!xTerm.toString().equals(yTerm.toString())) {
                    return false;
                }
            }

            else if (xTerm instanceof VarToken) {
                if (!unifyVariable( xTerm, yTerm, theta))
                    return false;
            }

            else{
                if (!unifyVariable( yTerm, xTerm, theta))
                    return false;
            }

            //not sure
            if((xTerm instanceof VarToken) && (yTerm instanceof VarToken)){
                return false;
            }
        }

        return true;
    }

    private boolean unifyVariable(Token var, Token token, Substitution theta) {

        ArrayList<Token> z = new ArrayList<>();
        ArrayList<Token> w = new ArrayList<>();
        ArrayList<Token> v = new ArrayList<>();
        z.add(theta.get(var.getID()));
        w.add(token);

        if (theta.contains(var.getID())) {
            return unifyTerm(z, w, theta);
        }

        else if ((token instanceof VarToken) && theta.contains(token.getID())){
            v.add(theta.get(token.getID()));
            return unifyTerm(z, v, theta);
        }

        else {
            theta.put(var.getID(), token);
            return true;
        }
    }

}