package DescriptionProcessing;

/**
 * Created by siavj on 09/01/2017.
 */

import DescriptionProcessing.PropNetComponents.*;
import DeductiveDatabase.Fact;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class PropNet{

    private HashSet<PropNetNode> propNetNodes;
    private HashSet<Latch> latches;
    private HashMap<String, Latch> baseLatches;
    private HashMap<String, Latch> inputLatches;
    private HashMap<String, HashSet<Latch>> legalLatches;
    private HashMap<String, HashSet<Latch>> goalLatches;
    private Latch initLatches;
    public HashSet<Latch> getDrawLatches() {
        return drawLatches;
    }
    private HashSet<Latch> drawLatches;
    private Latch terminalLatches;
    private ArrayList<Player> roles;

    public PropNet(ArrayList<Player> roles, HashSet<PropNetNode> propNetNodes) {

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

    }

    public ArrayList<Player> getRoles() {
        return roles;
    }
    public HashMap<String, Latch> getBaseLatches() {
        return baseLatches;
    }
    public HashSet<PropNetNode> getPropNetNodes() {
        return propNetNodes;
    }
    public HashMap<String, HashSet<Latch>> getGoalLatches() {
        return goalLatches;
    }
    public Latch getInitLatches() {
        return initLatches;
    }
    public HashMap<String, Latch> getInputLatches() {
        return inputLatches;
    }
    public HashMap<String, HashSet<Latch>> getLegalLatches() {
        return legalLatches;
    }
    public HashSet<Latch> getLatches() {
        return latches;
    }
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

    private HashMap<String, Latch> findBaseLatches() {

        HashMap<String, Latch> baseLatches = new HashMap<String, Latch>();
        for (Latch latch : latches) {

            if (latch.getNodeInputs().size() != 1)
                continue;

            PropNetNode propNetNode = latch.getNodeInputs().iterator().next();

            if (propNetNode instanceof Transition) {
                baseLatches.put(latch.getLabel().toString(), latch);
            }
        }

        return baseLatches;
    }

    private HashMap<String, HashSet<Latch>> findGoalLatches() {
        HashMap<String, HashSet<Latch>> goalLatches = new HashMap<>();
        for (Latch latch : latches) {

            if (latch.getLabel().getFact().size() <= 1)
                continue;

            Fact fact = latch.getLabel();
            if (!fact.getLeadAtom().getID().equals("goal"))//4?
                continue;

            Player thePlayer = new Player(fact.getFact().get(2));
            if (!goalLatches.containsKey(thePlayer.toString())) {
                goalLatches.put(thePlayer.toString(), new HashSet<>());
            }
            goalLatches.get(thePlayer.toString()).add(latch);
        }

        return goalLatches;
    }

    private Latch findInitLatches() {
        for (Latch latch : latches) {

            if (latch.getLabel().getFact().size() > 1)
                continue;

            if (latch.getLabel().getFact().get(0).getID().equals("init")) {
                return latch;
            }
        }
        return null;
    }

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
        return inputLatches;
    }


    private HashMap<String, HashSet<Latch>> findLegalLatches() {
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

    private HashSet<Latch> findLatches() {
        HashSet<Latch> latches = new HashSet<>();
        for (PropNetNode prop : propNetNodes) {
            if (prop instanceof Latch) {
                latches.add((Latch) prop);
            }
        }
        return latches;
    }


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