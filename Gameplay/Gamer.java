package Gameplay;

import DescriptionProcessing.Player;
import DescriptionProcessing.PropNet;
import DeductiveDatabase.Description;

import java.util.ArrayList;
import java.util.HashSet;

public interface Gamer {

    public String makeMove();
    public void initialize(ArrayList<Description> description);
    public void initialize(PropNet description);
    public void setMyRole(String role);
    public void restart();
    public String getMyRole();
    public void setContents(HashSet<String> contents);
    public ArrayList<String> getLegalMoves(HashSet<String> state, Player role);
    public HashSet<String> getContents();
}