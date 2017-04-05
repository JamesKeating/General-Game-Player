package Test;

import Gameplay.Gamer;
import Gameplay.MonteCarloPlayer;
import Gameplay.PropnetPlayer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by siavj on 04/04/2017.
 */

//runs tests to get the build time of the network and the players win rate vs a random ai
//can be easily modified for other games, runtimes, player, types ect.
public class PreformanceTestSuite {

    public static void main(String[] args) {
        String winRateOutputFile = "TestResults\\MCTSvsRandomResults.txt";
        String propnetOutputFile = "TestResults\\PropNetPerformaceResults.txt";

        Gamer mcts = new MonteCarloPlayer();
        Gamer rand = new PropnetPlayer();

        String[] tictactoe = {"Data\\TicTacToe", "oplayer", "xplayer"};
        String[] horseShoe = {"Data\\Horseshoe", "hsBlue", "hsRed"};
        String[] duikoshi = {"Data\\Duikoshi", "green", "red"};
        String[] blindTictactoe = {"Data\\BlindTicTacToe", "oplayer", "xplayer"};
        String[] threePuzzle = {"Data\\3-Puzzle", "player"};
        String[] eightPuzzle = {"Data\\8-Puzzle", "player"};
        String[] eotCatCit = {"Data\\EotCatCit", "oplayer", "xplayer"};
        String[] lightPuzzle = {"Data\\LightPuzzle", "player"};
        String[] highLow = {"Data\\HighLow", "player"};

        String[][] winRateGames = { tictactoe, horseShoe, duikoshi, blindTictactoe};
        String[][] propNetRateGames = {tictactoe, horseShoe, duikoshi, blindTictactoe,
                threePuzzle, eightPuzzle, eotCatCit, lightPuzzle, highLow};



        WinRateTest test = new WinRateTest();
        PropNetPerformanceTest pTest = new PropNetPerformanceTest();


        try {
            PrintWriter writer = new PrintWriter(winRateOutputFile);
            //writer.print("");
            writer = new PrintWriter(propnetOutputFile);
            //writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//        for (String[] game : propNetRateGames) {
//            pTest.runTest(game[0], propnetOutputFile);
//        }

        double timelimit = 20000;
        System.out.println(timelimit);


        ArrayList<String> role = new ArrayList<>();
        for (String[] game : winRateGames) {
            role.add(game[1]);
            role.add(game[2]);
            ArrayList<Gamer> gamers = new ArrayList<>();
            gamers.add(new MonteCarloPlayer(timelimit));
            gamers.add(new PropnetPlayer());

            System.out.println(role);
            test.runTest(game[0], 100, winRateOutputFile, role, gamers);
            role.clear();
        }
    }


}
