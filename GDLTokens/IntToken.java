package GDLTokens;

public class IntToken implements Token {

    public int getIntValue() {
        return intValue;
    }

    private int intValue;
    private String strValue;

    public IntToken(String id){
        this.intValue = 1;
        Integer.valueOf(id);
        strValue = id;
    }

    public String toString() {
        return strValue;
    }

    public int getValue() {
        return -3; //atom
    }


    public String getID() {
        return strValue;
    }

    @Override
    public Token copy() {
        return new IntToken(strValue);
    }

}
