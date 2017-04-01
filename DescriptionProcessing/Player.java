package DescriptionProcessing;

import GDLTokens.Token;
import DeductiveDatabase.Description;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by siavj on 09/01/2017.
 */
public class Player implements Serializable {

    private String name;

    public boolean isHuman() {
        return isHuman;
    }

    public void setHuman(boolean human) {
        isHuman = human;
    }

    private boolean isHuman = false;

    public Player(Token token){
        this.name = token.getID();
    }

    public static ArrayList<Player> listPlayers(ArrayList<Description> game){
        ArrayList<Player> playerList = new ArrayList<>();
        for (Description description : game){
            if (description.getLeadAtom().getID().equals("role"))
                playerList.add(new Player(description.getDescription().get(2)));

        }

        return playerList;
    }

    public String toString (){
        return name;
    }


}
