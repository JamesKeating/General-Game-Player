package Test;


import DescriptionProcessing.PropNet;
import DescriptionProcessing.PropNetBuilder;
import GDLParser.LexicalAnalyser;
import Player.PropnetPlayer;
import SylmbolTable.DescriptionTable;

public class Main {

	public static void main(String[] args) {
		LexicalAnalyser l = new LexicalAnalyser();
		l.analyseFile("lexerTest");
		DescriptionTable dt = new DescriptionTable(l.getTokenStream());
//		PropNetBuilder builder = new PropNetBuilder();
//		PropNet propNet = builder.create(dt.listTable());
//		propNet.renderToFile("TRYING");


		PropnetPlayer player = new PropnetPlayer();
		player.initialize(dt.listTable());

	}
}