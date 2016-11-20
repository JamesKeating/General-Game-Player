package SylmbolTable;

import GDLTokens.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by siavj on 30/10/2016.
 */
public class FactTable {

    public int size() {
        return facts.size();
    }

    private ArrayList<Fact> facts = new ArrayList<>();

    public void addFact(Fact fact){
        this.facts.add(fact);

    }

    public boolean searchFactTable(Fact fact){
        for (Fact f1 : this.facts){
            Iterator<Token> it1 = fact.getFact().iterator();
            Iterator<Token> it2 = f1.getFact().iterator();

            while (it1.hasNext() && it2.hasNext()) {
                if(!it1.next().getID().equals(it2.next().getID())){
                    break;
                }
            }
            if (!it1.hasNext() && !it2.hasNext())
                return true;

        }
        return false;
    }

    public HashMap<String, ArrayList<String>> searchFactTableV(HashMap<String, ArrayList<String>> varMap,  Fact fact){
        Token passed, stored;
        Iterator<Token> itPassed, itStored;
        HashMap<String, ArrayList<String>> temp = varMap;
        HashMap<String, ArrayList<String>> temp2 = varMap;

        for (Fact f1 : this.facts){
            itPassed = fact.getFact().iterator();
            itStored = f1.getFact().iterator();

            while (itPassed.hasNext() && itStored.hasNext()) {
                passed = itPassed.next();
                stored = itStored.next();

                if(!passed.getID().equals(stored.getID())){
                    if (passed.toString().equals("<Var>") && stored.toString().equals("<ID>"))
                        temp.get(passed.getID()).add(stored.getID());

                    else {
                        temp = varMap;
                        break;
                    }
                }
            }
            if (!itPassed.hasNext() && !itStored.hasNext())
                varMap = temp;

        }

        //if a variable already has values assigned from previous fact
        for (String key: varMap.keySet()) {
            if (temp2.get(key).size() > 0) {
                for (String value : varMap.get(key))
                    if (!temp2.get(key).contains(value))
                        varMap.get(key).remove(value);
            }
        }
        return varMap;
    }

    public String toString(){
        return this.facts.toString();
    }

}
