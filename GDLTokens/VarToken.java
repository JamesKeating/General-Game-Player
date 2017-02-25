package GDLTokens;

import java.io.Serializable;

public class VarToken implements Token, Serializable {

    private String value;

    public VarToken(String id){
        this.value = id;
    }

    public String toString() {
        return  value ;
    }

    public int getValue() {
        return -3; //atom
    }

    public String getID() {
        return value;
    }

    @Override
    public Token copy() {
        return new VarToken(value);
    }


}
