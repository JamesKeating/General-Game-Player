package GDLTokens;

public class IdToken implements Token {



    private String id;

    public IdToken(String id){
        this.id = id;
    }

    public String toString() {
        return id;
    }

    public int getValue() {
        return -3; //atom
    }

    public String getID() {
        return id;
    }

    @Override
    public Token copy() {
        return new IdToken(id);
    }


}
