package DescriptionProcessing;

/**
 * Created by siavj on 09/01/2017.
 */

import DescriptionProcessing.PropNetComponents.*;
import GDLTokens.*;
import DeductiveDatabase.Description;
import DeductiveDatabase.Fact;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class PropNetBuilder
{


    private ConcurrentHashMap<String, Latch> propositions;
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

        propositions = new ConcurrentHashMap<>();
        propNetNodes = new HashSet<>();
        for ( Description rule : description ) {

            if ( rule.getArity() > 1 )
                convertRule(rule);

            else
                convertStatic(rule.getFacts().get(0));

        }
        fixDisjunctions();
        fixNegation();
        return new PropNet(players, propNetNodes);
    }


    private void fixNegation() {

        Iterator<Latch> iterator = propositions.values().iterator();
        Latch latch = null;

            while(iterator.hasNext()){
              latch = iterator.next();

            if (latch.getLabel().getLeadAtom().getID().equals("true") && latch.getNodeInputs().size() == 0) {
                latch.getLabel().getFact().remove(0);
                latch.getLabel().getFact().remove(0);
                latch.getLabel().getFact().remove(latch.getLabel().getFact().size() - 1);
                link(getProposition(latch.getLabel()), latch);

            }
        }
    }


    private Latch convertConjunct(Fact fact) {

        ArrayList<Token> anon = new ArrayList<>();
        anon.add(new IdToken("anon"));
        if ( fact.getLeadAtom().getID().equals("distinct") ) {


            Latch latch = new Latch(new Fact(anon));
            Constant constant = new Constant(!fact.getFact().get(2).getID().equals(fact.getFact().get(3).getID()));

            link(constant, latch);

            propNetNodes.add(latch);
            propNetNodes.add(constant);

            return latch;
        }

        else if (fact.getLeadAtom().getID().equals("data")){
            Latch latch = new Latch(new Fact(anon));
            Constant constant;
            fact.getFact().remove(0);
            fact.getFact().remove(0);
            fact.getFact().remove(fact.getFact().size() -1);

            link(getProposition(fact), latch);

            propNetNodes.add(latch);

            return latch;
        }

        else if ( fact.getLeadAtom().getID().equals("not") ) {
            ArrayList<Token> inverse = new ArrayList<>();
            Fact inv;

            if(!fact.getFact().get(3).getID().equals("distinct")) {
                inverse.add(new LparToken());
                inverse.add(new KeyWordToken("true"));
                for (int i = 2; i < fact.getFact().size() - 1; i++) {
                    inverse.add(fact.getFact().get(i).copy());
                }
                inverse.add(new RparToken());
            }

            else {
                for (int i = 2; i < fact.getFact().size() - 1; i++) {
                    inverse.add(fact.getFact().get(i).copy());
                }
            }

            inv = new Fact(inverse) ;
            Latch input = convertConjunct(inv);
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
            Latch proposition = getProposition(fact);
            propNetNodes.add(proposition);
            return proposition;
        }
    }

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
            link(transition, head);

            propNetNodes.add(head);
            propNetNodes.add(transition);
            propNetNodes.add(preTransition);

            return preTransition;
        }

        else {

            Latch proposition = getProposition(sentence);
            propNetNodes.add(proposition);

            return proposition;
        }
    }

    private void convertRule(Description rule) {
        Latch head = convertHead(rule.getFacts().get(0));
        AndGate and = new AndGate();

        link(and, head);


        propNetNodes.add(head);
        propNetNodes.add(and);

        for ( int i = 1; i < rule.getFacts().size(); i++ ) {
            Fact literal = rule.getFacts().get(i);
            Latch conjunct = convertConjunct(literal);

            link(conjunct, and);

        }
    }

    private void convertStatic(Fact sentence) {

        if ( sentence.getLeadAtom().getID().equals("init") ) {

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

            propNetNodes.add(init);
            propNetNodes.add(transition);
            propNetNodes.add(proposition);
        }

        Constant constant = new Constant(true);

        Latch proposition = getProposition(sentence);

        link(constant, proposition);

        propNetNodes.add(constant);
        propNetNodes.add(proposition);
    }

    private void fixDisjunctions() {

        ArrayList<Latch> fixList = new ArrayList<>();


        for ( Latch latch : propositions.values() ) {
            if ( latch.getNodeInputs().size() > 1 )
                fixList.add(latch);
        }

        for ( Latch fixItem : fixList ) {
            OrGate or = new OrGate();
            int i = 0;

            for ( PropNetNode input : fixItem.getNodeInputs() ) {
                i++;

                Latch disjunct = null;
                ArrayList<Token> anon = new ArrayList<>();

                if ( fixItem.getLabel().getFact().size() == 1 ) {

                    anon.add(new IdToken(fixItem.getLabel().toString() + "-" + i));
                    disjunct = new Latch(new Fact(anon));

                }
                else {

                    for (Token token : fixItem.getLabel().getFact()){

                        if (token.getID().equals(fixItem.getLabel().getLeadAtom().getID()))
                            anon.add(new IdToken(fixItem.getLabel().getLeadAtom().toString() +"-" + i));

                        else
                            anon.add(token.copy());
                    }
                    disjunct = new Latch(new Fact(anon));
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


    private Latch getProposition(Fact sentence) {

        if ( !propositions.containsKey(sentence.toString()) ) {
            propositions.put(sentence.toString(), new Latch(sentence));
        }

        return propositions.get(sentence.toString());
    }


    private void link(PropNetNode source, PropNetNode target) {
        source.addOutput(target);
        target.addInput(source);
    }

}