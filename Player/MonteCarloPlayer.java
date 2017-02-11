package Player;

import DescriptionProcessing.Player;
import DescriptionProcessing.PropNetComponents.Latch;
import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by siavj on 31/01/2017.
 */
public class MonteCarloPlayer extends PropnetPlayer {

    private int count;
    private Player myRole = null;

    public MonteCarloPlayer(){
        super();
        count = 4;
    }

    public String makeMove(){

        for (Player player : getRoles()) {
            if (player.toString().equals(getMyRole())) {
                myRole = player;
                break;
            }
        }

        double timeLimit = 5000;
        double start = System.currentTimeMillis();
        double finishBy = start + timeLimit - 1000;

        Node root = new Node(getContents());
        Node selected;
        double estScore;


        while(System.currentTimeMillis() < finishBy){

            selected = selection(root);
            expand(selected);
            estScore = monteCarlo(selected.getContents(), myRole, count);
            backpropagate(selected, estScore);

        }

        String bestMove = root.getChildren().get(0).getMove();
        double bestMoveScore = root.getChildren().get(0).getScore();
        for (Node n: root.getChildren()){
            if (n.getScore() > bestMoveScore) {
                bestMoveScore = n.getScore();
                bestMove = n.getMove();
            }
        }

        return bestMove;

    }

    public double monteCarlo(HashSet<String> contents, Player player, int count) {


        int moveTotalPoints = 0;
        int moveTotalAttempts;
        for (moveTotalAttempts = 0; moveTotalAttempts < count; moveTotalAttempts++) {
            //time limit
            moveTotalPoints += drillDown(contents, player);
        }
        return moveTotalPoints / moveTotalAttempts;

    }




    private int drillDown(HashSet<String> rndContents, Player targetPlayer){//int[]theDepth
        //int nDepth = 0;
        while(!isTerminal(rndContents)) {
            //nDepth++;
            rndContents = randomNextState(rndContents);
        }
//        if(theDepth != null)
//            theDepth[0] = nDepth;
//
        return getGoal(rndContents, targetPlayer);
    }

    private HashSet<String> randomNextState(HashSet<String> contents, String move) {

        ArrayList<String> temp;
        ArrayList<String> moves = new ArrayList<>();
        for (Player player : getRoles()){
            if (player.toString().equals(getMyRole())){
                moves.add(move);
            }
            else {
                temp = getLegalMoves(contents, player);
                moves.add(temp.get(ThreadLocalRandom.current().nextInt(0, temp.size())));
            }
        }

        return getNextState(contents, moves);
    }

    private HashSet<String> randomNextState(HashSet<String> contents) {

        ArrayList<String> temp;
        ArrayList<String> moves = new ArrayList<>();
        for (Player player : getRoles()){

            temp = getLegalMoves(contents, player);
//            System.out.println("legal: " +player + "---"+temp);
//            System.out.println("terminal: " + isTerminal(contents) + "-- "+ contents);
            moves.add(temp.get(ThreadLocalRandom.current().nextInt(temp.size())));

        }

        return getNextState(contents, moves);
    }


    private Node selection (Node node) {

        if (  node.getVisits() == 0 ) {
            return node;
        }

        for (int i=0; i<node.getChildren().size(); i++) {

            if (node.getChildren().get(i).getVisits() == 0) {
                return node.getChildren().get(i);
            }
        }

        double score = 0, newscore;
        Node result = node;

        for (int i=0; i<node.getChildren().size(); i++) {

            newscore = selectfn(node.getChildren().get(i));

            if (newscore > score) {
                score = newscore;
                result=node.getChildren().get(i);
            }
        }

        return selection(result);
    }

    private double selectfn(Node node) {
        return node.getScore() + Math.sqrt(2*Math.log(node.getParent().getVisits())/node.getVisits());
    }

    private boolean expand (Node node) {
        ArrayList<String> moves = getLegalMoves(node.getContents(), myRole);

        for (int i=0; i < moves.size(); i++) {
            HashSet<String> newstate = randomNextState(node.getContents(), moves.get(i));
            Node newnode = new Node(newstate, node, moves.get(i));
        }

        return true;
    }

    private boolean backpropagate (Node node, double score) {
        node.setVisits(node.getVisits()+1);
        node.setScore(node.getScore() + score);

        if (node.getParent() != null){
            backpropagate(node.getParent(), score);
        }

        return true;
    }



//    private int maxscore(Player role, HashSet<String> contents) {
//
//        if(isTerminal(contents))
//            return getGoal(contents, role);
//
//
//        ArrayList<String> actions = getLegalMoves(contents, role);
//        int score = 0;
//
//        for (int i = 0; i < actions.size(); i++) {
//            ArrayList<String> ourAction = new ArrayList<String>();
//            ourAction.add(actions.get(i));
//            int result = maxscore(role, getNextState(contents, actions));
//            if (result > score) {
//                score = result;
//            }
//        }
//        return score;
//    }

}
