package GDLTokens;

public interface Token {
    public int getValue();
    public String getID();
    public Token copy();
}