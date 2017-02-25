package GDLTokens;

import java.io.Serializable;

public class LparToken implements Token, Serializable {

    public String toString() {
        return "(";
    }

    public int getValue() {
        return -1; //atom
    }

    public String getID() {
        return "(";
    }

    @Override
    public Token copy() {
        return new LparToken();
    }


}
