package Test;


import GDLParser.LexicalAnalyser;
import SylmbolTable.DescriptionTable;

public class Main {

	public static void main(String[] args) {
		LexicalAnalyser l = new LexicalAnalyser();
		l.analyseFile("modifiedKnightTour");

		DescriptionTable dt = new DescriptionTable(l.getTokenStream());

	}
}