package DescriptionProcessing.PropNetComponents;

/**
 * Created by siavj on 09/01/2017.
 */

public class AndGate extends PropNetNode
{
    public boolean getValue() {
        for ( PropNetNode propNetNode : getNodeInputs() ) {
            if ( !propNetNode.getValue() )
                return false;

        }

        return true;
    }

    public String toString() {
        return toDot("invhouse", "grey", "AND");
    }

}
