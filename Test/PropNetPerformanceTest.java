package Test;

import DeductiveDatabase.DescriptionTable;
import DescriptionProcessing.Player;
import GDLParser.LexicalAnalyser;
import Gameplay.GameManager;
import Gameplay.Gamer;
import Gameplay.PropnetPlayer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by siavj on 04/04/2017.
 */

//test the propnets performace its build time number of nodes ect..
public class PropNetPerformanceTest {

    public void runTest(String gameDirectory, String outputDirectory){

        double startTime = System.currentTimeMillis();
        double statesPerMin =0 , buildTime;
        BufferedWriter bw;
        FileWriter fw;
        LexicalAnalyser l = new LexicalAnalyser();
        l.analyseFile(gameDirectory);
        DescriptionTable dt = new DescriptionTable(l.getTokenStream());
        PropnetPlayer gm = new PropnetPlayer();

        gm.initialize(dt.listTable());
        buildTime = System.currentTimeMillis() - startTime;
        int nodeCount = gm.getPropNet().getPropNetNodes().size();


        double limit = 0;
        while (limit < 5000) {
            statesPerMin++;
            ArrayList<String> temp;
            ArrayList<String> move = new ArrayList<>();
            for (Player player : gm.getRoles()) {
                temp = gm.getLegalMoves(gm.getContents(), player);
                if (temp.size() > 0) {
                    move.add(temp.get(ThreadLocalRandom.current().nextInt(0, temp.size())));
                }
            }

            startTime = System.currentTimeMillis();
            gm.getNextState(gm.getContents(), move);
            limit +=  System.currentTimeMillis() - startTime;


            if (gm.isTerminal(gm.getContents()));
                gm.restart();

        }



        try {
            String content = "Build time for "+ gameDirectory+ " = " +buildTime/1000 +
                    "seconds.\nThis network contained "+ nodeCount+
                    " nodes.\nThe average number of states processed per second was: "+ (statesPerMin * 12) +"\n";

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
