package GDLTokens;

import java.io.Serializable;

public class SemiCoToken implements Token, Serializable {

    public String toString() {
        return ";";
    }

    public int getValue() {
        return -3; //atom
    }


    public String getID() {
        return ";";
    }

    @Override
    public Token copy() {
        return new SemiCoToken();
    }
}
