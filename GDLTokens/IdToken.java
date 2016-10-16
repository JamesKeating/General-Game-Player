package GDLTokens;

public class IdToken implements Token {

    private String value;

    public IdToken(String id){
        this.value = id;
    }

    public String toString() {
        return "<ID>";
    }


    public int getValue() {
        return -3; //atom
    }
}
