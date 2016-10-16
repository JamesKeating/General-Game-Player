package SylmbolTable;

import GDLParser.RDP;
import GDLTokens.Token;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by siavj on 16/10/2016.
 */
public class DescriptionTable {

    private RDP rdp = new RDP();
    private HashMap<String, HashMap<Integer, ArrayList<Description>>> table = new HashMap<>();
    private Description tempDescription;

    public DescriptionTable(ArrayList<Token> tokenStream){
        int parCount = 0, start = 0, end = 1;
        if (rdp.parse(tokenStream)) {
            tokenStream.remove(tokenStream.size() - 1);
            for (Token tok : tokenStream){

                if (tok.getValue()== -1)
                    parCount +=1;
                else if (tok.getValue()== -2)
                    parCount -=1;

                if (parCount == 0){
                    tempDescription = new Description(new ArrayList<Token>(tokenStream.subList(start, end)));
                    table.putIfAbsent(tempDescription.getLeadAtom().toString(), new HashMap<Integer, ArrayList<Description>>());
                    table.get(tempDescription.getLeadAtom().toString()).putIfAbsent(tempDescription.getArity(), new ArrayList<Description>());
                    table.get(tempDescription.getLeadAtom().toString()).get(tempDescription.getArity()).add(tempDescription);
                    start = end;
                }

                end +=1;
            }

        }

    }

    public HashMap<String, HashMap<Integer, ArrayList<Description>>> getTable() {
        return table;
    }

}
