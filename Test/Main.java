package Test;


import DescriptionProcessing.Player;
import GDLParser.LexicalAnalyser;
import Gameplay.*;
import DeductiveDatabase.DescriptionTable;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {

//		LexicalAnalyser l = new LexicalAnalyser();
//		l.analyseFile("D:\\FYP\\General-Game-Gameplay\\Data\\lexerTest");
//		DescriptionTable dt = new DescriptionTable(l.getTokenStream());
//		PropNetBuilder builder = new PropNetBuilder();
//		PropNet propNet = builder.create(dt.listTable());
//		propNet.renderToFile("TRYING");


//		PropnetPlayer player = new PropnetPlayer();
//		player.initialize(dt.listTable());


		int randWin1=0, randWin2=0, montWin1 = 0, montWin2=0, montDraw1 = 0, montDraw2 =0, randDraw2 = 0, randDraw1 =0;
		for (int i = 0; i < 100; i++) {
			ArrayList<Gamer> x = new ArrayList<Gamer>();
			ArrayList<String> y = new ArrayList<>();
			y.add("oplayer");
			y.add("xplayer");
			if (i % 2 > 0) {
				x.add(new MonteCarloPlayer());
				x.add(new PureMC());

			}
			else{
				x.add(new PureMC());
				x.add(new MonteCarloPlayer());
			}

			GameManager gm = new GameManager(x);
			LexicalAnalyser l = new LexicalAnalyser();
			l.analyseFile("D:\\FYP\\General-Game-Player\\Data\\TicTacToe");
			DescriptionTable dt = new DescriptionTable(l.getTokenStream());

			gm.setupGame(dt, y);

			while (!gm.getGameManager().isTerminal(gm.getGameManager().getContents())){
				gm.updateGame();
			}

			if (i % 2 > 0) {
				for (Player p : gm.getGameManager().getRoles())
					if (p.toString().equals(x.get(0).getMyRole())){
						int j = gm.getGameManager().getGoal(gm.getGameManager().getContents(), p);
						if (j == 0)
							randWin2++;

						if (j == 50){
							randDraw2++;
							montDraw1++;
						}

						if (j == 100)
							montWin1++;
				}
			}


			else{
				for (Player p : gm.getGameManager().getRoles())
					if (p.toString().equals(x.get(1).getMyRole())){
						int j = gm.getGameManager().getGoal(gm.getGameManager().getContents(), p);
						if (j == 0)
							randWin1++;

						if (j == 50){
							randDraw1++;
							montDraw2++;
						}

						if (j == 100)
							montWin2++;
					}
			}

			System.out.println("rand W1-W2-D1-D2-L1-L2 "+ randWin1+" - "+randWin2+" - "+randDraw1+" - "+randDraw2+" - "+montWin2+" - "+montWin1);
			System.out.println("mont W1-W2-D1-D2-L1-L2 " + montWin1+" - "+ montWin2+" - "+montDraw1+" - "+montDraw2+" - "+randWin2+" - "+randWin1 +
					"\ntotal mont win/loss =" + (montWin1+montWin2) +"/" +(randWin1 + randWin2));
			System.out.println();


		}

	}
}