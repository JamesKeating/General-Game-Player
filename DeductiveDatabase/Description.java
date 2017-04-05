package DeductiveDatabase;

import GDLTokens.*;

import java.util.ArrayList;

/**
 * Created by siavj on 16/10/2016.
 */

/**
 * Stores a single description from the game definition
 * eg. (<= (win)(true (made_good_move x)))
 * A description is made up of a collection of facts
 */

public class Description {

    private ArrayList<Token> description = new ArrayList<>();
    private ArrayList<Fact> facts = new ArrayList<>();

    public Description(ArrayList<Token> description){
        this.description = description;
        getArity();
    }

    public Description(Fact fact){
        for (Token token : fact.getFact()){
            this.description.add(token);
        }
        getArity();
    }

    public Description(Description baseDescription, ArrayList<Token> variables){

        ArrayList<Token> base = new ArrayList<>();
        int parcount = -1;
        int variableIndex = 0;

        for (Token term : baseDescription.getDescription()){

            if (term.getValue() == -1)
                parcount++;

            if (!term.getID().equals("base") && parcount < 1)
                base.add(term);

            if (term.getValue() == -2){
                if (parcount > 0){
                    base.add(variables.get(variableIndex));
                    variableIndex++;
                }
                parcount--;
            }
        }

        this.description = base;
        getArity();
    }

    /**
     * @return the number of facts in the description
     */
    public int getArity(){

        int parCount = 0, arity = 0, factStart = 2, factEnd = 0;
        this.facts.clear();

        for(Token tok : this.description){
            factEnd++;

            if(tok.getValue() == -1)
                parCount +=1;

            else if(tok.getValue() == -2) {
                parCount -= 1;
                if (parCount == 0)
                    return arity;

                else if(parCount == 1){
                    this.facts.add(new Fact(new ArrayList<>(this.description.subList(factStart, factEnd))));
                    factStart = factEnd;
                    arity +=1;
                }
            }

            else if(parCount == 1 && factEnd > 2){
                this.facts.add(new Fact(new ArrayList<>(description.subList(factStart, factEnd))));
                factStart = factEnd;
                arity += 1;
            }
        }
        return arity;
    }

    /**
     * @return the first significant atom in the description
     */
    public Token getLeadAtom(){
        for(Token tok : this.description){
            if(tok.getValue() == -3 && !tok.getID().equals("<="))
                return tok;
        }
        return null;
    }

    public String toString(){
        return this.description.toString().replace(",", "").replace("[", "").replace("]", "");
    }

    public ArrayList<Token> getDescription() {
        return this.description;
    }

    public ArrayList<Fact> getFacts() {
        return this.facts;
    }
}
