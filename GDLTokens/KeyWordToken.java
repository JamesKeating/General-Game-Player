package GDLTokens;

import java.io.Serializable;

public class KeyWordToken implements Token, Serializable {

    private String value;

    public KeyWordToken(String id){
        this.value = id;
    }

    public String toString() {
        return value ;
    }

    public int getValue() {
        return -3; //atom
    }


    public String getID() {
        return value;
    }

    @Override
    public Token copy() {
        return new KeyWordToken(value);
    }

    public void setId(String id) {
        value = id;
    }

}
