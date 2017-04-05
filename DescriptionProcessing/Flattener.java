package DescriptionProcessing;

import GDLTokens.KeyWordToken;
import GDLTokens.Token;
import DeductiveDatabase.Description;
import DeductiveDatabase.Fact;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *  This class takes a description for a game containing variables as an input
 *  it then creates an equivalent ground description (no variables)
 */
public class Flattener {

    private HashMap<String, ArrayList<Description>> instantiations;
    private HashMap<String, ArrayList<Description>> templates;

    private ArrayList<Description> descriptionList;

    public Flattener(ArrayList<Description> description) {
        this.descriptionList = description;
    }

    //flattens the description
    public ArrayList<Description> flatten() {
        templates = findTemplates(descriptionList);
        instantiations = initializeInstantiations(descriptionList);

        ArrayList<Description> flatDescription = new ArrayList<>();
        flatDescription.addAll(getInstantiations("init"));

        for ( String key : templates.keySet() ) {
            if (!key.equals("init"))
                flatDescription.addAll(getInstantiations(key));
        }

        return flatDescription;
    }

    //makes a description to represent the doing of every possible legal move
    private Description equivalentDoesRule(Description legalRule){

        ArrayList<Token> body = new ArrayList<>();
        for (Token token : legalRule.getDescription()){
            if (token.getID().equals("legal"))
                body.add(new KeyWordToken("does"));

            else
                body.add(token.copy());
        }
        return new Description(body);
    }

    //gets all the possible instantiations of each description containing variables
    private ArrayList<Description> getInstantiations(String constant) {

        if ( !instantiations.containsKey(constant) ) {
            instantiations.put(constant, new ArrayList<>());

            if ( constant.equals("does") ) {
                for ( Description rule : getInstantiations("legal") ) {
                    instantiations.get(constant).add(equivalentDoesRule(rule));
                }
            }

            else {
                for ( Description template : templates.get(constant) ) {
                    ArrayList<Description> results = new ArrayList<>();
                    instantiate(template, 1, new Substitution(), results);
                    instantiations.get(constant).addAll(results);
                }
            }
        }
        return instantiations.get(constant);
    }

    //gets the domains of all the variables specified in the base propositions
    private HashMap<String, ArrayList<Description>> initializeInstantiations(ArrayList<Description> descriptionList) {

        ArrayList<Description> trues = new ArrayList<>();
        for ( Description description : descriptionList ){

            if ( description.getLeadAtom().toString().equals("base") ) {
                ArrayList<Description> results = new ArrayList<>();
                expand(description, 1, new ArrayList<>(), results);
                trues.addAll(results);
            }

        }
        HashMap<String, ArrayList<Description>> instantiations = new HashMap<>();
        instantiations.put("true", trues);

        return instantiations;
    }
/**
 * Recursive method that uses the base propositions rule to build a set instantiations.
 * returns a list of every possible combination of values from the base proposition
 *
 * @param baseProp
 * the base proposition being used.
 * @param index
 * The index of the rule being considered.
 * @param currentInstantiations
 * the list of instatiations used so far
 * @param results
 * The results built up so far
 */
    private void expand(Description baseProp, int index, ArrayList<Token> currentInstantiations, ArrayList<Description> results) {

        if ( baseProp.getArity() == index ) {
            results.add(new Description(baseProp, currentInstantiations));
        }

        else {
            for ( Token term : ( baseProp.getFacts().get(index).getFact())){
                if(term.getValue() != -1 && term.getValue() != -2) {
                    currentInstantiations.add(term);
                    expand(baseProp, index + 1, currentInstantiations, results);
                    currentInstantiations.remove(currentInstantiations.size() - 1);
                }
            }
        }
    }

/**
 * A helper recursive method for generating every possible instantiation of a rule.
 * every combination of variable values in the rule are tried so long as they dont conflict
 * @param rule
 * The rule to instantiate.
 * @param index
 * The atom in the rule being worked on
 * @param substitution
 * The substitutions built up so far.
 * @param results
 * The list of results built up so far.
 */
    private void instantiate(Description rule, int index, Substitution substitution, ArrayList<Description> results) {
        Substituter sub = new Substituter();
        Merger merger = new Merger();

        if ( rule.getFacts().size() == index ) {
            Description d = sub.substitute(rule, substitution);
            boolean old = false;
            for (Description desc: results){
                if (desc.toString().equals(d.toString()))
                    old = true;
            }

            if (!old)
                results.add(d);

        }
        else {
            String check = rule.getFacts().get(index).getLeadAtom().getID();

            if (!check.equals("distinct") && !check.equals("not") && !check.equals("data")){
                Fact sentence =  sub.substitute(new Fact(rule.getFacts().get(index)), substitution);

                for ( Description instantiation : getInstantiations(sentence.getLeadAtom().getID()) ) {

                    Substitution substitutionPrime = merger.merge(sentence, instantiation);
                    if ( substitutionPrime!=null ) {
                        Substitution substitutionCopy = substitution.copy();
                        substitutionCopy = substitutionCopy.build(substitutionPrime);
                        instantiate(rule, index + 1, substitutionCopy, results);
                    }
                }
            }

            else {

                if (check.equals("not")) {

                    KeyWordToken t = (KeyWordToken) rule.getFacts().get(index).getLeadAtom();
                    t.setId("true");
                    Fact sentence =  sub.substitute(new Fact(rule.getFacts().get(index)), substitution);

                    for ( Description instantiation : getInstantiations(sentence.getLeadAtom().getID()) ) {
                        t.setId("not");
                        Substitution substitutionPrime = merger.merge(sentence, instantiation);
                        if (substitutionPrime != null) {
                            Substitution substitutionCopy = substitution.copy();
                            substitutionCopy = substitutionCopy.build(substitutionPrime);
                            check = "";
                            instantiate(rule, index + 1, substitutionCopy, results);
                        }
                    }
                    if (check.equals("not"))
                        instantiate(rule, index + 1, substitution, results);
                }

                else
                    instantiate(rule, index + 1, substitution, results);

                }
        }
    }

    private HashMap<String, ArrayList<Description>> findTemplates(ArrayList<Description> descriptionList){

        HashMap<String, ArrayList<Description>> rules = new HashMap<>();
        for ( Description description : descriptionList ) {

            if ( !description.getLeadAtom().getID().equals("base") ) {

                if ( !rules.containsKey(description.getLeadAtom().getID()) ){
                    rules.put(description.getLeadAtom().getID(), new ArrayList<>());
                }
                rules.get(description.getLeadAtom().getID()).add(description);
            }
        }

        return rules;
    }
}