package GDLParser;


import java.util.ArrayList;

public class GDLGrammar{

    public ArrayList<Production> grammarProductions = new ArrayList<>();

    public GDLGrammar(){
        grammarProductions.add(new Production(20, new int[]{-99}));//21,-99
        grammarProductions.add(new Production(20, new int[]{21, 20}));
        grammarProductions.add(new Production(21, new int[]{-1, 22, -2}));
        grammarProductions.add(new Production(22, new int[]{-3, 22}));
        //grammarProductions.add(new Production(22, new int[]{-4, 22}));
        grammarProductions.add(new Production(22, new int[]{21, 22}));
        grammarProductions.add(new Production(22, new int[]{}));

    }

}