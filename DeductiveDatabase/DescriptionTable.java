package DeductiveDatabase;

import DescriptionProcessing.Player;
import GDLParser.RDP;
import GDLTokens.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by siavj on 16/10/2016.
 */

/**
 * Stores all of the descriptions from the game definition
 */
public class DescriptionTable {

    private HashMap<String, HashMap<Integer, ArrayList<Description>>> table = new HashMap<>();
    private ArrayList<Description>  descriptionsList = new ArrayList<>();

    /**
     * Table of all the descriptions is built by parsing the game definition
     */
    public DescriptionTable(ArrayList<Token> tokenStream){
        int parCount = 0, start = 0, end = 1;
        RDP rdp = new RDP();
        if (rdp.parse(tokenStream)) {
            tokenStream.remove(tokenStream.size() - 1);
            for (Token tok : tokenStream){
                if (tok.getValue()== -1)
                    parCount +=1;
                else if (tok.getValue()== -2)
                    parCount -=1;

                if (parCount == 0){
                    Description tempDescription = new Description(new ArrayList<>(tokenStream.subList(start, end)));
                    descriptionsList.add(tempDescription);

                    table.putIfAbsent(tempDescription.getLeadAtom().toString(), new HashMap<>());
                    table.get(tempDescription.getLeadAtom().toString()).putIfAbsent(tempDescription.getArity(), new ArrayList<>());
                    table.get(tempDescription.getLeadAtom().toString()).get(tempDescription.getArity()).add(tempDescription);
                    start = end;
                }

                end +=1;
            }

        }

        else
            descriptionsList = null;


    }

    public ArrayList<Description> listTable(){
        return descriptionsList;
    }


    public HashMap<String, HashMap<Integer, ArrayList<Description>>> getTable() {
        return table;
    }

}
