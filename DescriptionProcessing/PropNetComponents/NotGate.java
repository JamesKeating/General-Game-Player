package DescriptionProcessing.PropNetComponents;

/**
 * Created by siavj on 09/01/2017.
 */


public final class NotGate extends PropNetNode
{

    public boolean getValue() {
//        assert getNodeInputs().size() == 1; //TODO: replace asssert?
        return !getNodeInputs().iterator().next().getValue();
    }

    public String toString()
    {
        return toDot("invtriangle", "grey", "NOT");
    }
}