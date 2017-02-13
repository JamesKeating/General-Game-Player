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


    public MonteCarloPlayer(){
        super();
        count = 5;
    }

    public String makeMove(){

        Player myrole = null;
        for (Player p : getRoles()){
            if (p.toString().equals(getMyRole())){
                myrole = p;
                break;
            }
        }

        if (getLegalMoves(getContents(), myrole).size() == 1)
            return getLegalMoves(getContents(), myrole).get(0);

        double timeLimit = 5000;
        double start = System.currentTimeMillis();
        double finishBy = start + timeLimit - 1000;


        Node root;
        Node selected;
        double estScore;


        root = new Node(new HashSet<>(getContents()));
        while(System.currentTimeMillis() < finishBy){

//            System.out.println("root:  " + root.getMove() + "--- --- "+ root.getContents());

            selected = selection(root);
//            System.out.println("selected:  " + selected.getMove() + "--- --- "+ selected.getContents());
            if (!isTerminal(selected.getContents())){
//                System.out.println("not terminal apparently: " + selected.getContents());
                expand(selected);
                estScore = monteCarlo(selected, count);
            }

            else {
                System.out.println(selected.getMove() +" -" + getPlayer(selected.getParent().getContents()));
                estScore = drillDown(selected.getContents(), getPlayer(selected.getParent().getContents()));
            }

//            System.out.println(estScore);
            backpropagate(selected, estScore);

        }

//        System.out.println(start -System.currentTimeMillis());
        String bestMove = root.getChildren().get(0).getMove();
        double bestMoveScore = root.getChildren().get(0).getScore()/ root.getChildren().get(0).getVisits();
        for (Node n: root.getChildren()){
            if (n.getScore()/n.getVisits() > bestMoveScore) {
                bestMoveScore = n.getScore()/n.getVisits();
                bestMove = n.getMove();
            }
        }

        return bestMove;

    }

    public double monteCarlo(Node selected, int count) {

//        HashSet<String> contents = new HashSet<>(selected.getContents());
        int moveTotalPoints = 0;
        int moveTotalAttempts;

        Player targetPlayer = getPlayer(selected.getContents());


//        System.out.println(targetPlayer + " ismax = "+selected.isMax());
        for (moveTotalAttempts = 0; moveTotalAttempts < count; moveTotalAttempts++) {


            moveTotalPoints += drillDown(selected.getContents(), targetPlayer);

        }


        return moveTotalPoints/moveTotalAttempts;

    }


    private int drillDown(HashSet<String> rndContents, Player targetPlayer){


        while(!isTerminal(rndContents)) {
            rndContents = randomNextState(rndContents);
        }


        if (targetPlayer.toString().equals(getMyRole())){
            return getGoal(rndContents, targetPlayer);

        }
        else
            return -getGoal(rndContents, targetPlayer);

    }

    private HashSet<String> randomNextState(HashSet<String> contents, String move, Player moveMaker) {

        ArrayList<String> temp;
        ArrayList<String> moves = new ArrayList<>();
        for (Player player : getRoles()){
            if (player == moveMaker){
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

        if (  node.getVisits() == 0 || isTerminal(node.getContents()) ) {
//            System.out.println("root or terminal");
            return node;
        }

        for (int i=0; i<node.getChildren().size(); i++) {

            if (node.getChildren().get(i).getVisits() == 0) {
//                System.out.println(node.getChildren().get(i).getMove() + " unvisted child");
                return node.getChildren().get(i);
            }

        }


        double score = Double.NEGATIVE_INFINITY, newscore;
        Node result = node;

        for (int i=0; i<node.getChildren().size(); i++) {

            newscore = selectfn(node.getChildren().get(i));

            if (newscore > score) {
                score = newscore;
                result = node.getChildren().get(i);
            }
        }

//        System.out.println("children of root all seen: -checking" + result.getMove() + "-------"+ result.getContents());
        return selection(result);
    }

    private double selectfn(Node node) {
        return node.getScore() + Math.sqrt(2*Math.log(node.getParent().getVisits())/node.getVisits());
    }

    private boolean expand (Node node) {


        Player moveMaker = getPlayer(node.getContents());
        ArrayList<String> moves = getLegalMoves(node.getContents(), moveMaker);

//        System.out.println("expanded state: "+ node.getContents().size() + " -- "+ node.getContents());

            for (int i=0; i < moves.size(); i++) {
                HashSet<String> newstate = randomNextState(node.getContents(), moves.get(i), moveMaker);
//                System.out.println("expanded state new " + newstate.size() + " : " + newstate);
                Node newnode = new Node(newstate, node, moves.get(i));
            }


        return true;
    }

    private boolean backpropagate (Node node, double score) {
        node.setVisits(node.getVisits()+1);

        if (isTerminal(node.getContents()))
            System.out.println(score + "- "+ node.isMax() + "- "+node.getMove()+ "- " + node.getContents());

        if (node.getVisits() > 1)
            node.setScore(node.getScore() +node.getPropagateValue(score));
        else
            node.setScore(score);

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
