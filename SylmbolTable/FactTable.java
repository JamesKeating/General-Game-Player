//package SylmbolTable;
//
//import GDLTokens.IdToken;
//import GDLTokens.IntToken;
//import GDLTokens.Token;
//import GDLTokens.VarToken;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//
///**
// * Created by siavj on 30/10/2016.
// */
//public class FactTable {
//
//    public int size() {
//        return facts.size();
//    }
//
//    public ArrayList<Fact> getFacts() {
//        return facts;
//    }
//
//    private ArrayList<Fact> facts = new ArrayList<>();
//
//    public void addFact(Fact fact){
//        this.facts.add(fact);
//
//    }
//
//    public boolean searchFactTable(Fact fact){
//        for (Fact f1 : this.facts){
//            Iterator<Token> it1 = fact.getFact().iterator();
//            Iterator<Token> it2 = f1.getFact().iterator();
//
//            while (it1.hasNext() && it2.hasNext()) {
//                if(!it1.next().getID().equals(it2.next().getID())){
//                    break;
//                }
//            }
//            if (!it1.hasNext() && !it2.hasNext())
//                return true;
//
//        }
//        return false;
//    }
//
//    public HashMap<String, ArrayList<String>> searchFactTableV(HashMap<String, ArrayList<String>> varMap,  Fact fact){
//        Token passed, stored;
//        Iterator<Token> itPassed, itStored;
//        HashMap<String, String> temp = new HashMap<>();
//        HashMap<String, ArrayList<String>> temp2 = new HashMap<>();
//        for (String key : varMap.keySet())
//            temp2.put(key, new ArrayList<>());
//
//
//
//        for (Fact f1 : this.facts){
//            itPassed = fact.getFact().iterator();
//            itStored = f1.getFact().iterator();
//
//            while (itPassed.hasNext() && itStored.hasNext()) {
//                passed = itPassed.next();
//                stored = itStored.next();
//
//                //System.out.println("Passed: "+passed.getID() +"-- stored: "+ stored.getID());
//                //System.out.println(!passed.getID().equals(stored.getID())+" test1");
//                if(!passed.getID().equals(stored.getID())){
//                    //System.out.println((passed instanceof VarToken && (stored instanceof IntToken || stored instanceof IdToken))+ "test2");
//                    if (passed instanceof VarToken && (stored instanceof IntToken || stored instanceof IdToken)){
//                        temp.putIfAbsent(passed.getID(), stored.getID());
//                        //System.out.println(temp + "test 3 ==============");
//                        if(!temp.get(passed.getID()).equals(stored.getID()))
//                            break;
//                    }
//
//                    else {
//                        break;
//                    }
//                }
//            }
//            //didnt break early
//            if (!itPassed.hasNext() && !itStored.hasNext()){
//                for(String key : temp2.keySet()) {
//                    temp2.get(key).add(temp.get(key));
//
//                }
//            }
//            temp.clear();
//
//        }
//
//        //if a variable already has values assigned from previous fact
//        int i = 0;
//        for (String key: varMap.keySet()) {
//
//            //System.out.println(temp2.get(key).size() + "aaaaaaaaaaaaaaaa" + varMap.get(key).size());
//
//            if (temp2.get(key).size() <= 0) {
//                varMap.put("noMatches", null);
//                return varMap;
//            }
//
//            if (varMap.get(key).size() == 0) {
//                return temp2;
//            }
//
//            boolean intersection = false;
//            for (int j = 0; j < varMap.get(key).size(); j++) {
//                if (varMap.get(key).get(j) != null){
//                    for (int k = 0; k < varMap.get(key).size(); k++) {
//                        if (temp2.get(key).get(j) != null) {
//                            intersection = true;
//                            varMap.get(key).retainAll(temp2.get(key));
//                            break;
//                        }
//                    }
//                }
//                if (!intersection){
//                    varMap.get(key).addAll(temp2.get(key));
//                    break;
//                }
//            }
//
//        }
//
//        return varMap;
//    }
//
//    public String toString(){
//        String string = "";
//        for (Fact fact : this.facts)
//            string += fact.toString() + "\n";
//        return string;
//    }
//
//}
