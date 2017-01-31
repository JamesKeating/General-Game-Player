package Player;

/**
 * Created by siavj on 31/01/2017.
 */
public class HumanPlayer extends PropnetPlayer {
//
    public void setSelectedMove(String selectedMove) {
        this.selectedMove = selectedMove;
    }

    private String selectedMove;

    @Override
    public String makeMove(){
        return selectedMove;
    }
}
