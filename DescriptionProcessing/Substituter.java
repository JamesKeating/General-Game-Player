package DescriptionProcessing;

import GDLTokens.*;
import DeductiveDatabase.Description;
import DeductiveDatabase.Fact;

import java.util.ArrayList;

/**
 * Created by siavj on 10/01/2017.
 */

/**
 * Helps the flattener remove variables from a description
 * substitutes values for each variable with values that have not already been substituted
 */
public class Substituter {

    public Substituter() {}

    public Fact substitute(Fact fact, Substitution substitution) {
        return substituteFact(fact, substitution);
    }

    public Description substitute(Description description, Substitution substitution) {
        return substituteDescription(description, substitution);
    }

    private Fact substituteFact(Fact function, Substitution substitution) {
        if (function.isGround())
            return function;

        else {
            ArrayList<Token> body = new ArrayList<>();

            for (int i = 0; i < function.getFact().size(); i++) {
                body.add(substituteTerm(function.getFact().get(i), substitution));
            }

            return new Fact(body);
        }
    }

    private Token substituteTerm(Token term, Substitution substitution) {
        if (term instanceof VarToken)
            return substituteVariable( (VarToken) term, substitution);

        return term;
    }

    private Token substituteVariable(VarToken variable, Substitution substitution) {

        if (!substitution.contains(variable.getID()))
            return variable;

        else {
            Token result = substitution.get(variable.getID());
            Token betterResult;

            while (!(betterResult = substituteTerm(result, substitution)).equals(result)) {
                result = betterResult;
            }

            substitution.put(variable.getID(), result);
            return result;
        }
    }

    private Description substituteDescription(Description rule, Substitution substitution) {
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
                body.add(substituteFact(literal, substitution));
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