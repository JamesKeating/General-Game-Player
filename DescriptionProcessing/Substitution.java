package DescriptionProcessing;

import GDLTokens.Token;
import GDLTokens.VarToken;

import java.util.HashMap;

/**
 * Created by siavj on 10/01/2017.
 */

//keeps track of values which have been substituted for the substituter
public class Substitution {

    private HashMap<String, Token> contents;

    public Substitution() {
        contents = new HashMap<>();
    }

    public Substitution build(Substitution substitionPrime) {
        Substitution result = new Substitution();

        result.contents.putAll(contents);
        result.contents.putAll(substitionPrime.contents);

        return result;
    }

    public boolean contains(String variable) {
        return contents.containsKey(variable);
    }

    @Override
    public boolean equals(Object o) {

        if ((o != null) && (o instanceof Substitution)) {

            Substitution substitution = (Substitution) o;
            return substitution.contents.equals(contents);
        }

        return false;
    }

    public Token get(String variable) {
        return contents.get(variable);
    }

    @Override
    public int hashCode()
    {
        return contents.hashCode();
    }

    public void put(String variable, Token term)
    {
        contents.put(variable, term);
    }

    public Substitution copy() {
        Substitution copy = new Substitution();
        copy.contents.putAll(contents);
        return copy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{ ");
        for (String variable : contents.keySet()) {
            sb.append(variable + "/" + contents.get(variable) + " ");
        }
        sb.append("}");

        return sb.toString();
    }

}
