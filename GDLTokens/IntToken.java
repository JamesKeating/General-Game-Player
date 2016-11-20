package GDLTokens;

public class IntToken implements Token {

    private int value;
    private String strValue;

    public IntToken(String id){
        this.value = 1;
        strValue = id;
    }

    public String toString() {
        return "<Int>";
    }

    public int getValue() {
        return -3; //atom
    }


    public String getID() {
        return strValue;
    }
}
