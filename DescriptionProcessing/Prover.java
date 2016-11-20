package DescriptionProcessing;

import SylmbolTable.Description;
import SylmbolTable.DescriptionTable;
import SylmbolTable.Fact;
import SylmbolTable.FactTable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by siavj on 30/10/2016.
 */
public class Prover {


    private FactTable ft;
    private ArrayList<Description> provable = new ArrayList<>();

    public void setDt(DescriptionTable dt) {
        for(Integer i :dt.getTable().get("<Implication>").keySet()){
            for (Description description : dt.getTable().get("<Implication>").get(i)){
                provable.add(description);
            }
        }
    }

    public void setFt(FactTable ft) {
        this.ft = ft;
    }

    public boolean proveFact(Fact fact){
        if (this.ft.searchFactTable(fact))
            return true;


        //checks recursion
        for (Description d: provable) {
            if(d.getFacts().get(0) == fact){
                if(!d.getFacts().subList(1,d.getFacts().size() -1).contains(fact)){
                    return proveRule(d);
                }
            }
        }

        return false;
    }


    public boolean proveRule(Description implication){
        if (!implication.getLeadAtom().toString().equals("<Implication>")){
            System.out.println("Cannot call on non rule description");
            return false;
        }
        //System.out.println(implication+"============");

        if (implication.getVar().size() == 0) {
            for (Fact f : implication.getFacts().subList(1, implication.getFacts().size())) {
                if (f.getFact().get(1).toString().equals("<true>"))
                    f.setFact(new ArrayList<>(f.getFact().subList(2, f.getFact().size() - 1)));

                if (!proveFact(f)) {
                    return false;
                }
                System.out.println("by proving: " + f);
            }
            System.out.println("This fact was Proved: " + implication.getFacts().get(0));
            this.ft.addFact(implication.getFacts().get(0));
            return true;
        }

        proveForVarOptions(implication);
        return false;


    }

    public FactTable getNewFacts(){
        for(Description d : provable) {
            proveRule(d);
        }
        return this.ft;
    }

    private void proveForVarOptions(Description implication) {
        ArrayList<Fact> possibleVarValues = new ArrayList<>();
        HashMap<String, ArrayList<String>> varMap = implication.getVar();

        for (Fact f : implication.getFacts()){
        //check fact against knowledge base
            //System.out.println(f);
            try {
                if (f.getFact().get(1).toString().equals("<true>"))
                    f.setFact(new ArrayList<>(f.getFact().subList(2, f.getFact().size() - 1)));
            }catch (IndexOutOfBoundsException e){}


            varMap = ft.searchFactTableV(varMap, f);
        }

        System.out.println(varMap);
    }
        //if it matches assign the id values to the var values
}
