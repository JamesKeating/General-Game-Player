package DescriptionProcessing;

import GDLTokens.KeyWordToken;
import GDLTokens.Token;
import DeductiveDatabase.Description;
import DeductiveDatabase.DescriptionTable;
import DeductiveDatabase.Fact;

import java.util.ArrayList;
import java.util.HashMap;

public class Flattener
{

    private HashMap<String, ArrayList<Description>> instantiations;
    private HashMap<String, ArrayList<Description>> templates;


    /**
     * Construct a BasicPropNetFlattener for a given game.
     *
     * @param description A game description.
     */
    private ArrayList<Description> descriptionList;
    private DescriptionTable descriptionTable;

    public Flattener(ArrayList<Description> description) {
        this.descriptionList = description;
    }

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

    private Description equivalentDoesRule(Description legalRule){
        ArrayList<Token> body = new ArrayList<>();
        for (Token token : legalRule.getDescription()){
            if (token.getID().equals("legal")){
                body.add(new KeyWordToken("does"));
            }
            else
                body.add(token.copy());
        }
        return new Description(body);
    }


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


    private HashMap<String, ArrayList<Description>> initializeInstantiations(ArrayList<Description> descriptionList) {

        ArrayList<Description> trues = new ArrayList<>();
        for ( Description description : descriptionList ){

            if ( description.getLeadAtom().toString().equals("base") ) {
                ArrayList<Description> results = new ArrayList<>();
                expandTrue(description, 1, new ArrayList<>(), results);
                trues.addAll(results);
            }

        }
        HashMap<String, ArrayList<Description>> instantiations = new HashMap<>();
        instantiations.put("true", trues);

        return instantiations;
    }


    private void expandTrue(Description base, int index, ArrayList<Token> workingSet, ArrayList<Description> results) {

        if ( base.getArity() == index ) {
            ArrayList<Token> body = new ArrayList<>(workingSet);
            results.add(new Description(base, workingSet));
        }

        else {
            for ( Token term : ( base.getFacts().get(index).getFact())){
                if(term.getValue() != -1 && term.getValue() != -2) {
                    workingSet.add(term);
                    expandTrue(base, index + 1, workingSet, results);
                    workingSet.remove(workingSet.size() - 1);
                }
            }
        }
    }

    private void instantiate(Description template, int index, Substitution theta, ArrayList<Description> results) {
        Substituter sub = new Substituter();
        Unifier uni = new Unifier();

        if ( template.getFacts().size() == index ) {
            Description d = sub.substitute(template, theta);
            boolean old = false;
            for (Description desc: results){
                if (desc.toString().equals(d.toString()))
                    old = true;
            }

            if (!old)
                results.add(d);

        }
        else {
            String check = template.getFacts().get(index).getLeadAtom().getID();

            if (!check.equals("distinct") && !check.equals("not") && !check.equals("data")){
                Fact sentence =  sub.substitute(new Fact(template.getFacts().get(index)), theta);

                for ( Description instantiation : getInstantiations(sentence.getLeadAtom().getID()) ) {

                    Substitution thetaPrime = uni.unify(sentence, instantiation);
                    if ( thetaPrime!=null ) {
                        Substitution thetaCopy = theta.copy();
                        thetaCopy = thetaCopy.compose(thetaPrime);
                        instantiate(template, index + 1, thetaCopy, results);
                    }
                }
            }

            else {

                if (check.equals("not")) {

                    KeyWordToken t = (KeyWordToken) template.getFacts().get(index).getLeadAtom();
                    t.setId("true");
                    Fact sentence =  sub.substitute(new Fact(template.getFacts().get(index)), theta);

                    for ( Description instantiation : getInstantiations(sentence.getLeadAtom().getID()) ) {
                        t.setId("not");
                        Substitution thetaPrime = uni.unify(sentence, instantiation);
                        if (thetaPrime != null) {
                            Substitution thetaCopy = theta.copy();
                            thetaCopy = thetaCopy.compose(thetaPrime);
                            check = "";
                            instantiate(template, index + 1, thetaCopy, results);
                        }
                    }
                    if (check.equals("not"))
                        instantiate(template, index + 1, theta, results);
                }

                else
                    instantiate(template, index + 1, theta, results);

                }
        }
    }

    private HashMap<String, ArrayList<Description>> findTemplates(ArrayList<Description> descriptionList){

        HashMap<String, ArrayList<Description>> templates = new HashMap<>();
        for ( Description description : descriptionList ) {


            if ( !description.getLeadAtom().getID().equals("base") ) {

                if ( !templates.containsKey(description.getLeadAtom().getID()) ){//string?
                    templates.put(description.getLeadAtom().getID(), new ArrayList<>());
                }
                templates.get(description.getLeadAtom().getID()).add(description);
            }
        }

        return templates;
    }
}