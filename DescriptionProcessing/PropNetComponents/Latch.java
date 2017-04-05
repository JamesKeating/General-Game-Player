package DescriptionProcessing.PropNetComponents;

/**
 * Created by siavj on 09/01/2017.
 */

import DeductiveDatabase.Fact;

//represents a Proposition in the network
public final class Latch extends PropNetNode {

    private Fact label;
    private boolean value = false;


    public Latch(Fact label) {
        this.label = label;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public Fact getLabel() {
        return label;
    }

    public void setLabel(Fact newLabel) {
        label = newLabel;
    }

    public String toString()
    {
        return toDot("circle", value ? "red" : "white", label.toString());
    }
}