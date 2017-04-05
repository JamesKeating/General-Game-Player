package DescriptionProcessing.PropNetComponents;

/**
 * Created by siavj on 09/01/2017.
 */

//represents a flip flop in a circuit used to control when nodes are updated.
public class Transition extends PropNetNode {

    public boolean getValue() {
        return getSingleInput().getValue();
    }

    public String toString()
    {
        return toDot("box", "grey", "TRANSITION");
    }
}