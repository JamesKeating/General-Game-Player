package DescriptionProcessing.PropNetComponents;

/**
 * Created by siavj on 09/01/2017.
 */

//represents a logical Not-Gate
public final class NotGate extends PropNetNode {

    public boolean getValue() {
        return !getNodeInputs().iterator().next().getValue();
    }

    public String toString()
    {
        return toDot("invtriangle", "grey", "NOT");
    }
}