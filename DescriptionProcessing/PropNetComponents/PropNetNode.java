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

//represents the nodes in th network all components extend this class
public abstract class PropNetNode{

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

    String toDot(String shape, String fillcolor, String label) {
        StringBuilder sb = new StringBuilder();

        sb.append("\"@").append(Integer.toHexString(hashCode())).append("\"[shape=").append(shape).append(", style= filled, fillcolor=").append(fillcolor).append(", label=\"").append(label).append("\"]; ");
        for ( PropNetNode propNetNode : getNodeOutputs() ) {
            sb.append("\"@").append(Integer.toHexString(hashCode())).append("\"->").append("\"@").append(Integer.toHexString(propNetNode.hashCode())).append("\"; ");
        }

        return sb.toString();
    }
}
