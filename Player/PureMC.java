package Player;

import DescriptionProcessing.Player;
import DescriptionProcessing.PropNetComponents.Latch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by siavj on 31/01/2017.
 */
public class PureMC extends PropnetPlayer {

    int count = 0;
    public String makeMove() {
        double timeLimit = 5000;
        double start = System.currentTimeMillis();
        double finishBy = start + timeLimit - 1000;


        Player myRole = null;
        ArrayList<String> moves;
        for (Player player : getRoles()) {
            if (player.toString().equals(getMyRole())) {
                myRole = player;
                break;
            }
        }

        moves = getLegalMoves(getContents(), myRole);
        String move = moves.get(0);


        if (moves.size() > 1) {

            int[] moveTotalPoints = new int[moves.size()];
            int[] moveTotalAttempts = new int[moves.size()];

            for(int i = 0; true; i = (i+1) % moves.size()) {

                if (System.currentTimeMillis() > finishBy)
                    break;


                int theScore = drillDown(myRole, moves.get(i));
                moveTotalPoints[i] += theScore;
                moveTotalAttempts[i] += 1;
            }

//            System.out.println(System.currentTimeMillis() - start+ ":" + count );

            double[] moveExpectedPoints = new double[moves.size()];
            for (int i = 0; i < moves.size(); i++) {
                moveExpectedPoints[i] = (double)moveTotalPoints[i] / moveTotalAttempts[i];
            }


            int bestMove = 0;
            double bestMoveScore = moveExpectedPoints[0];
            for (int i = 1; i < moves.size(); i++) {
                if (moveExpectedPoints[i] > bestMoveScore) {
                    bestMoveScore = moveExpectedPoints[i];
                    bestMove = i;
                }
            }
            move = moves.get(bestMove);
        }

        double stop = System.currentTimeMillis();
        return move;
    }

    private int[] depth = new int[1];
    private int drillDown(Player myRole, String myMove) {
        HashSet<String> contents = getContents();
        try {
            contents = recursiveDrill(randomNextState(contents, myMove), depth);
            return getGoal(contents, myRole);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private HashSet<String> recursiveDrill(HashSet<String> rndContents, int[]theDepth){
        int nDepth = 0;
        while(!isTerminal(rndContents)) {
            nDepth++;
            rndContents = randomNextState(rndContents);
        }
        if(theDepth != null)
            theDepth[0] = nDepth;
        return rndContents;
    }

    private HashSet<String> randomNextState(HashSet<String> contents, String move) {

        ArrayList<String> temp;
        ArrayList<String> moves = new ArrayList<>();
        for (Player player : getRoles()){
            if (player.toString().equals(getMyRole())){
                moves.add(move);
            }
            else {
                temp = getLegalMoves(contents, player);
                moves.add(temp.get(ThreadLocalRandom.current().nextInt(0, temp.size())));
            }
        }

        return getNextState(contents, moves);
    }

    private HashSet<String> randomNextState(HashSet<String> contents) {

        ArrayList<String> temp;
        ArrayList<String> moves = new ArrayList<>();
        for (Player player : getRoles()){

            temp = getLegalMoves(contents, player);
//            System.out.println("legal: " +player + "---"+temp);
//            System.out.println("terminal: " + isTerminal(contents) + "-- "+ contents);
            moves.add(temp.get(ThreadLocalRandom.current().nextInt(temp.size())));

        }

        return getNextState(contents, moves);
    }


}