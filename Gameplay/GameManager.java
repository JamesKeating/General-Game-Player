package Gameplay;

import DescriptionProcessing.Player;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

/**
 * Created by siavj on 31/01/2017.
 */
public class GameManager{

    public PropnetPlayer getGameManager() {
        return gameManager;
    }

    private PropnetPlayer gameManager = new PropnetPlayer();
    private ArrayList<Gamer> gamers = new ArrayList<>();

    public GameManager(ArrayList<Gamer> players){
        for (Gamer player : players){
            gamers.add(player);
        }
        gameManager.setMyRole("RANDOM");

    }



    public void setupGame(PropnetPlayer propnetPlayer, ArrayList<String> roles){
        gameManager = propnetPlayer;

        for (int i = 0; i < gamers.size(); i++){
            gamers.get(i).initialize(gameManager.getPropNet());
            gamers.get(i).setMyRole(roles.get(i));
        }


    }

    public void restartGame(){
        gameManager.restart();

        for (int i = 0; i < gamers.size(); i++){
            gamers.get(i).restart();
        }


    }

    public PropnetPlayer updateGame(){

        ArrayList<String> moves = new ArrayList<>();
        for (Player role: gameManager.getRoles()){

            for(Gamer gamer : gamers){
                if (role.toString().equals(gamer.getMyRole()))
                    moves.add(gamer.makeMove());
            }

            if (role.toString().equals("RANDOM"))
                moves.add(gameManager.makeMove());

        }

        gameManager.setContents(gameManager.getNextState(gameManager.getContents(), moves));

        for (Gamer gamer: gamers){
            gamer.setContents(gameManager.getContents());
        }

        return gameManager;
    }

    public PropnetPlayer undo(){
        gameManager.undo();

        for (Gamer gamer: gamers){
            gamer.setContents(gameManager.getContents());
        }
        return gameManager;
    }

    public TreeMap<Player, ArrayList<String>> getAllCurrentLegalMoves(){
        TreeMap<Player, ArrayList<String>> moves = new TreeMap<>(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
        for (Player role: gameManager.getRoles()){
            for(Gamer gamer : gamers){

                if (role.toString().equals(gamer.getMyRole())){

                    if (HumanPlayer.class.isInstance(gamer)){
                        role.setHuman(true);
                    }

                    moves.put(role, gamer.getLegalMoves(gamer.getContents(), role));
                }
                else if (role.toString().equals("RANDOM")){
                    moves.put(role, gamer.getLegalMoves(gameManager.getContents(), role));
                }
            }
        }

        return moves;
    }

    public HumanPlayer getGamer(Player player){
        for (Gamer gamer: gamers){
            if (gamer.getMyRole().equals(player.toString()))
                return (HumanPlayer) gamer;
        }

        return null;
    }

}
