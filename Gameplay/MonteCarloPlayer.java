package Gameplay;

import DescriptionProcessing.Player;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static oracle.jrockit.jfr.events.Bits.doubleValue;

/**
 * Created by siavj on 31/01/2017.
 */
public class MonteCarloPlayer extends PropnetPlayer {

    private int count;


    public MonteCarloPlayer(){
        super();
        count = 1;
    }

    public String makeMove(){

        Player myrole = null;
        for (Player p : getRoles()){
            if (p.toString().equals(getMyRole())){
                myrole = p;
                break;
            }
        }

        if (getLegalMoves(getContents(), myrole).size() == 1){
            return getLegalMoves(getContents(), myrole).get(0);
        }


        double timeLimit = 20000;
        double start = System.currentTimeMillis();
        double finishBy = start + timeLimit;


        Node root;
        Node selected;
        HashMap<Player, Double> estScore;


        root = new Node(new HashSet<>(getContents()), myrole);
        while(System.currentTimeMillis() < finishBy){

            selected = selection(root);

            if (!isTerminal(selected.getContents())){
                expand(selected);
                expand(selected);
                estScore = monteCarlo(selected, count);
            }

            else
                estScore = drillDown(selected.getContents());

            backpropagate(selected, estScore);

        }

        int first = ThreadLocalRandom.current().nextInt(root.getChildren().size());
        String bestMove = root.getChildren().get(first).getMove();
        double bestMoveScore = root.getChildren().get(first).getVisits();
        for (Node n: root.getChildren()){
            if (n.getVisits() > bestMoveScore) {
                bestMoveScore = n.getVisits();
                bestMove = n.getMove();
            }
        }

        return bestMove;
    }

    public HashMap<Player, Double> monteCarlo(Node selected, int count) {

        return drillDown(selected.getContents());

    }


    private HashMap<Player, Double> drillDown(HashSet<String> rndContents){


        while(!isTerminal(rndContents)) {
            rndContents = randomNextState(rndContents);
        }


        HashMap<Player, Double> rewards = new HashMap<>();
        for (Player p: getRoles()){
            if (p.toString().equals("RANDOM"))
                rewards.put(p, 0.0);
            else{
                rewards.put(p, doubleValue(getGoal(rndContents, p)));
            }
        }

        return rewards;
    }

    private HashSet<String> randomNextState(HashSet<String> contents, String move, Player moveMaker) {

        ArrayList<String> temp;
        ArrayList<String> moves = new ArrayList<>();
        for (Player player : getRoles()){
            if (player == moveMaker){
                moves.add(move);
            }
            else {
                //could put null here for random
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
            moves.add(temp.get(ThreadLocalRandom.current().nextInt(temp.size())));
        }

        return getNextState(contents, moves);
    }


    private Node selection (Node node) {

        if (  node.getVisits() == 0 || isTerminal(node.getContents()) )
            return node;

        for (int i=0; i<node.getChildren().size(); i++) {

            if (node.getChildren().get(i).getVisits() == 0)
                return node.getChildren().get(i);

        }

        double score = Double.NEGATIVE_INFINITY, newscore, total = 0.0;
        ArrayList<Double> scores = new ArrayList<>();
        Node result = node;

        for (int i=0; i<node.getChildren().size(); i++) {

            newscore = selectfn(node.getChildren().get(i));
            scores.add(newscore);
            total += newscore;
//
            if (newscore > score) {
                score = newscore;
                result = node.getChildren().get(i);
            }
        }
//
        Double count = 0.0;
        Double randomNum = ThreadLocalRandom.current().nextDouble(0, total + 1);
        for (int i = 0; i < node.getChildren().size(); i++){
            count += scores.get(i);
            if (count >= randomNum){
                result = node.getChildren().get(i);
                break;
            }
        }

        return selection(result);
    }

    private double selectfn(Node node) {
        return node.getScore()/node.getVisits() + Math.sqrt(2*Math.log(node.getParent().getVisits())/node.getVisits());
    }

    private boolean expand (Node node) {


        Player moveMaker = getPlayer(node.getContents());
        ArrayList<String> moves = getLegalMoves(node.getContents(), moveMaker);

            for (int i=0; i < moves.size(); i++) {
                HashSet<String> newstate = randomNextState(node.getContents(), moves.get(i), moveMaker);
                Node newnode = new Node(newstate, node, moves.get(i), moveMaker);
            }

        return true;
    }

    private boolean backpropagate (Node node, HashMap<Player, Double> score) {
        node.setVisits(node.getVisits()+1);

        if (node.getVisits() > 1)
            node.setScore(node.getScore() + score.get(node.getMoveMaker()));

        else {
            node.setScore(score.get(node.getMoveMaker()));
        }

        if (node.getParent() != null){
            backpropagate(node.getParent(), score);
        }


        return true;
    }


}
