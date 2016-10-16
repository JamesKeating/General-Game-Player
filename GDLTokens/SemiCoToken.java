package GDLTokens;

public class SemiCoToken implements Token {

    public String toString() {
        return "<Semi>";
    }

    public int getValue() {
        return -3; //atom
    }
}
