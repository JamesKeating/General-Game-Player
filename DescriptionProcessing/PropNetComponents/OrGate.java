package DescriptionProcessing.PropNetComponents;

/**
 * Created by siavj on 09/01/2017.
 */

public final class OrGate extends PropNetNode
{

    public boolean getValue() {
        for ( PropNetNode PropNetNode : getNodeInputs() ) {
            if ( PropNetNode.getValue() )
                return true;
        }
        return false;
    }

    public String toString()
    {
        return toDot("ellipse", "grey", "OR");
    }
}