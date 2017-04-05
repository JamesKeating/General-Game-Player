package DescriptionProcessing.PropNetComponents;

/**
 * Created by siavj on 09/01/2017.
 */

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public abstract class PropNetNode implements Serializable
{

    public abstract boolean getValue();
    public abstract String toString();

    private HashSet<PropNetNode> inputs;
    private HashSet<PropNetNode> outputs;


    public PropNetNode() {
        this.inputs = new HashSet<>();
        this.outputs = new HashSet<>();
    }

    public HashSet<PropNetNode> getNodeInputs()
    {
        return inputs;
    }

    public HashSet<PropNetNode> getNodeOutputs()
    {
        return outputs;
    }


    protected String toDot(String shape, String fillcolor, String label)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("\"@" + Integer.toHexString(hashCode()) + "\"[shape=" + shape + ", style= filled, fillcolor=" + fillcolor + ", label=\"" + label + "\"]; ");
        for ( PropNetNode propNetNode : getNodeOutputs() )
        {
            sb.append("\"@" + Integer.toHexString(hashCode()) + "\"->" + "\"@" + Integer.toHexString(propNetNode.hashCode()) + "\"; ");
        }

        return sb.toString();
    }

    public PropNetNode getSingleOutput() {
        assert outputs.size() == 1;
        return outputs.iterator().next();
    }

    public PropNetNode getSingleInput() {
        assert inputs.size() == 1;
        return inputs.iterator().next();
    }

    public void addInput(PropNetNode input) {
        inputs.add(input);
    }

    public void addOutput(PropNetNode output) {
        outputs.add(output);
    }


}
