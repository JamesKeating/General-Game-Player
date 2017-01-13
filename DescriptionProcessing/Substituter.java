package DescriptionProcessing;

import GDLTokens.*;
import SylmbolTable.Description;
import SylmbolTable.Fact;

import java.util.AbstractList;
import java.util.ArrayList;

/**
 * Created by siavj on 10/01/2017.
 */
public class Substituter
{

    public Substituter() {
    }


    public Fact substitute(Fact fact, Substitution theta) {
        return substituteFact(fact, theta);
    }


    public Description substitute(Description description, Substitution theta) {
        return substituteDescription(description, theta);
    }

    public Token substitute(Token token, Substitution theta){
        return substituteTerm(token, theta);
    }

    private Fact substituteFact(Fact function, Substitution theta) {
        if (function.isGround()) {
            //System.out.println("3.1");
            return function;
        }

        else {
//            System.out.println("3.2");
            ArrayList<Token> body = new ArrayList<>();

            for (int i = 0; i < function.getFact().size(); i++) {
                body.add(substituteTerm(function.getFact().get(i), theta));
            }


            return new Fact(body);// GdlPool.getFunction(name, body);
        }
    }


    private Token substituteTerm(Token term, Substitution theta)
    {
        if (term instanceof VarToken) {
//            System.out.println("4= " + term);
            return substituteVariable( (VarToken) term, theta);
        }

        return term;

    }

    private Token substituteVariable(VarToken variable, Substitution theta) {

        if (!theta.contains(variable.getID())) {

//            System.out.println("5.1= +"+ theta+ " = " + variable);
            return variable;
        }

        else {

//            System.out.println("5.2");
            Token result = theta.get(variable.getID());
            Token betterResult = null;

//            System.out.println("222222222222222222" +result);
            while (!(betterResult = substituteTerm(result, theta)).equals(result)) {
                result = betterResult;
            }

            theta.put(variable.getID(), result);
//            System.out.println("how in the hell..." + result + "   " + variable);
            return result;
        }
    }

    private Description substituteDescription(Description rule, Substitution theta) {
//        System.out.println("2");

        ArrayList<Fact> body = new ArrayList<>();
        ArrayList<Token> result = new ArrayList<>();

        result.add(new LparToken());
        result.add(new ImplicationToken());
        if (rule.getLeadAtom().getID().equals("role") || rule.getLeadAtom().getID().equals("init")) {
            for (Token token : rule.getDescription()) {
                result.add(token);
            }
        }
        else {

            for (Fact literal : rule.getFacts()) {
                body.add(substituteFact(literal, theta));
            }

            for (Fact fact : body) {
                for (Token token : fact.getFact()) {
                    result.add(token);
                }
            }
        }
        result.add(new RparToken());
        return new Description(result);
    }
}