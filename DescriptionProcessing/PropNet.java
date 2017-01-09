package DescriptionProcessing;

/**
 * Created by siavj on 09/01/2017.
 */

import DescriptionProcessing.PropNetComponents.Latch;
import DescriptionProcessing.PropNetComponents.PropNetNode;
import DescriptionProcessing.PropNetComponents.Transition;
import GDLTokens.Token;
import SylmbolTable.Fact;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class PropNet
{
    /** References to every PropNetNode in the PropNet. */
    private HashSet<PropNetNode> propNetNodes;

    /** References to every Proposition in the PropNet. */
    private HashSet<Latch> latches;

    /** References to every BaseProposition in the PropNet, indexed by name. */
    private HashMap<Fact, Latch> baseLatches;

    /** References to every InputProposition in the PropNet, indexed by name. */
    private HashMap<Fact, Latch> inputLatches;

    //TODO: i called prop = latch , sentence = fact, rule = desc
    /** References to every LegalProposition in the PropNet, indexed by role. */
    private HashMap<Player, HashSet<Latch>> legalLatches;

    /** References to every GoalProposition in the PropNet, indexed by role. */
    private HashMap<Player, HashSet<Latch>> goalLatches;

    /** A reference to the single, unique, InitProposition. */
    private Latch initLatches;

    /** A reference to the single, unique, TerminalProposition. */
    private Latch terminalLatches;

    /** A helper mapping between input/legal propositions. */
    private HashMap<Latch, Latch> legalInputMap;

    /** A helper list of all of the roles. */
    private ArrayList<Player> roles;

    public void addPropNetNode(PropNetNode propNetNode) {
        propNetNodes.add(propNetNode);
        
        if (propNetNode instanceof Latch) {
            latches.add((Latch) propNetNode);
        }
    }

 
    public PropNet(ArrayList<Player> roles, HashSet<PropNetNode> propNetNodes) {
        //TODO:finish these
        this.roles = roles;
        this.propNetNodes = propNetNodes;
        this.latches = findLatches();
        this.baseLatches = findBaseLatches();
        this.inputLatches = findInputLatches();
        this.legalLatches = findLegalLatches();
        this.goalLatches = findGoalLatches();
        this.initLatches = findInitLatches();
        this.terminalLatches = findTerminalLatches();
        this.legalInputMap = makeLegalInputMap();
    }

    public ArrayList<Player> getRoles() {
        return roles;
    }

    public HashMap<Latch, Latch> getLegalInputMap() {
        return legalInputMap;
    }

    private HashMap<Latch, Latch> makeLegalInputMap() {
        HashMap<Latch, Latch> legalInputMap = new HashMap<>();
        // Create a mapping from Body->Input.
        //TODO: Changed GDLTerm To TOKEN? and check var names prop => lat
        HashMap<ArrayList<Token>, Latch> inputLatsByBody = new HashMap<>();
        for(Latch inputLat : inputLatches.values()) {
            ArrayList<Token> inputLatBody = (inputLat.getLabel()).getFact();
            inputLatsByBody.put(inputLatBody, inputLat);
        }
        // Use that mapping to map Input->Legal and Legal->Input
        // based on having the same Body Latch.
        for(HashSet<Latch> legalLats : legalLatches.values()) {
            for(Latch legalLat : legalLats) {
                ArrayList<Token> legalLatBody = (legalLat.getLabel()).getFact();
                if (inputLatsByBody.containsKey(legalLatBody)) {
                    Latch inputLat = inputLatsByBody.get(legalLatBody);
                    legalInputMap.put(inputLat, legalLat);
                    legalInputMap.put(legalLat, inputLat);
                }
            }
        }
        return legalInputMap;
    }

    /**
     * Getter method.
     *
     * @return References to every BaseProposition in the PropNet, indexed by
     *         name.
     */
    public HashMap<Fact, Latch> getBaseLatches() {
        return baseLatches;
    }

    /**
     * Getter method.
     *
     * @return References to every PropNetNode in the PropNet.
     */
    public HashSet<PropNetNode> getPropNetNodes() {
        return propNetNodes;
    }

    /**
     * Getter method.
     *
     * @return References to every GoalProposition in the PropNet, indexed by
     *         player name.
     */
    public HashMap<Player, HashSet<Latch>> getGoalLatches() {
        return goalLatches;
    }

    /**
     * Getter method. A reference to the single, unique, InitProposition.
     *
     * @return
     */
    public Latch getInitLatches() {
        return initLatches;
    }

    /**
     * Getter method.
     *
     * @return References to every InputProposition in the PropNet, indexed by
     *         name.
     */
    public HashMap<Fact, Latch> getInputLatches() {
        return inputLatches;
    }

    /**
     * Getter method.
     *
     * @return References to every LegalProposition in the PropNet, indexed by
     *         player name.
     */
    public HashMap<Player, HashSet<Latch>> getLegalLatches() {
        return legalLatches;
    }

    /**
     * Getter method.
     *
     * @return References to every Proposition in the PropNet.
     */
    public HashSet<Latch> getLatches() {
        return latches;
    }

    /**
     * Getter method.
     *
     * @return A reference to the single, unique, TerminalProposition.
     */
    public Latch getTerminalLatches() {
        return terminalLatches;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("digraph propNet\n{\n");
        for ( PropNetNode PropNetNode : propNetNodes ) {
            sb.append("\t" + PropNetNode.toString() + "\n");
        }
        sb.append("}");

        return sb.toString();
    }

    /**
     * Outputs the propnet in .dot format to a particular file.
     * This can be viewed with tools like Graphviz and ZGRViewer.
     *
     * @param filename the name of the file to output to
     */
    public void renderToFile(String filename) {
        try {
            File f = new File(filename);
            FileOutputStream fos = new FileOutputStream(f);
            OutputStreamWriter fout = new OutputStreamWriter(fos, "UTF-8");
            fout.write(toString());
            fout.close();
            fos.close();
        } catch(Exception e) {

        }
    }

    /**
     * Builds an index over the BasePropositions in the PropNet.
     *
     * This is done by going over every single-input proposition in the network,
     * and seeing whether or not its input is a transition, which would mean that
     * by definition the proposition is a base proposition.
     *
     * @return An index over the BasePropositions in the PropNet.
     */
    private HashMap<Fact, Latch> findBaseLatches()
    {
        HashMap<Fact, Latch> baseLatches = new HashMap<Fact, Latch>();
        for (Latch latch : latches) {
            // Skip all propositions without exactly one input.
            if (latch.getNodeInputs().size() != 1)
                continue;

            PropNetNode propNetNode = latch.getNodeInputs().iterator().next();

            if (propNetNode instanceof Transition) {
                baseLatches.put(latch.getLabel(), latch);
            }
        }

        return baseLatches;
    }

    /**
     * Builds an index over the GoalPropositions in the PropNet.
     *
     * This is done by going over every function proposition in the network
     * where the name of the function is "goal", and extracting the name of the
     * role associated with that goal proposition, and then using those role
     * names as keys that map to the goal propositions in the index.
     *
     * @return An index over the GoalPropositions in the PropNet.
     */
    private HashMap<Player, HashSet<Latch>> findGoalLatches() {
        HashMap<Player, HashSet<Latch>> goalPropositions = new HashMap<>();
        for (Latch latch : latches) {

            Fact fact = latch.getLabel();
            //TODO: correct possition for goal confirmaton
            if (!fact.getFact().get(1).getID().equals("goal"))//4?
                continue;

            //TODO: correct possition for role confirmaton && convert to player
            Player thePlayer = new Player(fact.getFact().get(2));
            if (!goalLatches.containsKey(thePlayer)) {
                goalLatches.put(thePlayer, new HashSet<>());
            }
            goalPropositions.get(thePlayer).add(latch);
        }

        return goalPropositions;
    }

    /**
     * Returns a reference to the single, unique, InitProposition.
     *
     * @return A reference to the single, unique, InitProposition.
     */
    private Latch findInitLatches() {
        for (Latch latch : latches) {
            //TODO: check 1 for correct init pos also for netx 2 at least
            if (latch.getLabel().getFact().get(1).getID().equals("init")) {
                return latch;
            }
        }
        return null;
    }

    /**
     * Builds an index over the InputPropositions in the PropNet.
     *
     * @return An index over the InputPropositions in the PropNet.
     */
    private HashMap<Fact, Latch> findInputLatches()
    {
        HashMap<Fact, Latch> inputLatches = new HashMap<>();
        for (Latch latch : latches) {

            if (latch.getLabel().getFact().get(1).getID().equals("does"))
                inputLatches.put(latch.getLabel(), latch);

        }

        return inputLatches;
    }

    /**
     * Builds an index over the LegalPropositions in the PropNet.
     *
     * @return An index over the LegalPropositions in the PropNet.
     */
    private HashMap<Player, HashSet<Latch>> findLegalLatches()
    {
        HashMap<Player, HashSet<Latch>> legalLatches = new HashMap<>();
        for (Latch latch : latches) {

            if (latch.getLabel().getFact().get(1).getID().equals("legal")) {

                Player player = new Player(latch.getLabel().getFact().get(2));

                if (!legalLatches.containsKey(player)) {
                    legalLatches.put(player, new HashSet<>());
                }
                legalLatches.get(player).add(latch);
            }
        }

        return legalLatches;
    }

    /**
     * Builds an index over the Propositions in the PropNet.
     *
     * @return An index over Propositions in the PropNet.
     */
    private Set<Proposition> findLatches()
    {
        Set<Proposition> propositions = new HashSet<Proposition>();
        for (PropNetNode PropNetNode : PropNetNodes)
        {
            if (PropNetNode instanceof Proposition) {
                propositions.add((Proposition) PropNetNode);
            }
        }
        return propositions;
    }

    /**
     * Records a reference to the single, unique, TerminalProposition.
     *
     * @return A reference to the single, unqiue, TerminalProposition.
     */
    private Proposition findTerminalLatches()
    {
        for ( Proposition proposition : propositions )
        {
            if ( proposition.getName() instanceof GdlProposition )
            {
                GdlConstant constant = ((GdlProposition) proposition.getName()).getName();
                if ( constant.getValue().equals("terminal") )
                {
                    return proposition;
                }
            }
        }

        return null;
    }

    public int getSize() {
        return PropNetNodes.size();
    }

    public int getNumAnds() {
        int andCount = 0;
        for(PropNetNode c : PropNetNodes) {
            if(c instanceof And)
                andCount++;
        }
        return andCount;
    }

    public int getNumOrs() {
        int orCount = 0;
        for(PropNetNode c : PropNetNodes) {
            if(c instanceof Or)
                orCount++;
        }
        return orCount;
    }

    public int getNumNots() {
        int notCount = 0;
        for(PropNetNode c : PropNetNodes) {
            if(c instanceof Not)
                notCount++;
        }
        return notCount;
    }

    public int getNumLinks() {
        int linkCount = 0;
        for(PropNetNode c : PropNetNodes) {
            linkCount += c.getOutputs().size();
        }
        return linkCount;
    }

    /**
     * Removes a PropNetNode from the propnet. Be very careful when using
     * this method, as it is not thread-safe. It is highly recommended
     * that this method only be used in an optimization period between
     * the propnet's creation and its initial use, during which it
     * should only be accessed by a single thread.
     *
     * The INIT and terminal PropNetNodes cannot be removed.
     */
    public void removePropNetNode(PropNetNode c) {


        //Go through all the collections it could appear in
        if(c instanceof Proposition) {
            Proposition p = (Proposition) c;
            GdlSentence name = p.getName();
            if(basePropositions.containsKey(name)) {
                basePropositions.remove(name);
            } else if(inputPropositions.containsKey(name)) {
                inputPropositions.remove(name);
                //The map goes both ways...
                Proposition partner = legalInputMap.get(p);
                if(partner != null) {
                    legalInputMap.remove(partner);
                    legalInputMap.remove(p);
                }
            } else if(name == GdlPool.getProposition(GdlPool.getConstant("INIT"))) {
                throw new RuntimeException("The INIT PropNetNode cannot be removed. Consider leaving it and ignoring it.");
            } else if(name == GdlPool.getProposition(GdlPool.TERMINAL)) {
                throw new RuntimeException("The terminal PropNetNode cannot be removed.");
            } else {
                for(Set<Proposition> propositions : legalPropositions.values()) {
                    if(propositions.contains(p)) {
                        propositions.remove(p);
                        Proposition partner = legalInputMap.get(p);
                        if(partner != null) {
                            legalInputMap.remove(partner);
                            legalInputMap.remove(p);
                        }
                    }
                }
                for(Set<Proposition> propositions : goalPropositions.values()) {
                    propositions.remove(p);
                }
            }
            propositions.remove(p);
        }
        PropNetNodes.remove(c);

        //Remove all the local links to the PropNetNode
        for(PropNetNode parent : c.getInputs())
            parent.removeOutput(c);
        for(PropNetNode child : c.getOutputs())
            child.removeInput(c);
        //These are actually unnecessary...
        //c.removeAllInputs();
        //c.removeAllOutputs();
    }
}