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
    private boolean isMax = true;
    private double propagateValue = Double.NEGATIVE_INFINITY;
    private Player moveMaker;

    public boolean isMax() {
        return isMax;
    }

    public void setMax(boolean max) {
        isMax = max;
    }


    public Node(HashSet<String> state){
        this.contents = state;
        children = new ArrayList<>();
//        this.moveMaker = moveMaker;
    }

    public Node(HashSet<String> state, Node myParent, String move){
        myParent.getChildren().add(this);
        this.parent = myParent;
        this.contents = state;
        this.move = move;
        children = new ArrayList<>();
        isMax = !myParent.isMax;
//        this.moveMaker = moveMaker;
    }

    public ArrayList<Node> getChildren(){
        return children;
    }

    public Player getMoveMaker() {
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

    public double getPropagateValue(double newVal){
        if (propagateValue == Double.NEGATIVE_INFINITY)
            propagateValue = newVal;

        else if (isMax && newVal > propagateValue)
            propagateValue = newVal;

        else if (!isMax && newVal < propagateValue)
            propagateValue = newVal;

        return propagateValue;
    }
}
