package Player;

import DescriptionProcessing.Player;
import GDLParser.LexicalAnalyser;
import GUI.Grahpics;
import SylmbolTable.DescriptionTable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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

    public GameManager(Gamer...players){
        for (Gamer player : players){
            gamers.add(player);
        }


    }

    public void setupGame(String game_description_filename, String[] roles){

        LexicalAnalyser l = new LexicalAnalyser();
        l.analyseFile(game_description_filename);


        DescriptionTable dt = new DescriptionTable(l.getTokenStream());

        gameManager.initialize(dt.listTable());
        for (int i = 0; i < gamers.size(); i++){
            gamers.get(i).initialize(dt.listTable());
            gamers.get(i).setMyRole(roles[i]);
        }

    }

    public PropnetPlayer updateGame(){

        ArrayList<String> moves = new ArrayList<>();
        for (Player role: gameManager.getRoles()){
            for(Gamer gamer : gamers){
                if (role.toString().equals(gamer.getMyRole())){
                    moves.add(gamer.makeMove());
                }
            }
        }


        gameManager.setContents(gameManager.getNextState(gameManager.getContents(), moves));
        System.out.println(gameManager.getContents() + " cont");

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

                    if (HumanPlayer.class.isInstance(gamer))
                        role.setHuman(true);

                    moves.put(role, gamer.getLegalMoves(gamer.getContents(), role));
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
