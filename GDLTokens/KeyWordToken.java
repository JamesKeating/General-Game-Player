package GDLTokens;

public class KeyWordToken implements Token {

    private String value;

    public KeyWordToken(String id){
        this.value = id;
    }

    public String toString() {
        return "<" + value + ">";
    }

}
