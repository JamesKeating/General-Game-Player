package GDLParser;

import GDLTokens.Token;

import java.util.ArrayList;

//parses a stream of tokens to check they are syntactically valid based on the grammar provided
public class RDP{

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
                return nextPos;
            }

        }
        return 0;
    }
}