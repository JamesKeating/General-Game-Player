package GDLTokens;

/**
 * Created by siavj on 15/10/2016.
 */
public class EofToken implements Token {
    public int getValue() {
        return -99; //atom
    }


    public String getID() {
        return "Error";
    }

    @Override
    public Token copy() {
        return null;
    }

    public String toString() {
        return "<EOF>";
    }
}
