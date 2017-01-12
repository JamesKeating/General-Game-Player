package DescriptionProcessing;

/**
 * Created by siavj on 09/01/2017.
 */

import DescriptionProcessing.PropNetComponents.*;
import GDLTokens.IdToken;
import GDLTokens.KeyWordToken;
import GDLTokens.Token;
import SylmbolTable.Description;
import SylmbolTable.DescriptionTable;
import SylmbolTable.Fact;

import java.util.*;

public final class PropNetBuilder
{

    /** An archive of Propositions, indexed by name. */
    private Map<String, Latch> propositions;
    /** An archive of Components. */
    private HashSet<PropNetNode> propNetNodes;

    public PropNetBuilder() {
    }


    public PropNet create(ArrayList<Description> gameDescription) {
        try {
            ArrayList<Description> flattenedGameDescription = new Flattener(gameDescription).flatten();
            return convert(Player.listPlayers(gameDescription), flattenedGameDescription);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    private PropNet convert(ArrayList<Player> players, ArrayList<Description> description) {

        propositions = new HashMap<>();
        propNetNodes = new HashSet<>();

        //System.out.println(description.size());
        int i =0;
        for ( Description rule : description ) {
            i++;

//            System.out.println(rule + "first");
            if ( rule.getArity() > 1 ) {
                //System.out.println("check 1");
                convertRule(rule);
            }
            else {
//                System.out.println(rule + "2nd");
                convertStatic(rule.getFacts().get(0));
            }
//            if (i == 20){
////                System.out.println(propNetNodes);
////                System.out.println(propNetNodes.size());
//                System.exit(0);
//            }
//            System.exit(0);
        }
        //System.out.println(propNetNodes.size());
//        System.out.println(propNetNodes);
        fixDisjunctions();
        addMissingInputs();

        //System.out.println(propNetNodes.size());
        return new PropNet(players, propNetNodes);
    }

    /**
     * Creates an equivalent InputProposition for every LegalProposition where
     * none already exists.
     */
    private void addMissingInputs() {

        ArrayList<Latch> addList = new ArrayList<>();
        for ( Latch latch : propositions.values() ) {

//            System.out.println(latch.getLabel());
            if ( latch.getLabel().getFact().size() > 1 ) {

                if ( latch.getLabel().getLeadAtom().getID().equals("legal") )
                {
//                    System.out.println(latch.getLabel());
                    addList.add(latch);
                }
            }
        }


        for ( Latch addItem : addList ) {
            Fact relation = addItem.getLabel();

            ArrayList<Token> body = new ArrayList<>();
            for (Token token : relation.getFact()){
                if (token.getID().equals("legal")){
                    body.add(new KeyWordToken("does"));
                }
                else
                    body.add(token.copy());
            }
            if (body.size() > 0)
                propNetNodes.add(getProposition(new Fact(body)));
        }





    }

    /**
     * Converts a literal to equivalent PropNet Components and returns a
     * reference to the last of those components.
     *
     * param literal
     *            The literal to convert to equivalent PropNet Components.
     * @return The last of those components.
     */
    private Latch convertConjunct(Fact fact) {

//        System.out.println(fact);
        ArrayList<Token> anon = new ArrayList<>();
        anon.add(new IdToken("anon"));

        if ( fact.getLeadAtom().getID().equals("distinct") ) {


            Latch latch = new Latch(new Fact(anon));
            Constant constant = new Constant(!fact.getFact().get(2).equals(fact.getFact().get(3)));

            link(constant, latch);

            propNetNodes.add(latch);
            propNetNodes.add(constant);

            return latch;
        }

        else if ( fact.getLeadAtom().getID().equals("not") ) {
            ArrayList<Token> inverse = new ArrayList<>();

            for (Token token : fact.getFact()){
                if (!token.getID().equals("not"))//possibly remove brackets as well?
                    inverse.add(token);
            }

            Latch input = convertConjunct(new Fact(inverse));
            NotGate no = new NotGate();
            Latch output = new Latch(new Fact(anon));

            link(input, no);
            link(no, output);

            propNetNodes.add(input);
            propNetNodes.add(no);
            propNetNodes.add(output);

            return output;
        }
        else {
//            System.out.println(fact + "---sentence");

            Latch proposition = getProposition(fact);

//            System.out.println(proposition.getNodeOutputs() + "---PROP");
            propNetNodes.add(proposition);

            return proposition;
        }
    }

    /**
     * Converts a sentence to equivalent PropNet Components and returns the
     * first of those components.
     *
     * @param sentence
     *            The sentence to convert to equivalent PropNet Components.
     * @return The first of those Components.
     */
    private Latch convertHead(Fact sentence) {
        if ( sentence.getLeadAtom().getID().equals("next") ) {
            ArrayList<Token> tru = new ArrayList<>();
            for (Token token : sentence.getFact()){
                if (token == sentence.getLeadAtom())
                    tru.add(new KeyWordToken("true"));

                else
                    tru.add(token.copy());
            }

            Latch head = getProposition(new Fact(tru));
            Transition transition = new Transition();
            ArrayList<Token> anon = new ArrayList<>();
            anon.add(new IdToken("anon"));
            Latch preTransition = new Latch(new Fact(anon));

            link(preTransition, transition);
            link(transition, head);//sen to head

            propNetNodes.add(head);
            propNetNodes.add(transition);
            propNetNodes.add(preTransition);

            return preTransition;
        }
        else
        {

            Latch proposition = getProposition(sentence);
            propNetNodes.add(proposition);

            return proposition;
        }
    }

    /**
     * Converts a rule into equivalent PropNet Components by invoking the
     * <tt>convertHead()</tt> method on the head, and the
     * <tt>convertConjunct</tt> method on every literal in the body and
     * joining the results by an and gate.
     *
     * @param rule
     *            The rule to convert.
     */
    private void convertRule(Description rule)
    {
        Latch head = convertHead(rule.getFacts().get(0));
        AndGate and = new AndGate();

        link(and, head);

//        System.out.println(head);
//        System.out.println(and);


        propNetNodes.add(head);
        propNetNodes.add(and);

        for ( int i = 1; i < rule.getFacts().size(); i++ ) {
            Fact literal = rule.getFacts().get(i);
            Latch conjunct = convertConjunct(literal);

//            System.out.println(literal + "-------literal");
//            System.out.println(conjunct + "-------conjunct");
            link(conjunct, and);
//            System.out.println(conjunct + "-------conjunct after");
//            System.out.println(and + "-------and");
        }
    }

    /**
     * Converts a sentence to equivalent PropNet Components.
     *
     * @param sentence
     *            The sentence to convert to equivalent PropNet Components.
     */
    private void convertStatic(Fact sentence) {

//        System.out.println("\n============\n");
//        System.out.println(sentence + " static");
        if ( sentence.getLeadAtom().getID().equals("init") )
        {
//            System.out.println(sentence + " init");
            ArrayList<Token> initConst = new ArrayList<>();
            initConst.add(new IdToken("init"));

            Latch init = getProposition(new Fact(initConst));
            Transition transition = new Transition();
            ArrayList<Token> initTrue = new ArrayList<>();
            for (Token token : sentence.getFact()){
                if (token == sentence.getLeadAtom())
                    initTrue.add(new KeyWordToken("true"));
                else
                    initTrue.add(token.copy());
            }

            Latch proposition = getProposition(new Fact(initTrue));


            link(init, transition);
            link(transition, proposition);

            System.out.println("init: "+ init + " trans: " + transition + " prop: " + proposition);
//            System.out.println(transition.getNodeInputs() + "--- " + transition.getNodeOutputs() + "\n\n");
            propNetNodes.add(init);
            propNetNodes.add(transition);
            propNetNodes.add(proposition);
        }

        Constant constant = new Constant(true);

        Latch proposition = getProposition(sentence);
//        System.out.println(constant);
        //System.out.println(proposition);
        link(constant, proposition);



        propNetNodes.add(constant);
        propNetNodes.add(proposition);
    }

    /**
     * Creates an or gate to combine the inputs to a Proposition wherever one
     * has more than one input.
     */
    private void fixDisjunctions() {

        ArrayList<Latch> fixList = new ArrayList<>();


        for ( Latch latch : propositions.values() ) {
//            System.out.println(latch);
            if ( latch.getNodeInputs().size() > 1 ) {
                fixList.add(latch);

            }
        }

        for ( Latch fixItem : fixList ) {
            OrGate or = new OrGate();
            int i = 0;

            for ( PropNetNode input : fixItem.getNodeInputs() ) {
                i++;

                Latch disjunct = null;
                ArrayList<Token> anon = new ArrayList<>();

                if ( fixItem.getLabel().getFact().size() == 1 ) {//this is wrong as hell

                    anon.add(new IdToken(fixItem.getLabel().toString() + "-" + i));
                    disjunct = new Latch(new Fact(anon));
//                    System.out.println(disjunct);
                }
                else {

                    for (Token token : fixItem.getLabel().getFact()){

                        if (token.getID().equals(fixItem.getLabel().getLeadAtom().getID()))
                            anon.add(new IdToken(fixItem.getLabel().getLeadAtom().toString() +"-" + i));

                        else
                            anon.add(token.copy());
                    }


                    disjunct = new Latch(new Fact(anon));
//                    System.out.println(disjunct);
                }

                input.getNodeOutputs().clear();

                link(input, disjunct);
                link(disjunct, or);


                propNetNodes.add(disjunct);
            }

            fixItem.getNodeInputs().clear();
            link(or, fixItem);


            propNetNodes.add(or);
        }
    }

    /**
     * Returns a Proposition with name <tt>term</tt>, creating one if none
     * already exists.
     *
     * @param sentence
     *            The name of the Proposition.
     * @return A Proposition with name <tt>term</tt>.
     */
    private Latch getProposition(Fact sentence)
    {
//        System.out.println(sentence + "--key");
        if ( !propositions.containsKey(sentence.toString()) )
        {
            propositions.put(sentence.toString(), new Latch(sentence));
        }


        return propositions.get(sentence.toString());
    }

    /**
     * Adds inputs and outputs to <tt>source</tt> and <tt>target</tt> such
     * that <tt>source becomes an input to <tt>target</tt>.
     * @param source A component.
     * @param target A second component.
     */
    private void link(PropNetNode source, PropNetNode target)
    {
        source.addOutput(target);
        target.addInput(source);
    }


}