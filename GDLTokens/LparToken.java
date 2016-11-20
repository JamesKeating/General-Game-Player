package GDLTokens;

public class LparToken implements Token {

    public String toString() {
        return "<Lpar>";
    }

    public int getValue() {
        return -1; //atom
    }

    public String getID() {
        return "(";
    }

}
