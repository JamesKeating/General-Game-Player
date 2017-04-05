package Gameplay;

/**
 * Created by siavj on 31/01/2017.
 */

//simple extention for human player to allow there moves to be selected
public class HumanPlayer extends PropnetPlayer {
    public void setSelectedMove(String selectedMove) {
        this.selectedMove = selectedMove;
    }

    private String selectedMove;

    @Override
    public String makeMove(){
        return selectedMove;
    }
}
