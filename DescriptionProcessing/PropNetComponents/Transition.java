package DescriptionProcessing.PropNetComponents;

/**
 * Created by siavj on 09/01/2017.
 */

public final class Transition extends PropNetNode
{

    public boolean getValue() {
        assert getNodeInputs().size() == 1; //TODO: replace asssert?
        return getNodeInputs().iterator().next().getValue();
    }

    public String toString()
    {
        return toDot("box", "grey", "TRANSITION");
    }
}