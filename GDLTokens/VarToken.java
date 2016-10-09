package GDLTokens;

public class VarToken implements Token {

    private String value;

    public VarToken(String id){
        this.value = id;
    }

    public String toString() {
        return "<Var>";
    }

}
