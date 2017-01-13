package GDLTokens;

public class ImplicationToken implements Token{

    public String toString() {
        return "<=";
    }

    public int getValue() {
        return -3; //atom
    }


    public String getID() {
        return "<=";
    }

    @Override
    public Token copy() {
        return new ImplicationToken();
    }


}
