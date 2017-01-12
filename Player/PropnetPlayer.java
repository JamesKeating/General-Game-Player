package Player;

import DescriptionProcessing.Player;
import DescriptionProcessing.PropNet;
import DescriptionProcessing.PropNetBuilder;
import DescriptionProcessing.PropNetComponents.Latch;
import DescriptionProcessing.PropNetComponents.PropNetNode;
import SylmbolTable.Description;
import SylmbolTable.Fact;

import java.util.*;

/**
 * Created by siavj on 12/01/2017.
 */
public class PropnetPlayer {

    /** The underlying proposition network  */
    private PropNet propNet;
    /** The topological ordering of the propositions */
    private ArrayList<Latch> ordering;
    /** The player roles */
    private ArrayList<Player> roles;

    private HashSet<Fact> contents;

    /**
     * Initializes the PropNetStateMachine. You should compute the topological
     * ordering here. Additionally you may compute the initial state here, at
     * your discretion.
     */

    public void initialize(ArrayList<Description> description) {
        try {

            PropNetBuilder builder = new PropNetBuilder();
            propNet = builder.create(description);
            roles = propNet.getRoles();
            ordering = getOrdering();
            getInitialState();
            System.out.println(contents);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Computes if the state is terminal. Should return the value
     * of the terminal proposition for the state.
     */

    public boolean isTerminal() {
        // TODO: Compute whether the MachineState is terminal.
        return false;
    }

    /**
     * Computes the goal for a role in the current state.
     * Should return the value of the goal proposition that
     * is true for that role. If there is not exactly one goal
     * proposition true for that role, then you should throw a
     * GoalDefinitionException because the goal is ill-defined.
     */

    public int getGoal(Player role){
            //throws GoalDefinitionException {
        // TODO: Compute the goal for role in state.
        return -1;
    }

    /**
     * Returns the initial state. The initial state can be computed
     * by only setting the truth value of the INIT proposition to true,
     * and then computing the resulting state.
     */

    public HashSet<Fact> getInitialState() {
        propNet.getInitLatches().setValue(true);

        for (PropNetNode latch : propNet.getInitLatches().getNodeOutputs()){
            System.out.println(latch.getSingleOutput());

        }
        return null;
    }

    /**
     * Computes the legal moves for role in state.
     */
//
//    public List<Move> getLegalMoves(MachineState state, Role role)
//            throws MoveDefinitionException {
//        // TODO: Compute legal moves.
//        return null;
//    }
//
//    /**
//     * Computes the next state given state and the list of moves.
//     */
//    @Override
//    public MachineState getNextState(MachineState state, List<Move> moves)
//            throws TransitionDefinitionException {
//        // TODO: Compute the next state.
//        return null;
//    }
//
//    /**
//     * This should compute the topological ordering of propositions.
//     * Each component is either a proposition, logical gate, or transition.
//     * Logical gates and transitions only have propositions as inputs.
//     *
//     * The base propositions and input propositions should always be exempt
//     * from this ordering.
//     *
//     * The base propositions values are set from the MachineState that
//     * operations are performed on and the input propositions are set from
//     * the Moves that operations are performed on as well (if any).
//     *
//     * @return The order in which the truth values of propositions need to be set.
     //
    public ArrayList<Latch> getOrdering()
    {
        // List to contain the topological ordering.
        ArrayList<Latch> order = new ArrayList<>();

        // All of the components in the PropNet
        ArrayList<PropNetNode> propNetNodes = new ArrayList<PropNetNode>(propNet.getPropNetNodes());

        // All of the propositions in the PropNet.
        ArrayList<Latch> propositions = new ArrayList<Latch>(propNet.getLatches());

        // TODO: Compute the topological ordering.

        return order;
    }

    /* Already implemented for you */
    public ArrayList<Player> getRoles() {
        return roles;
    }

    /* Helper methods */

    /**
     * The Input propositions are indexed by (does ?player ?action).
     *
     * This translates a list of Moves (backed by a sentence that is simply ?action)
     * into GdlSentences that can be used to get Propositions from inputPropositions.
     * and accordingly set their values etc.  This is a naive implementation when coupled with
     * setting input values, feel free to change this for a more efficient implementation.
     *
     * @param moves
     * @return
     */
//    private List<GdlSentence> toDoes(List<Move> moves)
//    {
//        List<GdlSentence> doeses = new ArrayList<GdlSentence>(moves.size());
//        Map<Role, Integer> roleIndices = getRoleIndices();
//
//        for (int i = 0; i < roles.size(); i++)
//        {
//            int index = roleIndices.get(roles.get(i));
//            doeses.add(ProverQueryBuilder.toDoes(roles.get(i), moves.get(index)));
//        }
//        return doeses;
//    }

    /**
     * Takes in a Legal Proposition and returns the appropriate corresponding Move
     * @param p
     * @return a PropNetMove
     */
//    public static Move getMoveFromProposition(Proposition p)
//    {
//        return new Move(p.getName().get(1));
//    }

    /**
     * Helper method for parsing the value of a goal proposition
     * @param goalProposition
     * @return the integer value of the goal proposition
     */
//    private int getGoalValue(Proposition goalProposition)
//    {
//        GdlRelation relation = (GdlRelation) goalProposition.getName();
//        GdlConstant constant = (GdlConstant) relation.get(1);
//        return Integer.parseInt(constant.toString());
//    }

    /**
     * A Naive implementation that computes a PropNetMachineState
     * from the true BasePropositions.  This is correct but slower than more advanced implementations
     * You need not use this method!
     * @return PropNetMachineState
     */



    public HashSet<Fact> getBaseContents()
    {
        HashSet<Fact> contents = new HashSet<>();
        for (Latch p : propNet.getBaseLatches().values()) {

            if (p.getNodeInputs().size() != 1){

                p.setValue(p.getNodeInputs().iterator().next().getValue());
                if (p.getValue())
                {
                    contents.add(p.getLabel());
                }
            }

        }
        return contents;
    }
}