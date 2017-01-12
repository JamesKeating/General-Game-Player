package GDLTokens;

public class RparToken implements Token {

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
