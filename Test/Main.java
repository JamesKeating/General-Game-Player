package Test;

import GDLParser.LexicalAnalyser;
import GDLParser.RDP;
import SylmbolTable.DescriptionTable;

public class Main {

	public static void main(String[] args) {
		LexicalAnalyser l = new LexicalAnalyser();
		l.analyseFile();
		//System.out.println(l.getTokenStream());

		DescriptionTable dt = new DescriptionTable(l.getTokenStream());
		System.out.println(dt.getTable().get("<role>").get(1) + "\n");
		System.out.println(dt.getTable().get("<init>").get(1));

	}

}
