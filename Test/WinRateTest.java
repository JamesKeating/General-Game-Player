package Test;

import DeductiveDatabase.DescriptionTable;
import DescriptionProcessing.Player;
import GDLParser.LexicalAnalyser;
import Gameplay.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by siavj on 03/04/2017.
 */
public class WinRateTest {


    public void runTest(String gameDirectory, int iterations, String outputDirectory, ArrayList<String> roles, ArrayList<Gamer> gamers){
        int win = 0, draw = 0, lose = 0;

        BufferedWriter bw;
        FileWriter fw;
        LexicalAnalyser l = new LexicalAnalyser();
        l.analyseFile(gameDirectory);
        DescriptionTable dt = new DescriptionTable(l.getTokenStream());
        PropnetPlayer gm = new PropnetPlayer();

        gm.initialize(dt.listTable());

        GameManager gameManager = new GameManager(gamers);
        gameManager.setupGame(gm, roles);


        for(int i = 0, score = 0; i < iterations; i++) {

            while(!gameManager.getGameManager().isTerminal(gameManager.getGameManager().getContents())){
                gameManager.updateGame();
            }

            for (Player player : gm.getRoles()){
                if (player.toString().equals(gamers.get(0).getMyRole()))
                    score = gameManager.getGameManager().getGoal(gameManager.getGameManager().getContents(), player);
            }

            switch (score){

                case 100:
                    win++;
                    break;

                case 50:
                    draw++;
                    break;

                case 0:
                    lose++;
                    break;

            }
            System.out.println("win " +win + " draw: " + draw + " lose: " + lose);
            gameManager.restartGame();

        }

        try {


            String content = gamers.get(0).getClass().toString()+" played "+ iterations + " games of "+
                    gameDirectory +" vs "
                    +gamers.get(1).getClass().toString() + "\n"+ gamers.get(0).getClass().toString()+
                    "  Won: " +win +" Drew: "+ draw+" Lost: " + lose + "\n\n";
            fw = new FileWriter(outputDirectory, true);
            bw = new BufferedWriter(fw);
            System.out.println(content);
            bw.write(content);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }




}

