package DescriptionProcessing;

/**
 * Created by siavj on 09/01/2017.
 */

import DescriptionProcessing.PropNetComponents.*;
import GDLTokens.Token;
import SylmbolTable.Fact;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;


public class PropNet implements Serializable
{
    /** References to every PropNetNode in the PropNet. */
    private HashSet<PropNetNode> propNetNodes;

    /** References to every Proposition in the PropNet. */
    private HashSet<Latch> latches;

    /** References to every BaseProposition in the PropNet, indexed by name. */
    private HashMap<String, Latch> baseLatches;

    /** References to every InputProposition in the PropNet, indexed by name. */
    private HashMap<String, Latch> inputLatches;

    //TODO: i called prop = latch , sentence = fact, rule = desc
    /** References to every LegalProposition in the PropNet, indexed by role. */
    private HashMap<String, HashSet<Latch>> legalLatches;

    /** References to every GoalProposition in the PropNet, indexed by role. */
    private HashMap<String, HashSet<Latch>> goalLatches;

    /** A reference to the single, unique, InitProposition. */
    private Latch initLatches;

    public HashSet<Latch> getDrawLatches() {
        return drawLatches;
    }

    private HashSet<Latch> drawLatches;

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
        this.drawLatches = findDrawLatches();
        this.goalLatches = findGoalLatches();
        this.initLatches = findInitLatches();
        this.terminalLatches = findTerminalLatches();

//        this.legalInputMap = makeLegalInputMap();
//        System.out.println(legalInputMap);
    }

    public ArrayList<Player> getRoles() {
        return roles;
    }


    /**
     * Getter method.
     *
     * @return References to every BaseProposition in the PropNet, indexed by
     *         name.
     */
    public HashMap<String, Latch> getBaseLatches() {
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
    public HashMap<String, HashSet<Latch>> getGoalLatches() {
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
    public HashMap<String, Latch> getInputLatches() {
        return inputLatches;
    }

    /**
     * Getter method.
     *
     * @return References to every LegalProposition in the PropNet, indexed by
     *         player name.
     */
    public HashMap<String, HashSet<Latch>> getLegalLatches() {
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
    private HashMap<String, Latch> findBaseLatches()
    {
//        System.out.println("000000000000");
        HashMap<String, Latch> baseLatches = new HashMap<String, Latch>();
        for (Latch latch : latches) {

//            System.out.println(latch.getLabel());
            // Skip all propositions without exactly one input.
            if (latch.getNodeInputs().size() != 1)
                continue;

            PropNetNode propNetNode = latch.getNodeInputs().iterator().next();

            if (propNetNode instanceof Transition) {
                baseLatches.put(latch.getLabel().toString(), latch);
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
    private HashMap<String, HashSet<Latch>> findGoalLatches() {
        HashMap<String, HashSet<Latch>> goalLatches = new HashMap<>();
        for (Latch latch : latches) {

            if (latch.getLabel().getFact().size() <= 1)
                continue;

            Fact fact = latch.getLabel();
            //TODO: correct possition for goal confirmaton
            if (!fact.getLeadAtom().getID().equals("goal"))//4?
                continue;

            //TODO: correct possition for role confirmaton && convert to player
            Player thePlayer = new Player(fact.getFact().get(2));
            if (!goalLatches.containsKey(thePlayer.toString())) {
                goalLatches.put(thePlayer.toString(), new HashSet<>());
            }
            goalLatches.get(thePlayer.toString()).add(latch);
        }

        return goalLatches;
    }

    /**
     * Returns a reference to the single, unique, InitProposition.
     *
     * @return A reference to the single, unique, InitProposition.
     */
    private Latch findInitLatches() {
        for (Latch latch : latches) {


            if (latch.getLabel().getFact().size() > 1)
                continue;

            //System.out.println(latch.getLabel());
            //TODO: check 1 for correct init pos also for netx 2 at least
            if (latch.getLabel().getFact().get(0).getID().equals("init")) {
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
    private HashMap<String, Latch> findInputLatches()
    {
        HashMap<String, Latch> inputLatches = new HashMap<>();
        for (Latch latch : latches) {


            if (latch.getLabel().getFact().size() <= 1)
                continue;


            if (latch.getLabel().getLeadAtom().getID().equals("does")) {
                inputLatches.put(latch.getLabel().toString(), latch);
            }

        }
//        System.out.println(inputLatches);
        return inputLatches;
    }

    /**
     * Builds an index over the LegalPropositions in the PropNet.
     *
     * @return An index over the LegalPropositions in the PropNet.
     */
    private HashMap<String, HashSet<Latch>> findLegalLatches()
    {
        HashMap<String, HashSet<Latch>> legalLatches = new HashMap<>();
        for (Latch latch : latches) {

            if(latch.getLabel().getFact().size() <= 1)
                continue;

            if (latch.getLabel().getLeadAtom().getID().equals("legal")) {

                Player player = new Player(latch.getLabel().getFact().get(2));

                if (!legalLatches.containsKey(player.toString())) {
                    legalLatches.put(player.toString(), new HashSet<>());
                }
                legalLatches.get(player.toString()).add(latch);
            }
        }


        return legalLatches;
    }

    private HashSet<Latch> findDrawLatches()
    {
        HashSet<Latch> drawLatches = new HashSet<>();
        for (Latch latch : latches) {

            if(latch.getLabel().getFact().size() <= 1)
                continue;


            if (latch.getLabel().getLeadAtom().getID().equals("drawit")) {
                drawLatches.add(latch);
            }
        }

        return drawLatches;
    }

    /**
     * Builds an index over the Propositions in the PropNet.
     *
     * @return An index over Propositions in the PropNet.
     */
    private HashSet<Latch> findLatches()
    {
        HashSet<Latch> latches = new HashSet<>();
        for (PropNetNode prop : propNetNodes) {
            if (prop instanceof Latch) {
                latches.add((Latch) prop);
            }
        }
        return latches;
    }

    /**
     * Records a reference to the single, unique, TerminalProposition.
     *
     * @return A reference to the single, unqiue, TerminalProposition.
     */
    private Latch findTerminalLatches() {

        for ( Latch latch : latches ) {

            if (latch.getLabel().getFact().size() == 1) {
                if (latch.getLabel().getFact().get(0).getID().equals("terminal")) {

                    return latch;
                }
            }
        }

        return null;
    }

    public int getSize() {
        return propNetNodes.size();
    }


}