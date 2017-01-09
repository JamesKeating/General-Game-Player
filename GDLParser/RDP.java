package GDLParser;

import GDLTokens.Token;

import java.util.ArrayList;



public class RDP{

    private int nextPos;
    private GDLGrammar grammar = new GDLGrammar();
    private ArrayList<Token> tokenStream = new ArrayList<>();

    public boolean parse (ArrayList<Token> tokenStream){
        this.tokenStream = tokenStream;
        if (this.recursiveParse(20, 0) != 0)
            return true;
        return false;

    }

    public int recursiveParse(int nt, int pos){
        int nextPos;
        boolean valid = false;
        for (Production p : grammar.grammarProductions){
            nextPos = pos;
            //System.out.println(p + "--- " + nt + "#### " + tokenStream.get(nextPos).getValue());
            if(nt == p.lhs) {
                valid = true;
                for (int symbol : p.rhs) {
                    valid = false;
                    if (symbol < 0) {
                        if (tokenStream.get(nextPos).getValue() == symbol) {
                            nextPos += 1;
                            valid = true;
                        }
                        else {
                            break;
                        }

                    }
                    else {
                        nextPos = this.recursiveParse(symbol, nextPos);
                        if (nextPos != 0) {
                            valid = true;
                        } else
                            break;
                    }
                }
            }

            if (valid) {
               // System.out.println(p + "=====" + nt);
                return nextPos; //pos
            }

        }
        return 0;
    }
}