package Player;

import DescriptionProcessing.Player;
import DescriptionProcessing.PropNet;
import DescriptionProcessing.PropNetBuilder;
import DescriptionProcessing.PropNetComponents.Latch;
import DescriptionProcessing.PropNetComponents.PropNetNode;
import GDLTokens.KeyWordToken;
import GDLTokens.Token;
import SylmbolTable.Description;
import SylmbolTable.Fact;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by siavj on 12/01/2017.
 */
public class PropnetPlayer implements Gamer {

    /** The underlying proposition network  */
    private PropNet propNet;
    /** The topological ordering of the propositions */
    private ArrayList<Latch> ordering;
    /** The player roles */
    private ArrayList<Player> roles;

    public String getMyRole() {
        return myRole;
    }

    public void setMyRole(String myRole) {
        this.myRole = myRole;
    }

    private String myRole;

    public HashSet<String> getContents() {
        return contents;
    }

    public void setContents(HashSet<String> contents) {
        this.contents = contents;
    }

    private HashSet<String> contents = new HashSet<>();


    public void initialize(ArrayList<Description> description) {


        PropNetBuilder builder= new PropNetBuilder();
        propNet = builder.create(description);
        roles = propNet.getRoles();
        ordering = getOrdering();
        contents = getInitialState();
//        System.out.println("initial: "+ contents);


//
//        System.out.println("game terminated: \n" + roles.get(0) + " score = " + getGoal(contents, roles.get(0)));
////        + "\n" + roles.get(1) + " score = " + getGoal(contents, roles.get(1)));

    }

    public ArrayList<Latch> getOrdering() {

        ArrayList<Latch> order = new ArrayList<>();
        ArrayList<PropNetNode> propNetNodes = new ArrayList<>(propNet.getPropNetNodes());
        ArrayList<Latch> latches = new ArrayList<>(propNet.getLatches());
//        System.out.println(" ones to be removeed : " +propNet.getBaseLatches().values().size());
//        System.out.println("total = " + propNetNodes.size() );


        propNetNodes.removeAll(propNet.getBaseLatches().values());
        propNetNodes.removeAll(propNet.getInputLatches().values());
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


    public int getGoal(HashSet<String> state, Player role) {
        SetBasePropositions(state);
        Propagate();

        for (Latch latch: propNet.getGoalLatches().get(role.toString())) {
            if (latch.getValue()) {
                return Integer.parseInt(latch.getLabel().getFact().get(3).getID());
            }
        }
        return 0;
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
                return temp.get(ThreadLocalRandom.current().nextInt(0, temp.size()));
            }
        }

        return null;
    }
}
