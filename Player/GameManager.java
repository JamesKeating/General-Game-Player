package Player;

import DescriptionProcessing.Player;
import DescriptionProcessing.PropNet;
import GDLParser.LexicalAnalyser;
import GUI.Grahpics;

import java.io.*;

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

    public GameManager(ArrayList<Gamer> players){
        for (Gamer player : players){
            gamers.add(player);
        }
        gameManager.setMyRole("RANDOM");

    }



    public void setupGame(DescriptionTable dt, ArrayList<String> roles){
        gameManager.initialize(dt.listTable());


//        try {
//            FileOutputStream f = new FileOutputStream(new File("myObjects.txt"));
//
//            ObjectOutputStream o = new ObjectOutputStream(f);
//
//            // Write objects to file
//            o.writeObject(gameManager.getPropNet());
//            o.close();
//            f.close();
//
//            for (int i = 0; i < gamers.size(); i++){
//                FileInputStream fi = new FileInputStream(new File("myObjects.txt"));
//                ObjectInputStream oi = new ObjectInputStream(fi);
//                PropNet propNet = (PropNet) oi.readObject();
//                gamers.get(i).initialize(propNet);
//                gamers.get(i).setMyRole(roles.get(i));
//                oi.close();
//                fi.close();
//
//            }
//
//
//        } catch (Exception e) {

//        }
        for (int i = 0; i < gamers.size(); i++){
            gamers.get(i).initialize(dt.listTable());
            gamers.get(i).setMyRole(roles.get(i));
        }

    }

    public PropnetPlayer updateGame(){

        ArrayList<String> moves = new ArrayList<>();
        for (Player role: gameManager.getRoles()){

            for(Gamer gamer : gamers){
                if (role.toString().equals(gamer.getMyRole())){
                    moves.add(gamer.makeMove());
                    System.out.println(moves);

                }
            }
            if (role.toString().equals("RANDOM")){
                moves.add(gameManager.makeMove());
            }
        }
        System.out.println(moves + " tagh");

//        System.out.println(moves + " 1");
//        System.out.println(gameManager.getContents()+ " 2");
        gameManager.setContents(gameManager.getNextState(gameManager.getContents(), moves));
//        System.out.println(gameManager.getContents() + " 3");


        for (Gamer gamer: gamers){
            gamer.setContents(gameManager.getContents());
        }

        System.out.println(gameManager.getContents() + " ----here");
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
                    System.out.println(role + " - " +role.isHuman());
                    moves.put(role, gamer.getLegalMoves(gamer.getContents(), role));
                }
                else if (role.toString().equals("RANDOM")){
                    moves.put(role, gamer.getLegalMoves(gameManager.getContents(), role));
                }
            }
        }
        System.out.println(moves + "all legal");
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
