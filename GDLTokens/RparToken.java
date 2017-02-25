package GDLTokens;

import java.io.Serializable;

public class RparToken implements Token, Serializable {

    public String toString() {
        return ")";
    }

    public int getValue() {
        return -2; //atom
    }


    public String getID() {
        return ")";
    }

    @Override
    public Token copy() {
        return new RparToken();
    }

}
