package DescriptionProcessing.PropNetComponents;

/**
 * Created by siavj on 09/01/2017.
 */

public class Transition extends PropNetNode
{

    public boolean getValue() {
        return getSingleInput().getValue();
    }

    public String toString()
    {
        return toDot("box", "grey", "TRANSITION");
    }
}