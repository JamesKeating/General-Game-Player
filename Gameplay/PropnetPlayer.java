package Gameplay;

import DescriptionProcessing.Player;
import DescriptionProcessing.PropNet;
import DescriptionProcessing.PropNetBuilder;
import DescriptionProcessing.PropNetComponents.Latch;
import DescriptionProcessing.PropNetComponents.PropNetNode;
import GDLTokens.KeyWordToken;
import GDLTokens.Token;
import GUI.Drawable;
import DeductiveDatabase.Description;
import DeductiveDatabase.Fact;


import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by siavj on 12/01/2017.
 */

/**
 * Most basic type of player
 * Makes moves entirely randomly
 * Implements all essential functions for a player
 * All other players are extensions of this class and implement more sophisticated move selection
 */

public class PropnetPlayer  implements Gamer{

    private PropNet propNet;
    private String myRole;
    private ArrayList<Latch> ordering;
    private ArrayList<Player> roles;

    public String getMyRole() {
        return myRole;
    }
    public void setMyRole(String myRole) {
        this.myRole = myRole;
    }
    public PropNet getPropNet() {
        return propNet;
    }
    public HashSet<String> getContents() {
        return contents;
    }

    public void setContents(HashSet<String> contents) {
        this.contents = contents;
        contents_list.add(contents);
    }

    public void undo(){
        if (contents_list.size() > 0) {
            contents_list.remove(contents_list.size() - 1);
            setContents(contents_list.get(contents_list.size() - 1));
        }
    }

    public void restart(){
        if (contents_list.size() > 0) {
            setContents(contents_list.get(0));
            contents_list.clear();
            contents_list.add(contents);
        }
    }

    private HashSet<String> contents = new HashSet<>();
    private ArrayList<HashSet<String>> contents_list = new ArrayList<>();


    public void initialize(PropNet propNet) {
        this.propNet = propNet;
        roles = propNet.getRoles();
        ordering = getOrdering();
        setContents(getInitialState());
    }

    public void initialize(ArrayList<Description> description) {

        PropNetBuilder builder= new PropNetBuilder();
        propNet = builder.create(description);
        roles = propNet.getRoles();
        ordering = getOrdering();
        setContents(getInitialState());

    }

    //computes a topological orderingof the graph
    public ArrayList<Latch> getOrdering() {

        ArrayList<Latch> order = new ArrayList<>();
        ArrayList<PropNetNode> propNetNodes = new ArrayList<>(propNet.getPropNetNodes());
        ArrayList<Latch> latches = new ArrayList<>(propNet.getLatches());

        propNetNodes.removeAll(propNet.getBaseLatches().values());
        propNetNodes.removeAll(propNet.getInputLatches().values());//remove?
        propNetNodes.remove(propNet.getInitLatches());

        while (!propNetNodes.isEmpty()) {

            ArrayList<PropNetNode> propNetNodesCopy = new ArrayList<>(propNetNodes);
            for ( PropNetNode node : propNetNodes ) {

                boolean inputsSatisfied = true;
                for ( PropNetNode input : node.getNodeInputs() ) {

                    if ( propNetNodes.contains(input) ) {
                        inputsSatisfied = false;
                        break;
                    }
                }
                if (inputsSatisfied) {
                    propNetNodesCopy.remove(node);
                    if (latches.contains(node)){
                        order.add((Latch) node);
                    }
                }
            }
            propNetNodes = propNetNodesCopy;
        }
        return order;
    }


    public Integer getGoal(HashSet<String> state, Player role) {
        SetBasePropositions(state);
        Propagate();

        for (Latch latch: propNet.getGoalLatches().get(role.toString())) {
            if (latch.getValue()) {
                return Integer.parseInt(latch.getLabel().getFact().get(3).getID());
            }
        }
        return 0;
    }

    public ArrayList<Drawable> getDrawable(HashSet<String> state){
        ArrayList<Drawable> drawables = new ArrayList<>();
        SetBasePropositions(state);
        Propagate();

        int x, y;
        String file;
        for (Latch latch :propNet.getDrawLatches()){

            if (latch.getValue()){
                x = Integer.valueOf(latch.getLabel().getFact().get(3).toString());
                y = Integer.valueOf(latch.getLabel().getFact().get(4).toString());
                file = latch.getLabel().getFact().get(5).toString();

                drawables.add(new Drawable(x, y, file));
            }

        }

        return drawables;
    }

    public boolean isTerminal(HashSet<String> state) {
        SetBasePropositions(state);
        Propagate();
        return propNet.getTerminalLatches().getValue();
    }

    public ArrayList<Player> getRoles() {
        return roles;
    }

    public HashSet<String> getInitialState() {
        HashSet<String> contents = new HashSet<>();

        propNet.getInitLatches().setValue(true);

        for (Latch latch: propNet.getBaseLatches().values()) {
            if (latch.getSingleInput().getValue()) {
                contents.add(latch.getLabel().toString());
            }
            latch.setValue(latch.getSingleInput().getValue());
        }

        propNet.getInitLatches().setValue(false);
        return contents;
    }


    public ArrayList<String> getLegalMoves(HashSet<String> state, Player role) {
        ArrayList<String> moves = new ArrayList<>();
        SetBasePropositions(state);
        Propagate();


        for (Latch latch: propNet.getLegalLatches().get(role.toString())) {


            if (latch.getValue()) {
                ArrayList<Token> move = new ArrayList<>();
                for (Token token : latch.getLabel().getFact()){
                    if (!token.getID().equals("legal"))
                        move.add(token);
                    else
                        move.add(new KeyWordToken("does"));
                }
                moves.add(new Fact(move).toString());
            }
        }

        return moves;
    }



    public HashSet<String> getNextState(HashSet<String> state, ArrayList<String> moves) {
        HashSet<String> contents = new HashSet<>();
        SetBasePropositions(state);
        SetInputPropositions(moves);
        Propagate();

        for (Latch latch: propNet.getBaseLatches().values()) {
            if (latch.getSingleInput().getValue()) {
                contents.add(latch.getLabel().toString());
            }
        }
        return contents;
    }

    private void SetBasePropositions(HashSet<String> state) {

        for (Latch latch: propNet.getBaseLatches().values()) {
            if (state.contains(latch.getLabel().toString())) {
                latch.setValue(true);
            } else {
                latch.setValue(false);
            }
        }
    }

    private void SetInputPropositions( ArrayList<String> moves) {

        for (Latch input : propNet.getInputLatches().values()){
            input.setValue(moves.contains(input.getLabel().toString()));
        }
    }

    private void Propagate() {
        for (Latch latch: ordering) {
            if (latch.getNodeInputs().size() == 1)
                latch.setValue(latch.getSingleInput().getValue());
        }
    }

    public String makeMove() {

        ArrayList<String> temp;

        for (Player player : getRoles()){
            if (player.toString().equals(myRole)){
                temp = getLegalMoves(contents, player);
                if (temp.size() > 0)
                    return temp.get(ThreadLocalRandom.current().nextInt(0, temp.size()));
            }
        }

        return null;
    }


    public boolean allAI(){
        for (Player player : getRoles()){
            if (player.isHuman())
                return false;
        }
        return true;
    }

    public Player getPlayer(HashSet<String> contents){

        ArrayList<String> moves;
        Player temp = null;
        for (Player p : getRoles()){
            moves = getLegalMoves(contents, p);

            if (moves.size() > 1 ||(moves.size() == 1 && !moves.get(0).contains("noop"))) {
                if (temp == null)
                    temp = p;
                else
                    return null;
            }
        }
        return temp;
    }
}
