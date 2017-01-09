package DescriptionProcessing;

import GDLTokens.IdToken;
import GDLTokens.IntToken;
import GDLTokens.Token;
import GDLTokens.VarToken;
import SylmbolTable.Description;
import SylmbolTable.DescriptionTable;
import SylmbolTable.Fact;
import SylmbolTable.FactTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

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

        if (fact.getFact().get(1).toString().equals("<distinct>")) {
            return !(fact.getFact().get(2).getID().equals(fact.getFact().get(3).getID()));
        }

        if (this.ft.searchFactTable(fact))
            return true;

        //System.out.println(fact.getFact().get(1));

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
                //System.out.println("by proving: " + f);
            }
            //System.out.println("This fact was Proved: " + implication.getFacts().get(0));
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
        HashMap<String, ArrayList<String>> varMap = implication.getVar();
        boolean distinct = false;

        for (Fact f : implication.getFacts().subList(1, implication.getFacts().size())) {
            //check fact against knowledge base
            //System.out.println(f);
            try {
                if (f.getFact().get(1).toString().equals("<true>"))
                    f.setFact(new ArrayList<>(f.getFact().subList(2, f.getFact().size() - 1)));
            } catch (IndexOutOfBoundsException e) {
            }


            boolean proveRule = true;
            for (Token t : f.getFact()) {
                if (t.getClass() == VarToken.class)
                    proveRule = false;
            }

            if (proveRule) {
                if (!proveFact(f)){
                    return;
                }
            }

            else {
                for (Description d : provable) {
                    //System.out.println(d.getFacts()+ " ====== " + f);
                    if (test(d.getFacts().get(0), f)) {
                        if (!d.getFacts().subList(1, d.getFacts().size() - 1).contains(f)) {
                            proveRule = proveRule(d);
                        }
                    }
                }
                //System.out.println(f);
                //System.out.println(varMap);
                if (f.getFact().get(1).toString().equals("<distinct>")) {
                   // distinct  = true;
//                    arg1 f.getFact().get(2).toString();
//                    arg2 f.getFact().get(3).toString();
                }
//                    ArrayList<String> arg1 = new ArrayList<>() , arg2 = new ArrayList<>();
//                    if (f.getFact().get(2) instanceof VarToken) {
//                        arg1 = varMap.get(f.getFact().get(2).getID());
//                        if (f.getFact().get(3) instanceof VarToken) {
//                            arg2 = varMap.get(f.getFact().get(3).getID());
//
//                        }
//                        else
//                            arg2.add(f.getFact().get(3).getID());
//
//                    }
//                    else {
//                        arg1 = varMap.get(f.getFact().get(3).getID());
//                        arg2.add(f.getFact().get(2).getID());
//                    }
//
//                    String val2 = arg2.get(0);
//                    ArrayList<String> nonNull = null;
//                        for (int i = 0, cap = 0; i < arg1.size(); i++) {
//                            if (arg2.size() != 1)
//                                val2 = arg2.get(i);
//
//                            if (arg1.get(i) == null && nonNull == null) {
//                                nonNull = new ArrayList<>(arg1);
//                                nonNull.removeAll(Collections.singleton(null));
//                                cap = nonNull.size();
//                            }
//
//                            String value = arg1.get(i);
//                            if (value == null) {
//                                for (int j = 0; j < cap; j++) {
//                                    value = nonNull.get(j);
//
//                                }
//                            }
//
//                            if (value.equals(val2)){
//                                for (String key : varMap.keySet())
//                                    varMap.get(key).remove(i);
//                                i--;
//                            }
//
//                        }
//
//                    System.out.println(varMap);
//
//                }

                else {
                    varMap = ft.searchFactTableV(varMap, f);
                    //System.out.println(f + "   ===   " + varMap);


                    if (!proveRule && varMap.containsKey("noMatches"))
                        return;
                }
            }

        }

       //System.out.println(varMap);
        try{
            int i = 0;
        while (true){
            HashMap<String, ArrayList<String>> nonNull = new HashMap<>();
            //System.out.println(varMap + "99999999");
            for (int j = 0, cap = 1; j < cap; j++ ) {
                Fact newFact = new Fact(null);
                newFact.setFact(implication.getFacts().get(0).getFact());

                for (String var : varMap.keySet()) {
                    String value = varMap.get(var).get(i);
                    if (value == null && nonNull.get(var) == null) {
                        nonNull.put(var, new ArrayList<>(varMap.get(var)));
                        //System.out.println(nonNull+ "33333333");
                        nonNull.get(var).removeAll(Collections.singleton(null));
                        //System.out.println(nonNull + "aaaaa");
                        cap = nonNull.size();
                    }

                    if (value == null)
                        value = nonNull.get(var).get(j);

                    //System.out.println(newFact + "-------------");
                    newFact.setVarValue(var, value);


                }
                if (!this.ft.searchFactTable(newFact)){
//                    if (distinct) {
//                        if pos1
//
//                        distinct = false;
//                    }
                    //System.out.println(newFact + "=============");
                    this.ft.addFact(newFact);
                }
            }


            i++;
        }
        }catch (Exception e){}

    }



    private boolean test(Fact a, Fact b){
        Token x, y;
        Iterator<Token> itA, itB;
        itA = a.getFact().iterator();
        itB = b.getFact().iterator();

        while (itA.hasNext() && itB.hasNext()) {
            x = itA.next();
            y = itB.next();

            if(!x.getID().equals(y.getID())){
                if (!(x instanceof VarToken && (y instanceof IntToken || y instanceof IdToken))){
                    return false;

                }
            }
        }
        return true;
    }
}
