package GDLTokens;

public class ImplicationToken implements Token{

    public String toString() {
        return "<Implication>";
    }

    public int getValue() {
        return -3; //atom
    }


    public String getID() {
        return "<=";
    }

}
