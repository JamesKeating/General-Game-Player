package SylmbolTable;

import GDLTokens.Token;
import GDLTokens.VarToken;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by siavj on 16/10/2016.
 */
public class Description {

    private ArrayList<Token> description = new ArrayList<>();
    private ArrayList<Fact> facts = new ArrayList<>();

    public Description(ArrayList<Token> description){
        this.description = description;
        getArity();
    }



    public int getArity(){
        this.facts.clear();
        int parCount = 0, arity = 0, factStart = 2, factEnd = 0;
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

    public Token getLeadAtom(){
        for(Token tok : this.description){
            if(tok.getValue() == -3) {
                return tok;
            }
        }
        return null;
    }

    public String toString(){
        return this.description.toString() + "\n";
    }


    public ArrayList<Token> getDescription() {
        return this.description;
    }

    public ArrayList<Fact> getFacts() {
        return this.facts;
    }

    public HashMap<String, ArrayList<String>> getVar(){
        HashMap<String, ArrayList<String>> vars = new HashMap<>();
        for(Token t : this.description) {
            if (t.getClass() == VarToken.class)
                vars.putIfAbsent(t.getID(),new ArrayList<String>());
        }
        return vars;
    }
}
