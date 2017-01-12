package Test;


import DescriptionProcessing.PropNet;
import DescriptionProcessing.PropNetBuilder;
import GDLParser.LexicalAnalyser;
import SylmbolTable.DescriptionTable;

public class Main {

	public static void main(String[] args) {
		LexicalAnalyser l = new LexicalAnalyser();
		l.analyseFile("lexerTest");
		DescriptionTable dt = new DescriptionTable(l.getTokenStream());

		PropNetBuilder builder = new PropNetBuilder();
		PropNet propNet = builder.create(dt);

	}
}