package Player;

import DescriptionProcessing.Player;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by siavj on 11/02/2017.
 */
public class Node {

    private HashSet<String> contents;
    private ArrayList<Node> children;
    private Node parent;
    private int visits;
    private double score;
    private String move;
    private double propagateValue = Double.POSITIVE_INFINITY;

    public void setMoveMaker(Player moveMaker) {
        this.moveMaker = moveMaker;
    }

    private Player moveMaker;

    public Node(HashSet<String> state, Player moveMaker){
        this.contents = state;
        children = new ArrayList<>();
        this.moveMaker = moveMaker;
    }

    public Node(HashSet<String> state, Node myParent, String move, Player moveMaker){
        myParent.getChildren().add(this);
        this.parent = myParent;
        this.contents = state;
        this.move = move;
        children = new ArrayList<>();
        this.moveMaker = moveMaker;
    }

    public ArrayList<Node> getChildren(){
        return children;
    }

    public Player getMoveMaker() {
        if (moveMaker == null)
            return this.getParent().getMoveMaker();

        return moveMaker;
    }


    public Node getParent(){
        return parent;
    }

    public int getVisits(){
        return visits;
    }

    public void setVisits(int visits){
        this.visits = visits;
    }

    public double getScore(){
        return score;
    }

    public void setScore(double score){
        this.score = score;
    }

    public HashSet<String> getContents(){
        return contents;
    }

    public String getMove(){
        return move;
    }


}
