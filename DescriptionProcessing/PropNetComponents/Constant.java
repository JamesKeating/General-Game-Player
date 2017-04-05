package DescriptionProcessing.PropNetComponents;

/**
 * Created by siavj on 09/01/2017.
 */

public final class Constant extends PropNetNode {

    private boolean value;

    public Constant(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public String toString()
    {
        return toDot("doublecircle", "grey", Boolean.toString(value).toUpperCase());
    }
}